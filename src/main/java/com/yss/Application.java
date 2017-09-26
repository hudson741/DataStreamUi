package com.yss;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhangchi on 2017/3/20.
 */
@Configuration
@EnableAutoConfiguration
@ImportResource(value = { "classpath:conf/act-sofa.xml"})
@ComponentScan(basePackages = { "com.yss" })
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
            if(ArrayUtils.isNotEmpty(args) && args[0].equals("ftp")){
                System.setProperty("ftp","1");
                ApplicationContext factory = new ClassPathXmlApplicationContext(new String[]{"classpath:conf/act-ftp.xml"});
        }else {
            SpringApplication.run(Application.class, args);
        }

    }
}
