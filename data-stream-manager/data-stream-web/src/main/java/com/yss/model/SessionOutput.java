package com.yss.model;


import com.yss.entity.SystemEntity;

/**
 * Output from ssh session
 */
public class SessionOutput extends SystemEntity {
    Long sessionId;
    StringBuilder output = new StringBuilder();

    public SessionOutput() {


    }
    public SessionOutput(Long sessionId, SystemEntity hostSystem) {
        this.sessionId=sessionId;
        this.setId(hostSystem.getId());
        this.setInstanceId(hostSystem.getInstanceId());
        this.setUser(hostSystem.getUser());
        this.setHost(hostSystem.getHost());
        this.setPort(hostSystem.getPort());
        this.setDisplayNm(hostSystem.getDisplayNm());
        this.setAuthorizedKeys(hostSystem.getAuthorizedKeys());

    }
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public StringBuilder getOutput() {
        return output;
    }

    public void setOutput(StringBuilder output) {
        this.output = output;
    }

}