package com.yss.yarn.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.floodCtr.generate.FloodJobRunningState;
import com.yss.yarn.discovery.YarnMasterServerDiscovery;
import com.yss.yarn.discovery.YarnThriftClient;
import com.yss.yarn.model.DockerJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.storm.security.auth.ThriftClient;
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

    public Map<String,Object> getApps(){
        String data = yarnMonitorRestClient.getYarnApps();
        return toMaps(data);
    }

    public  List<DockerJob> getDockerJobs(){
        return yarnThriftClient.getDockerJobs();
    }


    public static Map<String, Object> toMaps(String json) {
        return JSON.parseObject(json, Map.class);
    }


}
