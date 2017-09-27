package com.yss.dao;

import com.yss.entity.SparkJobEntity;

import java.util.Map;


public interface SparkJobDao extends BaseDao<SparkJobEntity> {

        int updateBatch(Map<String, Object> param);

        int insert(SparkJobEntity object);
}