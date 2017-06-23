package com.yss.yarn.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.yss.storm.controller.StormTopologySubmitController;
import com.yss.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yss.yarn.launch.YarnLaunchService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/19
 */
@RestController
public class YarnLaunchController {

    private Logger logger = LoggerFactory.getLogger(YarnLaunchController.class);
    @Autowired
    private YarnLaunchService yarnLaunchService;

    @PostMapping("/dockerPub")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("hdfsUrl") String hdfsUrl,
                                   @RequestParam("yarnUrl") String yarnUrl,
                                   @RequestParam("launchMainClass") String launchMainClass,
                                   HttpServletRequest httpServletRequest) throws Exception {
        String filePath = httpServletRequest.getServletContext().getRealPath("/");

        logger.info(" file tmp is " + filePath);

        URI path = FileUtil.store(filePath + "dockerPub", file);

        String jarName = file.getOriginalFilename();

        yarnLaunchService.launchStorm(path.getPath(),jarName,hdfsUrl,yarnUrl,launchMainClass);

        return "上传成功";
    }

}
