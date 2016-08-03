package com.huyingbao.hyb.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Keys;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/15.
 */
public class CommonUtils {
    /**
     * 返回完整路径
     *
     * @param key
     * @param partName
     * @return
     */
    public static String getFullPath(String key, String partName) {
        switch (partName) {
            case Keys.PART_NAME_HEAD_IMAGE:
                return Keys.URL_HEAD_IMAGE + key;
            default:
                return key;
        }
    }

    /**
     * 得到用时间戳生成的文件名字
     *
     * @param localPath
     * @return
     */
    public static String getFileNameByTime(String localPath) {
        return System.currentTimeMillis() + "." + FileUtils.getExtensionName(localPath);
    }

    /**
     * 初始化emptyview
     *
     * @param context
     * @param viewGroup
     * @param icEmpty
     * @param infoEmpty
     * @return
     */
    public static View initEmptyView(Context context, ViewGroup viewGroup, int icEmpty, String infoEmpty) {
        View emptyView = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.v_empty, viewGroup, false);
        ImageView ivEmpty = ButterKnife.findById(emptyView, R.id.iv_empty);
        TextView tvEmpty = ButterKnife.findById(emptyView, R.id.tv_empty);
        ivEmpty.setImageResource(icEmpty);
        tvEmpty.setText(infoEmpty);
        return emptyView;
    }
}


