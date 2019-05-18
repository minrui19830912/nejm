package com.android.nejm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.android.nejm.R;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        showBack();
    }

    @OnClick(R.id.textViewNext)
    public void onClickNext() {
        /*HttpUtils.getRole(this, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {

            }
        });*/
        String userName = editTextUserName.getText().toString().trim();
        if(TextUtils.isEmpty(userName)) {
            ToastUtil.showShort(this, "用户名不能为空");
            return;
        }

        String phone = editTextPhone.getText().toString().trim();
        String phoneVerifyCode = editTextVerifyCode.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String emailVerifyCode = editTextEmailVerifyCode.getText().toString().trim();
        if(TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)) {
            ToastUtil.showShort(this, "手机和邮箱至少选一项");
            return;
        }

        if(!TextUtils.isEmpty(phone) && TextUtils.isEmpty(phoneVerifyCode)) {
            ToastUtil.showShort(this, "短信验证码不能为空");
            return;
        }

        if(!TextUtils.isEmpty(email) && TextUtils.isEmpty(emailVerifyCode)) {
            ToastUtil.showShort(this, "邮件验证码不能为空");
            return;
        }

        String password = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(password)) {
            ToastUtil.showShort(this, "密码不能为空");
            return;
        }

        startActivity(new Intent(this, IdentityInfoActivity.class));
        finish();
        /*Map<String, String> params = new HashMap<>();
        params.put("mobile", "13912345678");
        params.put("membername", "tom2019");
        params.put("mcode", "666666");
        params.put("password", "123456");

        HttpUtils.register(this, params, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                Log.e("dpp", "json = " + json.toString());
            }
        });*/
    }

    @OnClick(R.id.textViewVerifyCode)
    public void onClickPhoneVerifyCode() {
        String mobile = editTextPhone.getText().toString().trim();
        if(TextUtils.isEmpty(mobile)) {
            ToastUtil.showShort(this, "手机号码不能为空");
            editTextPhone.requestFocus();
            return;
        }
        HttpUtils.sendMobileVerifyCode(this, mobile, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                ToastUtil.showShort(RegisterActivity.this, "发送手机验证码成功");
            }
        });
    }

    @OnClick(R.id.textViewEmailVerifyCode)
    public void onClickEmailVerifyCode() {
        String email = editTextEmail.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
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
}
