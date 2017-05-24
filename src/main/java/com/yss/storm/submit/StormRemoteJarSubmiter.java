package com.yss.storm.submit;

import com.google.common.collect.Lists;
import com.yss.sofa.stream.Builder;
import com.yss.storm.nimbus.NimbusNode;
import com.yss.storm.nimbus.NimbusNodesService;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.flux.model.SpoutDef;
import org.apache.storm.generated.Bolt;
import org.apache.storm.generated.SpoutSpec;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangchi on 2017/5/16.
 */

public class StormRemoteJarSubmiter implements StormSubmiter {

    private Logger logger = LoggerFactory.getLogger(StormRemoteJarSubmiter.class);

    @Autowired
    private NimbusNodesService nimbusNodesService;

    private String remoteZKServer;

    private int remoteZKPort;

    private Builder loadBuilder(URI uri) throws Exception{
        ClassLoader cl;
        Builder zcBuilder = null;
            // 从Jar文件得到一个Class加载器
        cl = new URLClassLoader(new URL[]{uri.toURL()}, Thread.currentThread().getContextClassLoader());
            // 从加载器中加载Class
        String classPath = "com.yss.sofa.stream.BuilderImpl";
        Class<?> c = cl.loadClass(classPath);
            // 从Class中zhangc实例出一个对象
        zcBuilder = (Builder) c.newInstance();
        return zcBuilder;
    }

    private Map<String,Object> buildConf(){
        Map stormConf = Utils.readStormConfig();
        //Create a builder and configurations.
        Config config = new Config();

        List<NimbusNode> nimbusNodes = nimbusNodesService.getNimbusNodeList();

        if(CollectionUtils.isEmpty(nimbusNodes)){
            throw new RuntimeException("storm集群出了问题，请联系管理员");
        }

        List<String> nimbusHosts = Lists.newArrayList();

        for(NimbusNode nimbusNode:nimbusNodes){
            nimbusHosts.add(nimbusNode.getHost());
        }

        config.put(Config.NIMBUS_SEEDS, nimbusHosts);

        config.put(Config.NIMBUS_THRIFT_PORT, nimbusNodes.get(0).getPort());

        config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(remoteZKServer.split(",")));
        //Arrays.asList("192.168.102.133","ss");
        config.put(Config.STORM_ZOOKEEPER_PORT, remoteZKPort);
        stormConf.putAll(config);
        return stormConf;
    }

    @Override
    public void SubmitStormTopology(URI uri){
        try {

            Builder zcBuilder = loadBuilder(uri);

            if (zcBuilder == null) {
                throw new RuntimeException("解析失败");
            }

            Map<String,Object> stormConf = buildConf();

            StormTopology stormTopology = zcBuilder.getTopology();
            Map<String,Bolt> map = stormTopology.get_bolts();
            Map<String,SpoutSpec> map1 = stormTopology.get_spouts();
            for(Bolt bolt:map.values()){
                bolt.get_common().set_parallelism_hint(2);
            }

            System.setProperty("storm.jar", new File(uri).toString());
            StormSubmitter.submitTopology("zc" + System.currentTimeMillis() + "", stormConf, zcBuilder.getTopology());

        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            FileSystemUtils.deleteRecursively(new File(uri));
        }
    }

    public String getRemoteZKServer() {
        return remoteZKServer;
    }

    public void setRemoteZKServer(String remoteZKServer) {
        this.remoteZKServer = remoteZKServer;
    }

    public int getRemoteZKPort() {
        return remoteZKPort;
    }

    public void setRemoteZKPort(int remoteZKPort) {
        this.remoteZKPort = remoteZKPort;
    }


}
