package com.yss.storm.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

//@Service
public class StormRestClient {
    private static PoolingClientConnectionManager ccm    = new PoolingClientConnectionManager();
    private static HttpClient                     client = new DefaultHttpClient(ccm);

    static {
        ccm.setMaxTotal(4096);
        ccm.setDefaultMaxPerRoute(4096);
    }

    String stormRestHost;
    String apiBase;

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

    public String getApiBase() {
        return apiBase;
    }

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

    public String getClusterConfig() {
        return getApiData("cluster/configuration");
    }

    public String getClusterSummary() throws IOException {
        return getApiData("cluster/summary");
    }

    public String getComponentDetails(String topoId, String compId) {
        String url = String.format("topology/%s/component/%s?sys=true&window=600", topoId, compId);

        return getApiData(url);
    }

    public String getNimbusSummary() {
        return getApiData("nimbus/summary");
    }

    public String getStormRestHost() {
        return stormRestHost;
    }

    public void setStormRestHost(String stormRestHost) {
        this.stormRestHost = stormRestHost;
    }

    public String getSupervisorSummary() {
        return getApiData("supervisor/summary");
    }

    public Topologies getTopoSummary() {
        String     topos      = getApiData("topology/summary");
        Topologies topologies = JSON.parseObject(topos, Topologies.class);

        return topologies;
    }

    public String getTopologyDetails(String topoId) {
        String url = String.format("topology/%s?sys=true&window=600", topoId);

        return getApiData(url);
    }

    public String getTopologyVisualization(String topoId) {
        String url = String.format("topology/%s/visualization", topoId);

        return getApiData(url);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
