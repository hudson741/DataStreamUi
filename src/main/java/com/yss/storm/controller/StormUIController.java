package com.yss.storm.controller;

import com.alibaba.fastjson.JSON;
import com.yss.storm.model.Host;
import com.yss.storm.model.Topology;
import com.yss.storm.monitor.StormMonitorRestApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StormUIController {
    @Autowired
    private StormMonitorRestApiService stormMonitorRestApiService;

    @RequestMapping(
            value = "/activeTopo",
            method = RequestMethod.GET
    )
    public String activeTopology(@RequestParam("topoid") String topoid) throws Exception {
        Map<String,Object> data = stormMonitorRestApiService.activeTopo(topoid);
        return JSON.toJSONString(data);
    }

    @RequestMapping(
            value = "/deactiveTopo",
            method = RequestMethod.GET
    )
    public String deactiveTopology(@RequestParam("topoid") String topoid) throws Exception {
        Map<String,Object> data = stormMonitorRestApiService.deactiveTopo(topoid);
        return JSON.toJSONString(data);
    }


    @RequestMapping(
       value = "/killTopology",
       method = RequestMethod.GET
    )
    public String killTopology(@RequestParam("topoid") String topoid) throws Exception {
        Map<String,Object> data = stormMonitorRestApiService.killTopology(topoid);
        return JSON.toJSONString(data);
    }


    @RequestMapping(
       value="/topoWorkNumModify",
       method = RequestMethod.GET
    )
    public String topoWorkNumModify(@RequestParam("topoid") String topoid,@RequestParam("num") int num) throws Exception {
        Map<String,Object> data = stormMonitorRestApiService.modifyTopoWorkerNum(topoid,num);
        return JSON.toJSONString(data);
    }

    @RequestMapping(
            value="/topoCExeModify",
            method = RequestMethod.GET
    )
    public String topoCExeModify(@RequestParam("topoid") String topoid,@RequestParam("cid") String cid,@RequestParam("num") int num) throws Exception {

        Map<String,Integer> map = new HashMap<>();
        map.put(cid,num);

        Map<String,Object> data = stormMonitorRestApiService.modufyTopoComponentExecutors(topoid,map);

        return JSON.toJSONString(data);
    }

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

    @RequestMapping(value = "/topom" , method = RequestMethod.GET)
    public String topoManager(@RequestParam("topoid") String topoid) {
        Map<String,Object> topo = stormMonitorRestApiService.getTopoComponents(topoid);

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
            value  = "/streamManager",
            method = RequestMethod.GET
    )
    public String streamManager() {
        Map<String,Object> topologies = stormMonitorRestApiService.getTopologies();

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
