package com.huyingbao.hyb.model;

/**
 * Created by Administrator on 2016/7/14.
 */
public class LocalFile {
    public LocalFile(String localPath, String fileKey) {
        this.localPath = localPath;
        this.fileKey = fileKey;
    }

    /**
     * 文件本地路径
     */
    private String localPath;
    /**
     * 文件七牛key
     */
    private String fileKey;

    private String urlPath;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
