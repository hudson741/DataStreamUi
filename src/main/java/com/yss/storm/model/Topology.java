package com.yss.storm.model;

public class Topology {
    String id;
    String name;
    String owner;
    String status;
    String uptime;
    int    tasksTotal;
    int    workersTotal;
    int    executorsTotal;
    double assignedCpu;

    public double getAssignedCpu() {
        return assignedCpu;
    }

    public void setAssignedCpu(double assignedCpu) {
        this.assignedCpu = assignedCpu;
    }

    public int getExecutorsTotal() {
        return executorsTotal;
    }

    public void setExecutorsTotal(int executorsTotal) {
        this.executorsTotal = executorsTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTasksTotal() {
        return tasksTotal;
    }

    public void setTasksTotal(int tasksTotal) {
        this.tasksTotal = tasksTotal;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public int getWorkersTotal() {
        return workersTotal;
    }

    public void setWorkersTotal(int workersTotal) {
        this.workersTotal = workersTotal;
    }
}
