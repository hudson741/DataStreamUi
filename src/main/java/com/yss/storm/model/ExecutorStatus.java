package com.yss.storm.model;

import java.util.regex.Pattern;

import com.google.common.base.Splitter;


/**
 * The type Executor status.
 */
public class ExecutorStatus implements Comparable<ExecutorStatus> {
    private static final Splitter SPLITTER = Splitter.on(Pattern.compile("[\\[|\\-|\\]]"))
                                                     .omitEmptyStrings()
                                                     .trimResults();
    /**
     * The Port.
     */
    int    port;
    /**
     * The Host.
     */
    String host;
    /**
     * The Executor id.
     */
    String executorId;
    /**
     * The Uptime.
     */
    String uptime;
    /**
     * The Topo name.
     */
    String topoName;
    /**
     * The Topo id.
     */
    String topoId;
    /**
     * The Comp id.
     */
    String compId;
    /**
     * The Comp type.
     */
    String compType;
    /**
     * The Emitted.
     */
    long   emitted;
    /**
     * The Transferred.
     */
    long   transferred;
    /**
     * The Capacity.
     */
    long   capacity;
    /**
     * The Execute latency.
     */
    long   executeLatency;
    /**
     * The Executed.
     */
    long   executed;
    /**
     * The Process latency.
     */
    String processLatency;
    /**
     * The Acked.
     */
    long   acked;
    /**
     * The Failed.
     */
    long   failed;

    @Override
    public int compareTo(ExecutorStatus o) {
        Iterable<String> t1s = SPLITTER.split(this.executorId);
        String           t1  = "-1";

        for (String str : t1s) {
            t1 = str;

            break;
        }

        Iterable<String> t2s = SPLITTER.split(o.executorId);
        String           t2  = "-1";

        for (String str : t2s) {
            t2 = str;

            break;
        }

        return Integer.parseInt(t1) - Integer.parseInt(t2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ExecutorStatus)) {
            return false;
        }

        ExecutorStatus that = (ExecutorStatus) o;

        if ((executorId != null)
            ? !executorId.equals(that.executorId)
            : that.executorId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (executorId != null)
               ? executorId.hashCode()
               : 0;
    }

    /**
     * Gets acked.
     *
     * @return the acked
     */
    public long getAcked() {
        return acked;
    }

    /**
     * Sets acked.
     *
     * @param acked the acked
     */
    public void setAcked(long acked) {
        this.acked = acked;
    }

    /**
     * Gets capacity.
     *
     * @return the capacity
     */
    public long getCapacity() {
        return capacity;
    }

    /**
     * Sets capacity.
     *
     * @param capacity the capacity
     */
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets comp id.
     *
     * @return the comp id
     */
    public String getCompId() {
        return compId;
    }

    /**
     * Sets comp id.
     *
     * @param compId the comp id
     */
    public void setCompId(String compId) {
        this.compId = compId;
    }

    /**
     * Gets comp type.
     *
     * @return the comp type
     */
    public String getCompType() {
        return compType;
    }

    /**
     * Sets comp type.
     *
     * @param compType the comp type
     */
    public void setCompType(String compType) {
        this.compType = compType;
    }

    /**
     * Gets emitted.
     *
     * @return the emitted
     */
    public long getEmitted() {
        return emitted;
    }

    /**
     * Sets emitted.
     *
     * @param emitted the emitted
     */
    public void setEmitted(long emitted) {
        this.emitted = emitted;
    }

    /**
     * Gets execute latency.
     *
     * @return the execute latency
     */
    public long getExecuteLatency() {
        return executeLatency;
    }

    /**
     * Sets execute latency.
     *
     * @param executeLatency the execute latency
     */
    public void setExecuteLatency(long executeLatency) {
        this.executeLatency = executeLatency;
    }

    /**
     * Gets executed.
     *
     * @return the executed
     */
    public long getExecuted() {
        return executed;
    }

    /**
     * Sets executed.
     *
     * @param executed the executed
     */
    public void setExecuted(long executed) {
        this.executed = executed;
    }

    /**
     * Gets executor id.
     *
     * @return the executor id
     */
    public String getExecutorId() {
        return executorId;
    }

    /**
     * Sets executor id.
     *
     * @param executorId the executor id
     */
    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    /**
     * Gets failed.
     *
     * @return the failed
     */
    public long getFailed() {
        return failed;
    }

    /**
     * Sets failed.
     *
     * @param failed the failed
     */
    public void setFailed(long failed) {
        this.failed = failed;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets process latency.
     *
     * @return the process latency
     */
    public String getProcessLatency() {
        return processLatency;
    }

    /**
     * Sets process latency.
     *
     * @param processLatency the process latency
     */
    public void setProcessLatency(String processLatency) {
        this.processLatency = processLatency;
    }

    /**
     * Gets topo id.
     *
     * @return the topo id
     */
    public String getTopoId() {
        return topoId;
    }

    /**
     * Sets topo id.
     *
     * @param topoId the topo id
     */
    public void setTopoId(String topoId) {
        this.topoId = topoId;
    }

    /**
     * Gets topo name.
     *
     * @return the topo name
     */
    public String getTopoName() {
        return topoName;
    }

    /**
     * Sets topo name.
     *
     * @param topoName the topo name
     */
    public void setTopoName(String topoName) {
        this.topoName = topoName;
    }

    /**
     * Gets transferred.
     *
     * @return the transferred
     */
    public long getTransferred() {
        return transferred;
    }

    /**
     * Sets transferred.
     *
     * @param ransferred the ransferred
     */
    public void setTransferred(long ransferred) {
        this.transferred = ransferred;
    }

    /**
     * Gets uptime.
     *
     * @return the uptime
     */
    public String getUptime() {
        return uptime;
    }

    /**
     * Sets uptime.
     *
     * @param uptime the uptime
     */
    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
