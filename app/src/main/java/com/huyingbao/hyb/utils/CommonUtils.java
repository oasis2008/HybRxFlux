package com.huyingbao.hyb.utils;

import com.huyingbao.hyb.actions.Keys;

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
}


