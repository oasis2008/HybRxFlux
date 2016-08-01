package com.huyingbao.hyb.stores;

import com.huyingbao.hyb.model.MsgFromUser;

import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface MsgStoreInterface {
    boolean getSendStatus();
    List<MsgFromUser> getMsgFromUserList();
}
