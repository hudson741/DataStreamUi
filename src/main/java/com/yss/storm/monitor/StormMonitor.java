package com.yss.storm.monitor;

import java.io.IOException;

import java.util.Map;

import com.yss.storm.model.Host;
import com.yss.storm.model.Topology;

/**
 * Created by zhangchi on 2017/5/19.
 *
 * storm监控
 */
public interface StormMonitor {

    /**
     * 集群配置
     * @return
     */
    Map<String, Object> getClusterConfig();

    /**
     * 集群总览
     * @return
     */
    Map<String, Object> getClusterSummary() throws IOException;

    /**
     * 组件明细
     * @param topoId
     * @param compId
     * @return
     */
    Map<String, Object> getComponentDetails(String topoId, String compId);

    /**
     * 服务器明细
     * @return
     */
    Map<String, Host> getHostWithExecutorDetails();

    /**
     * 主节点总览
     * @return
     */
    Map<String, Object> getNimbusSummary();

    /**
     * 从节点总览
     * @return
     */
    Map<String, Object> getSupervisorSummary();

    /**
     * 计算拓扑总览
     * @return
     */
    Map<String, Object> getTopologies();

    /**
     * 计算拓扑总览
     * @return
     */
    Topology[] getTopologiesSummary();

    /**
     * 计算拓扑详细
     * @param topoId
     * @return
     */
    Map<String, Object> getTopologyDetails(String topoId);

    /**
     * 计算拓扑下组件明细
     * @param topoId
     * @return
     */
    Map<String, Object> getTopologyDetailsWithComponentDetails(String topoId);

    /**
     * 杀死计算拓扑
     * @param topologyId
     * @return
     */
    Map<String,Object> killTopology(String topologyId);

    /**
     * 激活拓扑
     * @param topoId
     * @return
     */
    Map<String,Object> activeTopo(String topoId);

    /**
     * 失效拓扑
     * @param topoId
     * @return
     */
    Map<String,Object> deactiveTopo(String topoId);
}


//~ Formatted by Jindent --- http://www.jindent.com
