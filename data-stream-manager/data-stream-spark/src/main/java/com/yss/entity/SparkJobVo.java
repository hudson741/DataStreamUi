package com.yss.entity;

import java.util.Date;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/19
 */
public class SparkJobVo {

    private String jobName;

    private String yarnAppId;

    private String status;

    private String trackingUi;

    private Date startTime;

    private String consumeTime;

    private String trackingUI;

    private String logUI;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getYarnAppId() {
        return yarnAppId;
    }

    public void setYarnAppId(String yarnAppId) {
        this.yarnAppId = yarnAppId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackingUi() {
        return trackingUi;
    }

    public void setTrackingUi(String trackingUi) {
        this.trackingUi = trackingUi;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(String consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getTrackingUI() {
        return trackingUI;
    }

    public void setTrackingUI(String trackingUI) {
        this.trackingUI = trackingUI;
    }

    public String getLogUI() {
        return logUI;
    }

    public void setLogUI(String logUI) {
        this.logUI = logUI;
    }



    @Override
    public String toString() {
        return "SparkJobVo{" +
                "jobName='" + jobName + '\'' +
                ", yarnAppId='" + yarnAppId + '\'' +
                ", status='" + status + '\'' +
                ", trackingUi='" + trackingUi + '\'' +
                ", startTime=" + startTime +
                ", consumeTime='" + consumeTime + '\'' +
                ", trackingUI='" + trackingUI + '\'' +
                ", logUI='" + logUI + '\'' +
                '}';
    }
}
