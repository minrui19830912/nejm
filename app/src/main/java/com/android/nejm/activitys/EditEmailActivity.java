package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.nejm.R;

import butterknife.ButterKnife;

public class EditEmailActivity extends BaseActivity {

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, EditEmailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        showBack();
        setCommonTitle("编辑邮箱");
        ButterKnife.bind(this);
    }
}
