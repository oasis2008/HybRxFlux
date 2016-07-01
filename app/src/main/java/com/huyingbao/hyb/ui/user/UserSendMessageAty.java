package com.huyingbao.hyb.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huyingbao.hyb.R;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.widget.KeywordsFlow;

import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

public class UserSendMessageAty extends BaseActivity {
    @Bind(R.id.kf_tag)
    KeywordsFlow kfTag;

    public String[] keywords = {"QQ", "安sdfsdf全", "APK",
            "GFW", "铅笔",//
            "短信", "桌面", "安全", "平板", "雅诗烂",//
            "Base", "笔记本", "SPY", "安全", "捕鱼",//
            "清理", "地图", "导航", "闹钟", "主题",//
            "通讯录", "播放器", "CSDN", "安全", "联系",//
            "美女", "天气", "4743", "戴尔", "联想",//
            "欧朋", "浏览器", "愤怒小鸟", "优酷", "网易",//
            "土豆", "油水", "网游App", "互联网", "日历",//
            "脸部", "谷歌", "导航", "中国", "苹果",//
            "失败", "摩托", "魅族", "小米"};

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_user_send_message;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initFlowView();
    }

    @OnClick(R.id.bt_send)
    public void onClick() {

    }

    /**
     * 初始化飞入飞去控件
     */
    private void initFlowView() {
        kfTag.setDuration(800l);
        feedKeywordsFlow(kfTag, keywords);
        kfTag.go2Show(KeywordsFlow.ANIMATION_IN);
        kfTag.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((TextView) v).getText().toString();
                Toast.makeText(mContext, "点击了View:" + text, Toast.LENGTH_SHORT).show();
                setChange(text);
            }
        });
    }

    /**
     * 随机飞入飞去
     *
     * @param keywordsFlow
     * @param arr
     */
    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        Random random = new Random();
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            int ran = random.nextInt(arr.length);
            String tmp = arr[ran];
            keywordsFlow.feedKeyword(tmp);
        }
    }

    /**
     * 点击之后状态改变
     *
     * @param keyword
     */
    private void setChange(String keyword) {
        kfTag.rubKeywords();
        feedKeywordsFlow(kfTag, keywords);
        kfTag.go2Show(KeywordsFlow.ANIMATION_OUT);
    }
}
