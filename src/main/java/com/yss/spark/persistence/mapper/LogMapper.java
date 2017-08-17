package com.yss.spark.persistence.mapper;

import com.yss.spark.persistence.entity.LogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by xingjun.Li on 2017/8/17.
 */
public interface LogMapper {

    int insert(LogEntity record);

}