package com.yss.ftp;

import com.yss.config.Conf;
import com.yss.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/8/28
 */
public class FtpConnectionFactory extends BasePooledObjectFactory<FTPClient> {

    private Logger logger = LoggerFactory.getLogger(FtpService.class);

    @Override
    public FTPClient create() throws Exception {
        try {
            FTPClient ftpClient = new FTPClient();
            int reply;
            ftpClient.connect(getAddr(), Integer.parseInt(getPort()));
            ftpClient.login(getUserName(), getPassword());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.enterLocalPassiveMode();

            return ftpClient;
        }catch(Throwable e){
            logger.error("ftp连接异常，请确认ftpServer健康状态 ",e);
        }

        return null;
    }

    public static String getRemoteFtpServerAddress() {
        if (StringUtils.isNotEmpty(getUserName())) {
            return "ftp://" + getUserName() + ":" + getPassword() + "@" + getAddr() + ":" + getPort();
        }

        return "ftp://" + getAddr() + ":" + getPort();
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<FTPClient>(ftpClient);
    }

    public static String getUserName() {
        return Conf.getFS_DEFAULT_FS().split("@")[0].split(":")[1].replace("//","");
    }


    public static String getPassword() {
        return Conf.getFS_DEFAULT_FS().split("@")[0].split(":")[2];
    }


    public static String getAddr() {

        return Conf.getFS_DEFAULT_FS().split("@")[1].split(":")[0];
    }


    public static String getPort() {
        return Conf.getFS_DEFAULT_FS().split("@")[1].split(":")[1];
    }

}
