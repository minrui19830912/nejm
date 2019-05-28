package com.android.nejm.Fragments;

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

import com.android.nejm.R;
import com.android.nejm.activitys.NotificationActivity;
import com.android.nejm.activitys.SearchActivity;
import com.android.nejm.adapter.PeriodArticleAdapter;
import com.android.nejm.adapter.PeriodArticleItem;
import com.android.nejm.data.Paper;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PeriodArticleFragment extends BaseFragment {
    private RecyclerView mRecylerView;
    private SmartRefreshLayout refreshLayout;
    private PeriodArticleAdapter mPeriodArticleAdapter;
    private ArrayList<Paper> mPaperList = new ArrayList<>();
    private List<PeriodArticleItem> periodArticleItemList = new ArrayList<>();
    private ImageView notification;

    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private String id = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.period_article_fragment,container,false);
        mRecylerView = view.findViewById(R.id.period_article_recyclerview);
        mPeriodArticleAdapter = new PeriodArticleAdapter(mContext);
        for (int i = 0; i < 6; i++) {
            Paper paper = new Paper();
            mPaperList.add(paper);
        }
        //mPeriodArticleAdapter.setData(mPaperList);
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecylerView.setAdapter(mPeriodArticleAdapter);
        //mPeriodArticleAdapter.notifyDataSetChanged();

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                year--;
                getData(false, false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                year = Calendar.getInstance().get(Calendar.YEAR);
                getData(false, true);
            }
        });

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(intent);
            }
        });

        notification = view.findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NotificationActivity.class);
                mContext.startActivity(intent);
            }
        });

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

    private void getData(boolean showLoadingDialog, boolean clearList) {
        if(showLoadingDialog) {
            LoadingDialog.showDialogForLoading(mContext);
        }

        HttpUtils.getYearArticles(mContext,String.valueOf(year), new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                refreshLayout.finishRefresh();

                List<PeriodArticleItem> list = new ArrayList<>();

                int count = json.optInt("count");
                if(count == 0) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();

                    JSONObject jsonObject = json.optJSONObject("items");
                    for(int i = 12; i > 0; i--) {
                        String key = String.format(Locale.CHINA, "%02d", i);
                        JSONArray jsonArray = jsonObject.optJSONArray(key);
                        if(jsonArray != null) {
                            PeriodArticleItem periodArticleItem = new PeriodArticleItem();
                            periodArticleItem.articleItems = new ArrayList<>();
                            for(int j = 0; j < jsonArray.length(); j++) {
                                JSONObject item = jsonArray.optJSONObject(j);
                                PeriodArticleItem.ArticleItem articleItem = new PeriodArticleItem.ArticleItem();
                                articleItem.cover = item.optString("cover");
                                articleItem.id = item.optString("id");
                                articleItem.thedate = item.optString("thedate");
                                articleItem.title = item.optString("title");
                                periodArticleItem.articleItems.add(articleItem);
                            }

                            periodArticleItem.month = key;
                            periodArticleItem.year = String.valueOf(year);
                            list.add(periodArticleItem);
                        }
                    }
                }

                if(clearList) {
                    periodArticleItemList.clear();
                }

                periodArticleItemList.addAll(list);
                mPeriodArticleAdapter.setData(periodArticleItemList);
                mPeriodArticleAdapter.notifyDataSetChanged();
            }
        });

    }
}
