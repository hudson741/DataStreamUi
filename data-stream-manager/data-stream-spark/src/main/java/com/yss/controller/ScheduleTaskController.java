package com.yss.controller;

import com.yss.annotation.SysLog;
import com.yss.entity.SparkJobEntity;
import com.yss.entity.SparkJobLogEntity;
import com.yss.service.ScheduleJobLogService;
import com.yss.service.ScheduleJobService;
import com.yss.utils.PageUtils;
import com.yss.utils.Query;
import com.yss.utils.R;
import com.yss.validator.ValidatorUtils;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by xingjun.Li on 2017/8/22.
 */
@RestController
@RequestMapping("/sys/spark")
public class ScheduleTaskController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTaskController.class);

    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private ScheduleJobLogService scheduleJobLogService;


    @RequestMapping("/list")
    @RequiresPermissions("sys:spark:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询spark列表数据
        Query query = new Query(params);
        List<SparkJobEntity> jobList = scheduleJobService.queryList(query);
        int total = scheduleJobService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


    /**
     * 保存spark定时任务
     */
    @SysLog("新增spark定时任务")
    @RequestMapping("/save")
    @RequiresPermissions("sys:spark:save")
    public R save(@RequestBody SparkJobEntity sparkTaskEntity) {
        ValidatorUtils.validateEntity(sparkTaskEntity);
        scheduleJobService.save(sparkTaskEntity);
        return R.ok();
    }

    /**
     * 定时任务信息
     */
    @RequestMapping("/info/{taskId}")
    @RequiresPermissions("sys:spark:info")
    public R info(@PathVariable("taskId") Long taskId) {
        SparkJobEntity schedule = scheduleJobService.queryObject(taskId);
        return R.ok().put("schedule", schedule);
    }

    @SysLog("修改定时任务")
    @RequestMapping("/update")
    @RequiresPermissions("sys:spark:update")
    public R update(@RequestBody SparkJobEntity sparkTaskEntity) {
        ValidatorUtils.validateEntity(sparkTaskEntity);
        scheduleJobService.update(sparkTaskEntity);
        return R.ok();
    }

    @SysLog("删除定时任务")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:spark:delete")
    public R delete(@RequestBody Long[] jobIds) {
        scheduleJobService.deleteBatch(jobIds);
        return R.ok();
    }

    @SysLog("暂停定时任务")
    @RequestMapping("/pause")
    @RequiresPermissions("sys:spark:pause")
    public R pause(@RequestBody Long[] jobIds) {
        scheduleJobService.pause(jobIds);
        return R.ok();
    }

    @SysLog("恢复定时任务")
    @RequestMapping("/resume")
    @RequiresPermissions("sys:spark:resume")
    public R resume(@RequestBody Long[] jobIds) {
        scheduleJobService.resume(jobIds);
        return R.ok();
    }


    @SysLog("立即执行定时任务")
    @RequestMapping("/run")
    @RequiresPermissions("sys:spark:run")
    public R run(@RequestBody Long[] jobIds) {
        scheduleJobService.run(jobIds);
        return R.ok();
    }


    @RequestMapping("/log/list")
    @RequiresPermissions("sys:spark:log")
    public R log_list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<SparkJobLogEntity> jobLogList = scheduleJobLogService.queryList(query);
        int total = scheduleJobLogService.queryTotal(params);
        PageUtils pageUtil = new PageUtils(jobLogList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


}
