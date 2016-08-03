package com.huyingbao.hyb.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.HybApp;
import com.huyingbao.hyb.MainAty;
import com.huyingbao.hyb.MainShopAty;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.actions.Actions;
import com.huyingbao.hyb.base.BaseActivity;
import com.huyingbao.hyb.model.HybUser;
import com.huyingbao.hyb.stores.ShopStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.utils.HttpCode;
import com.huyingbao.hyb.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 登录
 */
public class LoginAty extends BaseActivity implements RxViewDispatch {

    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.btn_sign_in)
    Button emailSignInButton;
    @BindView(R.id.btn_register)
    Button emailRegisterButton;
    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.login_form)
    ScrollView mLoginFormView;
    @BindView(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;

    @Inject
    UsersStore usersStore;
    @Inject
    ShopStore shopStore;

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.a_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initActionBar(null, false);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.btn_sign_in)
    public void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String phone = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !StringUtils.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!StringUtils.isPhoneValid(phone)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            HybUser user = new HybUser();
            user.setPhone(phone);
            user.setPassword(password);
            hybActionCreator.login(user);
        }
    }

    @OnClick(R.id.btn_register)
    public void toRegister() {
        startActivity(RegisterAty.class);
        finish();
    }

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
        switch (change.getStoreId()) {
            case UsersStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.LOGIN:
                        showProgress(false);
                        if (HybApp.getUser().getType() == 0) {
                            startActivity(MainAty.class);
                            finish();
                        } else {
                            hybActionCreator.getBelongShop();
                        }
                        break;
                }
                break;
            case ShopStore.STORE_ID:
                switch (change.getRxAction().getType()) {
                    case Actions.GET_BELONG_SHOP:
                        startActivity(MainShopAty.class);
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        if (this.isFinishing()) {
            return;
        }
        showProgress(false);
        Throwable throwable = error.getThrowable();
        if (throwable != null) {
            if (throwable instanceof HttpException) {
                int httpCode = ((HttpException) throwable).code();
                if (httpCode == 411) {
                    mEmailView.setError(HttpCode.getHttpCodeInfo(httpCode));
                    mEmailView.requestFocus();
                    return;
                }
                if (httpCode == 412) {
                    mPasswordView.setError(HttpCode.getHttpCodeInfo(httpCode));
                    mPasswordView.requestFocus();
                    return;
                }
                Snackbar.make(rootCoordinator, httpCode + HttpCode.getHttpCodeInfo(httpCode), Snackbar.LENGTH_INDEFINITE)
                        .setAction("重试", v -> hybActionCreator.retry(error.getAction()))
                        .show();
            }
        } else {
            Toast.makeText(mContext, "未知错误", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRxViewRegistered() {

    }

    @Override
    public void onRxViewUnRegistered() {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Arrays.asList(usersStore, shopStore);
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        setLoadingFrame(show);
    }
}

