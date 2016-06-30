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
package com.yuntongxun.kitsdk.ui.group;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;












import com.yuntongxun.eckitsdk.R;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.kitsdk.ECDeviceKit;
import com.yuntongxun.kitsdk.core.CCPAppManager;
import com.yuntongxun.kitsdk.ui.ECChattingActivity;
import com.yuntongxun.kitsdk.ui.ECSuperActivity;
import com.yuntongxun.kitsdk.utils.DemoUtils;
import com.yuntongxun.kitsdk.utils.LogUtil;
import com.yuntongxun.kitsdk.view.ECListDialog;
import com.yuntongxun.kitsdk.view.ECProgressDialog;

/**
 * 群组创建功能
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-27
 * @version 4.0
 */
public class CreateGroupActivity extends ECSuperActivity implements
        View.OnClickListener , ECGroupManager.OnCreateGroupListener , GroupMemberService.OnSynsGroupMemberListener{

    private static final String TAG = "ECDemo.CreateGroupActivity";
    String[] stringArray = null;
    /**群组名称*/
    private EditText mNameEdit;
    /**群组公告*/
    private EditText mNoticeEdit;
    /**创建按钮*/
    private Button mCreateBtn;
    /**创建的群组*/
    private ECGroup group;
    private ECProgressDialog mPostingdialog;
    private Spinner mPermissionSpinner;
    private Button mSetPermission;
    private int mPermissionModel;

    final private TextWatcher textWatcher = new TextWatcher() {

        private int fliteCounts = 20;;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            LogUtil.d(LogUtil.getLogUtilsTag(textWatcher.getClass()), "fliteCounts=" + fliteCounts);
            fliteCounts = fliteCounts(s);
            if(fliteCounts < 0) {
                fliteCounts = 0;
            }
            if(checkNameEmpty()) {
                mCreateBtn.setEnabled(true);
                return ;
            }
            mCreateBtn.setEnabled(false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stringArray = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_group;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stringArray = getResources().getStringArray(R.array.group_join_model);
        getTopBarView().setTopBarToStatus(1, R.drawable.ytx_topbar_back_bt, -1, R.string.app_title_create_new_group, this);

        initView();
    }

    /**
     * 关闭对话框
     */
    private void dismissPostingDialog() {
        if(mPostingdialog == null || !mPostingdialog.isShowing()) {
            return ;
        }
        mPostingdialog.dismiss();
        mPostingdialog = null;
    }

    /**
     *
     */
    private void initView() {
        mNameEdit = (EditText) findViewById(R.id.group_name);
        mNoticeEdit = (EditText) findViewById(R.id.group_notice);
        mCreateBtn = (Button) findViewById(R.id.create);
        mCreateBtn.setOnClickListener(this);
        mCreateBtn.setEnabled(false);

        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = filter;
        mNameEdit.setFilters(inputFilters);
        mNameEdit.addTextChangedListener(textWatcher);

        mPermissionSpinner = (Spinner) findViewById(R.id.str_group_permission_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.group_join_model, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPermissionSpinner.setAdapter(adapter);
        mPermissionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        mPermissionModel = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        mSetPermission = (Button) findViewById(R.id.str_group_permission_spinner2);
        mSetPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPermissionDialog();
            }
        });
        initPermissionText();
    }

    /**
     * @return
     */
    private boolean checkNameEmpty() {
        return mNameEdit != null && mNameEdit.getText().toString().trim().length() > 0;
    }

    @Override
    public void onClick(View v) {
        
        
        if(v.getId()==R.id.btn_left){
        	hideSoftKeyboard();
            finish();
        }else if(v.getId()==R.id.create){
        	hideSoftKeyboard();
            showPermissionDialog();
        }
        	
        
    }

    private boolean showPermissionDialog() {
        ECListDialog dialog = new ECListDialog(this ,stringArray , mPermissionModel);
        dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
            @Override
            public void onDialogItemClick(Dialog d, int position) {
            	mPermissionModel = position;
//                initPermissionText();
            	mPostingdialog = new ECProgressDialog(CreateGroupActivity.this, R.string.create_group_posting);
				mPostingdialog.show();
				ECGroupManager ecGroupManager = ECDevice.getECGroupManager();
				if(!checkNameEmpty() || ecGroupManager == null) {
				    return ;
				}
				// 调用API创建群组、处理创建群组接口回调
				ecGroupManager.createGroup(getGroup(), CreateGroupActivity.this);
            }
        });
        dialog.setTitle(R.string.str_group_permission_spinner);
        dialog.show();
        return true;
    }

    private void initPermissionText() {
        mSetPermission.setText(stringArray[mPermissionModel]);
    }


    /**
     * 创建群组参数
     * @return
     */
    private ECGroup getGroup() {
        ECGroup group =  new ECGroup();
        // 设置群组名称
        group.setName(mNameEdit.getText().toString().trim());
        // 设置群组公告
        group.setDeclare(mNoticeEdit.getText().toString().trim());
        // 临时群组（100人）
        group.setScope(ECGroup.Scope.TEMP);
        // 群组验证权限，需要身份验证
        group.setPermission(ECGroup.Permission.values()[mPermissionModel + 1]);
        // 设置群组创建者
        group.setOwner(ECDeviceKit.getInstance().getUserId());
        return group;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GroupMemberService.addListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                + ", resultCode=" + resultCode + ", data=" + data);

        // If there's no data (because the user didn't select a picture and
        // just hit BACK, for example), there's nothing to do.
        if (requestCode == 0x2a) {
            if (data == null) {
                finish();
                return;
            }
        } else if (resultCode != RESULT_OK) {
            LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
            finish();
            return;
        }

        String[] selectUser = data.getStringArrayExtra("Select_Conv_User");
        if(selectUser != null && selectUser.length > 0) {
            mPostingdialog = new ECProgressDialog(this, R.string.invite_join_group_posting);
            mPostingdialog.show();
            GroupMemberService.inviteMembers(group.getGroupId(), "",ECGroupManager.InvitationMode.FORCE_PULL, selectUser);
        }

    }




    @Override
    public void onCreateGroupComplete(ECError error, ECGroup group) {
        
    	finish();
    }

    @Override
    public void onSynsGroupMember(String groupId) {
        dismissPostingDialog();
        Intent intent = new Intent(CreateGroupActivity.this , ECChattingActivity.class);
        intent.putExtra(ECChattingActivity.RECIPIENTS, groupId);
        intent.putExtra(ECChattingActivity.CONTACT_USER, group.getName());
        startActivity(intent);
        finish();
    }

    final InputFilter filter = new InputFilter () {

        private int limit = 30;
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            LogUtil.i(LogUtil.getLogUtilsTag(SearchGroupActivity.class), source
                    + " start:" + start + " end:" + end + " " + dest
                    + " dstart:" + dstart + " dend:" + dend);
            float count = calculateCounts(dest);
            int overplus = limit - Math.round(count) - (dend - dstart);
            if(overplus <= 0) {
                if ((Float.compare(count, (float) (limit - 0.5D)) == 0)
                        && (source.length() > 0)
                        && (!(DemoUtils.characterChinese(source.charAt(0))))) {
                    return source.subSequence(0, 1);
                }
                return "";
            }

            if( overplus >= (end - start)) {
                return null;
            }
            int tepmCont = overplus + start;
            if((Character.isHighSurrogate(source.charAt(tepmCont - 1))) && (--tepmCont == start)) {
                return "";
            }
            return source.subSequence(start, tepmCont);
        }

    };


    /**
     *
     * @param text
     * @return
     */
    public static int fliteCounts(CharSequence text) {
        int count = (30 - Math.round(calculateCounts(text)));
        LogUtil.v(LogUtil.getLogUtilsTag(SearchGroupActivity.class), "count " + count);
        return count;
    }

    /**
     *
     * @param text
     * @return
     */
    public static float calculateCounts(CharSequence text) {

        float lengh = 0.0F;
        for(int i = 0; i < text.length() ; i++) {
            if(!DemoUtils.characterChinese(text.charAt(i))) {
                lengh += 1.0F;
            } else {
                lengh += 0.5F;
            }
        }

        return lengh;
    }
}
