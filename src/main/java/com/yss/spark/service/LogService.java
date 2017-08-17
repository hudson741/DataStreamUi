package com.yss.spark.service;

import com.yss.spark.persistence.entity.LogEntity;
import com.yss.spark.persistence.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingjun.Li on 2017/8/17.
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;


    public int insertNewLog(Integer userId, String userName, String ip, String action){
        LogEntity logEntity = new LogEntity(userId, userName, ip, action);
        return logMapper.insert(logEntity);
    }

}
