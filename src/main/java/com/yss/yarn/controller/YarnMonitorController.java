package com.yss.yarn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yss.yarn.model.DockerJob;
import com.yss.yarn.monitor.YarnMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/27
 */

@RestController
public class YarnMonitorController {

    @Autowired
    private YarnMonitorService yarnMonitorService;


    @RequestMapping(
            value  = "/yarnindex",
            method = RequestMethod.GET
    )
    public String yarnIndex() throws Exception {
        JSONObject yarnCluster = yarnMonitorService.getCluster();

        JSONArray yarnApp = yarnMonitorService.getApps();

        Map<String,Object> yarnNodes = yarnMonitorService.getNodes();

        JSONObject yarn = new JSONObject();

        yarn.put("cluster",yarnCluster);
        yarn.put("apps",yarnApp);
        yarn.put("nodes",yarnNodes);

        List<DockerJob> jobs = yarnMonitorService.getDockerJobs();

        yarn.put("dockerjobs",jobs);

        return yarn.toJSONString();
    }

    @RequestMapping(
            value  = "/yarnRestart",
            method = RequestMethod.GET
    )
    public String yarnRestart(@RequestParam("jobId") String jobId) throws  Exception{

        return yarnMonitorService.yarnRestart(jobId);

    }

    @RequestMapping(
            value  = "/yarnKillApp",
            method = RequestMethod.GET
    )
    public String yarnKillApp(@RequestParam("appId") String appId) throws Exception{
        return yarnMonitorService.killApp(appId);
    }

    @RequestMapping(
            value  = "/yarnStopDockerJob",
            method = RequestMethod.GET
    )
    public String yarnStopDockerJob(@RequestParam("jobId") String jobId) throws Exception{
        return yarnMonitorService.stopDockerJob(jobId);
    }


}
