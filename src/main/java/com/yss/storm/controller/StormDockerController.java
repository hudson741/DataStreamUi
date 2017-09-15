package com.yss.storm.controller;

import com.yss.config.Conf;
import com.yss.storm.StormNodesService;
import com.yss.util.FileUtil;
import com.yss.util.PropertiesUtil;
import com.yss.yarn.launch.YarnLaunchService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
    public String dockerPub(@RequestParam(value = "file", required = false) MultipartFile file,
                            @RequestParam("appName") String appName,
                            @RequestParam("netUrl") String netUrl,
                            @RequestParam("uiIp") String uiIp,
                            @RequestParam("nimbusSeeds") String nimbusSeeds,
                            @RequestParam(value = "drpcServers",required = false) String drpcServers,
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

        logger.info("drpc  is "+drpcServers);
        env.put("netUrl", netUrl);
        env.put("zk", zk);
        env.put("uiIp", uiIp);
        env.put("nimbusSeeds", nimbusSeeds);
        if(StringUtils.isNotEmpty(drpcServers)) {
            env.put("drpc", drpcServers);
        }

        env.put("hadoopUser", PropertiesUtil.getProperty("hadoopUser"));
        env.put("hadoopUserPd",PropertiesUtil.getProperty("hadoopUserPd"));
        env.put("nimbusUIDockerImage" , PropertiesUtil.getProperty("NOMALDockerImage"));

        if (file != null) {
            String filePath = httpServletRequest.getServletContext().getRealPath("/");

            URI path = FileUtil.store(filePath + "dockerPub", file);

            yarnLaunchService.launchApp(path.getPath(), appName, env);
        } else {
            yarnLaunchService.launchApp(appName ,env);
        }

        return "上传成功";
    }

    @PostMapping("/stormdockerPub")
    public String stormdockerPub(@RequestParam("dockerIp") String dockerIp,
                                 @RequestParam("process") String process,
                                 @RequestParam("stormDockerConf") String cm,
                                 @RequestParam(value = "node" , required = false) String node,
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
                dockerIp,process,node,cm,null,hostMap);


        return "发布成功";
    }



}
