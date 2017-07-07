package com.floodCtr.generate;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.NodeId;

import java.io.Serializable;

/**
 * @Description  任务监控实体，发布任务后，即创建
 * @author: zhangchi
 * @Date: 2017/6/26
 */
public class FloodJobRunningState implements Serializable{

    private String jobId;

    private RUNNING_STATE runningState = RUNNING_STATE.WAITING;

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

    public FloodJobRunningState(){}

    public void setFloodJob(FloodJob floodJob) {
        this.floodJob = floodJob;
    }

    public FloodJobRunningState(String jobId,ContainerId containerId, NodeId nodeId, String businessType){
        this.containerId = containerId;
        this.nodeId = nodeId;
        this.businessType = businessType;
        this.jobId = jobId;
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

    public RUNNING_STATE getRunningState() {
        return runningState;
    }

    public void setRunningState(RUNNING_STATE runningState) {
        this.runningState = runningState;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public enum RUNNING_STATE{

        WAITING("WAITING"),RUNNING("RUNNING"),FAILD("FAILD"),RESTARTING("RESTARTING"),STOP("STOP");

        private RUNNING_STATE(String code){
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        private String code;
    }

}
