package com.yss.entity;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/1
 */
public class SystemEntity {

    private Integer id;

    @NotBlank(message = "displayNm不能为null")
    private String displayNm;

    private Integer addUserId;

    private String displayLabel;

    @NotBlank(message = "user不能为null")
    private String user;

    @NotBlank(message = "host不能为null")
    private String host;

    private Integer port = 22;

    private String authorizedKeys = "~/.ssh/authorized_keys";

    private String statusCd;

    private String  errorMsg;

    private Integer instanceId;

    private Date lastUpdateTime;




    public static final String INITIAL_STATUS="INITIAL";
    public static final String AUTH_FAIL_STATUS="AUTHFAIL";
    public static final String PUBLIC_KEY_FAIL_STATUS="KEYAUTHFAIL";
    public static final String GENERIC_FAIL_STATUS="GENERICFAIL";
    public static final String SUCCESS_STATUS="SUCCESS";
    public static final String HOST_FAIL_STATUS="HOSTFAIL";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayNm() {
        return displayNm;
    }

    public void setDisplayNm(String displayNm) {
        this.displayNm = displayNm;
    }

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAuthorizedKeys() {
        return authorizedKeys;
    }

    public void setAuthorizedKeys(String authorizedKeys) {
        this.authorizedKeys = authorizedKeys;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public Integer getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "SystemEntity{" +
                "id=" + id +
                ", displayNm='" + displayNm + '\'' +
                ", addUserId=" + addUserId +
                ", user='" + user + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", authorizedKeys='" + authorizedKeys + '\'' +
                ", statusCd='" + statusCd + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
