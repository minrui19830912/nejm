package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.nejm.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        showBack();
    }

    @OnClick(R.id.textViewAbout)
    public void onClickAbout() {

    }

    @OnClick(R.id.textViewUsage)
    public void onClickUsage() {

    }

    @OnClick(R.id.textViewService)
    public void onClickService() {

    }

    @OnClick(R.id.textViewPrivacy)
    public void onClickPrivacy() {

    }

    @OnClick(R.id.textViewVersion)
    public void onClickVersion() {

    }

    @OnClick(R.id.textViewClean)
    public void onClickClean() {

    }

    @OnClick(R.id.textViewPush)
    public void onClickPush() {

    }

    @OnClick(R.id.textViewQuit)
    public void onClickQuit() {

    }
}
