package com.yss.ws;

import com.jcraft.jsch.*;
import com.yss.dao.StatusDao;
import com.yss.dao.SystemDao;
import com.yss.entity.ApplicationKeyEntity;
import com.yss.entity.StatusEntity;
import com.yss.entity.SystemEntity;
import com.yss.model.SchSession;
import com.yss.model.SessionOutput;
import com.yss.model.UserSchSessions;
import com.yss.service.ApplicationKeyService;
import com.yss.task.SecureShellTask;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lixingjun on 2017/9/2.
 */
@Service
public class SSHService {

    private static Logger logger = LoggerFactory.getLogger(SSHService.class);

    //system path to public/private key
    public static final String KEY_PATH = SSHService.class.getClassLoader().getResource(".").getPath() + "keydb";
    public static final String KEY_TYPE = "rsa";

    //private key name
    public static final String PVT_KEY = KEY_PATH + "/id_" + KEY_TYPE;
    //public key name
    public static final String PUB_KEY = PVT_KEY + ".pub";
    public static final int KEY_LENGTH = 2048;

    public static final int SERVER_ALIVE_INTERVAL =  5 * 1000;
    public static final int SESSION_TIMEOUT = 5000;
    public static final int CHANNEL_TIMEOUT = 5000;

    public static final boolean keyManagementEnabled = false;

    @Autowired
    private ApplicationKeyService applicationKeyService;

    @Autowired
    private StatusDao statusDao;

    @Autowired
    private SystemDao systemDao;



    public  String keygen(){
        String passphrase =  UUID.randomUUID().toString();

        try {
            FileUtils.forceMkdir(new File(KEY_PATH));
            deleteGenSSHKeys();

                //set key type
                int type = KeyPair.RSA;
                if ("dsa".equals(SSHService.KEY_TYPE)) {
                    type = KeyPair.DSA;
                } else if ("ecdsa".equals(SSHService.KEY_TYPE)) {
                    type = KeyPair.ECDSA;
                }
                String comment = "keybox@global_key";

                JSch jsch = new JSch();


                KeyPair keyPair = KeyPair.genKeyPair(jsch, type, KEY_LENGTH);

                keyPair.writePrivateKey(PVT_KEY, passphrase.getBytes());
                keyPair.writePublicKey(PUB_KEY, comment);
                keyPair.dispose();


        } catch (Exception e) {
            logger.error(e.toString(), e);
        }

        return passphrase;

    }


