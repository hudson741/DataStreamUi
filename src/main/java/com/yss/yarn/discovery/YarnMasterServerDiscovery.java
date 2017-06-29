package com.yss.yarn.discovery;

import com.alibaba.fastjson.JSONObject;
import com.floodCtr.generate.FloodJobRunningState;
import com.yss.config.Conf;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryUntilElapsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/29
 */
public class YarnMasterServerDiscovery {

    private final static String MASTER_SERVER_PATH="/floodContrYu/masterServer";

    private Logger logger            = LoggerFactory.getLogger(YarnMasterServerDiscovery.class);

    private CuratorFramework curatorFramework = null;

    private NodeCache masterServerCache;

    @Autowired
    private YarnThriftClient thriftClient;

    private String masterServer="";

    private int port=0;

    public void updateCuratorFrameWork() throws Exception {
        try {
            if (curatorFramework != null) {
                curatorFramework.close();
            }
            if (StringUtils.isNotEmpty(Conf.getSTORM_ZK())) {
                RetryUntilElapsed retryUntilElapsed = new RetryUntilElapsed(6000, 1000);
                curatorFramework = CuratorFrameworkFactory.newClient(Conf.getSTORM_ZK() + ":" + "2181", retryUntilElapsed);
                curatorFramework.start();
                startFloodJobRunningStateCache();
            }
        }catch(Exception e){
            logger.error("error ",e);
        }
    }


    private void startFloodJobRunningStateCache() throws Exception {
        masterServerCache = new NodeCache(curatorFramework,MASTER_SERVER_PATH);
        masterServerCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                reload();
            }
        });

        masterServerCache.start();

    }

    private void reload() throws Exception {
        String data = new String(curatorFramework.getData().forPath(MASTER_SERVER_PATH));
        String[] server = data.split(":");
        this.masterServer = server[0];
        this.port = Integer.parseInt(server[1]);

        if(masterServer.equals("10.135.1.181")){
            masterServer = "zhangc1";
        }else if(masterServer.equals("10.186.58.13")){
            masterServer = "zhangc2";
        }else if(masterServer.equals("10.135.96.95")){
            masterServer="zhangc3";
        }else if(masterServer.equals("10.104.108.213")){
            masterServer="zhangc4";
        }else if(masterServer.equals("10.104.254.122")){
            masterServer = "zhangc5";
        }

        logger.info("fuck "+masterServer+"  "+port);

        thriftClient.update(masterServer,port);
    }

    public String getMasterServer() {
        return masterServer;
    }

    public void setMasterServer(String masterServer) {
        this.masterServer = masterServer;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public YarnThriftClient getThriftClient() {
        return thriftClient;
    }

    public void setThriftClient(YarnThriftClient thriftClient) {
        this.thriftClient = thriftClient;
    }

    public static void main(String[] args) throws Exception {
        YarnThriftClient thriftClient = new YarnThriftClient();
        YarnMasterServerDiscovery yarnRunningDiscovery = new YarnMasterServerDiscovery();
        yarnRunningDiscovery.setThriftClient(thriftClient);
        yarnRunningDiscovery.updateCuratorFrameWork();
        Thread.currentThread().sleep(4000);
        String a = thriftClient.getAllDockerJob();

        List<FloodJobRunningState> list = JSONObject.parseArray(a,FloodJobRunningState.class);

        for(FloodJobRunningState floodJobRunningState:list){
            if(floodJobRunningState.getBusinessType().equals("storm")){
                if(floodJobRunningState.getFloodJob().getBusinessTag().equals("ui")){
                    System.out.println("ui "+floodJobRunningState.getRunIp());
                }
            }
        }

        Thread.currentThread().sleep(1000000);
    }



}
