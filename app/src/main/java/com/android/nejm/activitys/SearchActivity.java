package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.SearchKnowledgeAdapter;
import com.android.nejm.data.Paper;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.search_box)
    EditText editTextSearch;
    @BindView(R.id.search_content)
    View mSearchContent;
    @BindView(R.id.recyler_view)
    RecyclerView mRecylerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private ArrayList<Paper> mPaperList = new ArrayList<>();
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setCommonTitle("搜索",true);
        ButterKnife.bind(this);

        SearchKnowledgeAdapter adapter = new SearchKnowledgeAdapter(mContext);
        for (int i = 0; i < 6; i++) {
            Paper paper = new Paper();
            mPaperList.add(paper);
        }
        adapter.setData(mPaperList);
        mRecylerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        mRecylerView.setAdapter(adapter);

        refreshLayout.autoLoadMore();
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                search(false, true);
            }
        });
    }

    @OnEditorAction(R.id.search_box)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            search(true, false);
            return true;
        }

        return false;
    }

    private void search(boolean showLoading, boolean loadMore) {
        if(showLoading) {
            LoadingDialog.showDialogForLoading(this);
        }

        String keyword = editTextSearch.getText().toString();
        HttpUtils.search(this, keyword, "", page, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }
}
