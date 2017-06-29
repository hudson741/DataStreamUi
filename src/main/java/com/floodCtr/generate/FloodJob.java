package com.floodCtr.generate;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.hadoop.yarn.api.records.LocalResource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/22
 */
public class FloodJob implements Serializable{

    // 发布任务id
    private String jobId;

    // cpu强制要求
    private int cpu;

    // memory强制要求
    private int memory;

   //docker网络
    private String netUrl;

    // docker 启动相关参数
    DockerCMD dockerCMD;

    //是否制定IP地址运行
    private String nodeBind;

    //业务标识
    private String businessTag;

    // dockerjob运行中本地资源描述，通常指向hdfs
    Map<String, LocalResource> localResources;

    public FloodJob(String jobId, int cpu, int memory) {
        this.jobId  = jobId;
        this.cpu    = cpu;
        this.memory = memory;
    }

    public FloodJob(){

    }

    public DockerCMD buildDockerCMD() {
        DockerCMD dockerCMD = new DockerCMD();
        this.dockerCMD = dockerCMD;
        return dockerCMD;
    }

    public FloodJob clone() {
        FloodJob floodJob;

        try {
            floodJob = new FloodJob();
            BeanUtils.copyProperties(floodJob,this);
        } catch (Exception e) {
            e.printStackTrace();
            floodJob = new FloodJob(this.jobId,this.cpu,this.memory);
            floodJob.setDockerCMD(this.dockerCMD);
            floodJob.setNodeBind(this.getNodeBind());
            floodJob.setNetUrl(this.getNetUrl());
            floodJob.setBusinessTag(this.getBusinessTag());
            floodJob.setLocalResources(this.localResources);
        }
        return floodJob;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public DockerCMD getDockerCMD() {
        return dockerCMD;
    }

    public void setDockerCMD(DockerCMD dockerCMD) {
        this.dockerCMD = dockerCMD;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Map<String, LocalResource> getLocalResources() {
        return localResources;
    }

    public void setLocalResources(Map<String, LocalResource> localResources) {
        this.localResources = localResources;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public String getNodeBind() {
        return nodeBind;
    }

    public void setNodeBind(String nodeBind) {
        this.nodeBind = nodeBind;
    }

    public String getBusinessTag() {
        return businessTag;
    }

    public void setBusinessTag(String businessTag) {
        this.businessTag = businessTag;
    }

    /**
     * 用于创建docker容器所需的配置
     */
    public static class DockerCMD implements Serializable {

        // docker host映射
        private Map<String, String> host = new HashMap<>();

        // port 宿主机映射
        private Map<String, String> port = new HashMap<>();

        // 使用镜像
        private String imageName;

        // 容器名称
        private String containerName;

        // 启动docker容器传入参数
        private String dockerArgs;

        // overlay IP
        private String ip;

        // hostName
        private String hostName;

        public DockerCMD containerName(String containerName) {
            this.containerName = containerName;

            return this;
        }

        public DockerCMD dockerArgs(String dockerArgs) {
            this.dockerArgs = dockerArgs;

            return this;
        }

        public DockerCMD host(String domainName, String ip) {
            this.host.put(domainName, ip);

            return this;
        }

        public DockerCMD hosts(Map<String ,String> hostMap){
            if(MapUtils.isNotEmpty(hostMap)) {
                for (String domainName : hostMap.keySet()) {
                    this.host.put(domainName, hostMap.get(domainName));
                }
            }
            return this;
        }

        public DockerCMD hostName(String hostName) {
            this.hostName = hostName;

            return this;
        }

        public DockerCMD imageName(String imageName) {
            this.imageName = imageName;

            return this;
        }

        public DockerCMD ip(String ip) {
            this.ip = ip;

            return this;
        }

        public DockerCMD port(String dockerPort, String hostPort) {
            this.port.put(dockerPort, hostPort);

            return this;
        }

        public DockerCMD ports(Map<String,String> portMap) {
            if(MapUtils.isNotEmpty(portMap)) {
                for (String dockerPort : portMap.keySet()) {
                    this.port.put(dockerPort, portMap.get(dockerPort));
                }
            }
            return this;
        }

        public String getContainerName() {
            return containerName;
        }

        public String getDockerArgs() {
            return dockerArgs;
        }

        public Map<String, String> getHost() {
            return host;
        }

        public String getHostName() {
            return hostName;
        }

        public String getImageName() {
            return imageName;
        }

        public String getIp() {
            return ip;
        }

        public Map<String, String> getPort() {
            return port;
        }


    }
}
