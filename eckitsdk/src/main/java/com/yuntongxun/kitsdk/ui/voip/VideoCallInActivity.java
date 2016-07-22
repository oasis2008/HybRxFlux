package com.yuntongxun.kitsdk.ui.voip;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.CameraCapability;
import com.yuntongxun.ecsdk.CameraInfo;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.kitsdk.utils.LogUtil;

import org.webrtc.videoengine.ViERenderer;

import java.util.Arrays;

public class VideoCallInActivity extends ECVoIPBaseActivity implements
        OnClickListener {

    private static final String TAG = "VideoCallInActivity";
    private RelativeLayout mInfoLl;
    private FrameLayout mVedioGoing;
    private TextView mName;
    private TextView mPhone;
    private ImageView mVideoIcon;
    private TextView mVideoTopTips;
    private ImageButton answer;
    private ImageButton handUpBefore;
    private Chronometer mChronometer;
    public RelativeLayout mLoaclVideoView;
    private SurfaceView mVideoView;
    private View mCameraSwitch;
    private ImageButton video_switch;
    private ImageButton handUpLate;
    private ImageButton mMute;

    private long duration = 0;
    boolean isConnect = false;

    public int defaultCameraId;
    public int mCameraCapbilityIndex;
    public int cameraCurrentlyLocked;
    CameraInfo[] cameraInfos;
    int numberOfCameras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallId = getIntent().getStringExtra(ECDevice.CALLID);
        mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);
        mCallName = mCallNumber;
        initResourceRefs();
        mName.setText(mCallName);
        mPhone.setText(mCallNumber);
        mVideoTopTips.setText(getString(R.string.str_vedio_call_in, mCallName));
        cameraInfos = ECDevice.getECVoIPSetupManager().getCameraInfos();

        if (cameraInfos != null) {
            numberOfCameras = cameraInfos.length;
        }
        for (int i = 0; i < numberOfCameras; i++) {
            if (cameraInfos[i].index == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                defaultCameraId = i;
                comportCapbilityIndex(cameraInfos[i].caps);
            }
        }
        ECDevice.getECVoIPSetupManager().setVideoView(mVideoView, null);
        DisplayLocalSurfaceView();
    }

    private void initResourceRefs() {

        mInfoLl = (RelativeLayout) findViewById(R.id.vedio_prepare);
        mName = (TextView) findViewById(R.id.tv_name);
        mPhone = (TextView) findViewById(R.id.tv_phone);
        mVideoIcon = (ImageView) findViewById(R.id.video_icon);
        mVideoTopTips = (TextView) findViewById(R.id.notice_tips);
        answer = (ImageButton) findViewById(R.id.answer);
        handUpBefore = (ImageButton) findViewById(R.id.hand_up_before);

        answer.setOnClickListener(this);
        handUpBefore.setOnClickListener(this);

        mVedioGoing = (FrameLayout) findViewById(R.id.vedio_going);
        mVideoView = (SurfaceView) findViewById(R.id.video_view);
        mLoaclVideoView = (RelativeLayout) findViewById(R.id.localvideo_view);
        mCameraSwitch = findViewById(R.id.camera_switch);
        mCameraSwitch.setOnClickListener(this);
        video_switch = (ImageButton) findViewById(R.id.video_switch);
        video_switch.setOnClickListener(this);
        handUpLate = (ImageButton) findViewById(R.id.hand_up_late);
        handUpLate.setOnClickListener(this);
        mMute = (ImageButton) findViewById(R.id.mute);
        mMute.setOnClickListener(this);
        mVideoView.getHolder().setFixedSize(240, 320);
    }

    public void DisplayLocalSurfaceView() {
        if (mCallType == ECVoIPCallManager.CallType.VIDEO
                && mLoaclVideoView != null
                && mLoaclVideoView.getVisibility() == View.VISIBLE) {
            SurfaceView localView = ViERenderer.CreateLocalRenderer(this);
            localView.setZOrderOnTop(true);
            mLoaclVideoView.removeAllViews();
            mLoaclVideoView.setBackgroundColor(getResources().getColor(
                    R.color.white));
            mLoaclVideoView.addView(localView);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.answer) {
            VoIPCallHelper.acceptCall(mCallId);

        } else if (v.getId() == R.id.hand_up_before) {
            doHandUpReleaseCall();

        } else if (v.getId() == R.id.hand_up_late) {
            doHandUpReleaseCall();

        } else if (v.getId() == R.id.mute) {
            VoIPCallHelper.setMute();
            boolean mute = VoIPCallHelper.getMute();
            mMute.setImageResource(mute ? R.drawable.mute_icon_on
                    : R.drawable.mute_selector);

        } else if (v.getId() == R.id.camera_switch) {
            if (numberOfCameras == 1) {
                return;
            }
            mCameraSwitch.setEnabled(false);
            cameraCurrentlyLocked = (cameraCurrentlyLocked + 1)
                    % numberOfCameras;
            comportCapbilityIndex(cameraInfos[cameraCurrentlyLocked].caps);

            ECDevice.getECVoIPSetupManager().selectCamera(
                    cameraCurrentlyLocked, mCameraCapbilityIndex, 15,
                    ECVoIPSetupManager.Rotate.ROTATE_AUTO, false);

            if (cameraCurrentlyLocked == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                defaultCameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
                Toast.makeText(VideoCallInActivity.this,
                        R.string.camera_switch_front, Toast.LENGTH_SHORT)
                        .show();
            } else {
                defaultCameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
                Toast.makeText(VideoCallInActivity.this,
                        R.string.camera_switch_back, Toast.LENGTH_SHORT).show();
            }
            mCameraSwitch.setEnabled(true);
        }
    }

    @Override
    public void onCallProceeding(String callId) {

    }

    @Override
    public void onCallAlerting(String callId) {

    }

    @Override
    public void onCallAnswered(String callId) {
        if (callId != null && callId.equals(mCallId) && !isConnect) {
            initResVideoSuccess();
        }
    }

    private void initResVideoSuccess() {
        isConnect = true;
        mInfoLl.setVisibility(View.GONE);
        mVedioGoing.setVisibility(View.VISIBLE);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setVisibility(View.VISIBLE);
        mChronometer.start();

        // 默认设为前置摄像头
        if (numberOfCameras == 1) {
            return;
        }
        cameraCurrentlyLocked = defaultCameraId;
        comportCapbilityIndex(cameraInfos[cameraCurrentlyLocked].caps);
        ECDevice.getECVoIPSetupManager().selectCamera(cameraCurrentlyLocked,
                mCameraCapbilityIndex, 15,
                ECVoIPSetupManager.Rotate.ROTATE_AUTO, true);
    }

    @Override
    public void onMakeCallFailed(String callId, int reason) {
        if (callId != null && callId.equals(mCallId)) {
            finishCalling(reason);
        }
    }

    @Override
    public void onCallReleased(String callId) {
        if (callId != null && callId.equals(mCallId)) {
            finishCalling();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoIPCallHelper.mHandlerVideoCall = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ec_video_call_in;
    }

    private void finishCalling() {
        mInfoLl.setVisibility(View.VISIBLE);
        answer.setVisibility(View.GONE);
        handUpBefore.setVisibility(View.GONE);
        mVideoTopTips.setText(R.string.ec_voip_calling_finish);
        if (isConnect) {
            duration = SystemClock.elapsedRealtime() - mChronometer.getBase();
            mChronometer.stop();
            mVedioGoing.setVisibility(View.GONE);
            mLoaclVideoView.removeAllViews();
            mLoaclVideoView.setVisibility(View.GONE);
        }
        // insertCallLog();
        finish();
        isConnect = false;
    }

    private void finishCalling(int reason) {
        mInfoLl.setVisibility(View.VISIBLE);
        answer.setVisibility(View.GONE);
        handUpBefore.setVisibility(View.GONE);
        mVideoTopTips.setText(CallFailReason.getCallFailReason(reason));
        mLoaclVideoView.removeAllViews();
        mLoaclVideoView.setVisibility(View.GONE);
        if (isConnect) {
            duration = SystemClock.elapsedRealtime() - mChronometer.getBase();
            mChronometer.stop();
            mVedioGoing.setVisibility(View.GONE);
        }
        isConnect = false;
        VoIPCallHelper.releaseCall(mCallId);
        if (reason == 175603) {
            return;
        }
        // insertCallLog();
        finish();
    }

    /**
     * 通话记录入库
     */
    // private void insertCallLog() {
    // VoipCalls vc = new VoipCalls();
    // //vc.setDuration(time + "");
    // vc.setPhoneNum(mCallNumber);
    // vc.setCallDate(System.currentTimeMillis() + "");
    // //呼出或者呼入
    // vc.setCallType(VoipCalls.INCOMING_TYPE);
    // //通话类型
    // vc.setVoip_type(mCallType.ordinal() + "");
    // vc.setDuration(duration + "");
    // // vc.setSipaccount(mVoipAccount);
    // VoipCallRecordSqlManager.getInstance().saveVoipCall(vc);
    // CCPAppManager.sendBroadcast(this ,CASIntent.ACTION_CALL_LOG_INIT);
    // }
    protected void doHandUpReleaseCall() {

        // Hang up the video call...
        LogUtil.d(TAG,
                "[VideoActivity] onClick: Voip talk hand up, CurrentCallId "
                        + mCallId);
        try {
            if (mCallId != null) {
                VoIPCallHelper.releaseCall(mCallId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isConnect) {
            finish();
        }
    }

    public void comportCapbilityIndex(CameraCapability[] caps) {

        if (caps == null) {
            return;
        }
        int pixel[] = new int[caps.length];
        int _pixel[] = new int[caps.length];
        for (CameraCapability cap : caps) {
            if (cap.index >= pixel.length) {
                continue;
            }
            pixel[cap.index] = cap.width * cap.height;
        }

        System.arraycopy(pixel, 0, _pixel, 0, caps.length);

        Arrays.sort(_pixel);
        for (int i = 0; i < caps.length; i++) {
            if (pixel[i] == /* _pixel[0] */352 * 288) {
                mCameraCapbilityIndex = i;
                return;
            }
        }
    }

    @Override
    public void onMakeCallback(ECError arg0, String arg1, String arg2) {


    }
}
