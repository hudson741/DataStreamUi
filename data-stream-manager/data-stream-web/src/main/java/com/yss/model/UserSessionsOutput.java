package com.yss.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserSessionsOutput {

    //instance id, host output
    Map<Integer, SessionOutput> sessionOutputMap = new ConcurrentHashMap<>();


    public Map<Integer, SessionOutput> getSessionOutputMap() {
        return sessionOutputMap;
    }

    public void setSessionOutputMap(Map<Integer, SessionOutput> sessionOutputMap) {
        this.sessionOutputMap = sessionOutputMap;
    }
}



