package com.yss.yarn.monitor;

import com.yss.config.Conf;
import com.yss.util.HttpUtilManager;
import com.yss.yarn.controller.YarnLaunchController;
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

    private Logger logger = LoggerFactory.getLogger(YarnLaunchController.class);

    private static PoolingClientConnectionManager ccm    = new PoolingClientConnectionManager();
    private static HttpClient client = new DefaultHttpClient(ccm);

    public String getApiBase() {
        if(StringUtils.isEmpty(Conf.getYarnResourceUiAddress())){
            return null;
        }
        return "http://" + Conf.getYarnResourceUiAddress() + "/ws/v1/cluster/";
    }

    public String getYarnApps(){
        return getApiData("apps");
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
