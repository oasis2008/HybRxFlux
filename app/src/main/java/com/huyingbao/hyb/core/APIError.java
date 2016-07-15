package com.huyingbao.hyb.core;

/**
 * Created by Administrator on 2016/7/14.
 */
public class APIError extends Exception {
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private int statusCode;
    private String error;

    public APIError(int statusCode, String error) {
        this.statusCode=statusCode;
        this.error=error;
    }
}
