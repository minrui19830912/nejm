package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.android.nejm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPhoneStep2Activity extends BaseActivity {
    @BindView(R.id.editTextNewPhone)
    EditText editTextNewPhone;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_step2);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("修改手机号码");
    }

    @OnClick(R.id.textViewSendVerifyCode)
    public void onClickSendVerifyCode() {

    }

    @OnClick(R.id.buttonConfirm)
    public void onClickConfirm() {

    }
}
