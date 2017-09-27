package com.yss.entity;

import com.yss.config.HadoopConf;
import com.yss.utils.IPUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class SparkJobLogEntity {
    private Long logId;

    private Long jobId;

    private String jobName;

    private String yarnAppId;

    private String appName;

    private String mainClass;

    private String params;

    private String status;

    private String trackingUi;

    private Date startTime;

    private Date endTime;

    private Date lastUpdateTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getYarnAppId() {
        return yarnAppId;
    }

    public void setYarnAppId(String yarnAppId) {
        this.yarnAppId = yarnAppId == null ? null : yarnAppId.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass == null ? null : mainClass.trim();
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getTrackingUi() {
        return trackingUi;
    }

    public void setTrackingUi(String trackingUi) {
        this.trackingUi = trackingUi == null ? null : trackingUi.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "SparkJobLogEntity{" +
                "logId=" + logId +
                ", jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", yarnAppId='" + yarnAppId + '\'' +
                ", appName='" + appName + '\'' +
                ", mainClass='" + mainClass + '\'' +
                ", params='" + params + '\'' +
                ", status='" + status + '\'' +
                ", trackingUi='" + trackingUi + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }

    public SparkJobVo changeToVo() throws UnknownHostException {
        SparkJobVo vo = new SparkJobVo();
        vo.setJobName(jobName);
        vo.setYarnAppId(yarnAppId);
        vo.setStatus(status);
        vo.setStartTime(startTime);
        if (trackingUi != null) {
            String[] hostPort = trackingUi.replace("http://", "").split(":");
            String host = hostPort[0];
            String port = hostPort[1];
            String ip = IPUtils.host2Ip(host);
            String ipTrackingUi = "http://" + ip + ":" + port;
            vo.setTrackingUI(ipTrackingUi);
        }

        String yarnWebAddress = HadoopConf.yarnConfiguration.get("yarn.resourcemanager.webapp.address");
        String host = yarnWebAddress.split(":")[0];
        String port = yarnWebAddress.split(":")[1];
        String hadoopLogUI = "http://" + IPUtils.host2Ip(host) + ":" + port + "/cluster/app/" + yarnAppId;
        vo.setLogUI(hadoopLogUI);

        if (endTime != null) {
            long time = (endTime.getTime() - startTime.getTime()) / 1000;

            StringBuilder builder = new StringBuilder();
            //计算秒
            long second = time % 60;
            long minute = time / 60;
            long hour = time / 60 / 60;
            if (hour != 0) {
                builder.append(hour + "h");
            }
            if (minute != 0) {
                builder.append(minute + "m");
            }
            builder.append(second + "s");
            vo.setConsumeTime(builder.toString());

        }
        return vo;
    }
}