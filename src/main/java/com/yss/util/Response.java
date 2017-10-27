package com.yss.util;

import com.alibaba.fastjson.JSONObject;


/**
 * Created by zhangchi on 2017/9/12.
 */
public class Response {


    public static final String RESPONSE = "response";

    private boolean successed;

    private String message;

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Response setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    private Response() {
    };

    public static Response getInstance(boolean successed, String message) {
        return getInstance(successed,ErrorCode.COMPLETE, message);
    }

    public static Response getInstance(boolean successed,ErrorCode errorCode,String msg){
        Response response = new Response();
        response.successed = successed;
        response.message = msg;
        response.errorCode = errorCode;
        return response;

    }

    public boolean isSuccessed() {
        return successed;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public Response setSuccessed(boolean successed) {
        this.successed = successed;
        return this;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public enum ErrorCode {

        NETWORK, SYSTEM,COMPLETE;

    }
}
