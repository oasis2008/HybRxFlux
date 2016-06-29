package com.huyingbao.hyb.push;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.huyingbao.hyb.utils.DevUtils;

import java.util.List;


public class BaiduPushBase {
    public final static String SERVICE_ACTION = "com.baidu.android.pushservice.action.PUSH_SERVICE";

    public static void start(Context context) {
        if (PushManager.isPushEnabled(context)) {
            return;
        }
        PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, DevUtils.getMetaValue(context, "baidu_push_api_key"));
    }

    public static void stop(Context context) {
        PushManager.stopWork(context);
    }

    /**
     * 百度推送 - 低版本无法启动推送服务的解决方法
     *
     * @param context
     * @return
     */
    private static String getHighPriorityPackage(Context context) {
        Intent i = new Intent(SERVICE_ACTION);
        List<ResolveInfo> localList = context.getPackageManager().queryIntentServices(i, 0);
        String myPkgName = context.getPackageName();
        String pkgName = "";
        long pkgPriority = 0;
        for (ResolveInfo info : localList) {
            if (!info.serviceInfo.exported) {
                continue;
            }
            String pkg = info.serviceInfo.packageName;
            if (!info.serviceInfo.exported) {
                continue;
            }
            Context context1;
            try {
                context1 = context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
            } catch (NameNotFoundException e) {
                continue;
            }
            String spName = new StringBuilder().append(pkg).append(".push_sync").toString();
            SharedPreferences sp = context1.getSharedPreferences(spName, Context.MODE_WORLD_READABLE);
            long priority = sp.getLong("priority2", 0L);
            if (priority > pkgPriority || (myPkgName.equals(pkg) && priority >= pkgPriority)) {
                pkgPriority = priority;
                pkgName = pkg;
            }
            Log.d("push", "pkg--" + pkg + ", priority=" + priority);
        }
        return pkgName;
    }
}
