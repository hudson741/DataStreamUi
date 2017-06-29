package com.floodCtr.generate;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.NodeId;

import java.io.Serializable;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/26
 */
public class FloodJobRunningState implements Serializable{

    private int state;

    private String runIp;

    private FloodJob floodJob;

    private ContainerId containerId;

    private NodeId nodeId;

    private String businessType;

    public String getRunIp() {
        return runIp;
    }

    public void setRunIp(String runIp) {
        this.runIp = runIp;
    }

    public FloodJob getFloodJob() {
        return floodJob;
    }

    public void setFloodJob(FloodJob floodJob) {
        this.floodJob = floodJob;
    }

    public FloodJobRunningState(){}

    public FloodJobRunningState(ContainerId containerId, NodeId nodeId, String businessType){
        this.containerId = containerId;
        this.nodeId = nodeId;
        this.businessType = businessType;
    }

    @JSONField(serialize = false)
    public ContainerId getContainerId() {
        return containerId;
    }

    public void setContainerId(ContainerId containerId) {
        this.containerId = containerId;
    }

    @JSONField(serialize = false)
    public NodeId getNodeId() {
        return nodeId;
    }

    public void setNodeId(NodeId nodeId) {
        this.nodeId = nodeId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
