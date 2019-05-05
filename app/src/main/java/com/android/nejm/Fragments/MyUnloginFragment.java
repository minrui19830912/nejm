package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyUnloginFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my_unlogin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.textViewSetting)
    public void onClickSetting() {

    }

    @OnClick(R.id.textViewLogin)
    public void onClickLogin() {

    }

    @OnClick(R.id.textViewRegister)
    public void onClickRegister() {

    }

    @OnClick(R.id.textViewPrivacy)
    public void onClickPrivacy() {

    }

    @OnClick(R.id.textViewInstruction)
    public void onClickInstruction() {

    }

    @OnClick(R.id.textViewContactUs)
    public void onClickContactUs() {

    }

    @OnClick(R.id.textViewVersion)
    public void onClickVersion() {

    }
}


