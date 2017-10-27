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


import com.yss.storm.StormNodesService;
import com.yss.storm.monitor.StormMonitorRestApiService;
import com.yss.storm.node.NimbusNode;
import com.yss.storm.submit.StormSubmiter;
import org.springframework.web.servlet.ModelAndView;

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


    @PostMapping("/stormTopoPub")
    public ModelAndView stormTopoPub(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest)  {
        String filePath = httpServletRequest.getServletContext().getRealPath("/");

        logger.info(" file tmp is " + filePath);

        URI path = null;
        try {
            path = FileUtil.store(filePath + "stormComputeJarDir", file);
        } catch (Exception e) {
            logger.error("error ",e);
        }

        try {

            stormSubmiter.SubmitStormTopology(path);
        } catch (StormRmoteSubException e) {
            logger.error("error ",e);
        } catch (ZKConfException e) {
            logger.error("error ",e);
        }

        return new ModelAndView("/index");
    }


    @RequestMapping("/nimbus")
    public String nimbus() {
        List<NimbusNode> list = stormNodesService.getNimbusNodeList();

        return com.alibaba.fastjson.JSONObject.toJSONString(list);
    }



    @RequestMapping("/test")
    public String getJar(@RequestParam("code") String id) {
        String result = id;

        return result;
    }
}
