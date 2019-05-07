package com.android.nejm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.android.nejm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textViewOr)
    public void onClickRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewForgetPwd)
    public void onClickForgetPwd() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewShowPwd)
    public void onClickShowPwd() {
        editTextPassword.setText(editTextPassword.getText());
    }

    @OnClick(R.id.textViewLogin)
    public void onClickLogin() {
        String name = editTextName.getText().toString().trim();
        String pwd = editTextPassword.getText().toString().trim();
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }

    @OnClick(R.id.textViewSkip)
    public void onClickSkip() {
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }
}
