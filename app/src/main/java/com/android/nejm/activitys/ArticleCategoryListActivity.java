package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.nejm.R;

public class ArticleCategoryListActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_activity);
        setCommonTitle("NEJM医学前沿");
        showBack();
    }
}
