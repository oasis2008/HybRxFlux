package com.huyingbao.hyb.utils;

/**
 * Created by Administrator on 2016/6/7.
 */
public class StringUtils {
    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }
}
