package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.android.nejm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPhoneStep1Activity extends BaseActivity {
    @BindView(R.id.editTextOldPhone)
    EditText editTextOldPhone;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, ModifyPhoneStep1Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_step1);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("修改手机号码");
    }

    @OnClick(R.id.textViewSendVerifyCode)
    public void onClickSendVerifyCode() {

    }

    @OnClick(R.id.buttonNext)
    public void onClickNext() {
        Intent intent = new Intent(this, ModifyPhoneStep2Activity.class);
        startActivity(intent);
    }
}
