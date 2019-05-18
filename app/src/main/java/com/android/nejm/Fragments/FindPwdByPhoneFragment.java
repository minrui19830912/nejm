package com.android.nejm.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.nejm.R;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.AppUtil;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdByPhoneFragment extends BaseFragment {
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;
    @BindView(R.id.editTextPwd)
    EditText editTextPwd;
    @BindView(R.id.editTextPwdAgain)
    EditText editTextPwdAgain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_find_by_phone, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.textViewSendVerifyCode)
    public void onClickSendVerifyCode() {
        String mobile = AppUtil.getTextContent(editTextPhone);
        if(TextUtils.isEmpty(mobile)) {
            ToastUtil.showShort(mContext, "手机号码不能为空");
            editTextPhone.requestFocus();
            return;
        }
        HttpUtils.sendMobileVerifyCode(mContext, mobile, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                ToastUtil.showShort(mContext, "发送手机验证码成功");
            }
        });
    }

    @OnClick(R.id.textViewShowPwd)
    public void onClickShowPwd() {
        String password = AppUtil.getTextContent(editTextPwd);
        editTextPwd.setInputType(EditorInfo.TYPE_CLASS_TEXT);
    }

    @OnClick(R.id.textViewShowPwdAgain)
    public void onClickShowPwdAgain() {
        editTextPwdAgain.setInputType(EditorInfo.TYPE_CLASS_TEXT);
    }

    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {
        String mobile = AppUtil.getTextContent(editTextPhone);
        String verifyCode = AppUtil.getTextContent(editTextVerifyCode);
        String password = AppUtil.getTextContent(editTextPwd);
        String passwordAgain = AppUtil.getTextContent(editTextPwdAgain);

        if(TextUtils.isEmpty(mobile)) {
            ToastUtil.showShort(mContext, "手机号码不能为空");
            editTextPhone.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showShort(mContext, "验证码不能为空");
            editTextVerifyCode.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            ToastUtil.showShort(mContext, "密码不能为空");
            editTextPwd.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(passwordAgain)) {
            ToastUtil.showShort(mContext, "密码不能为空");
            editTextPwdAgain.requestFocus();
            return;
        }

        if(!TextUtils.equals(password, passwordAgain)) {
            ToastUtil.showShort(mContext, "两次输入密码不一致");
            editTextPwdAgain.requestFocus();
            return;
        }

        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.resetPasswordMobile(mContext, mobile, verifyCode, password,
                new OnNetResponseListener() {
                    @Override
                    public void onNetDataResponse(JSONObject json) {
                        LoadingDialog.cancelDialogForLoading();
                        ToastUtil.showShort(mContext, "重设密码成功");
                        if(mContext != null) {
                            ((Activity)mContext).finish();
                        }
                    }
                });
    }
}
