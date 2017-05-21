package com.yss.storm.monitor;

import com.yss.storm.model.Host;
import com.yss.storm.model.Topology;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhangchi on 2017/5/19.
 *
 * storm监控
 */
public interface StormMonitor {

    /**
     * 计算拓扑总览
     * @return
     */
    public Topology[] getTopologiesSummary();


    /**
     * 集群总览
     * @return
     */
    public Map<String, Object> getClusterSummary() throws IOException;


    /**
     * 主节点总览
     * @return
     */
    public Map<String,Object> getNimbusSummary();

    /**
     * 从节点总览
     * @return
     */
    public Map<String, Object> getSupervisorSummary();


    /**
     * 集群配置
     * @return
     */
    public Map<String, Object> getClusterConfig();

    /**
     * 计算拓扑详细
     * @param topoId
     * @return
     */
    public Map<String, Object> getTopologyDetails(String topoId);


    /**
     * 组件明细
     * @param topoId
     * @param compId
     * @return
     */
    public Map<String, Object> getComponentDetails(String topoId, String compId);

    /**
     * 计算拓扑下组件明细
     * @param topoId
     * @return
     */
    public Map<String, Object> getTopologyDetailsWithComponentDetails(String topoId);

    /**
     * 服务器明细
     * @return
     */
    public Map<String, Host> getHostWithExecutorDetails();

}
