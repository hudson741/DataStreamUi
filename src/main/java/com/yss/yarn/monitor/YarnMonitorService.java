package com.yss.yarn.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.floodCtr.generate.FloodJobRunningState;
import com.yss.yarn.discovery.YarnMasterServerDiscovery;
import com.yss.yarn.discovery.YarnThriftClient;
import com.yss.yarn.model.DockerJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.security.auth.ThriftClient;
import org.apache.storm.shade.cheshire.generate.JSONable;
import org.apache.thrift.TException;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/27
 */
public class YarnMonitorService {

    @Autowired
    private YarnMonitorRestClient yarnMonitorRestClient;

    @Autowired
    private YarnThriftClient yarnThriftClient;

    public JSONArray getApps(){
        JSONArray jsonArray = yarnMonitorRestClient.getYarnApps();
        return jsonArray;

    }

    public Map<String,Object> getNodes(){
        String data = yarnMonitorRestClient.getYarnNodes();
        return toMaps(data);
    }

    public JSONObject getCluster(){
        String data = yarnMonitorRestClient.getYarnCluster();
        if(StringUtils.isEmpty(data)){
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(data);

        JSONObject cluster = jsonObject.getJSONObject("clusterMetrics");

        int allocatedMB = cluster.getInteger("allocatedMB");

        int totalMB = cluster.getInteger("totalMB");

        cluster.put("allocateGB",allocatedMB/1000);
        cluster.put("totalGB",totalMB/1000);

        return cluster;
    }

    public  List<DockerJob> getDockerJobs(){
        try {
            return yarnThriftClient.getDockerJobs();
        }catch (Throwable e){
            return Lists.newArrayList();
        }
    }

    public String yarnRestart(String jobId){
        try {
            return yarnThriftClient.restartDocker(jobId);
        } catch (TException e) {
           return "请刷新配置";
        }
    }

    public String killApp(String appId){
            return yarnThriftClient.killApplication(appId);
    }

    public String stopDockerJob(String jobId){
            return yarnThriftClient.stopDocker(jobId);
    }


    public static Map<String, Object> toMaps(String json) {
        if(StringUtils.isEmpty(json)){
            return new HashMap<>();
        }
        return JSON.parseObject(json, Map.class);
    }


}
