package com.yss.storm.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Host implements Comparable<Host> {
    List<SlotStatus> slots = new ArrayList<SlotStatus>();
    String           host;
    String           ip;
    String           uptime;
    String           supId;
    int              slotsTotal;
    int              slotsUsed;

    @Override
    public int compareTo(Host o) {
        return this.getHost().compareTo(o.getHost());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Host)) {
            return false;
        }

        Host host1 = (Host) o;

        if ((host != null)
            ? !host.equals(host1.host)
            : host1.host != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (host != null)
               ? host.hashCode()
               : 0;
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

    public List<SlotStatus> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotStatus> slots) {
        this.slots = slots;
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

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}
