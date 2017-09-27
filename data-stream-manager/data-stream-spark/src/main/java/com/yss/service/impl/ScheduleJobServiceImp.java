package com.yss.service.impl;

import com.alibaba.fastjson.JSON;
import com.yss.config.HadoopConf;
import com.yss.dao.SparkJobDao;
import com.yss.entity.SparkJobEntity;
import com.yss.quartz.ScheduleJob;
import com.yss.service.ScheduleJobService;
import com.yss.utils.Constant;
import com.yss.exception.RRException;
import com.yss.quartz.ScheduleUtils;
import com.yss.utils.ShiroUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.v2.app.webapp.App;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/8/22
 */
@Service
public class ScheduleJobServiceImp implements ScheduleJobService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobServiceImp.class);

    @Autowired
    private SparkJobDao sparkJobDao;
    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        List<SparkJobEntity> scheduleJobList = sparkJobDao.queryList(new HashMap<String, Object>());
        scheduler.clear();
        for (SparkJobEntity entity : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, entity.getJobId());
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, entity);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, entity);
            }
        }
    }

    @Override
    public void save(SparkJobEntity sparkJobEntity) {
        if (sparkJobEntity.getUserId() == null) {
            sparkJobEntity.setUserId(ShiroUtils.getUserEntity().getUserId());
        }
        if (sparkJobEntity.getCreateTime() == null) {
            sparkJobEntity.setCreateTime(new Date());
        }
        if (sparkJobEntity.getStatus() == null) {
            sparkJobEntity.setStatus(0);
        }
        sparkJobDao.save(sparkJobEntity);
        ScheduleUtils.createScheduleJob(scheduler, sparkJobEntity);
        logger.info("安装定时任务成功: " + JSON.toJSONString(sparkJobEntity));

    }

    @Override
    public List<SparkJobEntity> queryList(Map<String, Object> map) {
        return sparkJobDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sparkJobDao.queryTotal();
    }

    @Override
    public SparkJobEntity queryObject(Long jobId) {
        return sparkJobDao.queryObject(jobId);
    }

    @Override
    public void update(SparkJobEntity sparkTaskEntity) {
        if (sparkTaskEntity.getCreateTime() != null) {
            sparkTaskEntity.setCreateTime(null);
        }
        if (sparkTaskEntity.getJobId() == null) {
            throw new RRException("taskId为Null");
        }
        ScheduleUtils.updateScheduleJob(scheduler, sparkTaskEntity);
        sparkJobDao.update(sparkTaskEntity);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        }
        sparkJobDao.deleteBatch(jobIds);
    }

    @Override
    @Transactional
    public int updateBatch(Long[] jobIds, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", jobIds);
        map.put("status", status);
        return sparkJobDao.updateBatch(map);
    }

    @Override
    public void pause(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.pauseJob(scheduler, jobId);
        }
        updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
    }


    @Override
    public void resume(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.resumeJob(scheduler, jobId);
        }
        updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());

    }

    @Override
    public void run(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.run(scheduler, queryObject(jobId));
        }
    }

    @Override
    public synchronized void killApp(String appId) throws IOException, YarnException {

        ApplicationId applicationId = ScheduleJob.getAppId(appId);
        if (applicationId == null) {
            return;
        }

        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(HadoopConf.yarnConfiguration);
        yarnClient.start();
        yarnClient.killApplication(applicationId);

    }
}
