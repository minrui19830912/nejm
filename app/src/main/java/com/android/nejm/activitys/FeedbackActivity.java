package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.android.nejm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.editTextInfo)
    EditText editTextInfo;
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        showBack();
    }

    @OnClick(R.id.buttonSubmit)
    public void onClickSubmit() {

    }
}
