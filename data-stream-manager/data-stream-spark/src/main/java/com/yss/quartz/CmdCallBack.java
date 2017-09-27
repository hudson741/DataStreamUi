package com.yss.quartz;

/**
 * @author lixingjun
 * @email lixingjun@ysstech.com
 * @date 2017/9/13
 */
public interface CmdCallBack {

    void insertNewOutput(String out);

    void insertNewErrorOutput(String out);

    void exitCode(int exitCode);

}
