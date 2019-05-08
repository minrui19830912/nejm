package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.nejm.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPersonalInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        ButterKnife.bind(this);
        setCommonTitle("编辑个人信息");
        showBack();
    }

    @OnClick(R.id.textViewName)
    public void onClickName() {
        EditNameActivity.launchActivity(this);
    }

    @OnClick(R.id.textViewPhone)
    public void onClickPhone() {
        ModifyPhoneStep1Activity.launchActivity(this);
    }

    @OnClick(R.id.textViewEmail)
    public void onClickEmail() {
        EditEmailActivity.launchActivity(this);
    }
}
