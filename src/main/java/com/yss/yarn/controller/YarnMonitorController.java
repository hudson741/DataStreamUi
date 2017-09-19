package com.yss.yarn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yss.config.Conf;
import com.yss.yarn.model.DockerJob;
import com.yss.yarn.monitor.YarnMonitorService;
import org.apache.storm.utils.DRPCClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        yarn.put("href", Conf.getYarnResourceUiAddress());

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
            value = "/removeDockerJob",
            method = RequestMethod.GET
    )
    public String removeDockerJob(@RequestParam("jobId") String jobId) throws Exception {
        return yarnMonitorService.removeDockerJob(jobId);
    }

    @RequestMapping(
            value  = "/drpc",
            method = RequestMethod.GET
    )
    public String drpc(@RequestParam("host") String host,@RequestParam("func") String func,@RequestParam("msg") String msg) throws  Exception{

        DRPCClient client = new DRPCClient(new HashMap(),host, 3772);
        String result = client.execute(func, msg);
        return result;

    }

    @RequestMapping(
            value  = "/yarnKillApp",
            method = RequestMethod.GET
    )
    public String yarnKillApp(@RequestParam("appId") String appId){
        try {
            return yarnMonitorService.killApp(appId);
        }catch(Throwable e){
            return "done";
        }
    }

    @RequestMapping(
            value  = "/yarnStopDockerJob",
            method = RequestMethod.GET
    )
    public String yarnStopDockerJob(@RequestParam("jobId") String jobId) throws Exception{
        return yarnMonitorService.stopDockerJob(jobId);
    }


}
