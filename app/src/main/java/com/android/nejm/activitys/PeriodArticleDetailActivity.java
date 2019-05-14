package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeriodArticleDetailActivity extends BaseActivity {
    @BindView(R.id.draweeViewCover)
    SimpleDraweeView draweeViewCover;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewDir)
    TextView textViewDir;

    public static void launchActivity(Context context, String id) {
        Intent intent = new Intent(context, PeriodArticleDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_article);
        showBack();
        setCommonTitle("NEJM医学前沿 期刊");
        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(mContext);
        String id = getIntent().getStringExtra("id");

        HttpUtils.getPeriodArticleDetails(mContext, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }
}
