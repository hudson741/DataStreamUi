package com.yss.task;


import com.yss.model.SessionOutput;
import com.yss.ws.SessionOutputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Task to watch for output read from the ssh session stream
 */
public class SecureShellTask implements Runnable {

    private static Logger log = LoggerFactory.getLogger(SecureShellTask.class);

    InputStream outFromChannel;
    SessionOutput sessionOutput;

    public SecureShellTask(SessionOutput sessionOutput, InputStream outFromChannel) {

        this.sessionOutput = sessionOutput;
        this.outFromChannel = outFromChannel;
    }

    public void run() {
        InputStreamReader isr = new InputStreamReader(outFromChannel);
        BufferedReader br = new BufferedReader(isr);
        try {

            SessionOutputUtil.addOutput(sessionOutput);

            char[] buff = new char[1024];
            int read;
            while((read = br.read(buff)) != -1) {

                SessionOutputUtil.addToOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId(), buff,0,read);
                Thread.sleep(10);
            }

            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());

        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
    }

}
