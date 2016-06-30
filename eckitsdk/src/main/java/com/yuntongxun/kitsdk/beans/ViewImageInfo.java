package com.yuntongxun.kitsdk.beans;

import com.yuntongxun.kitsdk.db.ImgInfoSqlManager;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


public class ViewImageInfo implements Parcelable {


    public static final Parcelable.Creator<ViewImageInfo> CREATOR
                 = new Parcelable.Creator<ViewImageInfo>() {
                 public ViewImageInfo createFromParcel(Parcel in) {
                         return new ViewImageInfo(in);
                     }

                 public ViewImageInfo[] newArray(int size) {
                         return new ViewImageInfo[size];
                     }
             };


    private int index;
    private String msgLocalId;
    private String thumbnailurl;
    private String picurl;
    private boolean isDownload = false;

    public ViewImageInfo(int index , String thumb , String url) {
        this.index = index;
        this.thumbnailurl = thumb;
        this.picurl = url;
    }

    private ViewImageInfo(Parcel in) {
        this.index = in.readInt();
        this.thumbnailurl = in.readString();
        this.picurl = in.readString();

    }

    public ViewImageInfo(String thumb , String url) {
        this(0, thumb, url);
    }

    public ViewImageInfo(Cursor cursor) {
        setCursor(cursor);
    }

    public int getIndex() {
        return index;
    }

    public String getThumbnailurl() {
        return thumbnailurl;
    }

    public void setThumbnailurl(String thumbnailurl) {
        this.thumbnailurl = thumbnailurl;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getMsgLocalId() {
        return msgLocalId;
    }

    public void setMsgLocalId(String msgLocalId) {
        this.msgLocalId = msgLocalId;
    }

    public void setCursor(Cursor cursor) {
        this.index = cursor.getInt(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.ID));
        this.picurl = cursor.getString(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.BIG_IMGPATH));
        this.msgLocalId = cursor.getString(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.MSG_LOCAL_ID));
        this.thumbnailurl = cursor.getString(cursor.getColumnIndex(ImgInfoSqlManager.ImgInfoColumn.THUMBIMG_PATH));
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeString(this.thumbnailurl);
        dest.writeString(this.picurl);
    }
}
