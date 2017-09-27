package com.yss.model;


import com.yss.entity.SysUserEntity;

public class AuditWrapper {

    SysUserEntity user;
    SessionOutput sessionOutput;

    public AuditWrapper(SysUserEntity user, SessionOutput sessionOutput) {
        this.user=user;
        this.sessionOutput=sessionOutput;
    }

    public SysUserEntity getUser() {
        return user;
    }

    public void setUser(SysUserEntity user) {
        this.user = user;
    }

    public SessionOutput getSessionOutput() {
        return sessionOutput;
    }

    public void setSessionOutput(SessionOutput sessionOutput) {
        this.sessionOutput = sessionOutput;
    }
}
