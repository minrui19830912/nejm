package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.nejm.R;

import butterknife.ButterKnife;

public class EditNameActivity extends BaseActivity {

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, EditNameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        showBack();
        setCommonTitle("编辑姓名");
        ButterKnife.bind(this);
    }
}
