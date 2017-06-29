package com.yss.yarn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yss.yarn.model.DockerJob;
import com.yss.yarn.monitor.YarnMonitorService;
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
    public String yarnfuck() throws Exception {


        Map<String,Object> yarnApp = yarnMonitorService.getApps();

        JSONObject yarn = new JSONObject();

        yarn.put("totalNodes",4);
        yarn.put("totalVirtualCores",24);
        yarn.put("totalMB","40G");
        yarn.put("apps",yarnApp);

        List<DockerJob> jobs = yarnMonitorService.getDockerJobs();

        yarn.put("dockerjobs",jobs);

        return yarn.toJSONString();
    }



}