    /**
     * delete SSH keys
     */
    public  void deleteGenSSHKeys() {

        deletePvtGenSSHKey();
        //delete public key
        try {
            File file = new File(PUB_KEY);
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }


    /**
     * delete SSH keys
     */
    public  void deletePvtGenSSHKey() {

        //delete private key
        try {
            File file = new File(PVT_KEY);
            if (file.exists()) {
                FileUtils.forceDelete(file);
            }
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }


    }


    public  String getPublicKey() {

        String publicKey = PUB_KEY;
        //read pvt ssh key
        File file = new File(publicKey);
        try {
            publicKey = FileUtils.readFileToString(file);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

        return publicKey;
    }


    /**
     * returns the system's public key
     *
     * @return system's public key
     */
    public static String getPrivateKey() {

        String privateKey = PVT_KEY;
        //check to see if pub/pvt are defined in properties
        //read pvt ssh key
        File file = new File(privateKey);
        try {
            privateKey = FileUtils.readFileToString(file);
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

        return privateKey;
    }

    public  SystemEntity authAndAddPubKey(SystemEntity hostSystem, String passphrase, String password) {


        JSch jsch = new JSch();
        Session session = null;
        hostSystem.setStatusCd(SystemEntity.SUCCESS_STATUS);
        try {

            ApplicationKeyEntity appKey = applicationKeyService.getAllApplications().get(0);
            //check to see if passphrase has been provided
            if (passphrase == null || passphrase.trim().equals("")) {
                passphrase = appKey.getPassphrase();
                //check for null inorder to use key without passphrase
                if (passphrase == null) {
                    passphrase = "";
                }
            }
            //add private key
            jsch.addIdentity(appKey.getId().toString(), appKey.getPrivateKey().trim().getBytes(), appKey.getPublicKey().getBytes(), passphrase.getBytes());

            //create session
            session = jsch.getSession(hostSystem.getUser(), hostSystem.getHost(), hostSystem.getPort());

            //set password if passed in
            if (password != null && !password.equals("")) {
                session.setPassword(password);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setServerAliveInterval(SERVER_ALIVE_INTERVAL);
            session.connect(SESSION_TIMEOUT);


            addPubKey(hostSystem, session, appKey.getPublicKey());

        } catch (Exception e) {
            logger.info(e.toString(), e);
            hostSystem.setErrorMsg(e.getMessage());
            if (e.getMessage().toLowerCase().contains("userauth fail")) {
                hostSystem.setStatusCd(SystemEntity.PUBLIC_KEY_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("auth fail") || e.getMessage().toLowerCase().contains("auth cancel")) {
                hostSystem.setStatusCd(SystemEntity.AUTH_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("unknownhostexception")) {
                hostSystem.setErrorMsg("DNS Lookup Failed");
                hostSystem.setStatusCd(SystemEntity.HOST_FAIL_STATUS);
            } else {
                hostSystem.setStatusCd(SystemEntity.GENERIC_FAIL_STATUS);
            }


        }

        if (session != null) {
            session.disconnect();
        }

        return hostSystem;


    }


    public  SystemEntity addPubKey(SystemEntity hostSystem, Session session, String appPublicKey) {

        try {
            String authorizedKeys = hostSystem.getAuthorizedKeys().replaceAll("~\\/|~", "");

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("cat " + authorizedKeys);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.setInputStream(null);

            InputStream in = channel.getInputStream();
            InputStreamReader is = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(is);

            channel.connect(CHANNEL_TIMEOUT);

            String appPubKey = appPublicKey.replace("\n", "").trim();
            StringBuilder existingKeysBuilder = new StringBuilder("");

            String currentKey;
            while ((currentKey = reader.readLine()) != null) {
                existingKeysBuilder.append(currentKey).append("\n");
            }
            String existingKeys = existingKeysBuilder.toString();
            existingKeys = existingKeys.replaceAll("\\n$", "");
            reader.close();
            //disconnect
            channel.disconnect();

            StringBuilder newKeysBuilder = new StringBuilder("");
            if (keyManagementEnabled) {
                //get keys assigned to system
//                List<String> assignedKeys = PublicKeyDB.getPublicKeysForSystem(hostSystem.getId());
//                for (String key : assignedKeys) {
//                    newKeysBuilder.append(key.replace("\n", "").trim()).append("\n");
//                }
//                newKeysBuilder.append(appPubKey);
            } else {
                if (existingKeys.indexOf(appPubKey) < 0) {
                    newKeysBuilder.append(existingKeys).append("\n").append(appPubKey);
                } else {
                    newKeysBuilder.append(existingKeys);
                }
            }

            String newKeys = newKeysBuilder.toString();
            if (!newKeys.equals(existingKeys)) {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand("echo '" + newKeys + "' > " + authorizedKeys + "; chmod 600 " + authorizedKeys);
                ((ChannelExec) channel).setErrStream(System.err);
                channel.setInputStream(null);
                channel.connect(CHANNEL_TIMEOUT);
                //disconnect
                channel.disconnect();
            }

        } catch (Exception e) {
            logger.info(e.toString(), e);
            hostSystem.setErrorMsg(e.getMessage());
            hostSystem.setStatusCd(SystemEntity.GENERIC_FAIL_STATUS);
        }
        return hostSystem;
    }


    public SystemEntity openSSHTermOnSystem(String passphrase, String password, Long userId, Long sessionId, SystemEntity hostSystem, Map<Long, UserSchSessions> userSessionMap) {

        JSch jsch = new JSch();

        int instanceId = getNextInstanceId(sessionId, userSessionMap);
        hostSystem.setStatusCd(SystemEntity.SUCCESS_STATUS);
        hostSystem.setInstanceId(instanceId);


        SchSession schSession = null;

        try {
            ApplicationKeyEntity appKey = applicationKeyService.getAllApplications().get(0);
            //check to see if passphrase has been provided
            if (passphrase == null || passphrase.trim().equals("")) {
                passphrase = appKey.getPassphrase();
                //check for null inorder to use key without passphrase
                if (passphrase == null) {
                    passphrase = "";
                }
            }
            //add private key
            jsch.addIdentity(appKey.getId().toString(), appKey.getPrivateKey().trim().getBytes(), appKey.getPublicKey().getBytes(), passphrase.getBytes());

            //create session
            Session session = jsch.getSession(hostSystem.getUser(), hostSystem.getHost(), hostSystem.getPort());

            //set password if it exists
            if (password != null && !password.trim().equals("")) {
                session.setPassword(password);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setServerAliveInterval(SERVER_ALIVE_INTERVAL);
            session.connect(SESSION_TIMEOUT);
            Channel channel = session.openChannel("shell");
            ((ChannelShell) channel).setPtyType("xterm");

            InputStream outFromChannel = channel.getInputStream();


            //new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);

            Runnable run = new SecureShellTask(sessionOutput, outFromChannel);
            Thread thread = new Thread(run);
            thread.start();


            OutputStream inputToChannel = channel.getOutputStream();
            PrintStream commander = new PrintStream(inputToChannel, true);

            channel.connect();

            schSession = new SchSession();
            schSession.setUserId(userId);
            schSession.setSession(session);
            schSession.setChannel(channel);
            schSession.setCommander(commander);
            schSession.setInputToChannel(inputToChannel);
            schSession.setOutFromChannel(outFromChannel);
            schSession.setHostSystem(hostSystem);

            //refresh keys for session
            addPubKey(hostSystem, session, appKey.getPublicKey());


        } catch (Exception e) {
            logger.info(e.toString(), e);
            hostSystem.setErrorMsg(e.getMessage());
            if (e.getMessage().toLowerCase().contains("userauth fail")) {
                hostSystem.setStatusCd(SystemEntity.PUBLIC_KEY_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("auth fail") || e.getMessage().toLowerCase().contains("auth cancel")) {
                hostSystem.setStatusCd(SystemEntity.AUTH_FAIL_STATUS);
            } else if (e.getMessage().toLowerCase().contains("unknownhostexception")) {
                hostSystem.setErrorMsg("DNS Lookup Failed");
                hostSystem.setStatusCd(SystemEntity.HOST_FAIL_STATUS);
            } else {
                hostSystem.setStatusCd(SystemEntity.GENERIC_FAIL_STATUS);
            }
        }


        //add session to map
        if (hostSystem.getStatusCd().equals(SystemEntity.SUCCESS_STATUS)) {
            //get the server maps for user
            UserSchSessions userSchSessions = userSessionMap.get(sessionId);

            //if no user session create a new one
            if (userSchSessions == null) {
                userSchSessions = new UserSchSessions();
            }
            Map<Integer, SchSession> schSessionMap = userSchSessions.getSchSessionMap();

            //add server information
            schSessionMap.put(instanceId, schSession);
            userSchSessions.setSchSessionMap(schSessionMap);
            //add back to map
            userSessionMap.put(sessionId, userSchSessions);
        }

        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(hostSystem.getId());
        statusEntity.setStatusCd(hostSystem.getStatusCd());
        statusEntity.setUserId(userId.intValue());
        statusDao.update(statusEntity);
        systemDao.update(hostSystem);
        return hostSystem;
    }


    private int getNextInstanceId(Long sessionId, Map<Long, UserSchSessions> userSessionMap) {

        Integer instanceId = 1;
        if (userSessionMap.get(sessionId) != null) {

            for (Integer id : userSessionMap.get(sessionId).getSchSessionMap().keySet()) {
                if (!id.equals(instanceId) && userSessionMap.get(sessionId).getSchSessionMap().get(instanceId) == null) {
                    return instanceId;
                }
                instanceId = instanceId + 1;
            }
        }
        return instanceId;

    }

}
