package com.android.nejm.Fragments;

import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.ArrayList;

public class NewKnowledgeFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private RecyclerView mRecylerView;
    private NewKnowledgeAdapter mNewKnowledgeAdapter;
    private NewKnowledgeInfo newKnowledgeInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_knowledge_fragment,container,false);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.all);

        mRecylerView = view.findViewById(R.id.new_knowledge_recyclerview);
        mNewKnowledgeAdapter = new NewKnowledgeAdapter(mContext);

        mRecylerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecylerView.setAdapter(mNewKnowledgeAdapter);
        return view;
    }

    private void getData(String id) {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getNewKnowledge(mContext, id, 1, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                newKnowledgeInfo = new Gson().fromJson(json.toString(), NewKnowledgeInfo.class);
                mNewKnowledgeAdapter.setData(newKnowledgeInfo.items);
                mNewKnowledgeAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.all:
                getData("");
                break;
            case R.id.nejm_anim:
                if(newKnowledgeInfo != null && newKnowledgeInfo.types != null && newKnowledgeInfo.types.size() > 0) {
                    getData(newKnowledgeInfo.types.get(0).id);
                } else {
                    getData("");
                }
                break;
        }
    }
}
