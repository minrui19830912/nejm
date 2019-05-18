package com.android.nejm.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.nejm.R;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.AppUtil;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdByEmailFragment extends BaseFragment {
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_find_by_email, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.buttonConfirm)
    public void onClickConfirm() {
        String email = AppUtil.getTextContent(editTextEmail);
        if(TextUtils.isEmpty(email)) {
            ToastUtil.showShort(mContext, "邮箱不能为空");
            return;
        }

        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.resetPasswordEmail(mContext, email, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                ToastUtil.showShort(mContext, "重置密码成功, 请检查邮箱");
                if(mContext != null && mContext instanceof Activity) {
                    ((Activity)mContext).finish();
                }
            }
        });
    }
}
