package com.yss.storm.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yss.storm.StormNodesService;
import com.yss.storm.model.Rebalance;
import com.yss.storm.node.UiNode;
import com.yss.util.HttpUtilManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import com.alibaba.fastjson.JSON;

import com.yss.storm.model.Topologies;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangchi on 2017/5/19.
 */

public class StormRestClient implements InitializingBean {
    private static PoolingClientConnectionManager ccm    = new PoolingClientConnectionManager();
    private static HttpClient                     client = new DefaultHttpClient(ccm);

    static {
        ccm.setMaxTotal(4096);
        ccm.setDefaultMaxPerRoute(4096);
    }


    @Autowired
    private StormNodesService stormNodesService;

    public String getApiBase() {
        return "http://" + getUIHttp() + "/api/v1/";
    }

    private String getUIHttp(){
        List<UiNode>  list = stormNodesService.getUiNodeList();
        UiNode uiNode = list.get(0);

        String stormUIHost = uiNode.getHost()+":"+uiNode.getPort();
        return stormUIHost;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

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

//    public void updateStormUIHost(String stormRestHost) {
//        this.stormRestHost = stormRestHost;
//        this.apiBase       = "http://" + stormRestHost + "/api/v1/";
//    }

    /**
     * post请求
     * @param url
     * @param params
     * @return
     */
    private String getApiDataPost(String url, Map<String,String> params) {
        try {
            return HttpUtilManager.getInstance().requestHttpPost(getApiBase() , url, params, null);
        }catch(Exception e){

        }
        return null;
    }

    /**
     * postJSON请求
     * @param url
     * @return
     */
    private String getApiDataPostJson(String url, String jsonStr) {
        try {
            return HttpUtilManager.getInstance().requestHttpPostJSON(getApiBase() , url,jsonStr, null);
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
        HttpGet      get      = new HttpGet(getApiBase()  + url);
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
     * 动态调整topology worker 数量
     * @param topoId
     * @param num
     * @return
     */
    public String modifyTopoWorkerNum(String topoId,int num){

        String rebalance = Rebalance.getRebalanceInstanceJSONStr(num,null,null);

        return getApiDataPostJson("topology/"+topoId+"/rebalance/0",rebalance);
    }

    /**
     * 动态调整topology  组件的并行度
     * @param topoId
     * @param map
     * @return
     */
    public String modufyTopoComponentExecutors(String topoId,Map<String,Integer> map ){
        String rebalance = Rebalance.getRebalanceInstanceJSONStr(null,map,null);

        return getApiDataPostJson("topology/"+topoId+"/rebalance/0",rebalance);
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


}


