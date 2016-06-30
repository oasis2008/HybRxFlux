/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.yuntongxun.kitsdk.ui.chatting.model;

import android.database.Cursor;




public class ECConversation {

    private String sessionId;
    private int msgType;
    private long dateTime;
    private int sendStatus;
    private int unreadCount;
    private String content;
    private String username;
    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }
    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the msgType
     */
    public int getMsgType() {
        return msgType;
    }
    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    /**
     * @return the dateTime
     */
    public long getDateTime() {
        return dateTime;
    }
    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
    /**
     * @return the sendStatus
     */
    public int getSendStatus() {
        return sendStatus;
    }
    /**
     * @param sendStatus the sendStatus to set
     */
    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }
    /**
     * @return the unreadCount
     */
    public int getUnreadCount() {
        return unreadCount;
    }
    /**
     * @param unreadCount the unreadCount to set
     */
    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public void setCursor(Cursor cursor) {
        this.unreadCount = cursor.getInt(0);
        this.msgType = cursor.getInt(1);
        this.sendStatus = cursor.getInt(2);
        this.dateTime = cursor.getLong(3);
        this.sessionId = cursor.getString(4);
        this.content = cursor.getString(5);
        this.username = cursor.getString(6);

        if(this.username == null && !this.sessionId.startsWith("G")) {

                username = sessionId;
        }
    }
	@Override
	public String toString() {
		return "Conversation [sessionId=" + sessionId + ", msgType=" + msgType
				+ ", dateTime=" + dateTime + ", sendStatus=" + sendStatus
				+ ", unreadCount=" + unreadCount + ", content=" + content
				+ ", username=" + username + "]";
	}
    
    

}
