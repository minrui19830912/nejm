package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.nejm.R;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditEmailActivity extends BaseActivity {
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;


    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, EditEmailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        showBack();
        setCommonTitle("编辑邮箱");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textViewSendVerifyCode)
    public void onClickSendVerifyCode() {
        String email = editTextEmail.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            ToastUtil.showShort(mContext, "邮箱不能为空");
            return;
        }

        HttpUtils.sendEmailVerifyCode(this, email, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                ToastUtil.showShort(mContext, "发送邮件验证码成功");
            }
        });
    }

    @OnClick(R.id.buttonConfirm)
    public void onClickConfirm() {
        String email = editTextEmail.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            ToastUtil.showShort(this, "邮箱不能为空");
            editTextEmail.requestFocus();
            return;
        }

        String verifyCode = editTextVerifyCode.getText().toString().trim();
        if(TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showShort(mContext, "验证码不能为空");
            editTextVerifyCode.requestFocus();
            return;
        }

        LoadingDialog.showDialogForLoading(this);
        HttpUtils.editEmail(this, "", "", email, verifyCode,
                new OnNetResponseListener() {
                    @Override
                    public void onNetDataResponse(JSONObject json) {
                        LoginUserManager.getInstance().accountInfo.email = email;
                        ToastUtil.showShort(mContext, "编辑邮箱成功");
                        finish();
                    }
                });
    }
}
