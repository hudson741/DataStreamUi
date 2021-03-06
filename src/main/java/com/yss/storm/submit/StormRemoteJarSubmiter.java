package com.yss.storm.submit;

import java.io.File;

import java.net.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yss.config.Conf;
import com.yss.storm.exception.StormRmoteSubException;
import com.yss.storm.exception.ZKConfException;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.Bolt;
import org.apache.storm.generated.SpoutSpec;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.thrift.TException;
import org.apache.storm.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;

import com.alibaba.dcm.DnsCacheManipulator;

import com.google.common.collect.Lists;

import com.yss.sofa.stream.Builder;
import com.yss.storm.StormNodesService;
import com.yss.storm.node.NimbusNode;

/**
 * Created by zhangchi on 2017/5/16.
 */
public class StormRemoteJarSubmiter implements StormSubmiter {
    private Logger            logger = LoggerFactory.getLogger(StormRemoteJarSubmiter.class);
    @Autowired
    private StormNodesService stormNodesService;
    private String            remoteZKServer;
    private int               remoteZKPort;

    @Override
    public void SubmitStormTopology(URI uri) throws StormRmoteSubException, ZKConfException {
        try {
            Builder zcBuilder = loadBuilder(uri);

            if (zcBuilder == null) {
                throw new RuntimeException("解析失败");
            }

            Map<String, Object>    stormConf     = buildConf();

            System.setProperty("storm.jar", new File(uri).toString());
            StormSubmitter.submitTopology("zc" + System.currentTimeMillis() + "", stormConf, zcBuilder.getTopology());
        } catch(ZKConfException e){
            throw new ZKConfException(e.getMessage());
        } catch (Exception e) {
            throw new StormRmoteSubException(e.getMessage());
        } finally {
            FileSystemUtils.deleteRecursively(new File(uri));
        }
    }

    private Map<String, Object> buildConf() throws TException, UnknownHostException, ZKConfException {
        Map stormConf = Utils.readStormConfig();

        Config           config      = new Config();
        List<NimbusNode> nimbusNodes = stormNodesService.getNimbusNodeList();

        if (CollectionUtils.isEmpty(nimbusNodes)) {
            throw new RuntimeException("storm集群出了问题，请联系管理员");
        }

        List<String> nimbusHosts = Lists.newArrayList();

        List<String> list       = stormNodesService.getNimbusHosts();

        if(CollectionUtils.isEmpty(list)){
            throw new ZKConfException("请刷新zk地址");
        }

        int nimbusPort = 0;

        for (NimbusNode nimbusNode : nimbusNodes) {
            InetAddress  ip         = InetAddress.getByName(nimbusNode.getHost());
            String       nimbusAddr = ip.getHostAddress();
            String       dockerHost = nimbusNode.getDockerHost();

            for (String nimbusHost : list) {
                if (nimbusHost.contains(dockerHost)) {
                    logger.info("nimbus to dockerHosts " + nimbusHost + "  " + dockerHost);
                    dockerHost = nimbusHost;
                    break;
                }
            }
            nimbusPort = nimbusNode.getPort();
            logger.info("set dns " + dockerHost + " " + nimbusAddr);
            DnsCacheManipulator.setDnsCache(dockerHost, nimbusAddr);
            nimbusHosts.add(dockerHost);
        }

        config.put(Config.NIMBUS_SEEDS, nimbusHosts);
        config.put(Config.NIMBUS_THRIFT_PORT, nimbusPort);
        config.put(Config.STORM_ZOOKEEPER_SERVERS,  Arrays.asList(Conf.getSTORM_ZK().split(",")));
        config.put(Config.STORM_ZOOKEEPER_PORT, Conf.getStormZkPort());

//      Map<String, Object> hbConf = new HashMap<String, Object>();

//      hbConf.put("hbase.rootdir","hdfs://10.104.108.213:9000/hbase");
//      hbConf.put("hbase.zookeeper.quorum", "10.186.58.13:2181");
//      config.put("hbase.conf",hbConf);
        stormConf.putAll(config);

        return stormConf;
    }

    private Builder loadBuilder(URI uri) throws Exception {
        ClassLoader cl;
        Builder     zcBuilder = null;

        // 从Jar文件得到一个Class加载器
        cl = new URLClassLoader(new URL[] { uri.toURL() }, Thread.currentThread().getContextClassLoader());

        // 从加载器中加载Class
        String   classPath = "com.yss.sofa.stream.BuilderImpl";
        Class<?> c         = cl.loadClass(classPath);

        // 从Class中zhangc实例出一个对象
        zcBuilder = (Builder) c.newInstance();

        return zcBuilder;
    }

    public int getRemoteZKPort() {
        return remoteZKPort;
    }

    public void setRemoteZKPort(int remoteZKPort) {
        this.remoteZKPort = remoteZKPort;
    }

    public String getRemoteZKServer() {
        return remoteZKServer;
    }

    public void setRemoteZKServer(String remoteZKServer) {
        this.remoteZKServer = remoteZKServer;
    }
}
