package com.yss.Expansion;

import java.io.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.*;
import com.yss.util.FileUtil;
import org.slf4j.*;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/25
 */
public class JschService {

    private  static org.slf4j.Logger logger = LoggerFactory.getLogger(JschService.class);

    /**
     * 验证本地是否与远程机器有连接
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean checkConnect(String hadoopLocalUser,String password,String remoteHost) {
        try {
            String  localHostName = InetAddress.getLocalHost().getHostName();
            String result = JschProxy.execmd(hadoopLocalUser,password,localHostName,"ssh "+remoteHost+" echo hello");
            if(result.contains("hello")){
                return true;
            }
        } catch (Exception e) {
            logger.error("error ",e);
        }

        return false;
    }

    /**
     * 远程节点是否运行着nodemanager
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean isNodeManagerRunning(String user,String password,String host) throws Exception {
        String cmd = "ps -fe|grep NodeManager |grep -v grep | wc -l";
        String result = JschProxy.execmd(user,password,host,cmd);
        logger.info("result "+host+" : "+result);
        if(result.contains("1")){
            return true;
        }
        return false;
    }

    /**
     * 远程节点是否运行着ResourceManager
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean isResourceManagerRunning(String user,String password,String host) throws Exception {
        String cmd = "ps -fe|grep resourcemanager |grep -v grep | wc -l";
        String result = JschProxy.execmd(user,password,host,cmd);
        logger.info("result "+host+"  "+result);
        if(result.contains("1")){
            return true;
        }
        return false;
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
    public static boolean remoteAddUser(String root,String password,String remoteHost,String cUser,String cUserPassWord){
        String command = "useradd -nm "+cUser+" -p "+cUserPassWord+" > /dev/null 2>&1  && echo 1 || echo 0";
        String result = null;
        try {
            result = JschProxy.execmd(root,password,remoteHost,command);
            System.out.println("remoteAddUser "+result);
            logger.info("创建用户 "+cUser+" 在"+remoteHost+"结果为"+result);
            if(result.contains("1")){
                return true;
            }
        } catch (Exception e) {
            logger.error("error ",e);
        }

        return false;

    }

    /**
     * 修改用户密码
     * @param cUser
     * @param cUserPassWord
     * @return
     */
    public static boolean localUserPasswd(String root,String password,String host,String cUser,String cUserPassWord) throws IOException, InterruptedException {
        File file = new File(FileUtil.getJarPath(JschService.class) + "/passwd.sh");
        if(file.exists()){
            file.delete();
        }
        FileWriter     fileWritter   = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(fileWritter);
        pw.println("echo "+cUserPassWord+" | passwd --stdin "+cUser +" > /dev/null 2>&1 && echo 1 || echo 0");
        pw.flush();

        try {
            String command = "sh "+file.getAbsolutePath();
            String result = JschProxy.execmd(root,password,host,command);
            logger.info("修改用户 "+cUser+" 密码为"+cUserPassWord+" 结果:"+result);
            file.delete();
            if(result.contains("1")){
                return true;
            }
            logger.warn("修改用户 "+cUser+" 密码失败");
        }catch(Exception e){
            logger.error("修改用户密码有error ",e);
            return false;
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
    public static boolean remoteUserPasswd(String admin,String adminPasswd,String host,String cUser,String cUserPassword) throws Exception {
        File file = new File(FileUtil.getJarPath(JschService.class) + "/rpasswd.sh");
        if(file.exists()){
            file.delete();
        }
        FileWriter     fileWritter   = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(fileWritter);
        pw.println("echo "+cUserPassword+" | passwd --stdin "+cUser +" > /dev/null 2>&1 && echo 1 || echo 0");
        pw.flush();

        JschProxy.scpLocalFileToRemote(admin,adminPasswd,host,
                "/opt/rpasswd.sh",
                file.getAbsolutePath());
        Thread.currentThread().sleep(1000);
        String result =  JschProxy.execmd(admin,adminPasswd,host,"sh /opt/rpasswd.sh");
        logger.info("修改远程用户 "+cUser+" 密码为"+cUserPassword+"结果为："+result);
        if(result.contains("1")){
            return true;
        }
        logger.warn("修改远程用户hadoop密码失败");
        file.delete();
        return false;
    }


    /**
     * 验证远程用户是否存在
     * @param admin
     * @param adminPasswd
     * @param cUser
     * @return
     */
    public static boolean checkRemoteUserExsits(String admin,String adminPasswd,String host,String cUser){
        try {
            String command = "id "+cUser+"  > /dev/null 2>&1 && echo 1 || echo 0";
            String result = JschProxy.execmd(admin,adminPasswd,host,command);
            logger.info("验证远程用户 "+cUser+" 在"+host+"是否存在 结果为："+result);
            if(result.contains("1")){
                return true;
            }
        }catch(Exception e){
            logger.error("error ",e);
            return false;
        }
        return false;
    }

    /**
     * 验证本地用户是否存在
     * @param cUser
     * @return
     */
    public static boolean checkLocalUserExsits(String cUser) throws IOException, InterruptedException {
            Process ps = Runtime.getRuntime().exec("id "+cUser);
            ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            logger.info("检查本地"+cUser+"用户是否存在 ： "+result);
            ps.destroy();
            if(result.contains("uid=")){
                return true;
            }
            return false;
    }

    /**
     * 创建本地用户
     * @param cUser
     * @param passwd
     * @return
     * @throws UnknownHostException
     */
    public static boolean localUserAdd(String root,String password,String cUser,String passwd) throws Exception {
        String  localHostName = InetAddress.getLocalHost().getHostName();
        return  remoteAddUser(root,password,localHostName,cUser,passwd);
    }

    /**
     * 本机文件内容传输---apend模式
     * @param fileF
     * @param fileT
     * @throws IOException
     */
    public static void localWriteFileAppend(String fileF, String fileT) throws IOException {
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
        logger.info("追加文件"+fileF+"的内容至"+fileT);
    }

    /**
     * 将指定字符串append到指定文件
     * @param fileT
     * @throws IOException
     */
    public static void localWriteFileAppend(List<String> datalist, String fileT) throws IOException {

        File file = new File(fileT);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter     fileWritter   = new FileWriter(fileT, true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

        bufferWritter.write("\n");

        for (String data : datalist) {
            bufferWritter.write(data);
            logger.info("写入文件 "+fileT+" "+data);
        }

        bufferWritter.close();
    }


    /**
     * 远程生成目录
     * @param rDir
     * @return
     * @throws JSchException
     */
    public static void mkdirRemoteDir(String user,String password,String host,String rDir) throws Exception {
        String command = "mkdir "+rDir;
        JschProxy.execmd(user,password,host,command);
        logger.info("创建文件夹" +rDir);
        Thread.currentThread().sleep(2000);
    }

    /**
     * 生成远程ssh-keygen
     * @return
     * @throws JSchException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void generateRemoteSshKeygen(String user,String password,String host)
            throws Exception {
        String command = "ssh-keygen -t rsa -N \"\" -f ~/.ssh/id_rsa\n";
        JschProxy.execmd(user,password,host,command);
        logger.info("生成远程 "+host+" sshkey");
        Thread.currentThread().sleep(1500);
    }


    /**
     * 删除远程.ssh目录
     * @param user
     * @return
     * @throws JSchException
     * @throws InterruptedException
     */
    public static boolean delRemoteSSHDir(String ruser,String password,String host,String user) throws Exception {
        String command = "rm -rf /home/"+user+"/.ssh/*";
        JschProxy.execmd(ruser,password,host,command);
        logger.info("清空远程用户"+user+".ssh目录");
        Thread.currentThread().sleep(1000);
        return true;

    }



    /**
     * 将本地文件夹传输至远程
     * @param rDir
     * @param lDir
     */
    public static void scpLocalDirToRemote(String luser,String passwd,String lhost,String ruser,String host,String rDir,String lDir) throws Exception {
        String command = "scp -r "+lDir+" "+ruser+"@"+host+":"+rDir;
        String result = JschProxy.execmd(luser,passwd,lhost,command);
        logger.info("远程文件传输，"+lDir+"  to  "+rDir+"  result:"+result);
    }




    /**
     * 远程安装hadoop
     */
    public static boolean remoteInstallHadoop(String user,String password,String host,String cmd,String id) {
        JschPoolKey jschPoolKey = new JschPoolKey(user, password, host);
        Session session = null;
        Channel channel = null;
        try {
            session = JschProxy.getSession(jschPoolKey);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            channel.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            String logPath = FileUtil.getJarPath(JschService.class) + "/log";
            File logDir = new File(logPath);
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            String logFile = (logPath + "/" + id + ".log").replace("file:", "");
            File file = new File(logFile);
            if (file.exists()) {
                file.delete();
            }
            FileWriter fileWritter = new FileWriter(logFile, true);
            PrintWriter pw = new PrintWriter(fileWritter);

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                pw.println(line);
                pw.flush();
                if (line.contains("allEnd")) {
                    System.out.println("ALL DONE");
                    return true;
                }

            }
        }catch(Exception e){
            logger.error("error ",e);
            JschProxy.returnSession(jschPoolKey,session);
            channel.disconnect();
        }

        return false;
    }




    /**
     * 将本地的authorized_keys文件同步更新至远程服务器的authorized_keys文件(覆盖)
     *
     */
    public static void updateRemoteHostAuthorizedKeys(String user,String password,String host) throws Exception {
        JschProxy.scpLocalFileToRemote(user,password,host,
                             "/home/" + user + "/.ssh/authorized_keys",
                             "/home/" + user + "/.ssh/authorized_keys");
    }

    /**
     * 将本机的hosts覆盖至远程机器hosts
     *
     */
    public static void updateRemoteHosts(String remoteRoot,String password,String host) throws Exception {
        JschProxy.scpLocalFileToRemote(remoteRoot,password,host,
                "/etc/hosts",
                "/etc/hosts");
        logger.info("将本机/etc/hosts覆盖至远程"+host+"/etc/hosts");
    }

    /**
     * 将本地known_hosts文件同步更新至远程服务器authorized_keys（覆盖）
     * @param user
     * @throws JSchException
     */
    public static void updateRemoteKnownHosts( String user,String password,String host) throws Exception {
        JschProxy.scpLocalFileToRemote(user,password,host, "/home/" + user + "/.ssh/known_hosts", "/home/" + user + "/.ssh/known_hosts");
    }

    public static void delFileMatch(String fileStr,String lineToRemove) throws IOException {
        File file = new File(fileStr);
        if(!file.exists() || !file.isFile()){
            return;
        }
        File tempFile = new File(file.getAbsolutePath() + ".tmp");
        BufferedReader br = new BufferedReader(new FileReader(file));
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

        String line = null;

        while ((line = br.readLine()) != null) {

            if (!line.trim().endsWith(lineToRemove)) {
                pw.println(line);
                pw.flush();
            }
        }
        pw.close();
        br.close();

        //Delete the original file
        if (!file.delete()) {
            System.out.println("Could not delete file");
            return;
        }

        //Rename the new file to the filename the original file had.
        if (!tempFile.renameTo(file)) {
            System.out.println("Could not rename file");
        }
        logger.info("删除文件"+fileStr+" 配置"+lineToRemove+"的行");
    }

    /**
     * 将远程hosts设置至本地known_hosts中
     * @param user
     * @param password
     * @param host
     */
    public static void setRemoteKnownHosts(String user, String password, String host) throws JSchException {
        JSch    jsch    = new JSch();
        Session session = jsch.getSession(user, host, 22);

        session.setPassword(password);
        session.setUserInfo(new MyUserInfo());

        String localKnownHosts = "/home/" + user + "/.ssh/known_hosts";

        jsch.setKnownHosts(localKnownHosts);
        logger.info("设置远程"+host+"至本地用户"+user+"known_hosts中");

        UserInfo ui = new MyUserInfo();

        session.setUserInfo(ui);
        session.connect();
        session.disconnect();
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
