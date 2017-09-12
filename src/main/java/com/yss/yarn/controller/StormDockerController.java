package com.yss.yarn.controller;

import java.net.URI;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yss.storm.StormNodesService;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yss.config.Conf;
import com.yss.util.FileUtil;
import com.yss.yarn.launch.YarnLaunchService;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/19
 */
@RestController
public class StormDockerController {
    private Logger            logger = LoggerFactory.getLogger(StormDockerController.class);
    @Autowired
    private YarnLaunchService yarnLaunchService;

    @Autowired
    private StormNodesService stormNodesService;

    @PostMapping("/dockerPub")
    public String dockerPub(@RequestParam("file") MultipartFile file,
                            @RequestParam("appName") String appName,
                            @RequestParam("netUrl") String netUrl,
                            @RequestParam("stormDockerConf") String stormDockerConf,
                            @RequestParam("uiIp") String uiIp,
                            @RequestParam("nimbusSeeds") String nimbusSeeds,
                            HttpServletRequest httpServletRequest)
            throws Exception {
        String zk = Conf.getSTORM_ZK();

        if (StringUtils.isEmpty(zk)) {
            return "请在conf签中重新设定storm集群zookeeper地址";
        }

        if (StringUtils.isEmpty(Conf.getYARN_RESOURCEMANAGER_ADDREES())
                || StringUtils.isEmpty(Conf.getFS_DEFAULT_FS())
                || StringUtils.isEmpty(Conf.getYARN_RESOURCEMANAGER_SCHEDULER_ADDRESS())
                || StringUtils.isEmpty(Conf.getYarnResourceUiAddress())) {
            return "请在conf签中重新设定yarn相关配置地址";
        }

        try {
            stormNodesService.clearStorm();
        }catch(Exception e){
            return "请刷新zookeeper配置";
        }

        Map<String, String> env = new HashMap<>();

        env.put("netUrl", netUrl);
        env.put("cm", stormDockerConf);
        env.put("zk", zk);
        env.put("uiIp", uiIp);
        env.put("nimbusSeeds", nimbusSeeds);

        if (file != null) {
            String filePath = httpServletRequest.getServletContext().getRealPath("/");

            URI path = FileUtil.store(filePath + "dockerPub", file);

            yarnLaunchService.launchApp(path.getPath(), appName, env);
        } else {
            yarnLaunchService.launchApp(appName ,env);
        }

        return "上传成功";
    }


}
