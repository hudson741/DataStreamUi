package com.yss.yarn.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yss.config.Conf;
import com.yss.util.HttpUtilManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/27
 */
public class YarnMonitorRestClient {

    private Logger logger = LoggerFactory.getLogger(YarnMonitorRestClient.class);

    private static PoolingClientConnectionManager ccm    = new PoolingClientConnectionManager();
    private static HttpClient client = new DefaultHttpClient(ccm);

    public String getApiBase() {
        if(StringUtils.isEmpty(Conf.getYarnResourceUiAddress())){
            return null;
        }
        return "http://" + Conf.getYarnResourceUiAddress() + "/ws/v1/cluster/";
    }

    /**
     * yarn application信息
     * @return
     */
    public JSONArray getYarnApps(){
        String jsonStr =  getApiData("apps");
        if(StringUtils.isEmpty(jsonStr)){
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        JSONObject apps =  jsonObject.getJSONObject("apps");

        if(apps == null){
            return new JSONArray();
        }

        JSONArray jsonArray =  jsonObject.getJSONObject("apps").getJSONArray("app");

        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject1 =  jsonArray.getJSONObject(i);
            String state = jsonObject1.getString("state");
            String noShowKill = state.equals("RUNNING")?"false":"true";
            jsonObject1.put("noShowKill",noShowKill);
        }
        return jsonArray;


    }

    /**
     * yarn节点信息
     * @return
     */
    public String getYarnNodes(){
        return getApiData("nodes");
    }

    /**
     * yarn集群信息
     * @return
     */
    public String getYarnCluster(){
        return getApiData("metrics");
    }

    /**
     * get请求
     * @param url
     * @return
     */
    private String getApiData(String url) {
        if(StringUtils.isEmpty(getApiBase())){
            return null;
        }
        try {
            return HttpUtilManager.getInstance().httpGet(getApiBase()+url);
        } catch (IOException e) {
            logger.error("error ",e);
        } catch (HttpException e) {
            logger.error("error ",e);
        }

        return null;
    }


}
