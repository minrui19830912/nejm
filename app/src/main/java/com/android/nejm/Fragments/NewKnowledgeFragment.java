package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;
import com.android.nejm.adapter.NewKnowledgeAdapter;
import com.android.nejm.data.Paper;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import java.util.ArrayList;

public class NewKnowledgeFragment extends BaseFragment {
    private RecyclerView mRecylerView;
    private NewKnowledgeAdapter mNewKnowledgeAdapter;
    private ArrayList<Paper> mPaperList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_knowledge_fragment,container,false);
        mRecylerView = view.findViewById(R.id.new_knowledge_recyclerview);
        mNewKnowledgeAdapter = new NewKnowledgeAdapter(mContext);
        for (int i = 0; i < 6; i++) {
            Paper paper = new Paper();
            mPaperList.add(paper);
        }
        mNewKnowledgeAdapter.setData(mPaperList);
        mRecylerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecylerView.setAdapter(mNewKnowledgeAdapter);
        mNewKnowledgeAdapter.notifyDataSetChanged();
        getData();
        return view;
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getNewKnowledge(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();

            }
        });

    }
}
