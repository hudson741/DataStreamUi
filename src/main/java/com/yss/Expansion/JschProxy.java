package com.yss.Expansion;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Session;

import java.io.*;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/8/23
 */
public class JschProxy {


    private static GenericKeyedObjectPool<String, Session> pool;

    static {
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();

        poolConfig.setMaxTotal(800);
        poolConfig.setTimeBetweenEvictionRunsMillis(1000 * 60);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMaxTotalPerKey(10);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        // 使用非公平锁，提高并发效率
        poolConfig.setFairness(false);
        pool = new GenericKeyedObjectPool(new JschSessionObjectFactory(), poolConfig);
    }

    private static Logger logger = LoggerFactory.getLogger(JschProxy.class);

    public void clear() {
        logger.info("收到回调信息，清理本地无用对象");
        pool.clear();
    }


    public static Session getSession(JschPoolKey jschPoolKey) throws Exception {
        Session session = pool.borrowObject(jschPoolKey.Obj2Key(), 500);

        logger.info("pool now active :" + pool.getNumActive() + "  idle:" + pool.getNumIdle() + " wait: "
                    + pool.getNumWaiters());

        return session;
    }

    public static void returnSession(JschPoolKey jschPoolKey,Session session){
        pool.returnObject(jschPoolKey.Obj2Key(),session);
    }

    /**
     * shell执行
     * @param cmd
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public static String execmd(String user,String password,String host,String cmd) throws Exception {
        JschPoolKey jschPoolKey = null;
        Session session = null;
        Channel channel = null;
        try {
            jschPoolKey = new JschPoolKey(user, password, host);
            session = JschProxy.getSession(jschPoolKey);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            channel.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line + "\n");

            }
            return sb.toString();

        }catch(Exception e){
          logger.error("error ",e);
          session.disconnect();
          pool.invalidateObject(jschPoolKey.Obj2Key(),session);
          throw e;
        } finally {
            if(channel!=null) {
                channel.disconnect();
            }
            if(session.isConnected()) {
                JschProxy.returnSession(jschPoolKey, session);
            }else{
                pool.invalidateObject(jschPoolKey.Obj2Key(),session);
            }
        }
    }

    /**
     * 将本地文件传输至远程
     * @param rfile
     * @param lfile
     * @throws JSchException
     */
    public static void scpLocalFileToRemote(String remoteRoot,String passwd,String host, String rfile, String lfile)
            throws JSchException {
        FileInputStream fis     = null;
        Channel         channel = null;
        Session session = null;
        JschPoolKey jschPoolKey = new JschPoolKey(remoteRoot,passwd,host);

        try {
            boolean ptimestamp = true;
            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp
                    ? "-p"
                    : "") + " -t " + rfile;

            session = JschProxy.getSession(jschPoolKey);
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
            returnSession(jschPoolKey,session);
            channel.disconnect();
        }
        logger.info("将本地文件"+lfile+"传输至远程"+session.getHost()+rfile);
    }

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
     * 传输远程的rsa_pub至本地
     * @param remoteUser
     * @param remoteHost
     * @throws JSchException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String scpRemoteIdRsaPub(String remoteUser ,String password,String remoteHost)
            throws Exception {
        Channel channel = null;
        Session session = null;
        JschPoolKey jschPoolKey = new JschPoolKey(remoteUser,password,remoteHost);
        String           rfile  = "/home/" + remoteUser + "/.ssh/id_rsa.pub";
        String           lfile  = "/home/" + remoteUser + "/.ssh/" + remoteHost + ".pub";
        try {

            session = JschProxy.getSession(jschPoolKey);
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
            returnSession(jschPoolKey,session);
            channel.disconnect();
        }
        logger.info("传输远程"+remoteHost+"的rsa_pub至本地");
        return lfile;
    }
}
