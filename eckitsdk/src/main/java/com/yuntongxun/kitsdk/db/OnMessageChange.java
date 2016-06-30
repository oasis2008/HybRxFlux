package com.yuntongxun.kitsdk.db;


public interface OnMessageChange {
    /**
     * 数据库改变
     */
    public void onChanged(String sessionId);
}
