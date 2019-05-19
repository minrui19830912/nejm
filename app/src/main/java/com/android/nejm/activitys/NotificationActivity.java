package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.nejm.R;
import com.android.nejm.adapter.NotifyMessageAdapter;
import com.android.nejm.data.AnnounceMessage;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    AnnounceMessage announceMessage;
    NotifyMessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        showBack();
        setCommonTitle("通知");
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        messageAdapter = new NotifyMessageAdapter(this);
        recyclerView.setAdapter(messageAdapter);

        getData();
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.getMessageList(this, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                announceMessage = new Gson().fromJson(json.toString(), AnnounceMessage.class);
                messageAdapter.setData(announceMessage.items);
                messageAdapter.notifyDataSetChanged();
            }
        });
    }
}
