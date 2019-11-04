package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.nejm.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TitleActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        ButterKnife.bind(this);
        showBack();
    }

    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {
        finish();
    }
}
