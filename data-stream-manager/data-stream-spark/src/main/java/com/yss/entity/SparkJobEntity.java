package com.yss.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月28日 下午12:54:44
 */
public class SparkJobEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 任务调度参数key
	 */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";
	
	/**
	 * 任务id
	 */
	private Long jobId;

	@NotBlank(message = "任务名称不能为Null")
	private String jobName;

	private String jobDescript;

	@NotBlank(message = "jar文件名称不能为Null")
	private String jarFileName;

	@NotBlank(message = "主类不能为Null")
	private String mainClass;

	private String classArgs;

	@NotNull(message = "driverDocker不能为Null")
	private Integer driverDockerType;

	@NotNull(message = "executorDocker不能为Null")
	private Integer executorDockerType;

	private Integer executorNum;

	private String sparkConf;

	@NotBlank(message = "cron表达式不能为Null")
	private String cronExpression;

	private Integer status;

	private Long userId;

	private Date createTime;

	private Date lastUpdateTime;

	public String getExecutorMemory(){
		String memary = null;
		switch (executorDockerType){
			case 0:
				memary = "2048M";
				break;
			case 1:
				memary = "4096M";
				break;
		}
		return memary;
	}

	public String getExecutorCore(){
		String cores = null;
		switch (executorDockerType){
			case 0:
				cores = "1";
				break;
			case 1:
				cores = "2";
				break;
		}
		return cores;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobDescript() {
		return jobDescript;
	}

	public void setJobDescript(String jobDescript) {
		this.jobDescript = jobDescript;
	}

	public String getJarFileName() {
		return jarFileName;
	}

	public void setJarFileName(String jarFileName) {
		this.jarFileName = jarFileName;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public String getClassArgs() {
		return classArgs;
	}

	public void setClassArgs(String classArgs) {
		this.classArgs = classArgs;
	}

	public Integer getDriverDockerType() {
		return driverDockerType;
	}

	public void setDriverDockerType(Integer driverDockerType) {
		this.driverDockerType = driverDockerType;
	}

	public Integer getExecutorDockerType() {
		return executorDockerType;
	}

	public void setExecutorDockerType(Integer executorDockerType) {
		this.executorDockerType = executorDockerType;
	}

	public Integer getExecutorNum() {
		return executorNum;
	}

	public void setExecutorNum(Integer executorNum) {
		this.executorNum = executorNum;
	}

	public String getSparkConf() {
		return sparkConf;
	}

	public void setSparkConf(String sparkConf) {
		this.sparkConf = sparkConf;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "SparkJobEntity{" +
				"jobId=" + jobId +
				", jobName='" + jobName + '\'' +
				", jobDescript='" + jobDescript + '\'' +
				", jarFileName='" + jarFileName + '\'' +
				", mainClass='" + mainClass + '\'' +
				", classArgs='" + classArgs + '\'' +
				", driverDockerType=" + driverDockerType +
				", executorDockerType=" + executorDockerType +
				", executorNum=" + executorNum +
				", sparkConf='" + sparkConf + '\'' +
				", cronExpression='" + cronExpression + '\'' +
				", status=" + status +
				", userId=" + userId +
				", createTime=" + createTime +
				", lastUpdateTime=" + lastUpdateTime +
				'}';
	}
}
