package com.android.nejm.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.nejm.R;

public  class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext= this;
    }

    protected void setCommonTitle(String title){
        ((TextView)findViewById(R.id.common_tile)).setText(title);
    }
    protected void setCommonTitle(String title,boolean showBack){
        ((TextView)findViewById(R.id.common_tile)).setText(title);
        if(showBack){
            showBack();
        }
    }
    protected void showBack(){
      View back = findViewById(R.id.iv_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
