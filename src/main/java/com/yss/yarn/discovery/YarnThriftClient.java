package com.yss.yarn.discovery;

import com.alibaba.fastjson.JSONObject;
import com.floodCtr.generate.FloodContrThriftService;
import com.floodCtr.generate.FloodJobRunningState;
import com.yss.yarn.model.DockerJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.pacemaker.codec.ThriftNettyClientCodec;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/29
 */
public class YarnThriftClient implements FloodContrThriftService.Iface{

    private Logger logger            = LoggerFactory.getLogger(YarnThriftClient.class);

    FloodContrThriftService.Client client;


    private volatile boolean isConnected = false;

    public void update(String server,int port){


        if(StringUtils.isEmpty(server)|| port<1){
            return;
        }

        TTransport transport = new TSocket(server, port);
        try{
            transport.open();
            // 协议层
            TProtocol protocol = new TBinaryProtocol(transport);
            client = new FloodContrThriftService.Client(protocol);
            isConnected = true;
        }catch(Exception e){
            logger.error("error ",e);
            isConnected = false;
        }

    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }


    @Override
    public String getStormUi() throws TException {
        if(client == null){
            return null;
        }
        synchronized (this) {
            return client.getStormUi();
        }
    }

    @Override
    public String getStormNimbus() throws TException {
        if(client == null){
            return null;
        }
        synchronized (this) {
            return client.getStormNimbus();
        }
    }

    @Override
    public String getAllDockerJob() throws TException {
        if(client == null){
            return null;
        }
        synchronized (this) {
            return client.getAllDockerJob();
        }
    }

    @Override
    public void addDockerComponent(String imageName, String containerName, String dockerIp, String dockerArgs, String netUrl, Map<String, String> host, Map<String, String> port) throws TException {
        if(client==null){
            return ;
        }
        synchronized (this) {
            client.addDockerComponent(imageName, containerName, dockerIp, dockerArgs, netUrl, host, port);
        }
    }

    public List<DockerJob> getDockerJobs(){
        try {
            String json = getAllDockerJob();
            List<FloodJobRunningState> list = JSONObject.parseArray(json,FloodJobRunningState.class);
            if(CollectionUtils.isEmpty(list)){
                return Lists.newArrayList();
            }

            List<DockerJob> result = Lists.newArrayList();

            for(FloodJobRunningState floodJobRunningState:list){
                result.add(DockerJob.convert(floodJobRunningState));
            }

            return result;


        } catch (TException e) {
            e.printStackTrace();
        }

        return Lists.newArrayList();
    }

    public static void main(String[] args) throws TException, InterruptedException {
       final  YarnThriftClient yarnThriftClient = new YarnThriftClient();
        yarnThriftClient.update("zhangc3",9000);

        ExecutorService pool = Executors.newFixedThreadPool(20);

        for(int i=0;i<100;i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 40; i++) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        List<DockerJob> list = yarnThriftClient.getDockerJobs();
                        JSONObject yarn = new JSONObject();

                        yarn.put("dockerjobs", list);

                        System.out.println(yarn.toJSONString());

                    }
                }
            });
        }


        }



}
