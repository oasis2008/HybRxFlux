package com.huyingbao.hyb.utils;

/**
 * Created by Administrator on 2016/7/15.
 */
public class FileUtils {
    /**
     * Java文件操作 获取文件扩展名 Get the file extension, if no extension or file name
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }
}
