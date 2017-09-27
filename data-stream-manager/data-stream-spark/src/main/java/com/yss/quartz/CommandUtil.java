package com.yss.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/13
 */
public class CommandUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommandUtil.class);

    public static void executeAndGetOutput(String[] command, final CmdCallBack callBack) throws IOException, InterruptedException {
        final Process process = executeCommand(command, new File("."), null);
        logger.info("11111111111111111111111111111111111111111111111111111");
        Thread cmdExe = new Thread(){
            @Override
            public void run() {
                try {
                    int exitCode = process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        cmdExe.start();
    }

    private static Process executeCommand(String[] command, File workingDir, Map<String, String> extraEnvironment) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command);
        if(workingDir!=null){
            builder.directory(workingDir);
        }
        if(extraEnvironment!=null){
            Map<String, String> environment = builder.environment();
            for(Map.Entry<String, String> entry : extraEnvironment.entrySet()){
                environment.put(entry.getKey(), entry.getValue());
            }
        }
        return  builder.start();

    }



}
























