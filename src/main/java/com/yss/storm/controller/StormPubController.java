package com.yss.storm.controller;

import com.yss.yarn.launch.YarnLaunchService;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/27
 */
@RestController
public class StormPubController {

    @Autowired
    private YarnLaunchService yarnLaunchService;


    @PostMapping("/stormdockerPub")
    public String stormdockerPub(@RequestParam("dockerIp") String dockerIp,
                                 @RequestParam("process") String process,
                                 @RequestParam(value = "host",required=false) String host) throws Exception {


        Map<String,String> hostMap = new HashMap<>();
        if(StringUtils.isNotEmpty(host)) {
            String[] hostArray = host.split(",");
            for (String hostEntry : hostArray) {
                String[] hostMapArray = hostEntry.split(":");
                hostMap.put(hostMapArray[0], hostMapArray[1]);
            }
        }

        yarnLaunchService.launchStormDockerComponent(process+"-"+System.currentTimeMillis(),
                dockerIp,process,hostMap);


        return "发布成功";
    }



}
