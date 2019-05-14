package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.SearchKnowledgeAdapter;
import com.android.nejm.data.Paper;
import com.android.nejm.widgets.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnEditorAction;

public class SearchActivity extends BaseActivity {
    private View mSearchContent;
    private RecyclerView mRecylerView;
    private ArrayList<Paper> mPaperList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setCommonTitle("搜索",true);
        ButterKnife.bind(this);
        mSearchContent = findViewById(R.id.search_content);
        mRecylerView = findViewById(R.id.recyler_view);
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
    }

    @OnEditorAction(R.id.search_box)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId != EditorInfo.IME_ACTION_SEARCH) {
            return false;
        }

        return true;
    }
}
