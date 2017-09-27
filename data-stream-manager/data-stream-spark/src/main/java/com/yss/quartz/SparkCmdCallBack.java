package com.yss.quartz;

import com.alibaba.fastjson.JSON;
import com.yss.entity.SparkJobLogEntity;
import com.yss.service.ScheduleJobLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/13
 */
public class SparkCmdCallBack implements CmdCallBack{

    private static final Logger logger = LoggerFactory.getLogger(SparkCmdCallBack.class);

    private SparkJobLogEntity log;
    private ScheduleJobLogService scheduleJobLogService;

    public SparkCmdCallBack(SparkJobLogEntity log, ScheduleJobLogService scheduleJobLogService){
        this.log = log;
        this.scheduleJobLogService = scheduleJobLogService;
    }

    @Override
    public void insertNewOutput(String out) {
        logger.info(out);
    }

    @Override
    public void insertNewErrorOutput(String out) {
        logger.info(out);
        if(out.contains("Application report for")){
            boolean needUpdate = false;
            if(log.getYarnAppId()==null){
                String aa = out.split("Application report for")[1];
                String appId = aa.split("\\(state")[0].trim();
                log.setYarnAppId(appId);
                needUpdate = true;
            }

            String status = out.split("state:")[1].trim();
            if(!status.equals(log.getStatus())){
                log.setStatus(status.replace(")",""));
                needUpdate = true;
            }
            if(needUpdate){
                scheduleJobLogService.updateLog(log);
            }


        }
    }

    @Override
    public void exitCode(int exitCode) {

    }
}
