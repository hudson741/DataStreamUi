package com.yss.storm.controller;

import java.util.HashMap;
import java.util.Map;

import com.yss.yarn.discovery.YarnThriftClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import com.yss.storm.model.Host;
import com.yss.storm.model.Topology;
import com.yss.storm.monitor.StormMonitorRestApiService;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class StormUIViewController {
    @Autowired
    private StormMonitorRestApiService stormMonitorRestApiService;

    @Autowired
    private YarnThriftClient yarnThriftClient;

    @RequestMapping(
            value  = "/activeTopoftl",
            method = RequestMethod.POST
    )
    public String activeTopology(@RequestParam("topoid") String topoid) throws Exception {
        Map<String, Object> data = stormMonitorRestApiService.activeTopo(topoid);

//        return new ModelAndView("");
        return JSON.toJSONString(data);
    }

    @RequestMapping(
            value  = "/deactiveTopoftl",
            method = RequestMethod.POST
    )
    public String deactiveTopology(@RequestParam("topoid") String topoid) throws Exception {
        Map<String, Object> data = stormMonitorRestApiService.deactiveTopo(topoid);

        return JSON.toJSONString(data);
    }

    @RequestMapping(
            value  = "/hostsftl",
            method = RequestMethod.GET
    )
    public ModelAndView hosts() throws Exception {
        Map<String, Host> hosts = stormMonitorRestApiService.getHostWithExecutorDetails();

        ModelAndView modelAndView = new ModelAndView("/storm/host");
        modelAndView.addObject("host",hosts);
        return modelAndView;
    }

    @RequestMapping(
            value  = "/killTopologyftl",
            method = RequestMethod.POST
    )
    public String killTopology(@RequestParam("topoid") String topoid) throws Exception {
        Map<String, Object> data = stormMonitorRestApiService.killTopology(topoid);

        return JSON.toJSONString(data);
    }

    @RequestMapping(
            value  = "/streamManagerftl",
            method = RequestMethod.GET
    )
    public ModelAndView streamManager() {
        Map<String, Object> topologies = stormMonitorRestApiService.getTopologies();

        ModelAndView modelAndView = new ModelAndView("/storm/streamManager");

        modelAndView.addObject("topolist",topologies);
        return modelAndView;
    }



    @RequestMapping(value = "/topoftl")
    public ModelAndView topo(@RequestParam("topoid") String topoid) {
        Map<String, Object> topo = stormMonitorRestApiService.getTopologyDetailsWithComponentDetails(topoid);

        ModelAndView modelAndView = new ModelAndView("/storm/topo");
        modelAndView.addObject("topo",topo);
        return modelAndView;
    }

    @RequestMapping(
            value  = "/topoCExeModifyftl",
            method = RequestMethod.POST
    )
    public String topoCExeModify(@RequestParam("topoid") String topoid, @RequestParam("cid") String cid,
                                 @RequestParam("has") String has, @RequestParam("num") int num)
            throws Exception {
        Map<String, Integer> map = new HashMap<>();

        int hasI = 0;
        if(StringUtils.isNotEmpty(has)){
            hasI = Integer.parseInt(has);
        }

        num = hasI+num;
        map.put(cid, num);

        Map<String, Object> data = stormMonitorRestApiService.modufyTopoComponentExecutors(topoid, map);

        return JSON.toJSONString(data);
    }

    @RequestMapping(
            value  = "/topomftl",
            method = RequestMethod.GET
    )
    public ModelAndView topoManager(@RequestParam("topoid") String topoid) {
        Map<String, Object> topo = stormMonitorRestApiService.getTopoComponents(topoid);

        ModelAndView modelAndView = new ModelAndView("/storm/topom");
        modelAndView.addObject("topo",topo);

        return modelAndView;
    }

    @RequestMapping(
            value  = "/topoWorkNumModifyftl",
            method = RequestMethod.POST
    )
    public String topoWorkNumModify(@RequestParam("topoid") String topoid,@RequestParam("has") String has, @RequestParam("num") int num)
            throws Exception {

        if(StringUtils.isNotEmpty(has)){
            num = num+Integer.parseInt(has);
        }

        Map<String, Object> data = stormMonitorRestApiService.modifyTopoWorkerNum(topoid, num);

        return JSON.toJSONString(data);
    }

    @RequestMapping(
            value  = "/topolistftl",
            method = RequestMethod.GET
    )
    public String topolist() {
        Topology[] topologies = stormMonitorRestApiService.getTopologiesSummary();

        return JSON.toJSONString(topologies);
    }


    @RequestMapping(
            value  = "/overviewftl",
            method = RequestMethod.GET
    )
    public ModelAndView view() throws Exception {
        Map<String, Object> overview          = new HashMap<String, Object>();
        Map<String, String> clusterConfig     = stormMonitorRestApiService.getClusterConfigStr();
        Map<String, Object> clusterSummary    = stormMonitorRestApiService.getClusterSummary();
        Map<String, Object> supervisorSummary = stormMonitorRestApiService.getSupervisorSummary();
        Map<String, Object> nimbusSummary     = stormMonitorRestApiService.getNimbusSummary();
        Topology[]          topos             = stormMonitorRestApiService.getTopologiesSummary();

        overview.put("cluserConfig", clusterConfig);
        overview.put("clusterSummary", clusterSummary);
        overview.put("supervisorSummary", supervisorSummary);
        overview.put("topos", topos);
        overview.put("nimbusSummary", nimbusSummary);
        Map<String,String> n = new HashMap<>();
        n.put("ff","dddd");
        overview.put("a",n);
        overview.put("stormui",yarnThriftClient.getStormUi());

        ModelAndView modelAndView = new ModelAndView("/storm/overview");

        modelAndView.addObject("overview",overview);
        return modelAndView;
    }
}
