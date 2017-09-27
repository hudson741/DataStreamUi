package com.yss.service;

import com.yss.entity.SparkJobEntity;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/8/22
 */
public interface ScheduleJobService {

    void save(SparkJobEntity sparkTaskEntity);

    List<SparkJobEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    SparkJobEntity queryObject(Long taskId);

    void update(SparkJobEntity scheduleJob);

    void deleteBatch(Long[] jobIds);

    void pause(Long[] jobIds);

    void resume(Long[] jobIds);

    int updateBatch(Long[] jobIds, int status);

    void run(Long[] jobIds);

     void  killApp(String appId) throws IOException, YarnException;

}
