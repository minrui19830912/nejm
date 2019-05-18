package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ModifyPhoneStep2Activity extends BaseActivity {
    @BindView(R.id.editTextNewPhone)
    EditText editTextNewPhone;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;

    String oldMobile;
    String oldVerifyCode;

    public static void launchActivity(Context context, String oldMobile, String oldVerifyCode) {
        Intent intent = new Intent(context, ModifyPhoneStep2Activity.class);
        intent.putExtra("oldVerifyCode", oldVerifyCode);
        intent.putExtra("oldMobile", oldMobile);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_step2);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("修改手机号码");

        oldMobile = getIntent().getStringExtra("oldMobile");
        oldVerifyCode = getIntent().getStringExtra("oldVerifyCode");
    }

    @OnClick(R.id.textViewSendVerifyCode)
    public void onClickSendVerifyCode() {
        String mobile = editTextNewPhone.getText().toString().trim();
        if(TextUtils.isEmpty(mobile)) {
            ToastUtil.showShort(mContext, "新手机号码不能为空");
            return;
        }

        HttpUtils.sendMobileVerifyCode(this, mobile, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                ToastUtil.showShort(mContext, "发送手机验证码成功");
            }
        });
    }

    @OnClick(R.id.buttonConfirm)
    public void onClickConfirm() {
        String mobile = editTextNewPhone.getText().toString().trim();
        String verifyCode = editTextVerifyCode.getText().toString().trim();
        if(TextUtils.isEmpty(mobile)) {
            ToastUtil.showShort(mContext, "新手机号码不能为空");
            editTextNewPhone.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showShort(mContext, "验证码不能为空");
            editTextVerifyCode.requestFocus();
            return;
        }

        LoadingDialog.showDialogForLoading(this);
        HttpUtils.editPhone(this, oldMobile, oldVerifyCode, mobile, verifyCode, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                ToastUtil.showShort(mContext, "修改手机号码成功");
                LoginUserManager.getInstance().accountInfo.mobile = mobile;
                finish();
            }
        });
    }
}
