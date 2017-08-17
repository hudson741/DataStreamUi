package com.yss.spark.service;


import com.yss.spark.common.Result;
import org.apache.logging.log4j.core.util.CronExpression;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by xingjun.Li on 2017/8/17.
 */
@Service
public class SparkTaskService {



    public Date checkCronExpression(String expression) {
        try {
            //"0 0 12 * * ?"
            CronExpression cronExpression = new CronExpression(expression);
            return cronExpression.getNextValidTimeAfter(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
