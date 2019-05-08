package com.android.nejm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.nejm.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        showBack();
    }

    @OnClick(R.id.textViewNext)
    public void onClickNext() {
        startActivity(new Intent(this, IdentityInfoActivity.class));
        finish();
    }
}
