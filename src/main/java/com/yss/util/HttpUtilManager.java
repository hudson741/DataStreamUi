package com.yss.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;

import com.yss.storm.model.Rebalance;

/**
 * 封装HTTP get post请求，简化发送http请求
 * @author zhangchi
 *
 */
public class HttpUtilManager {
    private static HttpUtilManager                   instance       = new HttpUtilManager();
    private static long                              startTime      = System.currentTimeMillis();
    public static PoolingHttpClientConnectionManager cm             = new PoolingHttpClientConnectionManager();
    private static ConnectionKeepAliveStrategy       keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            long keepAlive = super.getKeepAliveDuration(response, context);

            if (keepAlive == -1) {
                keepAlive = 5000;
            }

            return keepAlive;
        }
    };
    private static RequestConfig requestConfig = RequestConfig.custom()
                                                              .setSocketTimeout(5000)
                                                              .setConnectTimeout(5000)
                                                              .setConnectionRequestTimeout(5000)
                                                              .build();
    private static HttpClient client;

    static{
        cm.setMaxTotal(30);
    }

    private HttpUtilManager() {
        client = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(keepAliveStrat).build();
    }

    public static void IdleConnectionMonitor() {
        if (System.currentTimeMillis() - startTime > 30000) {
            startTime = System.currentTimeMillis();
            cm.closeExpiredConnections();
            cm.closeIdleConnections(30, TimeUnit.SECONDS);
        }
    }

    private List<NameValuePair> convertMap2PostParams(Map<String, String> params) {
        if ((params == null) || params.isEmpty()) {
            return Lists.newArrayList();
        }

        List<String> keys = new ArrayList<String>(params.keySet());

        if (keys.isEmpty()) {
            return null;
        }

        int                 keySize = keys.size();
        List<NameValuePair> data    = new LinkedList<NameValuePair>();

        for (int i = 0; i < keySize; i++) {
            String key   = keys.get(i);
            String value = params.get(key);

            data.add(new BasicNameValuePair(key, value));
        }

        return data;
    }

    private HttpRequestBase httpGetMethod(String url) {
        return new HttpGet(url);
    }

    private HttpPost httpPostMethod(String url) {
        return new HttpPost(url);
    }

    public static void main(String[] args) throws Exception {
//        String url = "http://119.29.65.85:8080/api/v1/topology/zc1495679005819-6-1495679087/rebalance/0";
//
////      {"rebalanceOptions":{"executors":{"count":3,"spout":2},"numWorkers":3}}
////      {"rebalanceOptions":{"numWorkers":3}}
//        Map<String, Integer> map = new HashMap<>();
//
//        map.put("zcStep2", 2);
//        map.put("zcStep1", 2);
//
//        String rebalance = Rebalance.getRebalanceInstanceJSONStr(3, map, null);
//
//        System.out.println(rebalance);
//
//        String data = HttpUtilManager.getInstance().requestHttpPostJSON("", url, rebalance, null);
//
//        System.out.println(data);

        StringBuilder a = new StringBuilder();

     for(int i =0 ;i<100;i++){
         a.append("无双kjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj "+i+"\n");
     }

     Map<String,String> map = new HashMap<>();
        map.put("s",a.toString());
        map.put("dir","fuck3");

        HttpUtilManager.getInstance().requestHttpGet("http://","localhost:9090/filed?dir=fuck3",null,null);


    }

    public String httpGet(String url) throws IOException, HttpException {
       return requestHttpGet("",url,"",null);
    }

    public String requestHttpGet(String url_prex, String url, String param, Map<String, String> headers)
            throws HttpException, IOException {
        IdleConnectionMonitor();
        url = url_prex + url;

        if ((param != null) &&!param.equals("")) {
            if (url.endsWith("?")) {
                url = url + param;
            } else {
                url = url + "?" + param;
            }
        }

        HttpRequestBase method = this.httpGetMethod(url);

        if (headers != null) {
            Set<String> keys = headers.keySet();

            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();

                method.addHeader(key, headers.get(key));
            }
        }

        method.setConfig(requestConfig);

        HttpResponse response = client.execute(method);
        HttpEntity   entity   = response.getEntity();

        if (entity == null) {
            return "";
        }

        InputStream is           = null;
        String      responseData = "";

        try {
            is           = entity.getContent();
            responseData = IOUtils.toString(is, "UTF-8");
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return responseData;
    }

    public String requestHttpPost(String url_prex, String url, Map<String, String> params, Map<String, String> headers)
            throws HttpException, IOException {
        IdleConnectionMonitor();
        url = url_prex + url;

        HttpPost             method               = this.httpPostMethod(url);
        List<NameValuePair>  valuePairs           = this.convertMap2PostParams(params);
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);

        method.setEntity(urlEncodedFormEntity);
        method.setConfig(requestConfig);
        method.setHeader("Content-Type", "application/x-www-form-urlencoded");

        if (headers != null) {
            Set<String> keys = headers.keySet();

            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();

                method.addHeader(key, headers.get(key));
            }
        }

        HttpResponse response = client.execute(method);
        HttpEntity   entity   = response.getEntity();

        if (entity == null) {
            return "";
        }

        InputStream is           = null;
        String      responseData = "";

        try {
            is           = entity.getContent();
            responseData = IOUtils.toString(is, "UTF-8");
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return responseData;
    }

    public String requestHttpPostJSON(String url_prex, String url, String jsonStr, Map<String, String> headers)
            throws HttpException, IOException {
        IdleConnectionMonitor();
        url = url_prex + url;

        HttpPost method = this.httpPostMethod(url);

        method.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
        method.setConfig(requestConfig);
        method.addHeader("Content-type", "application/json; charset=utf-8");
        method.setHeader("Accept", "application/json");

        if (headers != null) {
            Set<String> keys = headers.keySet();

            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();

                method.addHeader(key, headers.get(key));
            }
        }

        HttpResponse response = client.execute(method);
        HttpEntity   entity   = response.getEntity();

        if (entity == null) {
            return "";
        }

        InputStream is           = null;
        String      responseData = "";

        try {
            is           = entity.getContent();
            responseData = IOUtils.toString(is, "UTF-8");
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return responseData;
    }

    public String requestHttpPutJSON(String url_prex, String url, String jsonStr, Map<String, String> headers)
            throws HttpException, IOException {
        IdleConnectionMonitor();
        url = url_prex + url;

        HttpPut method = new HttpPut(url);

        method.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
        method.setConfig(requestConfig);
        method.addHeader("Content-type", "application/json; charset=utf-8");
        method.setHeader("Accept", "application/json");

        if (headers != null) {
            Set<String> keys = headers.keySet();

            for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                String key = i.next();

                method.addHeader(key, headers.get(key));
            }
        }

        HttpResponse response = client.execute(method);
        HttpEntity   entity   = response.getEntity();

        if (entity == null) {
            return "";
        }

        InputStream is           = null;
        String      responseData = "";

        try {
            is           = entity.getContent();
            responseData = IOUtils.toString(is, "UTF-8");
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return responseData;
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

    public HttpClient getHttpClient() {
        return client;
    }

    public static HttpUtilManager getInstance() {
        return instance;
    }
}
