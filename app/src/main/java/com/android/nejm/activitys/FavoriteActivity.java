package com.android.nejm.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.android.nejm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        showBack();
        setCommonTitle("收藏记录");
        ButterKnife.bind(this);
    }
}
