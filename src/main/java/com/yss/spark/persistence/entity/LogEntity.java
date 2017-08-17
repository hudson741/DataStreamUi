package com.yss.spark.persistence.entity;

/**
 * Created by xingjun.Li on 2017/8/17.
 */
public class LogEntity {

    private Integer logId;

    private Integer userId;

    private String userName;

    private String ip;

    private String action;

    public LogEntity(Integer userId, String userName, String ip, String action) {
        this.userId = userId;
        this.userName = userName;
        this.ip = ip;
        this.action = action;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", ip='" + ip + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
