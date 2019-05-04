package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;
import com.android.nejm.adapter.VideoListAdapter;
import com.android.nejm.data.Paper;

import java.util.ArrayList;

public class VideoListFragment extends BaseFragment {
    private RecyclerView mRecylerView;
    private VideoListAdapter mVideoListAdapter;
    private ArrayList<Paper> mPaperList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_knowledge_fragment,container,false);
        mRecylerView = view.findViewById(R.id.new_knowledge_recyclerview);
        mVideoListAdapter = new VideoListAdapter(mContext);
        for (int i = 0; i < 6; i++) {
            Paper paper = new Paper();
            mPaperList.add(paper);
        }
        mVideoListAdapter.setData(mPaperList);
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecylerView.setAdapter(mVideoListAdapter);
        mVideoListAdapter.notifyDataSetChanged();
        return view;
    }
}

