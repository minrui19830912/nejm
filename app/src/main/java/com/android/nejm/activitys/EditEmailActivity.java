package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

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
    @BindView(R.id.textViewSendVerifyCode)
    TextView textViewSendVerifyCode;


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

        if (count == 0) {
            count = 60;
            textViewSendVerifyCode.setText("60");
            mHandler.sendEmptyMessageDelayed(0, 1000);
            HttpUtils.sendEmailVerifyCode(this, email, new OnNetResponseListener() {
                @Override
                public void onNetDataResponse(JSONObject json) {
                    ToastUtil.showShort(mContext, "发送邮件验证码成功");
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
                    textViewSendVerifyCode.setText("" + count);
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    textViewSendVerifyCode.setText("发送验证码");
                }
            }
        }
    };

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
                        LoadingDialog.cancelDialogForLoading();
                        LoginUserManager.getInstance().accountInfo.email = email;
                        ToastUtil.showShort(mContext, "编辑邮箱成功");
                        finish();
                    }
                });
    }
}
