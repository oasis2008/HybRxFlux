package com.yuntongxun.kitsdk.ui;

import java.util.Map;



import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.yuntongxun.kitsdk.core.ECKitConstant;
import com.yuntongxun.kitsdk.utils.LogUtil;
import com.yuntongxun.kitsdk.view.CCPActivityBase;
import com.yuntongxun.kitsdk.view.TopBarView;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;





public abstract class ECSuperActivity extends FragmentActivity {
	
	protected static final Object CODE_OK = "000000";
    private static final String TAG = ECSuperActivity.class.getSimpleName();
    /**
     * 初始化应用ActionBar
     */
    private CCPActivityBase mBaseActivity = new CCPActivityImpl(this);
    /**
     * 初始化广播接收器
     */
    private InternalReceiver internalReceiver;

   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBaseActivity.init(getBaseContext(), this);
        onActivityInit();
    }
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    private KeyguardManager mKeyguardManager = null;
    private PowerManager.WakeLock mWakeLock;
    
    protected void initProwerManager() {
        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP , "CALL_ACTIVITY#" + super.getClass().getName());
        mKeyguardManager = ((KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE));
    }
    
    /**
     * 释放资源
     */
    protected void releaseWakeLock() {
        try {
            if (this.mWakeLock.isHeld()) {
                if (this.mKeyguardLock != null) {
                    this.mKeyguardLock.reenableKeyguard();
                    this.mKeyguardLock = null;
                }
                this.mWakeLock.release();
            }
            return;
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }
    /**
     * 唤醒屏幕资源
     */
    protected void enterIncallMode() {
        if (!(mWakeLock.isHeld())) {
            // wake up screen
            // BUG java.lang.RuntimeException: WakeLock under-locked
            mWakeLock.setReferenceCounted(false);
            mWakeLock.acquire();
        }
        mKeyguardLock = this.mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
    }

    protected final void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(ECKitConstant.ACTION_KICK_OFF);
        for (String action : actionArray) {
            intentfilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        registerReceiver(internalReceiver, intentfilter);
    }


    /**
     * The sub Activity implement, set the Ui Layout
     * @return
     */
    protected abstract int getLayoutId();

    public void onActivityInit() {
    }

    /**
     * 如果子界面需要拦截处理注册的广播
     * 需要实现该方法
     * @param context
     * @param intent
     */
    protected void handleReceiver(Context context, Intent intent) {
        // 广播处理
        if(intent == null ) {
            return ;
        }
        if(ECKitConstant.ACTION_KICK_OFF.equals(intent.getAction())) {
            finish();
        }
    }

    public void onBaseContentViewAttach(View contentView) {
        setContentView(contentView);
    }

    public FragmentActivity getActionBarActivityContext() {
        return mBaseActivity.getFragmentActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaseActivity.onPause();
    }

    @Override
    protected void onResume() {
        // HSCoreService
        super.onResume();
        mBaseActivity.onResume();
        
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
       
        super.onDestroy();
        mBaseActivity.onDestroy();
        try {
            unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mBaseActivity.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(mBaseActivity.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }


    public void hideSoftKeyboard() {
        mBaseActivity.hideSoftKeyboard();
    }


    /**
     * 跳转
     * @param clazz
     * @param intent
     */
    protected void startCCPActivity(Class<? extends Activity> clazz , Intent intent) {
        intent.setClass(this, clazz);
        startActivity(intent);
    }


    // Internal calss.
    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null || intent.getAction() == null ) {
                return ;
            }
            handleReceiver(context, intent);
        }
    }

    public Activity getActivitContext() {
        if(getParent() != null) {
            return getParent();
        }
        return null;
    }

    public TopBarView getTopBarView() {
        return mBaseActivity.getTopBarView();
    }

    /**
     * 设置ActionBar标题
     * @param resid
     */
    public void setActionBarTitle(int resid) {
        mBaseActivity.setActionBarTitle(getString(resid));
    }

    /**
     * 设置ActionBar标题
     * @param text
     */
    public void setActionBarTitle(CharSequence text) {
        mBaseActivity.setActionBarTitle(text);
    }

    /**
     * 返回ActionBar 标题
     * @return
     */
    public final CharSequence getActionBarTitle() {
        return mBaseActivity.getActionBarTitle();
    }

    /**
     * #getLayoutId()
     * @return
     */
    public View getActivityLayoutView() {
        return mBaseActivity.getActivityLayoutView();
    }

    /**
     *
     */
    public final void showTitleView() {
        mBaseActivity.showTitleView();
    }

    /**
     *
     */
    public final void hideTitleView() {
        mBaseActivity.hideTitleView();
    }
    
    
    
	
	
	
	
}
