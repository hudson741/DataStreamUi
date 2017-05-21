package com.yss.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import com.yss.storm.model.Host;
import com.yss.storm.model.Topology;
import com.yss.storm.monitor.StormMonitorRestApiService;

@RestController
public class StormUIController {
    @Autowired
    private StormMonitorRestApiService stormMonitorRestApiService;

    @RequestMapping(
        value  = "/hosts",
        method = RequestMethod.GET
    )
    public String hosts() throws Exception {
        Map<String, Host> hosts = stormMonitorRestApiService.getHostWithExecutorDetails();

        return JSON.toJSONString(hosts);
    }

    @RequestMapping(value = "/topo")
    public String topo(@RequestParam("topoid") String topoid) {
        Map<String, Object> topo = stormMonitorRestApiService.getTopologyDetailsWithComponentDetails(topoid);

        return JSON.toJSONString(topo);
    }

    @RequestMapping(
        value  = "/topolist",
        method = RequestMethod.GET
    )
    public String topolist() {
        Topology[] topologies = stormMonitorRestApiService.getTopologiesSummary();

        return JSON.toJSONString(topologies);
    }

    @RequestMapping(
        value  = "/overview",
        method = RequestMethod.GET
    )
    public String view() throws Exception {
        Map<String, Object> overview          = new HashMap<String, Object>();
        Map<String, Object> clusterConfig     = stormMonitorRestApiService.getClusterConfig();
        Map<String, Object> clusterSummary    = stormMonitorRestApiService.getClusterSummary();
        Map<String, Object> supervisorSummary = stormMonitorRestApiService.getSupervisorSummary();
        Map<String, Object> nimbusSummary     = stormMonitorRestApiService.getNimbusSummary();
        Topology[]          topos             = stormMonitorRestApiService.getTopologiesSummary();

        overview.put("cluserConfig", clusterConfig);
        overview.put("clusterSummary", clusterSummary);
        overview.put("supervisorSummary", supervisorSummary);
        overview.put("topos", topos);
        overview.put("nimbusSummary", nimbusSummary);

        return JSON.toJSONString(overview);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
