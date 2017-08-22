package com.yss.Expansion;

import java.io.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.*;
import com.yss.util.FileUtil;
import com.yss.yarn.controller.ExpansionController;
import com.yss.yarn.controller.YarnLaunchController;
import org.apache.commons.lang.StringUtils;
import org.mortbay.util.StringUtil;
import org.slf4j.*;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/25
 */
public class JschUtil {

    private  static org.slf4j.Logger logger = LoggerFactory.getLogger(JschUtil.class);


    static int checkAck(InputStream in) throws IOException {
        int b = in.read();

        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if (b == 0) {
            return b;
        }

        if (b == -1) {
            return b;
        }

        if ((b == 1) || (b == 2)) {
            StringBuffer sb = new StringBuffer();
            int          c;

            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');

            if (b == 1) {    // error
                System.out.print(sb.toString());
            }

            if (b == 2) {    // fatal error
                System.out.print(sb.toString());
            }
        }

        return b;
    }

    /**
     * 创建与远程服务器的连接
     * @param user
     * @param password
     * @param host
     * @return
     * @throws JSchException
     */
    public static Session createSession(String user, String password, String host) throws JSchException {
        JSch    jsch    = new JSch();
        Session session = jsch.getSession(user, host, 22);

        session.setPassword(password);
        session.setUserInfo(new MyUserInfo());
        session.connect(3000);
        return session;
    }

    /**
     * 验证本地是否与远程机器有连接
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean checkConnect(String user,String password,String host) {
        Session session = null;
        try {
            session = JschUtil.createSession(user, password, host);
            String result = execmd(session,"echo hello");
            if(result.contains("hello")){
                return true;
            }
        } catch (JSchException e) {
            logger.error("error ",e);
        } catch (IOException e) {
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
    public static boolean isNodeManagerRunning(Session session) throws IOException, InterruptedException {
        String cmd = "ps -fe|grep NodeManager |grep -v grep | wc -l";
        String result = remoteShellExe(session,cmd);
        logger.info("result "+session.getHost()+"  "+result);
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
    public static boolean isResourceManagerRunning(Session session) throws IOException, InterruptedException {
        String cmd = "ps -fe|grep resourcemanager |grep -v grep | wc -l";
        String result = remoteShellExe(session,cmd);
        logger.info("result "+session.getHost()+"  "+result);
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
    public static boolean remoteAddUser(Session session,String remoteHost,String cUser,String cUserPassWord){
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            String command = "useradd -nm "+cUser+" -p "+cUserPassWord+" > /dev/null 2>&1  && echo 1 || echo 0";
            ((ChannelExec) channel).setCommand(command);
            InputStream in=channel.getInputStream();
            channel.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println("remoteAddUser "+result);
            logger.info("创建用户 "+cUser+" 在"+remoteHost+"结果为"+result);
            if(result.contains("1")){
                return true;
            }
        }catch(Exception e){
            logger.error("error ",e);
            return false;
        } finally {
            channel.disconnect();
        }
        return false;
    }

    /**
     * 修改用户密码
     * @param cUser
     * @param cUserPassWord
     * @return
     */
    public static boolean passwdUser(Session session,String cUser,String cUserPassWord) throws IOException, InterruptedException {
        File file = new File(FileUtil.getJarPath(JschUtil.class) + "/passwd.sh");
        if(file.exists()){
            file.delete();
        }
        FileWriter     fileWritter   = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(fileWritter);
        pw.println("echo "+cUserPassWord+" | passwd --stdin "+cUser +" > /dev/null 2>&1 && echo 1 || echo 0");
        pw.flush();

        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            String command = "sh "+file.getAbsolutePath();
            ((ChannelExec) channel).setCommand(command);
            InputStream in=channel.getInputStream();
            channel.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            logger.info("修改用户 "+cUser+" 密码为"+cUserPassWord+" 结果:"+result);
            file.delete();
            if(result.contains("1")){
                return true;
            }
            logger.warn("修改用户 "+cUser+" 密码失败");
        }catch(Exception e){
            logger.error("修改用户密码有error ",e);
            return false;
        } finally {
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
    public static boolean remoteUserPasswd(String admin,String adminPasswd,String host,String cUser,String cUserPassword) throws IOException, JSchException, InterruptedException {
        File file = new File(FileUtil.getJarPath(JschUtil.class) + "/rpasswd.sh");
        if(file.exists()){
            file.delete();
        }
        FileWriter     fileWritter   = new FileWriter(file, true);
        PrintWriter pw = new PrintWriter(fileWritter);
        pw.println("echo "+cUserPassword+" | passwd --stdin "+cUser +" > /dev/null 2>&1 && echo 1 || echo 0");
        pw.flush();

        Session session = createSession(admin,adminPasswd,host);
        scpLocalFileToRemote(session,
                "/opt/rpasswd.sh",
                file.getAbsolutePath());
        Thread.currentThread().sleep(1000);
        String result =  remoteShellExe(session,"sh /opt/rpasswd.sh");
        logger.info("修改远程用户 "+cUser+" 密码为"+cUserPassword+"结果为："+result);
        if(result.contains("1")){
            return true;
        }
        logger.warn("修改远程用户hadoop密码失败");
        return false;
    }

    public static String remoteShellExe(Session session,String cmd){
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            InputStream in=channel.getInputStream();
            channel.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            return result;
        }catch(Exception e){
            logger.error("error ",e);
        } finally {
            channel.disconnect();
        }
        return "";

    }

