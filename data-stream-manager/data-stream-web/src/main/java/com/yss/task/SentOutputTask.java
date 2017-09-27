package com.yss.task;

import com.google.gson.Gson;
import com.yss.entity.SysUserEntity;
import com.yss.model.SessionOutput;
import com.yss.ws.SessionOutputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.sql.Connection;
import java.util.List;

/**
 * class to send output to web socket client
 */
public class SentOutputTask implements Runnable {

    private static Logger log = LoggerFactory.getLogger(SentOutputTask.class);

    Session session;
    Long sessionId;
    SysUserEntity user;

    public SentOutputTask(Long sessionId, Session session, SysUserEntity user) {
        this.sessionId = sessionId;
        this.session = session;
        this.user = user;
    }

    public void run() {

        Gson gson = new Gson();

        while (session.isOpen()) {
            List<SessionOutput> outputList = SessionOutputUtil.getOutput(sessionId);
            try {
                if (outputList != null && !outputList.isEmpty()) {
                    String json = gson.toJson(outputList);
                    //send json to session
                    this.session.getBasicRemote().sendText(json);
                }
                Thread.sleep(25);
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
            }
        }
    }
}
