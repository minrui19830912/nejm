package com.android.nejm.activitys;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.nejm.R;
import com.android.nejm.adapter.OtherArticleAdapter;
import com.android.nejm.data.OtherArticleInfo;
import com.android.nejm.data.ReadRecord;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int page;
    private ReadRecord readRecord;
    private List<OtherArticleInfo.ArtitleItem> recordItemList = new ArrayList<>();

    OtherArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        showBack();
        setCommonTitle("收藏记录");
        ButterKnife.bind(this);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData(false, false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(false, true);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        articleAdapter = new OtherArticleAdapter(this);
        recyclerView.setAdapter(articleAdapter);

        getData(true, true);
    }

    private void getData(boolean showLoadingDialog, final boolean clearList) {
        if(showLoadingDialog) {
            LoadingDialog.showDialogForLoading(this);
        }

        HttpUtils.getFavoriteList(this, page, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                refreshLayout.finishRefresh(100);
                refreshLayout.finishLoadMore(100);

                readRecord = new Gson().fromJson(json.toString(), ReadRecord.class);
                if(clearList) {
                    recordItemList.clear();
                }

                recordItemList.addAll(readRecord.items);

                articleAdapter.setData(recordItemList);
                articleAdapter.notifyDataSetChanged();
            }
        });
    }
}
