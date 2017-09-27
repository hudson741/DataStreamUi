package com.yss.ws;

import com.google.gson.Gson;
import com.yss.model.SessionOutput;


import javax.websocket.Session;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * class to send output to web socket client
 */
public class SentOutputTask implements Runnable {


    Session session;
    Long sessionId;

    public SentOutputTask(Long sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;

    }

    public void run() {

        while (session.isOpen()) {
            List<SessionOutput> outputList = SessionOutputUtil.getOutput(sessionId);
            try {
                if (outputList != null && !outputList.isEmpty()) {
                    String json = new Gson().toJson(outputList);
                    //send json to session
                    this.session.getBasicRemote().sendText(json);
                }
                Thread.sleep(5);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }
}
