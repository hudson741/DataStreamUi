package com.yss.config;

import java.util.HashMap;
import java.util.Map;

import com.yss.yarn.discovery.ServerAddressDiscovery;
import com.yss.yarn.launch.YarnLaunchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import com.yss.storm.StormNodesService;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/28
 */
@RestController
public class SettingsController {
    @Autowired
    private ServerAddressDiscovery serverAddressDiscovery;
    @Autowired
    private StormNodesService         stormNodesService;
    @Autowired
    private YarnLaunchService yarnLaunchService;

    @RequestMapping(
        value  = "/conf",
        method = RequestMethod.GET
    )
    public String settings() throws Exception {
        Map<String, String> data = new HashMap();

        data.put("stormZk", Conf.getSTORM_ZK());
        data.put("hdfs", Conf.getFS_DEFAULT_FS());
        data.put("yarn", Conf.getYARN_RESOURCEMANAGER_ADDREES());
        data.put("yarns", Conf.getYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS());
        data.put("yarnui", Conf.getYarnResourceUiAddress());
        data.put("yarnJavaHome", Conf.getYarnJavaHome());
        data.put("yarnHadoopHome",Conf.getYarnHadoopHome());

        return JSON.toJSONString(data);
    }

    @RequestMapping(
        value  = "/settingsSet",
        method = RequestMethod.POST
    )
    public String settingsSet(@RequestParam("stormZk") String stormZk,
                              @RequestParam("hdfs") String hdfs,
                              @RequestParam("yarn") String yarn,
                              @RequestParam("yarns") String yarns,
                              @RequestParam("yarnui") String yarnui,
                              @RequestParam("yarnJavaHome") String yarnJavaHome,
                              @RequestParam("yarnHadoopHome") String yarnHadoopHome)
            throws Exception {
        Conf.setFS_DEFAULT_FS(hdfs);
        Conf.setSTORM_ZK(stormZk);
        Conf.setYARN_RESOURCEMANAGER_ADDREES(yarn);
        Conf.setYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS(yarns);
        Conf.setYarnResourceUiAddress(yarnui);
        Conf.setYarnJavaHome(yarnJavaHome);
        Conf.setYarnHadoopHome(yarnHadoopHome);
        serverAddressDiscovery.refresh();
        stormNodesService.refresh();
        yarnLaunchService.refresh();

        return JSON.toJSONString("已更新");
    }
}
