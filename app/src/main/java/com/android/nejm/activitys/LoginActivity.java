package com.android.nejm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.android.nejm.R;
import com.android.nejm.data.LoginBean;
import com.android.nejm.manage.UserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textViewOr)
    public void onClickRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewForgetPwd)
    public void onClickForgetPwd() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewShowPwd)
    public void onClickShowPwd() {
        editTextPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        editTextPassword.setText(editTextPassword.getText());
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    @OnClick(R.id.textViewLogin)
    public void onClickLogin() {
        String name = editTextName.getText().toString().trim();
        String pwd = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            ToastUtil.showShort(this, "用户名或密码不能为空");
            return;
        }

        HttpUtils.loginSystem(this, name, pwd, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoginBean loginBean = new Gson().fromJson(json.toString(), LoginBean.class);
                UserManager.getInstance().save(loginBean);
                startActivity(new Intent(mContext,MainActivity.class));
                finish();
            }
        });
    }

    @OnClick(R.id.textViewSkip)
    public void onClickSkip() {
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }
}
