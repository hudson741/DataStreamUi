package com.yss.storm.controller;

import java.net.URI;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yss.storm.exception.StormRmoteSubException;
import com.yss.storm.exception.ZKConfException;
import com.yss.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.yss.TestHandler;
import com.yss.storm.StormNodesService;
import com.yss.storm.monitor.StormMonitorRestApiService;
import com.yss.storm.node.NimbusNode;
import com.yss.storm.submit.StormSubmiter;

/**
 * Created by zhangchi on 2017/5/17.
 */
@RestController
public class StormTopologySubmitController {
    private Logger                     logger = LoggerFactory.getLogger(StormTopologySubmitController.class);
    @Autowired
    private StormSubmiter              stormSubmiter;
    @Autowired
    private StormNodesService          stormNodesService;
    @Autowired
    private StormMonitorRestApiService stormMonitorRestApiService;
    @Autowired
    private TestHandler                testHandler;

    @PostMapping("/fileu")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest)  {
        String filePath = httpServletRequest.getServletContext().getRealPath("/");

        logger.info(" file tmp is " + filePath);

        URI path = null;
        try {
            path = FileUtil.store(filePath + "stormComputeJarDir", file);
        } catch (Exception e) {
            logger.error("error ",e);
           return "存取上传的topoly失败，请查看后台系统日志";
        }

        try {
            stormSubmiter.SubmitStormTopology(path);
        } catch (StormRmoteSubException e) {
            logger.error("error ",e);
            return "发布失败，请查看后台系统日志";
        } catch (ZKConfException e) {
            logger.error("error ",e);
            return "获取nimbus节点失败，请刷新Zookeeper配置，并重试";
        }

        return "上传成功";
    }


    @RequestMapping("/nimbus")
    public String nimbus() {
        List<NimbusNode> list = stormNodesService.getNimbusNodeList();

        return com.alibaba.fastjson.JSONObject.toJSONString(list);
    }

    @GetMapping(value = "/test2")
    public String printHello(ModelMap model) {
        testHandler.say();

        return "index1";
    }

    @RequestMapping("/test")
    public String getJar(@RequestParam("code") String id) {
        String result = id;

        return result;
    }
}
