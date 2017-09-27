package com.yss.service.impl;

import com.yss.dao.SparkJobLogDao;
import com.yss.entity.SparkJobLogEntity;
import com.yss.entity.SparkJobVo;
import com.yss.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/8
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImp implements ScheduleJobLogService {

    @Autowired
    private SparkJobLogDao sparkJobLogDao;

    @Override
    public void insertNewLog(SparkJobLogEntity entity) {
        sparkJobLogDao.save(entity);
    }

    @Override
    public void updateLog(SparkJobLogEntity entity) {
        sparkJobLogDao.update(entity);
    }

    @Override
    public List<SparkJobLogEntity> queryList(Map<String, Object> params) {
        return sparkJobLogDao.queryList(params);
    }

    @Override
    public List<SparkJobVo> queryVoList(Map<String, Object> params) throws UnknownHostException {
        List<SparkJobLogEntity> sparkJobLogEntities = queryList(params);
        List<SparkJobVo> vos = new ArrayList<>();
        for(SparkJobLogEntity entity : sparkJobLogEntities){
            vos.add(entity.changeToVo());
        }
        return vos;
    }

    @Override
    public int queryTotal(Map<String, Object> params) {
        return sparkJobLogDao.queryTotal(new HashMap<String, Object>());
    }
}
