package com.android.nejm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;
import com.android.nejm.activitys.SearchActivity;
import com.android.nejm.adapter.PeriodArticleAdapter;
import com.android.nejm.adapter.PeriodArticleItem;
import com.android.nejm.data.Paper;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PeriodArticleFragment extends BaseFragment {
    private RecyclerView mRecylerView;
    private PeriodArticleAdapter mPeriodArticleAdapter;
    private ArrayList<Paper> mPaperList = new ArrayList<>();
    private List<PeriodArticleItem> periodArticleItemList;
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

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(intent);
            }
        });

        getData();
        return view;
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getYearArticles(mContext,"", new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                periodArticleItemList = new ArrayList<>();

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
                        periodArticleItem.year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                        periodArticleItemList.add(periodArticleItem);
                    }
                }

                mPeriodArticleAdapter.setData(periodArticleItemList);
                mPeriodArticleAdapter.notifyDataSetChanged();
            }
        });

    }
}
