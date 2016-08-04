package com.huyingbao.hyb.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.model.MsgFromUser;
import com.huyingbao.hyb.utils.TimeUtils;

import java.util.List;


public class MsgFromUserListAdapter extends BaseQuickAdapter<MsgFromUser> {


    public MsgFromUserListAdapter(List<MsgFromUser> data) {
        super(R.layout.i_msg_from_user, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MsgFromUser msgFromUser) {
        baseViewHolder
                .setText(R.id.tv_type, msgFromUser.getContent())
                .setText(R.id.tv_time, TimeUtils.dateFormater.format(msgFromUser.getCreatedAt()));
    }
}
