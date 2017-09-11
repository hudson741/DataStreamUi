package com.yss.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.hadoop.fs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;

import com.yss.util.FileUtil;
import com.yss.util.PropertiesUtil;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/5
 */
public class FtpService implements InitializingBean {
    private Logger    logger = LoggerFactory.getLogger(FtpService.class);

    private GenericObjectPool<FTPClient>  pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(30);
        poolConfig.setTimeBetweenEvictionRunsMillis(1000*60);
        poolConfig.setTestWhileIdle(true);
        //使用非公平锁，提高并发效率
        poolConfig.setFairness(false);
        pool = new GenericObjectPool<FTPClient>(new FtpConnectionFactory(),poolConfig);
    }

    public boolean changeWorkingDirectory(String directory) {
        boolean flag = false;
        FTPClient ftpClient = null;

        try {

            ftpClient = pool.borrowObject(1000);

            logger.info("pool now active :" + pool.getNumActive() + "  idle:"
                    + pool.getNumIdle() + " wait: "
                    + pool.getNumWaiters());

            flag = ftpClient.changeWorkingDirectory(directory);

            pool.returnObject(ftpClient);
            if (flag) {
                logger.debug("进入文件夹" + directory + " 成功！");
            } else {
                logger.debug("进入文件夹" + directory + " 失败！");
            }
        } catch (Exception e) {
            try {
                pool.invalidateObject(ftpClient);
            } catch (Exception e1) {
                logger.error("error ",e);
            }
            logger.error("error ",e);
        }

        return flag;
    }


    public boolean createDirecroty(String remote) throws Exception {
        boolean success   = true;
        String  directory = remote + "/";

//      String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") &&!changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end   = 0;

            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }

            end = directory.indexOf("/", start);

            String path  = "";
            String paths = "";

            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");

                path = path + "/" + subDirectory;

                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        logger.debug("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end   = directory.indexOf("/", start);

                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }

        return success;
    }

    // 判断ftp服务器文件是否存在
    public boolean existFile(String path)  {
        boolean   flag       = false;
        FTPClient ftpClient = null;
        try {
            ftpClient = pool.borrowObject(1000);
            logger.info("pool now active :" + pool.getNumActive() + "  idle:"
                    + pool.getNumIdle() + " wait: "
                    + pool.getNumWaiters());
            FTPFile[] ftpFileArr = ftpClient.listFiles(path);

            if (ftpFileArr.length > 0) {
                flag = true;
            }

            pool.returnObject(ftpClient);
            return flag;
        } catch (Exception e) {
            logger.error("error ",e);
            try {
                pool.invalidateObject(ftpClient);
            } catch (Exception e1) {
               logger.error("error ",e1);
            }
        }

        return flag;


    }

    public static void main(String[] args) throws Exception {
        FtpService ftpService = new FtpService();

//        ftpService.connect("zhangc2", "9099", "yarn", "yarn");
        System.out.println(ftpService.createDirecroty("/test"));

//        System.out.println(ftpService.getFtpFileTimeStamp("dockershell" + Path.SEPARATOR
//                                                          + "container_e03_1499334976379_0006_01_000002/execute.sh"));
//        System.out.println(ftpService.getFileSize("/dockershell" + Path.SEPARATOR
//                                                  + "container_e03_1499334976379_0006_01_000002",
//                                                  "execute.sh"));
    }

    // 创建目录
    public boolean makeDirectory(String dir) {
        boolean flag = true;

        try {
            FTPClient ftpClient = pool.borrowObject(1000);
            logger.info("pool now active :" + pool.getNumActive() + "  idle:"
                    + pool.getNumIdle() + " wait: "
                    + pool.getNumWaiters());

            flag = ftpClient.makeDirectory(dir);

            if (flag) {
                logger.debug("创建文件夹" + dir + " 成功！");
            } else {
                logger.debug("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            logger.error("error ",e);
        }

        return flag;
    }

    public void upload(String path, File file) {
        FTPClient ftpClient = null;
        try {
            ftpClient = pool.borrowObject(1000);
            logger.info("pool now active :" + pool.getNumActive() + "  idle:"
                    + pool.getNumIdle() + " wait: "
                    + pool.getNumWaiters());
            createDirecroty(path);
            changeWorkingDirectory(path);
            BufferedInputStream fiStream = new BufferedInputStream(new FileInputStream(file));
            ftpClient.storeFile(file.getName(), fiStream);
            fiStream.close();
            pool.returnObject(ftpClient);
        } catch (Exception e) {
            logger.error("error ",e);
            try {
                pool.invalidateObject(ftpClient);
            } catch (Exception e1) {
                logger.error("error ",e);
            }
            logger.error("error ",e);
        }

    }


    public long getFileSize(String filePath, String fileName) {

        changeWorkingDirectory(filePath);

        FTPClient ftpClient = null;
        try {
            ftpClient = pool.borrowObject(1000);
            logger.info("pool now active :" + pool.getNumActive() + "  idle:"
                    + pool.getNumIdle() + " wait: "
                    + pool.getNumWaiters());
            long size =  ftpClient.mlistDir(filePath + "/" + fileName)[0].getSize();
            pool.returnObject(ftpClient);
            return size;

        } catch (Exception e) {
            logger.error("error ",e);
            try {
                pool.invalidateObject(ftpClient);
            } catch (Exception e1) {
                logger.error("errpr ",e1);
            }
        }

        return 0l;
    }


    public long getFtpFileTimeStamp(String filePath) {

        FTPClient ftpClient = null;
        try {
            ftpClient = pool.borrowObject(1000);
            logger.info("pool now active :" + pool.getNumActive() + "  idle:"
                    + pool.getNumIdle() + " wait: "
                    + pool.getNumWaiters());
            FTPFile[] ftpFile = ftpClient.listFiles(filePath);

            if ((ftpFile == null) || (ftpFile.length == 0)) {
                return 0l;
            }

            long timeStamp = ftpFile[0].getTimestamp().getTimeInMillis();

            pool.returnObject(ftpClient);

            return timeStamp;
        } catch (Exception e) {
            try {
                pool.invalidateObject(ftpClient);
            } catch (Exception e1) {
               logger.error("error ",e1);
            }
            logger.error("error ",e);
        }

        return 0l;


    }


}
