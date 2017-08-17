package com.yss.test;

import com.yss.Application;
import com.yss.spark.service.SparkTaskService;
import org.apache.storm.shade.org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Test;

import java.util.Date;


/**
 * Created by xingjun.Li on 2017/8/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SparkTaskServiceTest {

    @Autowired
    SparkTaskService service;

    @Test
    public void findAllUsers()  {
        Date date = service.checkCronExpression("0 0 12 * * ?");
        Assert.assertNotNull(date);
    }

}
