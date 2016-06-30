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
package com.yuntongxun.kitsdk.setting;

/**
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-10
 * @version 4.0
 */
public enum ECPreferenceSettings {

    /**
     * Whether is the first use of the application
     *
     */
    SETTINGS_FIRST_USE("com.yuntongxun.kitsdk_first_use" , Boolean.TRUE),
    /**坚持云通讯登陆账号*/
    SETTINGS_YUNTONGXUN_ACCOUNT("com.yuntongxun.kitsdk_yun_account" , ""),
    /**检查是否需要自动登录*/
    SETTINGS_REGIST_AUTO("com.yuntongxun.kitsdk_account" , ""),
    /**是否使用回车键发送消息*/
    SETTINGS_ENABLE_ENTER_KEY("com.yuntongxun.kitsdk_sendmessage_by_enterkey" , Boolean.TRUE),
    /**聊天键盘的高度*/
    SETTINGS_KEYBORD_HEIGHT("com.yuntongxun.kitsdk_keybord_height" , 0),
    /**使用听筒播放语音*/
    SETTINGS_USE_HEAD_SET("com.yuntongxun.kitsdk_use_head_set" , true),
    /**自动接听回拨*/
    SETTINGS_AUTO_RECEIVE_CALL_BACK("com.yuntongxun.kitsdk_auto_receive_call_back" , true),
    /**手动选择拨打方式*/
    SETTINGS_CALL_TYPE("com.yuntongxun.kitsdk_choose_call_type_self" , Integer.valueOf(0)),
    /**接收新消息通知*/
    SETTINGS_SHOW_NOTIFY("com.yuntongxun.kitsdk_show_notify" , true),
    /**新消息声音*/
    SETTINGS_NEW_MSG_SOUND("com.yuntongxun.kitsdk_new_msg_sound" , true),
    /**新消息震动*/
    SETTINGS_NEW_MSG_SHAKE("com.yuntongxun.kitsdk_new_msg_shake" , true),
    SETTING_CHATTING_CONTACTID("com.yuntongxun.kitsdk_chatting_contactid" , ""),
    /**图片缓存路径*/
    SETTINGS_CROPIMAGE_OUTPUTPATH("com.yuntongxun.kitsdk_CropImage_OutputPath" , ""),




    SETTINGS_ABSOLUTELY_EXIT("com.yuntongxun.kitsdk_absolutely_exit" , Boolean.FALSE),
    SETTINGS_FULLY_EXIT("com.yuntongxun.kitsdk_fully_exit" , Boolean.FALSE),
    SETTINGS_PREVIEW_SELECTED("com.yuntongxun.kitsdk_preview_selected" , Boolean.FALSE),
    SETTINGS_OFFLINE_MESSAGE_VERSION("com.yuntongxun.kitsdk_offline_version" , 0),
    
    
    
    SETTINGS_NO_DISTURB_MODE("com.yuntongxun.kitsdk_no_disturb_mode" , false),
    SETTINGS_NO_DISTURB_MODE_START_TIME("com.yuntongxun.kitsdk_no_disturb_start_time" , "23:00"),
    SETTINGS_NO_DISTURB_MODE_END_TIME("com.yuntongxun.kitsdk_no_disturb_end_time" , "8:00");

    private final String mId;
    private final Object mDefaultValue;

    /**
     * Constructor of <code>CCPPreferenceSettings</code>.
     * @param id
     *            The unique identifier of the setting
     * @param defaultValue
     *            The default value of the setting
     */
    private ECPreferenceSettings(String id, Object defaultValue) {
        this.mId = id;
        this.mDefaultValue = defaultValue;
    }

    /**
     * Method that returns the unique identifier of the setting.
     * @return the mId
     */
    public String getId() {
        return this.mId;
    }

    /**
     * Method that returns the default value of the setting.
     *
     * @return Object The default value of the setting
     */
    public Object getDefaultValue() {
        return this.mDefaultValue;
    }

    /**
     * Method that returns an instance of {@link com.yuntongxun.kitsdk.common.utils.ECPreferenceSettings} from
     * its. unique identifier
     *
     * @param id
     *            The unique identifier
     * @return CCPPreferenceSettings The navigation sort mode
     */
    public static ECPreferenceSettings fromId(String id) {
        ECPreferenceSettings[] values = values();
        int cc = values.length;
        for (int i = 0; i < cc; i++) {
            if (values[i].mId == id) {
                return values[i];
            }
        }
        return null;
    }
}