    /**
     * 验证远程用户是否存在
     * @param admin
     * @param adminPasswd
     * @param cUser
     * @return
     */
    public static boolean checkRemoteUserExsits(String admin,String adminPasswd,String host,String cUser){
        Channel channel = null;
        try {
            Session session = createSession(admin,adminPasswd,host);
            channel = session.openChannel("exec");

            String command = "id "+cUser+"  > /dev/null 2>&1 && echo 1 || echo 0";
            ((ChannelExec) channel).setCommand(command);
            InputStream in=channel.getInputStream();
            channel.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            logger.info("验证远程用户 "+cUser+" 在"+host+"是否存在 结果为："+result);
            if(result.contains("1")){
                return true;
            }
        }catch(Exception e){
            logger.error("error ",e);
            return false;
        } finally {
            channel.disconnect();
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
    public static boolean localUserAdd(Session session,String cUser,String passwd) throws IOException, InterruptedException {
        String  localHostName = InetAddress.getLocalHost().getHostName();
        return  remoteAddUser(session,localHostName,cUser,passwd);
    }


    /**
     * 本地执行shell
     * @param cmd
     * @return
     */
    public static String execmd(String cmd) throws IOException, InterruptedException {
        logger.info("exec cmd "+cmd);
        Process ps = Runtime.getRuntime().exec(cmd);
        ps.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String result = sb.toString();
        System.out.println("exe result "+result);
        ps.destroy();
        return result;
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

    public static void main(String[] args) throws JSchException, IOException, InterruptedException {
//        JschUtil.generateRemoteSshKeygen("root", "Tudou=123", "zhangc4");
//        scpLocalFileToRemote("root", "Tudou=123", "zhangc5","/root/.ssh/a.txt","/Users/zhangchi/sourceCode/DataStreamUi/a.txt");

    }

    private static String read(InputStream is) throws IOException {
        StringBuilder buf = new StringBuilder(1024);
        byte[]        tmp = new byte[1024];

        while (is.available() > 0) {
            int i = is.read(tmp, 0, 1024);

            if (i < 0) {
                break;
            }

            buf.append(new String(tmp, 0, i));
        }

        return buf.toString();
    }

    /**
     * 远程生成目录
     * @param session
     * @param rDir
     * @return
     * @throws JSchException
     */
    public static Session mkdirRemoteDir(Session session,String rDir) throws JSchException, InterruptedException {
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            String command = "mkdir "+rDir;
            ((ChannelExec) channel).setCommand(command);
            logger.info("创建文件夹" +rDir);
            channel.connect();
        }finally {
            channel.disconnect();
        }
        Thread.currentThread().sleep(2000);

        return session;
    }

    /**
     * 生成远程ssh-keygen
     * @return
     * @throws JSchException
     * @throws IOException
     * @throws InterruptedException
     */
    public static Session generateRemoteSshKeygen(Session session)
            throws JSchException, IOException, InterruptedException {
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            String command = "ssh-keygen -t rsa -N \"\" -f ~/.ssh/id_rsa\n";
            ((ChannelExec) channel).setCommand(command);
            logger.info("生成远程"+session.getHost()+"sshkey");
            channel.connect();
        }finally {
            channel.disconnect();
        }
        Thread.currentThread().sleep(1500);
        return session;
    }


    /**
     * 删除远程.ssh目录
     * @param session
     * @param user
     * @return
     * @throws JSchException
     * @throws InterruptedException
     */
    public static boolean delRemoteSSHDir(Session session,String user) throws JSchException, InterruptedException {
        Channel channel = null;
        try{
            channel = session.openChannel("exec");
            String command = "rm -rf /home/"+user+"/.ssh/*";
            ((ChannelExec) channel).setCommand(command);
            logger.info("清空远程用户"+user+".ssh目录");
            channel.connect();
        }finally {
            channel.disconnect();
        }
        Thread.currentThread().sleep(1000);
        return true;

    }

    public static Session remoteInstall(Session session)
            throws JSchException, IOException, InterruptedException {
        final Channel           channel  = session.openChannel("shell");
        final PipedInputStream  pipeIn   = new PipedInputStream();
        final PipedOutputStream pipeOut  = new PipedOutputStream(pipeIn);
        final PipedInputStream  pipeIn2  = new PipedInputStream();

        channel.setInputStream(pipeIn);
        channel.connect(3000);
        pipeOut.write("cd /opt/expand ; sh expand.sh".getBytes());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.currentThread().sleep(1000);
                        String msg = read(pipeIn);
                        System.out.println(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    channel.disconnect();
                }
            }
        }).start();
        Thread.currentThread().sleep(10000);
        return session;
    }



    /**
     * 将本地文件传输至远程
     * @param rfile
     * @param lfile
     * @throws JSchException
     */
    public static Session scpLocalFileToRemote(Session session, String rfile, String lfile)
            throws JSchException {
        FileInputStream fis     = null;
        Channel         channel = null;

        try {
            boolean ptimestamp = true;
            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp
                                       ? "-p"
                                       : "") + " -t " + rfile;

            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream  in  = channel.getInputStream();

            channel.connect();

            if (checkAck(in) != 0) {
                throw new RuntimeException("有问题");
            }

            File _lfile   = new File(lfile);
            long filesize = _lfile.length();

            command = "C0644 " + filesize + " ";

            if (lfile.lastIndexOf('/') > 0) {
                command += lfile.substring(lfile.lastIndexOf('/') + 1);
            } else {
                command += lfile;
            }

            command += "\n";
            out.write(command.getBytes());
            out.flush();

            if (checkAck(in) != 0) {
                throw new RuntimeException("有问题");
            }

            // send a content of lfile
            fis = new FileInputStream(lfile);

            byte[] buf = new byte[1024];

            while (true) {
                int len = fis.read(buf, 0, buf.length);

                if (len <= 0) {
                    break;
                }

                out.write(buf, 0, len);    // out.flush();
            }

            fis.close();
            fis = null;

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            out.close();
            channel.disconnect();
        } catch (Exception e) {
            System.out.println(e);
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }finally {
            channel.disconnect();
        }
        logger.info("将本地文件"+lfile+"传输至远程"+session.getHost()+rfile);
        return session;
    }

    /**
     * 将本地文件夹传输至远程
     * @param session
     * @param rDir
     * @param lDir
     */
    public static void scpLocalDirToRemote(Session session,String rDir,String lDir) throws JSchException, InterruptedException {
        File lDirFile = new File(lDir);
        System.out.println("test1 "+lDirFile.getName()+"  :  "+lDirFile.getAbsolutePath());
        if(!lDirFile.exists()){
            return;
        }
        mkdirRemoteDir(session,rDir);
        File lfiles[] = lDirFile.listFiles();
        for(File lfile:lfiles){
            System.out.println("test2 "+rDir+"/"+lfile.getName()+"  :  "+lfile.getAbsolutePath());
            scpLocalFileToRemote(session,rDir+"/"+lfile.getName(),lfile.getAbsolutePath());
        }

    }

    /**
     * 将本地文件夹传输至远程
     * @param rDir
     * @param lDir
     */
    public static void scpLocalDirToRemote(String user,String host,String rDir,String lDir) throws JSchException, IOException, InterruptedException {

        execmd("scp -r "+lDir+" "+user+"@"+host+":"+rDir);

    }

    /**
     * 将本地文件夹传输至远程
     * @param rDir
     * @param lDir
     */
    public static Session scpLocalDirToRemote(Session session,String user,String host,String rDir,String lDir) throws JSchException, IOException, InterruptedException {

        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            String command = "scp -r "+lDir+" "+user+"@"+host+":"+rDir;
            ((ChannelExec) channel).setCommand(command);
            channel.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            logger.info("远程文件传输，"+lDir+"  to  "+rDir+"  result:"+result);
        }finally {
            channel.disconnect();
        }
        return session;
    }

    /**
     * shell执行
     * @param session
     * @param cmd
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public static String execmd(Session session,String cmd) throws JSchException, IOException {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(cmd);
        channel.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            sb.append(line+"\n");

        }
        return sb.toString();
    }


    /**
     * 远程安装hadoop
     */
    public static boolean remoteInstallHadoop(Session session,String cmd,String id) throws JSchException, IOException, InterruptedException {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(cmd);
        channel.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        String logPath = FileUtil.getJarPath(JschUtil.class) + "/log";
        File logDir = new File(logPath);
        if(!logDir.exists()){
            logDir.mkdir();
        }
        String logFile = (logPath+"/"+id+".log").replace("file:","");
        File file = new File(logFile);
        if(file.exists()){
            file.delete();
        }
        FileWriter     fileWritter   = new FileWriter(logFile, true);
        PrintWriter pw = new PrintWriter(fileWritter);

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            pw.println(line);
            pw.flush();
            if(line.contains("allEnd")){
                System.out.println("ALL DONE");
                return true;
            }

        }

        return false;
    }


    /**
     * 传输远程的rsa_pub至本地
     * @param remoteUser
     * @param remoteHost
     * @throws JSchException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String scpRemoteIdRsaPub(Session session,String remoteUser, String remoteHost)
            throws JSchException, IOException, InterruptedException {
        Channel channel = null;
        String           rfile  = "/home/" + remoteUser + "/.ssh/id_rsa.pub";
        String           lfile  = "/home/" + remoteUser + "/.ssh/" + remoteHost + ".pub";

        try {
            channel = session.openChannel("exec");

            FileOutputStream fos    = null;

            String           prefix = null;

            if (new File(lfile).isDirectory()) {
                prefix = lfile + File.separator;
            }

            // exec 'scp -f rfile' remotely
            String command = "scp -f " + rfile;

            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream  in  = channel.getInputStream();

            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            while (true) {
                int c = checkAck(in);

                if (c != 'C') {
                    break;
                }

                // read '0644 '
                in.read(buf, 0, 5);

                long filesize = 0L;

                while (true) {
                    if (in.read(buf, 0, 1) < 0) {

                        // error
                        break;
                    }

                    if (buf[0] == ' ') {
                        break;
                    }

                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                String file = null;

                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);

                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);

                        break;
                    }
                }

                // System.out.println("filesize="+filesize+", file="+file);
                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of lfile
                fos = new FileOutputStream((prefix == null)
                                           ? lfile
                                           : prefix + file);

                int foo;

                while (true) {
                    if (buf.length < filesize) {
                        foo = buf.length;
                    } else {
                        foo = (int) filesize;
                    }

                    foo = in.read(buf, 0, foo);

                    if (foo < 0) {

                        // error
                        break;
                    }

                    fos.write(buf, 0, foo);
                    filesize -= foo;

                    if (filesize == 0L) {
                        break;
                    }
                }

                fos.close();
                fos = null;

                if (checkAck(in) != 0) {
                    throw new RuntimeException("fuck");

                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }
        } finally {
            channel.disconnect();
        }
        logger.info("传输远程"+remoteHost+"的rsa_pub至本地");
        return lfile;
    }

    /**
     * 将本地的authorized_keys文件同步更新至远程服务器的authorized_keys文件(覆盖)
     *
     */
    public static Session updateRemoteHostAuthorizedKeys(Session session,String user) throws JSchException {
        scpLocalFileToRemote(session,
                             "/home/" + user + "/.ssh/authorized_keys",
                             "/home/" + user + "/.ssh/authorized_keys");
        return session;
    }

    /**
     * 将本机的hosts覆盖至远程机器hosts
     *
     */
    public static Session updateRemoteHosts(Session session) throws JSchException {
        scpLocalFileToRemote(session,
                "/etc/hosts",
                "/etc/hosts");
        logger.info("将本机/etc/hosts覆盖至远程"+session.getHost()+"/etc/hosts");
        return session;
    }

    /**
     * 将本地known_hosts文件同步更新至远程服务器authorized_keys（覆盖）
     * @param user
     * @throws JSchException
     */
    public static void updateRemoteKnownHosts(Session session , String user) throws JSchException {
        scpLocalFileToRemote(session, "/home/" + user + "/.ssh/known_hosts", "/home/" + user + "/.ssh/known_hosts");
    }

    /**
     * 将本机host设置到本地的known_hosts里
     * @param user
     * @throws UnknownHostException
     * @throws JSchException
     */
    public static void setLocalKnownHosts(String user, String password) throws UnknownHostException, JSchException {
        String  localHostName = InetAddress.getLocalHost().getHostName();
        JSch    jsch          = new JSch();
        Session session       = jsch.getSession(user, localHostName, 22);

        session.setPassword(password);
        session.setUserInfo(new MyUserInfo());

        String localKnownHosts = "/" + user + "/.ssh/known_hosts";

        jsch.setKnownHosts(localKnownHosts);

        UserInfo ui = new MyUserInfo();

        session.setUserInfo(ui);
        session.connect();
        session.disconnect();
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
