package com.yss.storm.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.yss.util.HttpUtilManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import com.alibaba.fastjson.JSON;

import com.yss.storm.model.Topologies;

/**
 * Created by zhangchi on 2017/5/19.
 */

public class StormRestClient {
    private static PoolingClientConnectionManager ccm    = new PoolingClientConnectionManager();
    private static HttpClient                     client = new DefaultHttpClient(ccm);

    static {
        ccm.setMaxTotal(4096);
        ccm.setDefaultMaxPerRoute(4096);
    }

    String stormRestHost;
    String apiBase;

    public String getApiBase() {
        return apiBase;
    }


    public StormRestClient(String stormUIHost) {
        updateStormUIHost(stormUIHost);
    }

    private static String readContent(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder  sb     = new StringBuilder();

        try {
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }

        return sb.toString();
    }

    public void updateStormUIHost(String stormRestHost) {
        this.stormRestHost = stormRestHost;
        this.apiBase       = "http://" + stormRestHost + "/api/v1/";
    }

    /**
     * post请求
     * @param url
     * @param params
     * @return
     */
    private String getApiDataPost(String url, Map<String,String> params) {
        try {
            return HttpUtilManager.getInstance().requestHttpPost(apiBase, url, params, null);
        }catch(Exception e){

        }
        return null;
    }

    /**
     * get请求
     * @param url
     * @return
     */
    private String getApiData(String url) {
        HttpGet      get      = new HttpGet(apiBase + url);
        HttpResponse response = null;

        try {
            response = client.execute(get);

            return readContent(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException("Got io exception");
        }
    }


    /**
     * 失效拓扑
     * @param topologyId
     * @return
     */
    public String deactiveTopo(String topologyId){
        return getApiDataPost("topology/"+topologyId+"/deactivate",null);
    }

    /**
     * 激活拓扑
     * @param topologyId
     * @return
     */
    public String activeTopolo(String topologyId){
        return getApiDataPost("topology/"+topologyId+"/activate",null);
    }

    /**
     * 移除拓扑
     * @param topologyId
     * @return
     */
    public String killTopology(String topologyId){
        return getApiDataPost("topology/"+topologyId+"/kill/"+0,null);
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
    public Topologies getTopoSummary() {
        String     topos      = getApiData("topology/summary");
        Topologies topologies = JSON.parseObject(topos, Topologies.class);

        return topologies;
    }

    /**
     * 组件总览
     * @return
     */
    public String getTopoList() {
        return getApiData("topology/summary");
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

    public String getStormRestHost() {
        return stormRestHost;
    }

    public void setStormRestHost(String stormRestHost) {
        this.stormRestHost = stormRestHost;
    }
}


