package com.android.nejm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import com.android.nejm.R;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextEmailVerifyCode)
    EditText editTextEmailVerifyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        showBack();
    }

    @OnClick(R.id.textViewNext)
    public void onClickNext() {
        /*startActivity(new Intent(this, IdentityInfoActivity.class));
        finish();*/
        Map<String, String> params = new HashMap<>();
        params.put("mobile", "13912345678");
        params.put("membername", "tom2019");
        params.put("mcode", "666666");
        params.put("password", "123456");

        HttpUtils.register(this, params, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                Log.e("dpp", "json = " + json.toString());
            }
        });
    }

    @OnClick(R.id.textViewVerifyCode)
    public void onClickPhoneVerifyCode() {
        String mobile = editTextPhone.getText().toString().trim();
        HttpUtils.sendMobileVerifyCode(this, mobile, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {

            }
        });
    }

    @OnClick(R.id.textViewEmailVerifyCode)
    public void onClickEmailVerifyCode() {
        String email = editTextEmail.getText().toString().trim();
        HttpUtils.sendEmailVerifyCode(this, email, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {

            }
        });
    }
}
