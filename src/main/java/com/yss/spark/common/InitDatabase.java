package com.yss.spark.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by xingjun.Li on 2017/8/17.
 */
@Component
//@Configuration
//@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true )
public class InitDatabase implements InitializingBean{

    private static Logger logger = LoggerFactory.getLogger(InitDatabase.class);

    private String BASE_DIR = InitDatabase.class.getClassLoader().getResource(".").getPath();

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;



    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            System.setProperty("h2.baseDir", BASE_DIR);

            Class.forName(driverClassName);

            Connection conn = DriverManager.getConnection(dbUrl,username, password);
            Statement stmt = conn.createStatement();

            stmt.execute("create table if not exists operation_log (log_id INTEGER PRIMARY KEY AUTO_INCREMENT, user_id INTEGER, user_name varchar, ip varchar, action varchar)");


            stmt.close();
            conn.close();

        }catch (Exception e){
            logger.error("h2数据库初始化失败", e);
            System.exit(-2);
        }




    }


}
