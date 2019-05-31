package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.nejm.R;
import com.android.nejm.adapter.RelatedArticleAdapter;
import com.android.nejm.adapter.SpeicalFieldArticleAdapter;
import com.android.nejm.data.RelatedArticle;
import com.android.nejm.data.SpecialFieldArticleInfo;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReleatedArticleActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String id;
    private int page = 1;

    RelatedArticleAdapter articleAdapter;
    List<RelatedArticle> relatedArticleList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_article);
        ButterKnife.bind(this);
        setCommonTitle("相关文章",true);
        id = getIntent().getStringExtra("id");
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(false, true);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        articleAdapter = new RelatedArticleAdapter(this);
        recyclerView.setAdapter(articleAdapter);

        getData(true, true);
    }

    private void getData(boolean showLoadingDialog, final boolean clearList) {
        if(showLoadingDialog) {
            LoadingDialog.showDialogForLoading(this);
        }

        HttpUtils.getRelateArticle(this, id, page, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                refreshLayout.finishRefresh();
                //refreshLayout.finishLoadMore();

                List<RelatedArticle> list = new Gson().fromJson(json.optJSONArray("relation").toString(), new TypeToken<List<RelatedArticle>>(){}.getType());
                if(clearList) {
                    relatedArticleList.clear();
                }

                if(list != null) {
                    relatedArticleList.addAll(list);
                }

                articleAdapter.setData(relatedArticleList);
                articleAdapter.notifyDataSetChanged();
            }
        });
    }
}
