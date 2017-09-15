/*
 * Copyright (c) 2013 Yahoo! Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */



package com.yss.yarn.launch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.yss.util.YarnUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Apps;
import org.apache.hadoop.yarn.util.Records;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class APPLaunch {
    private static final Logger LOG = LoggerFactory.getLogger(APPLaunch.class);

//  private MasterClient        _client = null;
    private YarnClient        _yarn;
    private YarnConfiguration _hadoopConf;
    private ApplicationId     _appId;

    private APPLaunch(Map<String, String> yarnConf) {
        this(null, yarnConf);
    }

    private APPLaunch(ApplicationId appId,
                      Map<String, String> yarnConf) {
        Configuration conf = null;

        if ((yarnConf != null) &&!yarnConf.isEmpty()) {
            conf = new Configuration();

            for (String key : yarnConf.keySet()) {
                conf.set(key, yarnConf.get(key));
            }
        }

        _hadoopConf = (conf == null)
                      ? new YarnConfiguration()
                      : new YarnConfiguration(conf);
        _yarn       = YarnClient.createYarnClient();
        _appId      = appId;
        _yarn.init(_hadoopConf);
        _yarn.start();
    }


    private void launchApp(String appName, String queue, int amMB, String appMasterJar,
                           String classPath,String lanchMainClass,Map<String,String> runenv)
            throws Exception {

        YarnClientApplication     client_app = _yarn.createApplication();
        GetNewApplicationResponse app        = client_app.getNewApplicationResponse();

        _appId = app.getApplicationId();
        LOG.debug("_appId:" + _appId);

        ApplicationSubmissionContext appContext = Records.newRecord(ApplicationSubmissionContext.class);

        appContext.setApplicationId(app.getApplicationId());
        appContext.setApplicationName(appName);
        appContext.setQueue(queue);

        ContainerLaunchContext     amContainer    = Records.newRecord(ContainerLaunchContext.class);
        Map<String, LocalResource> localResources = new HashMap<String, LocalResource>();

        LOG.info("Copy App Master jar from local filesystem and add to local environment");
        LOG.info("fuck  " + appMasterJar);

        FileSystem fs      = FileSystem.get(_hadoopConf);
        Path       src     = new Path(appMasterJar);
        String     appHome = YarnUtil.getApplicationHomeForId(_appId.toString());
        Path       dst     = new Path(fs.getHomeDirectory(), appHome + Path.SEPARATOR + "AppMaster.jar");

        fs.copyFromLocalFile(false, true, src, dst);
        localResources.put("AppMaster.jar", YarnUtil.newYarnAppResource(fs, dst));

        amContainer.setLocalResources(localResources);

        LOG.info("Set the environment for the application master");

        Map<String, String> env = new HashMap<String, String>();

        Apps.addToEnvironment(env, Environment.CLASSPATH.name(), "./AppMaster.jar");

        List<String>   yarn_classpath_cmd = java.util.Arrays.asList("yarn", "classpath");

        LOG.info("YARN CLASSPATH COMMAND = [" + yarn_classpath_cmd + "]");

        String yarn_class_path = classPath;

        LOG.info("YARN CLASSPATH = [" + yarn_class_path + "]");
        Apps.addToEnvironment(env, Environment.CLASSPATH.name(), yarn_class_path);

        String java_home = System.getenv("JAVA_HOME");

        if ((java_home != null) &&!java_home.isEmpty()) {
            env.put("JAVA_HOME", java_home);
        }

        LOG.info("Using JAVA_HOME = [" + env.get("JAVA_HOME") + "]");
        env.put("appJar", appMasterJar);
        env.put("appName", appName);
        env.put("appId", _appId.toString());
        env.put("STORM_LOG_DIR", ApplicationConstants.LOG_DIR_EXPANSION_VAR);
        env.putAll(runenv);
        amContainer.setEnvironment(env);

        // Set the necessary command to execute the application master
        Vector<String> vargs = new Vector<String>();

        if ((java_home != null) &&!java_home.isEmpty()) {
            vargs.add(env.get("JAVA_HOME") + "/bin/java");
        } else {
            vargs.add("java");
        }

        vargs.add("-Dlogfile.name=" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/master.log");

        vargs.add(lanchMainClass);
        vargs.add("1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout");
        vargs.add("2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr");

        // Set java executable command
        LOG.info("Setting up app master command:" + vargs);
        amContainer.setCommands(vargs);

        Resource capability = Records.newRecord(Resource.class);

        appContext.setResource(capability);
        appContext.setAMContainerSpec(amContainer);
        appContext.setApplicationName(appName);
        _yarn.submitApplication(appContext);
    }

    public static APPLaunch launchApplication(String appName, String queue, int amMB,
                                              String appJarPath, Map<String, String> yarnConf,
                                              String classPath,String lanchMainClass,Map<String,String> runenv)
            throws Exception {
        APPLaunch appLaunch = new APPLaunch(yarnConf);

        appLaunch.launchApp(appName, queue, amMB, appJarPath, classPath,lanchMainClass,runenv);

        return appLaunch;
    }

    public ApplicationId getAppId() {

        // TODO make this immutable
        return _appId;
    }
}
