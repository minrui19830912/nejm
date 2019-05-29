package com.android.nejm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.NotificationActivity;
import com.android.nejm.adapter.VideoListAdapter;
import com.android.nejm.data.VideoInfo;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoListFragment extends BaseFragment {
    private GridView gridView;
    private RecyclerView mRecylerView;
    private SmartRefreshLayout refreshLayout;
    private VideoListAdapter mVideoListAdapter;
    private List<VideoInfo.Videoitem> videoitems = new ArrayList<>();
    private VideoInfo videoInfo;
    ImageView notification;

    GridAdapter gridAdapter;

    private int page = 1;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.video_list_fragment,container,false);

        gridView = view.findViewById(R.id.gridView);
        gridAdapter = new GridAdapter();
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    VideoListFragment.this.id = "";
                } else {
                    VideoListFragment.this.id = videoInfo.types.get(position - 1).id;
                }

                page = 1;
                refreshLayout.resetNoMoreData();
                mRecylerView.scrollToPosition(0);
                getData(true, true);

                gridAdapter.setSelectIndex(position);
                gridAdapter.notifyDataSetChanged();
            }
        });

        refreshLayout = view.findViewById(R.id.refreshLayout);
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

        page = 1;
        getData(true, true);
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

    private void getData(boolean showLoadingDialog, final boolean clearList) {
        if(showLoadingDialog) {
            LoadingDialog.showDialogForLoading(mContext);
        }

        HttpUtils.getVideoList(mContext,id,page, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                refreshLayout.finishRefresh();

                videoInfo = new Gson().fromJson(json.toString(), VideoInfo.class);
                if(clearList) {
                    videoitems.clear();
                }

                videoitems.addAll(videoInfo.items);
                if(videoitems.size() >= videoInfo.total_count) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                }

                mVideoListAdapter.setData(videoitems);
                mVideoListAdapter.notifyDataSetChanged();

                if(page == 1) {
                    gridAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    class GridAdapter extends BaseAdapter {
        LayoutInflater inflater;
        int selectIndex = 0;

        GridAdapter() {
            inflater = LayoutInflater.from(mContext);
        }

        void setSelectIndex(int index) {
            selectIndex = index;
        }

        @Override
        public int getCount() {
            int count = (videoInfo != null && videoInfo.types != null) ? videoInfo.types.size() + 1 : 1;
            Log.e("dpp", "GridAdapter, count = " + count);
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.new_knowledge_grid_item, parent, false);
            }

            TextView textView = (TextView)convertView;
            if(position == 0) {
                textView.setText("全部");
            } else {
                VideoInfo.TypeItem typeItem = videoInfo.types.get(position - 1);
                textView.setText(typeItem.typename);
            }

            if(selectIndex == position) {
                textView.setBackgroundResource(R.drawable.grid_item_round_rectangle_selected);
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                textView.setBackgroundResource(R.drawable.grid_item_round_rectangle_unselected);
                textView.setTextColor(mContext.getResources().getColor(R.color.color_c92700));
            }

            return convertView;
        }
    }
}

