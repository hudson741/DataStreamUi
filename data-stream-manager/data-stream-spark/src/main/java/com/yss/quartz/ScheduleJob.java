package com.yss.quartz;

import com.yss.config.HadoopConf;
import com.yss.entity.SparkJobEntity;
import com.yss.entity.SparkJobLogEntity;
import com.yss.service.ScheduleJobLogService;
import com.yss.utils.SpringContextUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.spark.deploy.YssSparkContext;
import org.apache.spark.deploy.docker.YssClient;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 定时任务
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月30日 下午12:44:21
 */
@DisallowConcurrentExecution
public class ScheduleJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static ConcurrentHashMap<String, ApplicationId> appIdsMap = new ConcurrentHashMap<>();

    public static ApplicationId getAppId(String appId){
        return appIdsMap.get(appId);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        SparkJobEntity sparkJobEntity = (SparkJobEntity) context.getMergedJobDataMap()
                .get(SparkJobEntity.JOB_PARAM_KEY);
        logger.info("开始执行定时任务, id={}", sparkJobEntity.getJobId());

        //获取spring bean
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtils.getBean("scheduleJobLogService");

        //数据库执行记录
        SparkJobLogEntity log = new SparkJobLogEntity();
        log.setJobId(sparkJobEntity.getJobId());
        log.setJobName(sparkJobEntity.getJobName());
        log.setMainClass(sparkJobEntity.getMainClass());
        log.setParams(sparkJobEntity.getClassArgs());
        log.setStartTime(new Date());

        //任务开始时间
        long startTime = System.currentTimeMillis();
        scheduleJobLogService.insertNewLog(log);

        FinalApplicationStatus finalState = null;
        ApplicationId applicationId = null;
        try {
            List<String> params = new ArrayList<>();
            params.add("--jar");
            params.add("file:/root/spark-yss-app-shade.jar");
            params.add("--class");
            params.add("com.yss.yarn.YarnTest");
//            params.add("--arg");
//            params.add("sdsds");
            Map<String, String> sysprop = new HashMap<>();
            sysprop.put("spark.driver.memory", "1900");
            sysprop.put("spark.executor.memory", "1900");
            sysprop.put("spark.executor.instances", "1");
            sysprop.put("spark.app.name", "com.yss.yarn.YarnTest");
            sysprop.put("spark.submit.deployMode", "cluster");
            sysprop.put("spark.master", "yarn");
            sysprop.put("spark.executor.cores", "1");

            YssClient yssClient = YssSparkContext.createYarnClient(params, sysprop, HadoopConf.hadoopConf, HadoopConf.yarnConfiguration);
            applicationId = yssClient.submitYssApp();

            if(applicationId!=null){
                appIdsMap.put(applicationId.toString(), applicationId);
            }

            String connectionString = "192.168.102.156:2181";
            RetryPolicy retryPolicy = new RetryNTimes(4, 2000);

            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString(connectionString)
                    .retryPolicy(retryPolicy)
                    .connectionTimeoutMs(2000)
                    .sessionTimeoutMs(8000)
                    .build();
            client.start();

            YarnApplicationState currentState = null;
            String trackingUi = null;
            while (true){
                ApplicationReport report = yssClient.getApplicationReport(applicationId);
                YarnApplicationState state = report.getYarnApplicationState();

                if (YarnApplicationState.FINISHED.equals(state) ||
                        YarnApplicationState.FAILED.equals(state) ||
                        YarnApplicationState.KILLED.equals(state)) {
                    yssClient.cleanupStagingDir(applicationId);
                    finalState = report.getFinalApplicationStatus();
                    break;
                }

                if (!state.equals(currentState)) {
                    currentState = state;
                    log.setYarnAppId(applicationId.toString());
                    log.setStatus(currentState.name());
                    scheduleJobLogService.updateLog(log);
                }

                if (trackingUi == null && client.checkExists().forPath("/" + applicationId) != null) {
                    byte[] a = client.getData().forPath("/" + applicationId + "/tracking_ui");
                    trackingUi = new String(a);
                    log.setTrackingUi(trackingUi);
                    scheduleJobLogService.updateLog(log);
                }
                logger.info(new Date() + " appId = " + applicationId + " current status = " + currentState + " tracking ui= " + trackingUi);
                Thread.sleep(1000);
            }

            long times = System.currentTimeMillis() - startTime;
            logger.info("任务执行完毕，任务ID：" + sparkJobEntity.getJobId() + "  总共耗时：" + times + "毫秒");
        } catch (Exception e) {
            logger.error("任务执行失败，任务ID：" + sparkJobEntity.getJobId(), e);
        } finally {
            log.setEndTime(new Date());
            log.setStatus(finalState.name());
            scheduleJobLogService.updateLog(log);
            if(applicationId!=null){
                appIdsMap.remove(applicationId.toString());
            }
        }
    }
}

























