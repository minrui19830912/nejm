package com.android.nejm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.LoginBean;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.editTextUserName)
    EditText editTextUserName;
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextEmailVerifyCode)
    EditText editTextEmailVerifyCode;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.editTextPasswordAgain)
    EditText editTextPasswordAgain;
    @BindView(R.id.layoutPageOne)
    ConstraintLayout layoutPageOne;
    @BindView(R.id.layoutPageTwo)
    ConstraintLayout layoutPageTwo;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.textViewVerifyCode)
    TextView textViewVerifyCode;
    @BindView(R.id.textViewShowPwd)
    TextView textViewShowPwd;
    @BindView(R.id.textViewShowPwdAgain)
    TextView textViewShowPwdAgain;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        findViewById(R.id.iv_back).setVisibility(View.GONE);
        //showBack();
        layoutPageOne.setVisibility(View.VISIBLE);
        layoutPageTwo.setVisibility(View.GONE);
    }

    @OnClick(R.id.buttonNext)
    public void onClickNext() {
        layoutPageOne.setVisibility(View.GONE);
        layoutPageTwo.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.buttonPrev)
    public void onClickPrev() {
        layoutPageOne.setVisibility(View.VISIBLE);
        layoutPageTwo.setVisibility(View.GONE);
    }
    @OnClick(R.id.textViewShowPwd)
    public void onClickShowPwd() {
        if(editTextPassword.getInputType() == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editTextPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_CLASS_TEXT);
            textViewShowPwd.setText("显示密码");
        } else {
            editTextPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            textViewShowPwd.setText("隐藏密码");
        }

        editTextPassword.setText(editTextPassword.getText());
        editTextPassword.setSelection(editTextPassword.getText().length());
    }
    @OnClick(R.id.textViewShowPwdAgain)
    public void onClickShowPwdAgain() {
        if(editTextPasswordAgain.getInputType() == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editTextPasswordAgain.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_CLASS_TEXT);
            textViewShowPwdAgain.setText("显示密码");
        } else {
            editTextPasswordAgain.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            textViewShowPwdAgain.setText("隐藏密码");
        }

        editTextPasswordAgain.setText(editTextPasswordAgain.getText());
        editTextPasswordAgain.setSelection(editTextPasswordAgain.getText().length());
    }

    @OnClick(R.id.buttonRegister)
    public void onClickRegister() {
        String userName = editTextUserName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showShort(this, "用户名不能为空");
            return;
        }

        String phone = editTextPhone.getText().toString().trim();
        String phoneVerifyCode = editTextVerifyCode.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String emailVerifyCode = editTextEmailVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)) {
            ToastUtil.showShort(this, "手机和邮箱至少选一项");
            return;
        }

        if (!TextUtils.isEmpty(phone) && TextUtils.isEmpty(phoneVerifyCode)) {
            ToastUtil.showShort(this, "短信验证码不能为空");
            return;
        }

        if (!TextUtils.isEmpty(email) && TextUtils.isEmpty(emailVerifyCode)) {
            ToastUtil.showShort(this, "邮件验证码不能为空");
            return;
        }

        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort(this, "密码不能为空");
            return;
        }

        if (!checkBox.isChecked()) {
            ToastUtil.showShort(this, "请勾选上面的服务和隐私条款");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("email", email);
        params.put("membername", userName);
        params.put("mcode", phoneVerifyCode);
        params.put("ecode", emailVerifyCode);
        params.put("password", password);

        LoadingDialog.showDialogForLoading(this);
        HttpUtils.register(this, params, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                Log.e("dpp", "json = " + json.toString());
                LoadingDialog.cancelDialogForLoading();
                LoginBean loginBean = new Gson().fromJson(json.toString(), LoginBean.class);
                LoginUserManager.getInstance().register(loginBean);
                ToastUtil.showShort(mContext, "注册成功");
                startActivity(new Intent(mContext, IdentityInfoActivity.class));
                finish();
            }
        });
    }

    @OnClick(R.id.textViewVerifyCode)
    public void onClickPhoneVerifyCode() {
        String mobile = editTextPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.showShort(this, "手机号码不能为空");
            editTextPhone.requestFocus();
            return;
        }
        if (count == 0) {
            count = 60;
            textViewVerifyCode.setText("60");
            mHandler.sendEmptyMessageDelayed(0, 1000);
            HttpUtils.sendMobileVerifyCode(this, mobile, new OnNetResponseListener() {
                @Override
                public void onNetDataResponse(JSONObject json) {
                    ToastUtil.showShort(RegisterActivity.this, "发送手机验证码成功");
                }
            });
        }
    }

    private int count;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (count > 0) {
                    count--;
                    textViewVerifyCode.setText("" + count);
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    textViewVerifyCode.setText("手机验证码");
                }
            }
        }
    };

    @OnClick(R.id.textViewEmailVerifyCode)
    public void onClickEmailVerifyCode() {
        String email = editTextEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showShort(this, "邮箱不能为空");
            editTextEmail.requestFocus();
            return;
        }
        HttpUtils.sendEmailVerifyCode(this, email, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                ToastUtil.showShort(RegisterActivity.this, "发送邮件验证码成功");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}
