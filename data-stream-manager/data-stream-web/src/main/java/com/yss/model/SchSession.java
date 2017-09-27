package com.yss.model;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.yss.entity.SystemEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 * contains information for an ssh session
 */
public class SchSession {


    Long userId;
    Session session;
    Channel channel;
    PrintStream commander;
    InputStream outFromChannel;
    OutputStream inputToChannel;
    SystemEntity hostSystem;


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public PrintStream getCommander() {
        return commander;
    }

    public void setCommander(PrintStream commander) {
        this.commander = commander;
    }

    public InputStream getOutFromChannel() {
        return outFromChannel;
    }

    public void setOutFromChannel(InputStream outFromChannel) {
        this.outFromChannel = outFromChannel;
    }

    public OutputStream getInputToChannel() {
        return inputToChannel;
    }

    public void setInputToChannel(OutputStream inputToChannel) {
        this.inputToChannel = inputToChannel;
    }

    public SystemEntity getHostSystem() {
        return hostSystem;
    }

    public void setHostSystem(SystemEntity hostSystem) {
        this.hostSystem = hostSystem;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
