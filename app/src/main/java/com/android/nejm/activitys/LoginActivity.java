package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.nejm.R;
import com.android.nejm.data.LoginBean;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;
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
    private boolean justFinish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        justFinish =  getIntent().getBooleanExtra("just_finish",false);
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
        //name = "13912345678";
        //pwd = "123456";
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            ToastUtil.showShort(this, "用户名或密码不能为空");
            return;
        }
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.loginSystem(this, name, pwd, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                LoginBean loginBean = new Gson().fromJson(json.toString(), LoginBean.class);
                LoginUserManager.getInstance().login(loginBean);
                if(justFinish ){
                    finish();
                } else {
                startActivity(new Intent(mContext,MainActivity.class));
                finish();
                }
            }

            @Override
            public void onNetFailResponse(Context context, String msg, String msgCode) {
               // super.onNetFailResponse(context, msg, msgCode);
                LoadingDialog.cancelDialogForLoading();
                ToastUtil.showShort(mContext,msg);
            }
        });
    }

    @OnClick(R.id.textViewSkip)
    public void onClickSkip() {
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }
}
