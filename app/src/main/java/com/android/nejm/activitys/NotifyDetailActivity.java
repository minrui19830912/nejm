package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.MessageDetail;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifyDetailActivity extends BaseActivity {
    @BindView(R.id.textViewMsgTitle)
    TextView textViewMsgTitle;
    @BindView(R.id.textViewAuthor)
    TextView textViewAuthor;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewContent)
    TextView textViewContent;

    public static void launchActivity(Context context, String id) {
        Intent intent = new Intent(context, NotifyDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_detail);
        showBack();
        setCommonTitle("通知");
        ButterKnife.bind(this);

        String id = getIntent().getStringExtra("id");
        loadData(id);
    }

    private void loadData(String id) {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.getMessage(this, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();

                MessageDetail msgDetail = new Gson().fromJson(json.toString(), MessageDetail.class);
                textViewMsgTitle.setText(msgDetail.title);
                textViewDate.setText(msgDetail.postdate);
                textViewContent.setText(msgDetail.content);
            }
        });
    }
}
