package com.yss.storm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mzang
 * Date: 2014-10-09
 * Time: 17:05
 */
public class Host implements Comparable<Host> {
    String host;
    String ip;
    String uptime;
    String supId;
    int slotsTotal;
    int slotsUsed;
    List<SlotStatus> slots = new ArrayList<SlotStatus>();

    public List<SlotStatus> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotStatus> slots) {
        this.slots = slots;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public int getSlotsTotal() {
        return slotsTotal;
    }

    public void setSlotsTotal(int slotsTotal) {
        this.slotsTotal = slotsTotal;
    }

    public int getSlotsUsed() {
        return slotsUsed;
    }

    public void setSlotsUsed(int slotsUsed) {
        this.slotsUsed = slotsUsed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Host)) return false;

        Host host1 = (Host) o;

        if (host != null ? !host.equals(host1.host) : host1.host != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return host != null ? host.hashCode() : 0;
    }

    @Override
    public int compareTo(Host o) {
        return this.getHost().compareTo(o.getHost());
    }
}
