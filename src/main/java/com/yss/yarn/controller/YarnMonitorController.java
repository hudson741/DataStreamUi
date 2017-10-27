package com.yss.yarn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yss.auth.AuthConfig;
import com.yss.config.Conf;
import com.yss.yarn.model.DockerJob;
import com.yss.yarn.monitor.YarnMonitorService;
import org.apache.storm.utils.DRPCClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    public String yarnIndex() throws Exception {
        JSONObject yarnCluster = yarnMonitorService.getCluster();

        JSONArray yarnApp = yarnMonitorService.getApps();

        Map<String,Object> yarnNodes = yarnMonitorService.getNodes();

        JSONObject yarn = new JSONObject();

        yarn.put("cluster",yarnCluster);
        yarn.put("apps",yarnApp);
        yarn.put("nodes",yarnNodes);
        yarn.put("href", Conf.getYarnResourceUiAddress());

        List<DockerJob> jobs = yarnMonitorService.getDockerJobs();

        yarn.put("dockerjobs",jobs);

        return yarn.toJSONString();
    }

    private int numPercent(int a,int b){
        try {
            double f1 = new BigDecimal((float) a / b).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return (int) (f1 * 100);
        }catch (Throwable e){
            return 0;
        }
    }

    /**
     * storm 单容器
     * @param model
     * @return
     */
    @RequestMapping(
            value  = "/dockerindexftl",
            method = RequestMethod.GET
    )
    public ModelAndView dockerindexftl(ModelMap model) {
        //index.html全局首页

        List<DockerJob> jobs = yarnMonitorService.getDockerJobs();

        JSONObject yarn = new JSONObject();

        yarn.put("dockerjobs",jobs);

        ModelAndView modelAndView = new ModelAndView("/nodes/docker");

        modelAndView.addObject("yarn",yarn);

        return modelAndView;
    }


    @RequestMapping(
            value  = "/yarnindexftl",
            method = RequestMethod.GET
    )
    public ModelAndView yarnIndexftl(HttpServletRequest httpServletRequest) throws Exception {
        ModelAndView modelAndView = new ModelAndView("/yarn/yindex");

        JSONObject yarnCluster = yarnMonitorService.getCluster();

        JSONArray yarnApp = yarnMonitorService.getApps();

        Map<String,Object> yarnNodes = yarnMonitorService.getNodes();

        JSONObject yarn = new JSONObject();

        yarn.put("cluster",yarnCluster);
        yarn.put("apps",yarnApp);
        yarn.put("nodes",yarnNodes);
        yarn.put("href", Conf.getYarnResourceUiAddress());



        if(yarnCluster!=null) {
            int active2total = numPercent(yarnCluster.getInteger("activeNodes") , yarnCluster.getInteger("totalNodes"));
            int appRun2appT = numPercent(yarnCluster.getInteger("appsRunning") ,yarnCluster.getInteger("appsSubmitted"));
            int vc2tc = numPercent(yarnCluster.getInteger("allocatedVirtualCores") , yarnCluster.getInteger("totalVirtualCores"));
            int ag2tg = numPercent(yarnCluster.getInteger("allocateGB") , yarnCluster.getInteger("totalGB"));

            modelAndView.addObject("active2total",active2total);
            modelAndView.addObject("appRun2appT",appRun2appT);
            modelAndView.addObject("vc2tc",vc2tc);
            modelAndView.addObject("ag2tg",ag2tg);
        }

        modelAndView.addObject("yarn",yarn);

        AuthConfig.Auth auth = AuthConfig.getUser(httpServletRequest);
        if(auth!=null && auth == AuthConfig.Auth.SUPER){
            modelAndView.addObject("auth",1);
        }else{
            modelAndView.addObject("auth",0);
        }

        return modelAndView;
    }

    @RequestMapping(
            value  = "/yarnRestart",
            method = RequestMethod.POST
    )
    public String yarnRestart(@RequestParam("jobId") String jobId) throws  Exception{

        return yarnMonitorService.yarnRestart(jobId);

    }

    @RequestMapping(
            value = "/removeDockerJob",
            method = RequestMethod.POST
    )
    public String removeDockerJob(@RequestParam("jobId") String jobId) throws Exception {
        return yarnMonitorService.removeDockerJob(jobId);
    }

    @RequestMapping(
            value  = "/drpc",
            method = RequestMethod.GET
    )
    public String drpc(@RequestParam("host") String host,@RequestParam("func") String func,@RequestParam("msg") String msg) throws  Exception{

        DRPCClient client = new DRPCClient(new HashMap(),host, 3772);
        String result = client.execute(func, msg);
        return result;

    }

    @RequestMapping(
            value  = "/yarnKillApp",
            method = RequestMethod.GET
    )
    public String yarnKillApp(@RequestParam("appId") String appId){
        try {
            return yarnMonitorService.killApp(appId);
        }catch(Throwable e){
            return "已操作，请刷新界面";
        }
    }

    @RequestMapping(
            value  = "/yarnStopDockerJob",
            method = RequestMethod.POST
    )
    public String yarnStopDockerJob(@RequestParam("jobId") String jobId) throws Exception{
        return yarnMonitorService.stopDockerJob(jobId);
    }


}
