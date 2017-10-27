package com.yss.yarn.discovery;

import com.alibaba.fastjson.JSONArray;
import com.floodCtr.generate.FloodJobRunningState;
import com.floodCtr.generate.StormThriftService;
import com.yss.yarn.model.DockerJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.thrift.TException;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/29
 */
public class YarnThriftClient implements StormThriftService.Iface{

    private Logger logger            = LoggerFactory.getLogger(YarnThriftClient.class);

    @Autowired
    ThriftClientProxy thriftClientProxy;


    @Override
    public String getStormUi() throws TException {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
        return client.getStormUi();
    }

    @Override
    public String getStormDrpc() throws TException {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
        return client.getStormDrpc();
    }

    @Override
    public String getStormNimbus() throws TException {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
        return client.getStormNimbus();
    }

    @Override
    public String getAllDockerJob()  {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
//        synchronized (this) {
            try {
                return client.getAllDockerJob();
            } catch (TException e) {
                e.printStackTrace();
            }
//        }

        return null;
    }

    @Override
    public String stopDocker(String jobId)  {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
        try {
            return client.stopDocker(jobId);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String killApplication(String appId) {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
        try {
            return client.killApplication(appId);
        } catch (TException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String restartDocker(String jobId) throws TException {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
            return client.restartDocker(jobId);
    }

    @Override
    public String removeDocker(String jobId) throws TException {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
        return client.removeDocker(jobId);
    }


    @Override
    public void addDockerComponent(String imageName, String containerName, String runIp, String dockerIp, String businessTag, String priority, String dockerArgs, String netUrl, String cm,String appId,Map<String, String> host, Map<String, String> port) throws TException {
        StormThriftService.Iface client =(StormThriftService.Iface)thriftClientProxy.getProxyClient();
        client.addDockerComponent(imageName, containerName,runIp, dockerIp, businessTag,priority,dockerArgs, netUrl,cm,appId, host, port);
    }

    public List<DockerJob> getDockerJobs(){
            String json = getAllDockerJob();

            List<DockerJob> result = Lists.newArrayList();

            List<FloodJobRunningState> list = JSONArray.parseArray(json,FloodJobRunningState.class);
            if(CollectionUtils.isEmpty(list)){
                return Lists.newArrayList();
            }

            for(FloodJobRunningState floodJobRunningState:list){
                result.add(DockerJob.convert(floodJobRunningState));
            }

            return result;
    }


}
