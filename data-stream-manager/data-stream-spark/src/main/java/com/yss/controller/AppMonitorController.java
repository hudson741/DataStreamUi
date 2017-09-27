package com.yss.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yss.entity.SparkJobLogEntity;
import com.yss.entity.SparkJobVo;
import com.yss.exception.RRException;
import com.yss.service.ScheduleJobLogService;
import com.yss.service.ScheduleJobService;
import com.yss.utils.HttpKit;
import com.yss.utils.PageUtils;
import com.yss.utils.Query;
import com.yss.utils.R;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingjun.Li on 2017/8/21.
 */
@Controller
@RequestMapping("/sys/yarn")
public class AppMonitorController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    private ScheduleJobLogService scheduleJobLogService;

    private final static String FILTER_STATUS_FILED = "status";

    @RequestMapping("/metrics")
    @ResponseBody
    public R getYarnMetrics(){
        String yarnMetrics = null;
        try {
            yarnMetrics = HttpKit.sendGet("http://yarn2:8088/ws/v1/cluster/metrics", new HashMap<String, String>());
        } catch (IOException e) {
            throw new RRException("Yarn服务请求异常", 100);
        }
        JSONObject object = JSON.parseObject(yarnMetrics);
        return R.ok().put("clusterMetrics", object.get("clusterMetrics"));
    }


    @RequestMapping("/app_list")
    @ResponseBody
    public R listAllYarnApps(@RequestParam Map<String, Object> params) throws UnknownHostException {
        //查询spark_yarn_app数据
        if(params.get(FILTER_STATUS_FILED)!=null && params.get(FILTER_STATUS_FILED).equals("ALL")){
            params.remove(FILTER_STATUS_FILED);
        }
        Query query = new Query(params);
        List<SparkJobVo> vos = scheduleJobLogService.queryVoList(query);
        int total = scheduleJobLogService.queryTotal(params);
        PageUtils pageUtil = new PageUtils(vos, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


    @RequestMapping("/kill/{app_id}")
    @ResponseBody
    public R killApp(@PathVariable("app_id") String AppId) throws IOException, YarnException {
        scheduleJobService.killApp(AppId);
        return R.ok();
    }

    @RequestMapping("/apps")
    public ModelAndView printWelcome(@RequestParam Map<String, Object> params) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sys/app_monitor.html");
        return modelAndView;
    }


}
