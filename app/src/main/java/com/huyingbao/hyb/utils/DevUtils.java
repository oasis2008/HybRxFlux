package com.huyingbao.hyb.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import com.huyingbao.hyb.HybApp;

import java.io.File;

/**
 * Created by Administrator on 2016/6/8.
 */
public class DevUtils {
    /**
     * 获取ApiKey
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static boolean isPkgInstalled(Context context, String packageName) {

        if (packageName == null || "".equals(packageName))
            return false;
        android.content.pm.ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(packageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 返回带/的图片缓存路径
     *
     * @return
     */
    public static String getImageFilePath(Context context) {
        return getCacheFilePath(context) + HybApp.getUser().getUserId() + "/image/";
    }


    /**
     * 返回带/的图片缓存路径
     *
     * @param context
     * @return
     */
    public static String getCacheFilePath(Context context) {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // SD卡可用
            path = Environment.getExternalStorageDirectory()
                    + File.separator
                    + context.getPackageName()
                    + File.separator + "cache" + File.separator;
        } else { // SD卡不可用
            path = context.getDir("cache", Context.MODE_PRIVATE).getAbsolutePath() + File.separator;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }
}
