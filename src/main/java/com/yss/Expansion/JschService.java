package com.yss.Expansion;

import java.io.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import org.assertj.core.util.Lists;
import org.slf4j.*;

import com.jcraft.jsch.*;

import com.yss.util.FileUtil;
import com.yss.Expansion.Exception.*;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/25
 */
public class JschService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(JschService.class);


    /**
     * 验证远程服务器是否有安装docker
     * @param root
     * @param password
     * @param remoteHost
     * @return
     * @throws RemoteConnectionException
     */
    public static boolean checkIsDockerInstalled(String root,String password,String remoteHost) throws RemoteConnectionException {
        try{
            List<String> list = JschProxy.execmd(root,password,remoteHost,new String[]{"docker ps > /dev/null 2>&1 && echo 1 || echo 0"});
            String result =list2String(list);
            if(result.contains("1")){
                return true;
            }
            return false;

        }catch(Exception e){
            logger.error("error ",e);
            throw new RemoteConnectionException(e.getMessage());
        }
    }


    /**
     * 验证本地是否与远程机器有连接
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean checkConnect(String hadoopLocalUser, String password, String remoteHost) {
        try {
            String       localHostName = InetAddress.getLocalHost().getHostName();
            List<String> list          = JschProxy.execmd(hadoopLocalUser,
                                                          password,
                                                          localHostName,
                                                          new String[] { "ssh " + remoteHost + " echo hello" });
            String result = list2String(list);

            if (result.contains("hello")) {
                return true;
            }
        } catch (Exception e) {
            logger.error("error ", e);
        }

        return false;
    }

    /**
     * 验证本地用户是否存在
     * @param cUser
     * @return
     */
    public static boolean checkLocalUserExsits(String cUser) throws IOException, InterruptedException {
        Process ps = Runtime.getRuntime().exec("id " + cUser);

        ps.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer   sb = new StringBuffer();
        String         line;

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        String result = sb.toString();

        logger.info("检查本地" + cUser + "用户是否存在 ： " + result);
        ps.destroy();

        if (result.contains("uid=")) {
            return true;
        }

        return false;
    }

    /**
     * 验证远程用户是否存在
     * @param admin
     * @param adminPasswd
     * @param cUser
     * @return
     */
    public static boolean checkRemoteUserExsits(String admin, String adminPasswd, String host, String cUser) {
        try {
            String       command = "id " + cUser + "  > /dev/null 2>&1 && echo 1 || echo 0";
            List<String> result  = JschProxy.execmd(admin, adminPasswd, host, new String[] { command });

            logger.info("验证远程用户 " + cUser + " 在" + host + "是否存在 结果为：" + result);

            String resultStr = list2String(result);

            if (resultStr.contains("1")) {
                return true;
            }
        } catch (Exception e) {
            logger.error("error ", e);

            return false;
        }

        return false;
    }

    public static void delFileMatch(String fileStr, String lineToRemove) throws DelFileMatchException {
        try {
            File file = new File(fileStr);

            if (!file.exists() ||!file.isFile()) {
                return;
            }

            File           tempFile = new File(file.getAbsolutePath() + ".tmp");
            BufferedReader br       = new BufferedReader(new FileReader(file));
            PrintWriter    pw       = new PrintWriter(new FileWriter(tempFile));
            String         line     = null;

            while ((line = br.readLine()) != null) {
                if (!line.trim().endsWith(lineToRemove)) {
                    pw.println(line);
                    pw.flush();
                }
            }

            pw.close();
            br.close();

            // Delete the original file
            if (!file.delete()) {
                System.out.println("Could not delete file");

                return;
            }

            // Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(file)) {
                System.out.println("Could not rename file");
            }

            logger.info("删除文件" + fileStr + " 配置" + lineToRemove + "的行");
        } catch (Exception e) {
            logger.error("error ", e);

            throw new DelFileMatchException("清理本地" + fileStr + "旧匹配失败，请确认系统权限，或重试");
        }
    }

    /**
     * 删除远程.ssh目录
     * @param user
     * @return
     * @throws JSchException
     * @throws InterruptedException
     */
    public static void delRemoteSSHDir(String ruser, String password, String host, String user)
            throws DelRemoteSSHDirException {
        try {
            String command = "rm -rf /home/" + user + "/.ssh/*";

            JschProxy.execmd(ruser, password, host, new String[] { command });
            logger.info("清空远程用户" + user + ".ssh目录");
            Thread.currentThread().sleep(1000);
        } catch (Exception e) {
            logger.error("error ", e);

            throw new DelRemoteSSHDirException(e.getMessage());
        }
    }

    /**
     * 生成远程ssh-keygen
     * @return
     * @throws JSchException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void generateRemoteSshKeygen(String user, String password, String host)
            throws GenerateRemoteSSHkeyException {
        try {
            String[] commands = {"rm -rf /home/" + user + "/.ssh/*",
                    "ssh-keygen -t rsa -N \"\" -f ~/.ssh/id_rsa\n"};

            JschProxy.execmd(user, password, host,commands);
            logger.info("生成远程 " + host + " sshkey");
            Thread.currentThread().sleep(1500);
        } catch (Exception e) {
            logger.error("error ", e);

            throw new GenerateRemoteSSHkeyException(e.getMessage());
        }
    }

    public static String list2String(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (String str : list) {
            result.append(str + " ");
        }

        return result.toString();
    }

    /**
     * 创建本地用户
     * @param cUser
     * @param passwd
     * @return
     * @throws UnknownHostException
     */
    public static boolean localUserAdd(String root, String password, String cUser, String passwd) throws Exception {
        String localHostName = InetAddress.getLocalHost().getHostName();

        return remoteAddUser(root, password, localHostName, cUser, passwd);
    }

    /**
     * 修改用户密码
     * @param cUser
     * @param cUserPassWord
     * @return
     */
    public static boolean localUserPasswd(String root, String password, String host, String cUser, String cUserPassWord)
            throws IOException, InterruptedException {
        File file = new File(FileUtil.getJarPath(JschService.class) + "/passwd.sh");

        if (file.exists()) {
            file.delete();
        }

        FileWriter  fileWritter = new FileWriter(file, true);
        PrintWriter pw          = new PrintWriter(fileWritter);

        pw.println("echo " + cUserPassWord + " | passwd --stdin " + cUser + " > /dev/null 2>&1 && echo 1 || echo 0");
        pw.flush();

        try {
            String       command = "sh " + file.getAbsolutePath();
            List<String> result  = JschProxy.execmd(root, password, host, new String[] { command });

            logger.info("修改用户 " + cUser + " 密码为" + cUserPassWord + " 结果:" + result);
            file.delete();

            String resultStr = list2String(result);

            if (resultStr.contains("1")) {
                return true;
            }

            logger.warn("修改用户 " + cUser + " 密码失败");
        } catch (Exception e) {
            logger.error("修改用户密码有error ", e);

            return false;
        }

        return false;
    }

    /**
     * 将指定字符串append到指定文件
     * @param fileT
     * @throws IOException
     */
    public static void localWriteFileAppend(List<String> datalist, String fileT) throws WriteFileException {
        try {
            File file = new File(fileT);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter     fileWritter   = new FileWriter(fileT, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

            bufferWritter.write("\n");

            for (String data : datalist) {
                bufferWritter.write(data);
                logger.info("写入文件 " + fileT + " " + data);
            }

            bufferWritter.close();
        } catch (Exception e) {
            logger.error("error ", e);

            throw new WriteFileException("更新" + fileT + "失败，请确认系统权限或重试");
        }
    }

    /**
     * 本机文件内容传输---apend模式
     * @param fileF
     * @param fileT
     * @throws IOException
     */
    public static void localWriteFileAppend(String fileF, String fileT) throws WriteFileException {
        try {
            List<String> result = new ArrayList<>();

            try {
                BufferedReader br = new BufferedReader(new FileReader(fileF));    // 构造一个BufferedReader类来读取文件
                String         s  = null;

                while ((s = br.readLine()) != null) {                             // 使用readLine方法，一次读一行
                    result.add(System.lineSeparator() + s);
                    System.out.println(System.lineSeparator() + s);
                }

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            File file = new File(fileT);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter     fileWritter   = new FileWriter(fileT, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

            bufferWritter.write("\n");

            for (String data : result) {
                System.out.println(data);
                bufferWritter.write(data);
            }

            bufferWritter.close();
            logger.info("追加文件" + fileF + "的内容至" + fileT);
        } catch (Exception e) {
            logger.error("error ", e);

            throw new WriteFileException("更新" + fileT + "失败，请确认系统权限或重试");
        }
    }

    /**
     * 远程生成目录
     * @param rDir
     * @return
     * @throws JSchException
     */
    public static void mkdirRemoteDir(String user, String password, String host, String rDir) throws Exception {
        String command = "mkdir " + rDir;

        JschProxy.execmd(user, password, host, new String[] { command });
        logger.info("创建文件夹" + rDir);
        Thread.currentThread().sleep(2000);
    }

    /**
     * 使用超级用户创建远程用户
     * @param remoteHost
     * @param cUser
     * @param cUserPassWord
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public static boolean remoteAddUser(String root, String password, String remoteHost, String cUser,
                                        String cUserPassWord) {
        String[] cmds = new String[2];

        cmds[0] = "mkdir /home";
        cmds[1] = "useradd -nm " + cUser + " -p " + cUserPassWord + " > /dev/null 2>&1  && echo 1 || echo 0";

        try {
            List<String> list   = JschProxy.execmd(root, password, remoteHost, cmds);
            String       result = list2String(list);

            logger.info("创建用户 " + cUser + " 在" + remoteHost + "结果为" + result);

            if (result.contains("1")) {
                return true;
            }
        } catch (Exception e) {
            logger.error("error ", e);
        }

        return false;
    }

    /**
     * 远程安装hadoop
     */
    public static boolean remoteEnvSet(String user, String password, String host, String id) {
        JschPoolKey jschPoolKey = new JschPoolKey(user, password, host);
        Session     session     = null;
        Channel     channel     = null;

        try {
            session = JschProxy.getSession(jschPoolKey);
            channel = session.openChannel("exec");

            String cmd = "sh /home/hadoop/expand/env.sh";

            ((ChannelExec) channel).setCommand(cmd);
            channel.connect();

            BufferedReader br      = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            String         logPath = FileUtil.getJarPath(JschService.class) + "/log";
            File           logDir  = new File(logPath);

            if (!logDir.exists()) {
                logDir.mkdir();
            }

            String      logFile     = (logPath + "/" + id + ".log").replace("file:", "");
            FileWriter  fileWritter = new FileWriter(logFile, true);
            PrintWriter pw          = new PrintWriter(fileWritter);
            String      line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                pw.println(line);
                pw.flush();

                if (line.contains("envEnd")) {
                    System.out.println("ALL DONE");

                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("error ", e);
            JschProxy.returnSession(jschPoolKey, session);
            channel.disconnect();
        }

        return false;
    }

    public static boolean remoteInstallHadoop(String luser, String password, String remoteHost, String id) {
        String      cmd         = "ssh " + remoteHost + " sh /home/hadoop/expand/expand.sh";
        Session     session     = null;
        Channel     channel     = null;
        JschPoolKey jschPoolKey = null;

        try {
            String localHostName = InetAddress.getLocalHost().getHostName();

            jschPoolKey = new JschPoolKey(luser, password, localHostName);
            session     = JschProxy.getSession(jschPoolKey);
            channel     = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            channel.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));

            logger.info("");

            String logPath = FileUtil.getJarPath(JschService.class) + "/log";
            File   logDir  = new File(logPath);

            if (!logDir.exists()) {
                logDir.mkdir();
            }

            String      logFile     = (logPath + "/" + id + ".log").replace("file:", "");
            FileWriter  fileWritter = new FileWriter(logFile, true);
            PrintWriter pw          = new PrintWriter(fileWritter);
            String      line;

            while ((line = br.readLine()) != null) {
                logger.info(line);
                pw.println(line);
                pw.flush();

                if (line.contains("tarEnd")) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("error ", e);
            JschProxy.returnSession(jschPoolKey, session);
            channel.disconnect();
        }

        return false;
    }

    /**
     * 创建远程添加用户文件,并远程执行
     * @param admin
     * @param adminPasswd
     * @param host
     * @param cUser
     * @param cUserPassword
     * @throws IOException
     * @throws JSchException
     * @throws InterruptedException
     */
    public static boolean remoteUserPasswd(String admin, String adminPasswd, String host, String cUser,
                                           String cUserPassword) {
        try {
            File file = new File(FileUtil.getJarPath(JschService.class) + "/rpasswd.sh");

            if (file.exists()) {
                file.delete();
            }

            FileWriter  fileWritter = new FileWriter(file, true);
            PrintWriter pw          = new PrintWriter(fileWritter);

            pw.println("echo " + cUserPassword + " | passwd --stdin " + cUser
                       + " > /dev/null 2>&1 && echo 1 || echo 0");
            pw.flush();
            JschProxy.scpLocalFileToRemote(admin, adminPasswd, host, "/opt/rpasswd.sh", file.getAbsolutePath());
            Thread.currentThread().sleep(1500);

            List<String> result = JschProxy.execmd(admin, adminPasswd, host, new String[] { "sh /opt/rpasswd.sh" });

            logger.info("修改远程用户 " + cUser + " 密码为" + cUserPassword + "结果为：" + result);

            String resultStr = list2String(result);

            if (resultStr.contains("1")) {
                file.delete();
                return true;
            }

            logger.warn("修改远程用户hadoop密码失败");

            file.delete();
            return false;
        } catch (Exception e) {
            logger.error("error ", e);
        }

        return false;
    }

    /**
     * 将本地文件夹传输至远程
     * @param rDir
     * @param lDir
     */
    public static void scpLocalDirToRemote(String luser, String passwd, String lhost, String ruser, String host,
                                           String rDir, String lDir)
            throws LocalDirScpException {
        try {
            String       command = "scp -r " + lDir + " " + ruser + "@" + host + ":" + rDir;
            List<String> result  = JschProxy.execmd(luser, passwd, lhost, new String[] { command });

            logger.info("远程文件传输，" + lDir + "  to  " + rDir + "  result:" + result);
        } catch (Exception e) {
            logger.error("error ", e);

            throw new LocalDirScpException(e.getMessage());
        }
    }

    /**
     * 添加用户sudoer权限
     * @param rAdmin
     * @param password
     * @param host
     * @param user
     * @throws Exception
     */
    public static void sudoRemoteHadoop(String rAdmin, String password, String host, String user)
            throws SudoRemoteHadoopException {
        String[] cmds = { "sed -i '$a " + user + "     ALL=(ALL)   ALL'  /etc/sudoers",
                          "sed -i '/requiretty/d' /etc/sudoers", "sed -i '$a # Defaults    requiretty'  /etc/sudoers" };

        try {
            JschProxy.execmd(rAdmin, password, host, cmds);
        } catch (Exception e) {
            logger.error("error ", e);

            throw new SudoRemoteHadoopException(e.getMessage());
        }
    }

    /**
     * 将本地的authorized_keys文件同步更新至远程服务器的authorized_keys文件(覆盖)
     *
     */
    public static void updateRemoteHostAuthorizedKeys(String user, String password, String host)
            throws RemoteHostsAuthorizedKeysUpdateException {
        try {
            JschProxy.scpLocalFileToRemote(user,
                                           password,
                                           host,
                                           "/home/" + user + "/.ssh/authorized_keys",
                                           "/home/" + user + "/.ssh/authorized_keys");
        } catch (Exception e) {
            logger.error("error ", e);

            throw new RemoteHostsAuthorizedKeysUpdateException(e.getMessage());
        }
    }

    /**
     * 将本机的hosts覆盖至远程机器hosts
     *
     */
    public static void updateRemoteHosts(String remoteRoot, String password, String host)
            throws RemoteHostsUpdateException {
        try {
            JschProxy.scpLocalFileToRemote(remoteRoot, password, host, "/etc/hosts", "/etc/hosts");
            logger.info("将本机/etc/hosts覆盖至远程" + host + "/etc/hosts");
        } catch (Exception e) {
            logger.error("error ", e);

            throw new RemoteHostsUpdateException(e.getMessage());
        }
    }

    /**
     * 将本地known_hosts文件同步更新至远程服务器authorized_keys（覆盖）
     * @param user
     * @throws JSchException
     */
    public static void updateRemoteKnownHosts(String user, String password, String host)
            throws RemoteKnownHostsUpdateException {
        try {
            JschProxy.scpLocalFileToRemote(user,
                                           password,
                                           host,
                                           "/home/" + user + "/.ssh/known_hosts",
                                           "/home/" + user + "/.ssh/known_hosts");
        } catch (Exception e) {
            logger.error("error ", e);

            throw new RemoteKnownHostsUpdateException(e.getMessage());
        }
    }

    public static Map<String, Boolean> isHadoopProcessRunning(String user, String password, String host)
            throws Exception {
        String[] cmds = new String[4];

        cmds[0] = "ps -fe|grep NodeManager |grep -v grep | wc -l";
        cmds[1] = "ps -fe|grep resourcemanager |grep -v grep | wc -l";
        cmds[2] = "ps -fe|grep namenode.NameNode |grep -v grep | wc -l";
        cmds[3] = "ps -fe|grep datanode.DataNode |grep -v grep | wc -l";

        List<String> result = JschProxy.execmd(user, password, host, cmds);

        if (CollectionUtils.isEmpty(result)) {
            return new HashMap<>();
        }

        Map<String, Boolean> map = new HashMap<>();

        map.put("nm",
                result.get(0).contains("1")
                ? true
                : false);
        map.put("rm",
                result.get(1).contains("1")
                ? true
                : false);
        map.put("nn",
                result.get(2).contains("1")
                ? true
                : false);
        map.put("dn",
                result.get(3).contains("1")
                ? true
                : false);

        return map;
    }

    /**
     * 将远程hosts设置至本地known_hosts中
     * @param user
     * @param password
     * @param host
     */
    public static void setRemoteKnownHosts(String user, String password, String host)
            throws RemoteKnownHostsSetException {
        try {
            JSch    jsch    = new JSch();
            Session session = jsch.getSession(user, host, 22);

            session.setPassword(password);
            session.setUserInfo(new MyUserInfo());

            String localKnownHosts = "/home/" + user + "/.ssh/known_hosts";

            jsch.setKnownHosts(localKnownHosts);
            logger.info("设置远程" + host + "至本地用户" + user + "known_hosts中");

            UserInfo ui = new MyUserInfo();

            session.setUserInfo(ui);
            session.connect();
            session.disconnect();
        } catch (Exception e) {
            logger.error("error ", e);

            throw new RemoteKnownHostsSetException(e.getMessage());
        }
    }

    public static class MyUserInfo implements UserInfo {
        @Override
        public boolean promptPassphrase(String s) {
            return true;
        }

        @Override
        public boolean promptPassword(String s) {
            return true;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;
        }

        @Override
        public void showMessage(String s) {
            System.out.println(s);
        }

        @Override
        public String getPassphrase() {
            return "";
        }

        @Override
        public String getPassword() {
            return "Tudou=123";
        }
    }
}
