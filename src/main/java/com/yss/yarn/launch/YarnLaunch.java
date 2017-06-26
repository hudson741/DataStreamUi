package com.yss.yarn.launch;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.net.URLDecoder;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yss.util.FileUtil;
import com.yss.yarn.Exception.JarNotExsitsException;
import com.yss.yarn.controller.YarnLaunchController;

/**
 * @Description
 * @author: zhangchi
 * @Date: 2017/6/20
 */
public class YarnLaunch implements YarnLaunchService {
    private Logger              logger = LoggerFactory.getLogger(YarnLaunchService.class);
    private String              appName;
    private String              defaultQueue;
    private int                 amSize;
    private String              classPath;

    public void launchApp(String path,String jarName,String hdfsUrl,String yarnUrl,String lanchMainClass) throws Exception {
        URL jarPathUrl = new File(path).toURL();
        String jarPath    = jarPathUrl.getPath();

        if (StringUtils.isEmpty(jarPath)) {
            throw new JarNotExsitsException("需要指定运行的jar不存在");
        }

        String targetPath = getJarPath(YarnLaunchController.class) + "/dockerpubjar";
        targetPath=targetPath.replace("file:","");

        String target     = FileUtil.copy(jarPathUrl.openStream(), targetPath,jarName);

        Map<String, String> yarnConf = new HashMap<>();
        yarnConf.put("fs.defaultFS",hdfsUrl);
        yarnConf.put("yarn.resourcemanager.address",yarnUrl);

        APPLaunch.launchApplication(appName, defaultQueue, amSize, target, yarnConf, classPath,lanchMainClass);
    }

    public int getAmSize() {
        return amSize;
    }

    public void setAmSize(int amSize) {
        this.amSize = amSize;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getDefaultQueue() {
        return defaultQueue;
    }

    public void setDefaultQueue(String defaultQueue) {
        this.defaultQueue = defaultQueue;
    }

    private String getJarPath(Class<?> my_class) throws IOException {
        ClassLoader loader     = my_class.getClassLoader();
        String      class_file = my_class.getName().replaceAll("\\.", "/") + ".class";

        for (Enumeration<URL> itr = loader.getResources(class_file); itr.hasMoreElements(); ) {
            URL url = itr.nextElement();

            if ("jar".equals(url.getProtocol())) {
                String toReturn = url.getPath();

                toReturn = toReturn.replaceAll("\\+", "%2B");
                toReturn = URLDecoder.decode(toReturn, "UTF-8");

                String returnP = toReturn.replaceAll("!.*$", "");

                return new File(returnP).getParent();
            }
        }

        File   file = new File(loader.getResource(class_file).getFile());
        String pro  = System.getProperty("user.dir");

        while (true) {
            if (file.exists() && file.getAbsolutePath().endsWith(pro)) {
                return file.getAbsolutePath();
            } else {
                File file1 = file;

                file = new File(file1.getParent());
                logger.info("hudson " + file.getAbsolutePath());

                if (!file.exists()) {
                    return null;
                }
            }
        }
    }


}
