package com.yss.storm.monitor;

import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import com.yss.storm.model.*;

/**
 * Created by zhangchi on 2017/5/19.
 */
public class StormMonitorRestApiService implements StormMonitor {
    @Autowired
    private StormRestClient stormRestClient;

    private void addIP(Map<String, Object> supervisorSummary) {
        if (supervisorSummary == null) {
            return;
        }

        List<Map<String, Object>> summ = (List<Map<String, Object>>) supervisorSummary.get("supervisors");

        if (summ == null) {
            return;
        }

        for (Map<String, Object> sup : summ) {
            String host = sup.get("host").toString();

            sup.put("ip", getIpByHostName(host));
        }
    }

    private void addIPToExeStatus(List<Map<String, Object>> executorStats) {
        for (Map<String, Object> exeMap : executorStats) {
            String host = exeMap.get("host").toString();

            exeMap.put("ip", getIpByHostName(host));
        }
    }

    private String refineComponentId(String compId) {
        if (compId.startsWith("__") == false) {
            return compId;
        }

        int lastDot = compId.lastIndexOf('.');

        if (lastDot > 0) {
            return "__" + compId.substring(lastDot + 1);
        } else {
            return compId;
        }
    }

    public static Map<String, Object> toMaps(String json) {
        return JSON.parseObject(json, Map.class);
    }

    private List<ExecutorStatus> getAllExecutorStatus() {
        Topology[]           topos         = getTopologiesSummary();
        List<ExecutorStatus> exeStatusList = new ArrayList<ExecutorStatus>();

        for (Topology topo : topos) {
            String              topoId      = topo.getId();
            Map<String, Object> topoDetails = getTopologyDetails(topoId);
            List<Map>           spouts      = (List<Map>) topoDetails.get("spouts");

            for (Map spout : spouts) {
                String              spoutId      = (String) spout.get("spoutId");
                Map<String, Object> spoutDetails = getComponentDetails(topoId, spoutId);

                exeStatusList.addAll(getExecutorStatusFromComponent(spoutDetails));
            }

            List<Map> bolts = (List<Map>) topoDetails.get("bolts");

            for (Map bolt : bolts) {
                String              boltId      = (String) bolt.get("boltId");
                Map<String, Object> boltDetails = getComponentDetails(topoId, boltId);

                exeStatusList.addAll(getExecutorStatusFromComponent(boltDetails));
            }
        }

        return exeStatusList;
    }

    public Map<String, Object> getClusterConfig() {
        return toMaps(stormRestClient.getClusterConfig());
    }

    public Map<String, Object> getClusterSummary() throws IOException {
        return toMaps(stormRestClient.getClusterSummary());
    }

    public Map<String, Object> getComponentDetails(String topoId, String compId) {
        return toMaps(stormRestClient.getComponentDetails(topoId, compId));
    }

    private List<ExecutorStatus> getExecutorStatusFromComponent(Map<String, Object> compDetails) {
        List<ExecutorStatus> exeStatusList = new ArrayList<ExecutorStatus>();
        String               topoName      = compDetails.get("name").toString();
        String               topoId        = compDetails.get("topologyId").toString();
        String               compId        = refineComponentId(compDetails.get("id").toString());
        String               compType      = compDetails.get("componentType").toString();
        List<Map>            executors     = (List<Map>) compDetails.get("executorStats");

        for (Map executor : executors) {
            ExecutorStatus exeStatus = new ExecutorStatus();

            exeStatus.setTopoId(topoId);
            exeStatus.setTopoName(topoName);
            exeStatus.setCompId(compId);
            exeStatus.setCompType(compType);

            String hostName = executor.get("host").toString().toUpperCase();

            exeStatus.setHost(hostName);
            exeStatus.setEmitted(((Number) executor.get("emitted")).longValue());
            exeStatus.setTransferred(((Number) executor.get("transferred")).longValue());
            exeStatus.setAcked(((Number) executor.get("acked")).longValue());
            exeStatus.setFailed(((Number) executor.get("failed")).longValue());
            exeStatus.setPort(((Number) executor.get("port")).intValue());
            exeStatus.setProcessLatency((String) executor.get("completeLatency"));
            exeStatus.setUptime(executor.get("uptime").toString());
            exeStatus.setExecutorId(executor.get("id").toString());
            exeStatusList.add(exeStatus);
        }

        return exeStatusList;
    }

    public Map<String, Host> getHostWithExecutorDetails() {
        List<ExecutorStatus>                            exeStatusList        = getAllExecutorStatus();
        Map<String, Map<Integer, List<ExecutorStatus>>> host2Slots2Executors = new HashMap<String,
                                                                                           Map<Integer,
                                                                                               List<ExecutorStatus>>>();

        for (ExecutorStatus executorStatus : exeStatusList) {
            String                             host  = executorStatus.getHost();
            Integer                            port  = executorStatus.getPort();
            Map<Integer, List<ExecutorStatus>> slots = host2Slots2Executors.get(host);

            if (slots == null) {
                slots = new HashMap<Integer, List<ExecutorStatus>>();
                host2Slots2Executors.put(host, slots);
            }

            List<ExecutorStatus> exes = slots.get(port);

            if (exes == null) {
                exes = new ArrayList<ExecutorStatus>();
                slots.put(port, exes);
            }

            exes.add(executorStatus);
        }

        Map<String, Host> hosts = getHosts();

        for (Map.Entry<String, Map<Integer, List<ExecutorStatus>>> entry : host2Slots2Executors.entrySet()) {
            String hostName = entry.getKey();
            Host   host     = hosts.get(hostName);

            if (host == null) {
                System.err.println("No host found for " + entry);

                continue;
            }

            List<SlotStatus>                   slotsOfHost = new ArrayList<SlotStatus>();
            Map<Integer, List<ExecutorStatus>> slots       = entry.getValue();

            for (Map.Entry<Integer, List<ExecutorStatus>> slotEntry : slots.entrySet()) {
                SlotStatus slot = new SlotStatus();

                slot.setHost(hostName);
                slot.setIp(host.getIp());
                slot.setPort(slotEntry.getKey());

                List<ExecutorStatus> exes = slotEntry.getValue();

                Collections.sort(exes);
                slot.setStats(exes);

                for (ExecutorStatus status : exes) {
                    slot.updateUptime(status.getUptime());
                }

                slotsOfHost.add(slot);
            }

            Collections.sort(slotsOfHost);
            host.setSlots(slotsOfHost);
        }

        return hosts;
    }

