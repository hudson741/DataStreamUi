package com.yss.storm.submit;

import java.net.URI;

/**
 * Created by zhangchi on 2017/5/16.
 */
public interface StormSubmiter {
    void SubmitStormTopology(URI uri);
}
