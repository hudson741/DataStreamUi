package com.yss.yarn.model;

import com.floodCtr.generate.FloodJobRunningState;

import java.io.Serializable;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/29
 */
public class DockerJob implements Serializable{

    private String jobId;

    private String runIp;

    private String businessType;

    private int state;

    private int cpu;

    private int memory;

    private String dockerIp;

    private String netUrl;

    private String iPBind;

    private String businessTag;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getRunIp() {
        return runIp;
    }

    public void setRunIp(String runIp) {
        this.runIp = runIp;
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

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public String getDockerIp() {
        return dockerIp;
    }

    public void setDockerIp(String dockerIp) {
        this.dockerIp = dockerIp;
    }

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public String getiPBind() {
        return iPBind;
    }

    public void setiPBind(String iPBind) {
        this.iPBind = iPBind;
    }

    public String getBusinessTag() {
        return businessTag;
    }

    public void setBusinessTag(String businessTag) {
        this.businessTag = businessTag;
    }

    public static DockerJob convert(FloodJobRunningState floodJobRunningState){
        DockerJob dockerJob = new DockerJob();
        dockerJob.setBusinessTag(floodJobRunningState.getFloodJob().getBusinessTag());
        dockerJob.setBusinessType(floodJobRunningState.getBusinessType());
        dockerJob.setCpu(floodJobRunningState.getFloodJob().getCpu());
        dockerJob.setDockerIp(floodJobRunningState.getFloodJob().getDockerCMD().getIp());
        dockerJob.setState(floodJobRunningState.getState());
        dockerJob.setiPBind(floodJobRunningState.getFloodJob().getNodeBind());
        dockerJob.setMemory(floodJobRunningState.getFloodJob().getMemory());
        dockerJob.setJobId(floodJobRunningState.getFloodJob().getJobId());
        dockerJob.setNetUrl(floodJobRunningState.getFloodJob().getNetUrl());
        dockerJob.setRunIp(floodJobRunningState.getRunIp());
        return dockerJob;
    }


}
