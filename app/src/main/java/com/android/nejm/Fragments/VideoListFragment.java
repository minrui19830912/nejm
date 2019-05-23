package com.android.nejm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.android.nejm.R;
import com.android.nejm.activitys.NotificationActivity;
import com.android.nejm.adapter.VideoListAdapter;
import com.android.nejm.data.Paper;
import com.android.nejm.data.VideoInfo;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoListFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private RecyclerView mRecylerView;
    private VideoListAdapter mVideoListAdapter;
    private List<VideoInfo.Videoitem> videoitems = new ArrayList<>();
    private VideoInfo videoInfo;
    ImageView notification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_knowledge_fragment,container,false);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.all);

        mRecylerView = view.findViewById(R.id.new_knowledge_recyclerview);
        mVideoListAdapter = new VideoListAdapter(mContext);

        mVideoListAdapter.setData(videoitems);
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecylerView.setAdapter(mVideoListAdapter);
        //mVideoListAdapter.notifyDataSetChanged();
        notification = view.findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NotificationActivity.class);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AnnouceRecordManager.getInstance().hasUnread()) {
            notification.setImageResource(R.mipmap.icon_nav_msg_selected);
        } else {
            notification.setImageResource(R.mipmap.icon_nav_msg_normal);
        }

        if(LoginUserManager.getInstance().isLogin) {
            notification.setVisibility(View.VISIBLE);
        } else {
            notification.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            if(AnnouceRecordManager.getInstance().hasUnread()) {
                notification.setImageResource(R.mipmap.icon_nav_msg_selected);
            } else {
                notification.setImageResource(R.mipmap.icon_nav_msg_normal);
            }

            if(LoginUserManager.getInstance().isLogin) {
                notification.setVisibility(View.VISIBLE);
            } else {
                notification.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void getData(String id) {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getVideoList(mContext,id,1, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                videoInfo = new Gson().fromJson(json.toString(), VideoInfo.class);
                mVideoListAdapter.setData(videoInfo.items);
                mVideoListAdapter.notifyDataSetChanged();
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
                if(videoInfo != null && videoInfo.types != null && videoInfo.types.size() > 0) {
                    getData(videoInfo.types.get(0).id);
                } else {
                    getData("");
                }
                break;
        }
    }
}

