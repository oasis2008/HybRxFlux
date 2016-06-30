package com.yuntongxun.kitsdk.ui.voip;


import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;


import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.ECVoIPCallManager.CallType;
import com.yuntongxun.kitsdk.utils.LogUtil;
import com.yuntongxun.kitsdk.utils.ToastUtil;

public class VoIPCallActivity extends ECVoIPBaseActivity{

    private static final String TAG = "ECSDK_Demo.VoIPCallActivity";
	private long duration;
	private boolean isCallBack;

    @Override
    protected int getLayoutId() {
        return R.layout.ec_call_interface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        


        if(mIncomingCall) {
            // 来电
            mCallId = getIntent().getStringExtra(ECDevice.CALLID);
            mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);
            mCallName = mCallNumber;
        } else {
            // 呼出
            mCallName = getIntent().getStringExtra(EXTRA_CALL_NAME);
            mCallNumber = getIntent().getStringExtra(EXTRA_CALL_NUMBER);
            mCallFrom = getIntent().getStringExtra(EXTRA_CALL_FROM);
            isCallBack = getIntent().getBooleanExtra(ACTION_CALLBACK_CALL, false);
        }

        initView();
        if(!mIncomingCall) {
            // 处理呼叫逻辑
            if(TextUtils.isEmpty(mCallNumber)) {
                ToastUtil.showMessage(R.string.ec_call_number_error);
                finish();
                return ;
            }
			if (isCallBack) {
//				VoIPCallHelper.makeCallBack(CallType.VOICE, mCallNumber);
			} else {
	            mCallId = VoIPCallHelper.makeCall(mCallType ,  mCallNumber);
	            if(TextUtils.isEmpty(mCallId)) {
	                ToastUtil.showMessage(R.string.ec_app_err_disconnect_server_tip);
	                LogUtil.d(TAG, "Call fail, callId " + mCallId);
	                finish();
	                return ;
	            }
	            mCallHeaderView.setCallTextMsg(R.string.ec_voip_call_connecting_server);
			}
        }
    }

    private void initView() {
    	
        mCallHeaderView = (ECCallHeadUILayout) findViewById(R.id.call_header_ll);
        mCallControlUIView = (ECCallControlUILayout) findViewById(R.id.call_control_ll);
        mCallControlUIView.setOnCallControlDelegate(this);
        mCallHeaderView.setCallName(mCallName);
        mCallHeaderView.setCallNumber(mCallNumber);
        mCallHeaderView.setCalling(false);
        
        ECCallControlUILayout.CallLayout callLayout = mIncomingCall ? ECCallControlUILayout.CallLayout.INCOMING
                : ECCallControlUILayout.CallLayout.OUTGOING;
        mCallControlUIView.setCallDirect(callLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(VoIPCallHelper.getHandFree()){
        	//关闭扬声器
        	VoIPCallHelper.setHandFree();
        }
    }

    /**
     * 连接到服务器
     * @param callId 通话的唯一标识
     */
    @Override
    public void onCallProceeding(String callId) {
        if(mCallHeaderView == null || !needNotify(callId)) {
            return ;
        }
        LogUtil.d(TAG , "onUICallProceeding:: call id " + callId);
        mCallHeaderView.setCallTextMsg(R.string.ec_voip_call_connect);
    }

    /**
     * 连接到对端用户，播放铃音
     * @param callId 通话的唯一标识
     */
    @Override
    public void onCallAlerting(String callId) {
        if(!needNotify(callId) || mCallHeaderView == null) {
            return ;
        }
        LogUtil.d(TAG , "onUICallAlerting:: call id " + callId);
        mCallHeaderView.setCallTextMsg(R.string.ec_voip_calling_wait);
    }

    /**
     * 对端应答，通话计时开始
     * @param callId 通话的唯一标识
     */
    @Override
    public void onCallAnswered(String callId) {
        if(!needNotify(callId)|| mCallHeaderView == null) {
            return ;
        }
        LogUtil.d(TAG , "onUICallAnswered:: call id " + callId);
        mCallHeaderView.setCalling(true);
        mCallControlUIView.setCallDirect(ECCallControlUILayout.CallLayout.INCALL);
    }

    @Override
    public void onMakeCallFailed(String callId , int reason) {
        if(mCallHeaderView == null || !needNotify(callId)) {
            return ;
        }
        LogUtil.d(TAG , "onUIMakeCallFailed:: call id " + callId + " ,reason " + reason);
        mCallHeaderView.setCalling(false);
        mCallHeaderView.setCallTextMsg(CallFailReason.getCallFailReason(reason));
        VoIPCallHelper.releaseCall(mCallId);
        if(reason == 175603){
        	return;
        }
        finishCalling();
    }

    /**
     * 通话结束，通话计时结束
     * @param callId 通话的唯一标识
     */
    @Override
    public void onCallReleased(String callId) {
        if(mCallHeaderView == null || !needNotify(callId)) {
            return ;
        }
        LogUtil.d(TAG , "onUICallReleased:: call id " + callId);
        
        duration = mCallHeaderView.getCallDuration();
        mCallHeaderView.setCalling(false);
        mCallHeaderView.setCallTextMsg(R.string.ec_voip_calling_finish);
        mCallControlUIView.setControlEnable(false);
        finishCalling();
    }
    
    private void finishCalling(){
    	if(isFinishing()){
    		return;
    	}
    	insertCallLog();
    	finish();
    }
    
	/**
	 * 通话记录入库
	 */
	private void insertCallLog() {
//		if(!mIncomingCall){
//			if(mCallFrom.endsWith("@chat")){
//				Intent intent = new Intent(CASIntent.ACTION_IM_CALL_LOG_INIT);
//				intent.putExtra("Duration", duration);
//				CCPAppManager.sendBroadcast(this ,intent);
//				return;
//			}
//		}
//
//		VoipCalls vc = new VoipCalls();
//		//vc.setDuration(time + "");
//		vc.setPhoneNum(mCallNumber);
//		vc.setCallDate(System.currentTimeMillis() + "");
//		//呼出或者呼入
//		vc.setCallType(mIncomingCall ? VoipCalls.INCOMING_TYPE : VoipCalls.OUTGOING_TYPE);
//		//通话类型
//		vc.setVoip_type(mCallType.ordinal() + "");
//		vc.setDuration(duration + "");
////		vc.setSipaccount(mVoipAccount);
//		VoipCallRecordSqlManager.getInstance().saveVoipCall(vc);
//		CCPAppManager.sendBroadcast(this ,CASIntent.ACTION_CALL_LOG_INIT);
	}

	@Override
	public void onMakeCallback(ECError ecError, String caller, String called) {
		if(!TextUtils.isEmpty(mCallId)) {
			return ;
		}
		if(ecError.errorCode != SdkErrorCode.REQUEST_SUCCESS) {
			mCallHeaderView .setCallTextMsg("回拨呼叫失败[" + ecError.errorCode + "]");
		} else {
			mCallHeaderView .setCallTextMsg(R.string.ec_voip_call_back_success);
		}
		mCallHeaderView.setCalling(false);
        mCallControlUIView.setControlEnable(false);
		finish();
	}
}
