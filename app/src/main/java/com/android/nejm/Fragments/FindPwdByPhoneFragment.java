package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.nejm.R;

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

    }

    @OnClick(R.id.textViewShowPwd)
    public void onClickShowPwd() {

    }

    @OnClick(R.id.textViewShowPwdAgain)
    public void onClickShowPwdAgain() {

    }

    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {

    }
}
