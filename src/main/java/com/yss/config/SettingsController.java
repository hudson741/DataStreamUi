package com.yss.config;

import com.alibaba.fastjson.JSON;
import com.yss.storm.StormNodesService;
import com.yss.yarn.discovery.YarnMasterServerDiscovery;
import org.jcodings.util.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/28
 */

@RestController
public class SettingsController {

    @Autowired
    private YarnMasterServerDiscovery yarnMasterServerDiscovery;

    @Autowired
    private StormNodesService stormNodesService;

    @RequestMapping(
            value  = "/settingsSet",
            method = RequestMethod.POST
    )
    public String settingsSet(@RequestParam("stormZk") String stormZk,
                               @RequestParam("hdfs") String hdfs,
                               @RequestParam("yarn") String  yarn,
                               @RequestParam("yarns") String yarns,
                               @RequestParam("yarnui") String yarnui) throws Exception {

        Conf.setFS_DEFAULT_FS(hdfs);
        Conf.setSTORM_ZK(stormZk);
        Conf.setYARN_RESOURCEMANAGER_ADDREES(yarn);
        Conf.setYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS(yarns);
        Conf.setYarnResourceUiAddress(yarnui);

        yarnMasterServerDiscovery.updateCuratorFrameWork();

        stormNodesService.updateCuratorFrameWork();

        return JSON.toJSONString("已更新");
    }

    @RequestMapping(
            value  = "/conf",
            method = RequestMethod.GET
    )
    public String settings() throws Exception {

       Map<String,String> data = new HashMap();
        data.put("stormZk" , Conf.getSTORM_ZK());
        data.put("hdfs",Conf.getFS_DEFAULT_FS());
        data.put("yarn",Conf.getYARN_RESOURCEMANAGER_ADDREES());
        data.put("yarns",Conf.getYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS());
        data.put("yarnui",Conf.getYarnResourceUiAddress());

       return JSON.toJSONString(data);
    }


}
