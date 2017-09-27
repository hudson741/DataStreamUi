package com.yss.task;

import com.yss.entity.ApplicationKeyEntity;
import com.yss.service.ApplicationKeyService;
import com.yss.ws.SSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lixingjun on 2017/9/2.
 */
@Component
public class InitDBService implements InitializingBean{

    private static Logger logger = LoggerFactory.getLogger(InitDBService.class);

    @Autowired
    private ApplicationKeyService service;

    @Autowired
    private SSHService sshService;

    @Override
    public void afterPropertiesSet() throws Exception {


        List<ApplicationKeyEntity> applicationKeyEntities =  service.getAllApplications();
        if(applicationKeyEntities.size()==0){
            logger.info("初始化application key");
            String passphrase = sshService.keygen();
            String publicKey = sshService.getPublicKey();
            String privateKey = sshService.getPrivateKey();

            ApplicationKeyEntity entity = new ApplicationKeyEntity();
            entity.setPrivateKey(privateKey);
            entity.setPublicKey(publicKey);
            entity.setPassphrase(passphrase);

            service.addNewApplicationKey(entity);

        }


    }
}