    private Map<String, Host> getHosts() {
        Map<String, Host> hosts = new TreeMap<String, Host>();
        List<Map>         sups  = (List<Map>) getSupervisorSummary().get("supervisors");

        for (Map sup : sups) {
            Host host = new Host();

            host.setHost(sup.get("host").toString());
            host.setIp(sup.get("ip").toString());
            host.setSupId(sup.get("id").toString());
            host.setUptime(sup.get("uptime").toString());
            host.setSlotsUsed(((Number) sup.get("slotsUsed")).intValue());
            host.setSlotsTotal(((Number) sup.get("slotsTotal")).intValue());
            hosts.put(host.getHost().toUpperCase(), host);
        }

        return hosts;
    }

    public static String getIpByHostName(String hostName) {
        try {
            InetAddress address = InetAddress.getByName(hostName);

            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return "N/A";
        }
    }

    @Override
    public Map<String, Object> getNimbusSummary() {
        return toMaps(stormRestClient.getNimbusSummary());
    }

    public Map<String, Object> getSupervisorSummary() {
        Map<String, Object> supervisorSummary = toMaps(stormRestClient.getSupervisorSummary());

        addIP(supervisorSummary);

        return supervisorSummary;
    }

    public Topology[] getTopologiesSummary() {
        Topologies topologies = stormRestClient.getTopoSummary();

        if ((topologies == null) || (topologies.getTopologies() == null)) {
            return new Topology[0];
        }

        return topologies.getTopologies();
    }

    @Override
    public Map<String, Object> getTopologies() {
        String topos =  stormRestClient.getTopoList();

        JSONObject jsonObject = JSON.parseObject(topos);

        JSONArray jsonArray = jsonObject.getJSONArray("topologies");

        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

            if(jsonObject1.get("status").equals("ACTIVE")){
                jsonObject1.put("showa","true");
                jsonObject1.put("showd","false");
            }else{
                jsonObject1.put("showa","false");
                jsonObject1.put("showd","true");
            }

        }

        Map<String,Object> map = new HashMap<>();
        map.put("topologies",jsonArray);

        return map;

    }

    public Map<String, Object> getTopologyDetails(String topoId) {
        return toMaps(stormRestClient.getTopologyDetails(topoId));
    }

    public Map<String, Object> getTopologyDetailsWithComponentDetails(String topoId) {
        Map<String, Object>       ret    = toMaps(stormRestClient.getTopologyDetails(topoId));
        List<Map<String, Object>> bolts  = (List<Map<String, Object>>) ret.get("bolts");
        List<Map<String, Object>> spouts = (List<Map<String, Object>>) ret.get("spouts");

        for (Map<String, Object> spout : spouts) {
            String id = (String) spout.get("spoutId");

            spout.put("spoutId", refineComponentId(id));

            Map<String, Object> compDetails = this.getComponentDetails(topoId, id);

            spout.put("compDetails", compDetails);
            addIPToExeStatus((List<Map<String, Object>>) compDetails.get("executorStats"));
        }

        for (Map<String, Object> bolt : bolts) {
            String id = (String) bolt.get("boltId");

            bolt.put("boltId", refineComponentId(id));

            Map<String, Object> compDetails = this.getComponentDetails(topoId, id);

            bolt.put("compDetails", compDetails);
            addIPToExeStatus((List<Map<String, Object>>) compDetails.get("executorStats"));
        }

        Map<String, Object> defaultConfigs = this.getClusterConfig();
        Map<String, Object> topoConfigs    = (Map<String, Object>) ret.remove("configuration");
        Map<String, Object> sameConfig     = new HashMap<String, Object>();
        Map<String, Object> diffConfig     = new HashMap<String, Object>();

        for (Map.Entry<String, Object> conf : topoConfigs.entrySet()) {
            String key          = conf.getKey();
            Object value        = conf.getValue();
            Object defaultValue = defaultConfigs.get(key);

            if (Objects.equals(value, defaultValue) == true) {
                sameConfig.put(key, value);
            } else {
                diffConfig.put(key, value);
            }
        }

        ret.put("sameConfig", sameConfig);
        ret.put("diffConfig", diffConfig);

        return ret;
    }

    @Override
    public Map<String, Object> killTopology(String topologyId) {
        String data = stormRestClient.killTopology(topologyId);
        return toMaps(data);
    }

    @Override
    public Map<String, Object> activeTopo(String topoId) {
        return toMaps(stormRestClient.activeTopolo(topoId));
    }

    @Override
    public Map<String, Object> deactiveTopo(String topoId) {
        return toMaps(stormRestClient.deactiveTopo(topoId));
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
