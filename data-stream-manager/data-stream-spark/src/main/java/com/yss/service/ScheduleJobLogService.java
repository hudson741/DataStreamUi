package com.yss.service;

import com.yss.entity.SparkJobLogEntity;
import com.yss.entity.SparkJobVo;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/8/22
 */
public interface ScheduleJobLogService {

    void insertNewLog(SparkJobLogEntity entity);

    void updateLog(SparkJobLogEntity entity);

    List<SparkJobLogEntity> queryList(Map<String, Object> params);

    List<SparkJobVo> queryVoList(Map<String, Object> params) throws UnknownHostException;

    int queryTotal(Map<String, Object> params);

}
