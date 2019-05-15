package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.android.nejm.R;
import com.android.nejm.adapter.NewKnowledgeAdapter;
import com.android.nejm.data.NewKnowledgeInfo;
import com.android.nejm.data.Paper;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewKnowledgeFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private RecyclerView mRecylerView;
    private SmartRefreshLayout refreshLayout;
    private NewKnowledgeAdapter mNewKnowledgeAdapter;
    private NewKnowledgeInfo newKnowledgeInfo;
    private int pageIndex = 1;
    private String id = "";

    private List<NewKnowledgeInfo.NewKnowledgeitem> knowledgeitems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_knowledge_fragment,container,false);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.all);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getData(false, false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                getData(false, true);
            }
        });

        mRecylerView = view.findViewById(R.id.new_knowledge_recyclerview);
        mNewKnowledgeAdapter = new NewKnowledgeAdapter(mContext);

        mRecylerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecylerView.setAdapter(mNewKnowledgeAdapter);
        return view;
    }

    private void getData(boolean showLoadingDialog, final boolean clearList) {
        if(showLoadingDialog) {
            LoadingDialog.showDialogForLoading(mContext);
        }

        HttpUtils.getNewKnowledge(mContext, id, pageIndex, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                newKnowledgeInfo = new Gson().fromJson(json.toString(), NewKnowledgeInfo.class);
                if(clearList) {
                    knowledgeitems.clear();
                }

                knowledgeitems.addAll(newKnowledgeInfo.items);

                mNewKnowledgeAdapter.setData(knowledgeitems);
                mNewKnowledgeAdapter.notifyDataSetChanged();

                refreshLayout.finishRefresh(2000);
                refreshLayout.finishLoadMore(2000);
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.all:
                this.id = "";
                break;
            case R.id.nejm_anim:
                if(newKnowledgeInfo != null && newKnowledgeInfo.types != null && newKnowledgeInfo.types.size() > 0) {
                    this.id = newKnowledgeInfo.types.get(0).id;
                } else {
                    this. id = "";
                }
                break;
        }

        getData(true, true);
    }
}
