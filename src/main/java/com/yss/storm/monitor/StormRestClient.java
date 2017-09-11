package com.yss.storm.monitor;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.floodCtr.generate.FloodJobRunningState;
import com.yss.yarn.discovery.YarnThriftClient;

import org.apache.http.HttpException;
import org.apache.thrift.TException;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import com.yss.storm.model.Rebalance;
import com.yss.storm.model.Topologies;
import com.yss.util.HttpUtilManager;

/**
 * Created by zhangchi on 2017/5/19.
 */
public class StormRestClient implements InitializingBean {

    private Logger logger            = LoggerFactory.getLogger(StormRestClient.class);

    @Autowired
    private YarnThriftClient yarnThriftClient;

    /**
     * 激活拓扑
     * @param topologyId
     * @return
     */
    public String activeTopolo(String topologyId) {
        return getApiDataPost("topology/" + topologyId + "/activate", null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {}

    /**
     * 失效拓扑
     * @param topologyId
     * @return
     */
    public String deactiveTopo(String topologyId) {
        return getApiDataPost("topology/" + topologyId + "/deactivate", null);
    }

    /**
     * 移除拓扑
     * @param topologyId
     * @return
     */
    public String killTopology(String topologyId) {
        return getApiDataPost("topology/" + topologyId + "/kill/" + 0, null);
    }

    /**
     * 动态调整topology worker 数量
     * @param topoId
     * @param num
     * @return
     */
    public String modifyTopoWorkerNum(String topoId, int num) {
        String rebalance = Rebalance.getRebalanceInstanceJSONStr(num, null, null);

        return getApiDataPostJson("topology/" + topoId + "/rebalance/0", rebalance);
    }

    /**
     * 动态调整topology  组件的并行度
     * @param topoId
     * @param map
     * @return
     */
    public String modufyTopoComponentExecutors(String topoId, Map<String, Integer> map) {
        String rebalance = Rebalance.getRebalanceInstanceJSONStr(null, map, null);

        return getApiDataPostJson("topology/" + topoId + "/rebalance/0", rebalance);
    }


    public String getApiBase() {
        if(getUIHttp() == null){
            return  null;
        }
        return "http://" + getUIHttp() + "/api/v1/";
    }

    /**
     * get请求
     * @param url
     * @return
     */
    private String getApiData(String url) {
        if(getApiBase() == null){
            return null;
        }
        try {
            return HttpUtilManager.getInstance().httpGet(getApiBase()+url);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (Throwable e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * post请求
     * @param url
     * @param params
     * @return
     */
    private String getApiDataPost(String url, Map<String, String> params) {
        try {
            if(getApiBase() == null){
                return null;
            }
            return HttpUtilManager.getInstance().requestHttpPost(getApiBase(), url, params, null);
        } catch (Exception e) {}

        return "";
    }

    /**
     * postJSON请求
     * @param url
     * @return
     */
    private String getApiDataPostJson(String url, String jsonStr) {
        try {
            if(getApiBase() == null){
                return null;
            }
            return HttpUtilManager.getInstance().requestHttpPostJSON(getApiBase(), url, jsonStr, null);
        } catch (Exception e) {}

        return null;
    }

    /**
     * 获取全局配置
     * @return
     */
    public String getClusterConfig() {
        return getApiData("cluster/configuration");
    }

    /**
     * 总览
     * @return
     * @throws IOException
     */
    public String getClusterSummary() throws IOException {
        return getApiData("cluster/summary");
    }

    /**
     * 组件明细
     * @param topoId
     * @param compId
     * @return
     */
    public String getComponentDetails(String topoId, String compId) {
        String url = String.format("topology/%s/component/%s?sys=true&window=600", topoId, compId);

        return getApiData(url);
    }

    /**
     * 主节点总览
     * @return
     */
    public String getNimbusSummary() {
        return getApiData("nimbus/summary");
    }

    /**
     * 从节点总览
     * @return
     */
    public String getSupervisorSummary() {
        return getApiData("supervisor/summary");
    }

    /**
     * 组件总览
     * @return
     */
    public String getTopoList() {
        return getApiData("topology/summary");
    }

    /**
     * 组件总览
     * @return
     */
    public Topologies getTopoSummary() {
        String     topos      = getApiData("topology/summary");
        Topologies topologies = JSON.parseObject(topos, Topologies.class);

        return topologies;
    }

    /**
     * 组件明细
     * @param topoId
     * @return
     */
    public String getTopologyDetails(String topoId) {
        String url = String.format("topology/%s?sys=true&window=600", topoId);

        return getApiData(url);
    }

    public String getTopologyVisualization(String topoId) {
        String url = String.format("topology/%s/visualization", topoId);

        return getApiData(url);
    }

    private String getUIHttp() {
        try {
            logger.info("getUIHttp .... ");
            String ui = yarnThriftClient.getStormUi();
            logger.info("ui is "+ui);
            return ui;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
