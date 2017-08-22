package com.yss.ftp;

import com.yss.util.FileUtil;
import com.yss.util.PropertiesUtil;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/7/7
 */
public class SofaFtpServer {

    private Logger logger        = LoggerFactory.getLogger(SofaFtpServer.class);

    private FtpServerFactory serverFactory;
    private String              homeDirectory;

    public SofaFtpServer() throws FtpException {

        int serverPort = Integer.parseInt(PropertiesUtil.getProperty("ftpPort"));
        String userName = PropertiesUtil.getProperty("ftpUserName");
        String password = PropertiesUtil.getProperty("ftpPassword");

        serverFactory = new FtpServerFactory();

        try {
            homeDirectory = FileUtil.getJarPath(SofaFtpServer.class) + "/ftp";
        } catch (IOException e) {
            homeDirectory = "/opt";
        }

        FileUtil.init(homeDirectory);

        logger.info("homeDirectory :" +homeDirectory);

        BaseUser anonymous = new BaseUser();

        anonymous.setName("anonymous");
        anonymous.setHomeDirectory(homeDirectory);
        serverFactory.getUserManager().save(anonymous);
        List<Authority> authorities = new ArrayList<Authority>();

        authorities.add(new WritePermission());
        BaseUser baseUser = new BaseUser();

        baseUser.setName(userName);
        baseUser.setPassword(password);
        baseUser.setHomeDirectory(homeDirectory);
        baseUser.setAuthorities(authorities);
        serverFactory.getUserManager().save(baseUser);

        ListenerFactory factory = new ListenerFactory();

        factory.setPort(serverPort);
        serverFactory.addListener("default", factory.createListener());

        FtpServer server = serverFactory.createServer();

        server.start();
    }


    public FtpServerFactory getServerFactory() {
        return serverFactory;
    }

    public void setServerFactory(FtpServerFactory serverFactory) {
        this.serverFactory = serverFactory;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

}
