package com.yss.controller;

import java.net.URI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.yss.TestHandler;
import com.yss.storage.StorageService;
import com.yss.storm.monitor.StormMonitorRestApiService;
import com.yss.storm.nimbus.NimbusNode;
import com.yss.storm.nimbus.NimbusNodesService;
import com.yss.storm.submit.StormSubmiter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangchi on 2017/5/17.
 */
@RestController
public class StormTopologySubmitController {
    @Autowired
    private StormSubmiter              stormSubmiter;
    @Autowired
    private StorageService             storageService;
    @Autowired
    private NimbusNodesService         nimbusNodesService;
    @Autowired
    private StormMonitorRestApiService stormMonitorRestApiService;
    @Autowired
    private TestHandler                testHandler;

    @PostMapping("/fileu")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {


        URI path = storageService.store(file);

        stormSubmiter.SubmitStormTopology(path);

        return "上传成功";
    }

    @RequestMapping("/nimbus")
    public String nimbus() {
        List<NimbusNode> list = nimbusNodesService.getNimbusNodeList();

        return com.alibaba.fastjson.JSONObject.toJSONString(list);
    }

    @GetMapping(value = "/a")
    public String printHello(ModelMap model) {
        testHandler.say();

        return "index1";
    }

    /**
     * 默认首页
     * @param model
     * @return
     */
    @RequestMapping(
        value  = "/",
        method = RequestMethod.GET
    )
    public ModelAndView printHello2(ModelMap model, HttpServletRequest httpServletRequest) {

      System.out.println(  httpServletRequest.getServletContext().getRealPath("/")  );


//      return new ModelAndView("file1");
        return new ModelAndView("index");
    }

    @RequestMapping("/fuck")
    public String getJar(@RequestParam("code") String id) {
        String result = id;

        return result;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
