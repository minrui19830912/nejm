package com.android.nejm.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.NotificationActivity;
import com.android.nejm.adapter.VideoListAdapter;
import com.android.nejm.data.NewKnowledgeInfo;
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
    private RecyclerView gridView;
    private RecyclerView mRecylerView;
    private SmartRefreshLayout refreshLayout;
    private VideoListAdapter mVideoListAdapter;
    private List<VideoInfo.Videoitem> videoitems = new ArrayList<>();
    private VideoInfo videoInfo;
    ImageView notification;

    HorizontalTagAdapter gridAdapter;

    private int page = 1;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.video_list_fragment,container,false);

        gridView = view.findViewById(R.id.gridView);
        gridView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        gridAdapter = new HorizontalTagAdapter(getContext());
        gridView.setAdapter(gridAdapter);


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




    public class HorizontalTagAdapter extends  RecyclerView.Adapter<HorizontalTagAdapter.ViewHolder>{
        private Context context;
        private List<NewKnowledgeInfo.NewKnowledgeitem> newKnowledgeitems;
        int selectIndex = 0;

        public HorizontalTagAdapter(Context context) {

            this.context = context;
        }

        public void setData(List<NewKnowledgeInfo.NewKnowledgeitem> newKnowledgeitems){
            this.newKnowledgeitems = newKnowledgeitems;
        }
        @NonNull
        @Override
        public HorizontalTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.new_knowledge_grid_item,viewGroup,false);
            return new HorizontalTagAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HorizontalTagAdapter.ViewHolder viewHolder, int position) {

            TextView textView = (TextView)viewHolder.mView;
            //textView.setText("全部");
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

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        }

        @Override
        public int getItemCount() {
            //return 10;
            int count = (videoInfo != null && videoInfo.types != null) ? videoInfo.types.size() + 1 : 1;
            //Log.e("dpp", "GridAdapter, count = " + count);
            return count;
            }

        class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;


            public ViewHolder(View view) {
                super(view);
                mView = view;


            }
        }

        public void setSelectIndex(int index) {
            selectIndex = index;
        }
    }
}

