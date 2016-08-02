package com.huyingbao.hyb.model;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MsgFromUser {

    /**
     * belongUser : 13
     * msgUserId : 1
     * longitude : 118.77
     * latitude : 31.9849
     * radius : 1234
     * type : 0
     * content : 要一件红色的裤子
     * contenType : 0
     * status : 0
     * createdAt : 2016-08-01T08:09:00.000Z
     * updatedAt : 2016-08-01T08:09:00.000Z
     */

    private int belongUser;
    private int msgUserId;
    private double longitude;
    private double latitude;
    private int radius;
    private int type;
    private String content;
    private int contenType;
    private int status;
    private String createdAt;
    private String updatedAt;

    public int getBelongUser() { return belongUser;}

    public void setBelongUser(int belongUser) { this.belongUser = belongUser;}

    public int getMsgUserId() { return msgUserId;}

    public void setMsgUserId(int msgUserId) { this.msgUserId = msgUserId;}

    public double getLongitude() { return longitude;}

    public void setLongitude(double longitude) { this.longitude = longitude;}

    public double getLatitude() { return latitude;}

    public void setLatitude(double latitude) { this.latitude = latitude;}

    public int getRadius() { return radius;}

    public void setRadius(int radius) { this.radius = radius;}

    public int getType() { return type;}

    public void setType(int type) { this.type = type;}

    public String getContent() { return content;}

    public void setContent(String content) { this.content = content;}

    public int getContenType() { return contenType;}

    public void setContenType(int contenType) { this.contenType = contenType;}

    public int getStatus() { return status;}

    public void setStatus(int status) { this.status = status;}

    public String getCreatedAt() { return createdAt;}

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}

    public String getUpdatedAt() { return updatedAt;}

    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt;}
}

