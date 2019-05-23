package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.ArticleDetailDirAdapter;
import com.android.nejm.data.PeriodArticleDeatailInfo;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.DisplayUtil;
import com.android.nejm.widgets.LoadingDialog;
import com.android.nejm.widgets.SpacesItemDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PeriodArticleDetailActivity extends BaseActivity {
    @BindView(R.id.draweeViewCover)
    SimpleDraweeView draweeViewCover;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.recyclerViewDir)
    RecyclerView recyclerViewDir;
    @BindView(R.id.recyclerViewNEJM)
    RecyclerView recyclerViewNEJM;
    @BindView(R.id.recyclerViewNEJM2)
    RecyclerView recyclerViewNEJM2;
    @BindView(R.id.recyclerViewSpecialist)
    RecyclerView recyclerViewSpecialist;
    @BindView(R.id.draweeViewVideo)
    SimpleDraweeView draweeViewVideo;
    @BindView(R.id.textViewVideo)
    TextView textViewVideo;

    PeriodArticleDeatailInfo articleDeatailInfo;

    ArticleDetailDirAdapter dirAdapter;
    ArticleDetailDirAdapter nejmAdapter;
    ArticleDetailDirAdapter nejm2Adapter;
    ArticleDetailDirAdapter specialistAdapter;

    public static void launchActivity(Context context, String id) {
        Intent intent = new Intent(context, PeriodArticleDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_article);
        showBack();
        setCommonTitle("NEJM医学前沿 期刊");
        ButterKnife.bind(this);

        recyclerViewDir.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewDir.addItemDecoration(new SpacesItemDecoration(DisplayUtil.dip2px(mContext, 9)));
        dirAdapter = new ArticleDetailDirAdapter(this, new ArticleDetailDirAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                DirectoryDetailActivity.launchActivity(mContext, articleDeatailInfo.items_0.get(index).id);
            }
        });
        recyclerViewDir.setAdapter(dirAdapter);

        recyclerViewNEJM.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewNEJM.addItemDecoration(new SpacesItemDecoration(DisplayUtil.dip2px(mContext, 9)));
        nejmAdapter = new ArticleDetailDirAdapter(this, new ArticleDetailDirAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                String url = HttpUtils.ARTICLE_DETAIL_URL+ articleDeatailInfo.items_5.get(index).id;
                Log.e("dpp", "url = " + url);
                String cover = "";
                if(articleDeatailInfo.video != null && articleDeatailInfo.video.thumb != null) {
                    cover = articleDeatailInfo.video.thumb;
                }
                ArticleDetailActivity.launchActivity(mContext,articleDeatailInfo.items_5.get(index).id,
                        url,articleDeatailInfo.items_5.get(index).title,cover,articleDeatailInfo.items_5.get(index).title);
            }
        });
        recyclerViewNEJM.setAdapter(nejmAdapter);

        recyclerViewNEJM2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewNEJM2.addItemDecoration(new SpacesItemDecoration(DisplayUtil.dip2px(mContext, 9)));
        nejm2Adapter = new ArticleDetailDirAdapter(this, new ArticleDetailDirAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                String url = HttpUtils.ARTICLE_DETAIL_URL+ articleDeatailInfo.items_6.get(index).id;
                String cover = "";
                if(articleDeatailInfo.video != null && articleDeatailInfo.video.thumb != null) {
                    cover = articleDeatailInfo.video.thumb;
                }
                Log.e("dpp", "url = " + url);
                ArticleDetailActivity.launchActivity(mContext,articleDeatailInfo.items_6.get(index).id,
                        url,articleDeatailInfo.items_6.get(index).title,cover,articleDeatailInfo.items_6.get(index).title);
            }
        });
        recyclerViewNEJM2.setAdapter(nejm2Adapter);

        recyclerViewSpecialist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSpecialist.addItemDecoration(new SpacesItemDecoration(DisplayUtil.dip2px(mContext, 9)));
        specialistAdapter = new ArticleDetailDirAdapter(this, new ArticleDetailDirAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                String url = HttpUtils.ARTICLE_DETAIL_URL+ articleDeatailInfo.wantsay.get(index).id;
                String cover = "";
                if(articleDeatailInfo.video != null && articleDeatailInfo.video.thumb != null) {
                    cover = articleDeatailInfo.video.thumb;
                }
                Log.e("dpp", "url = " + url);
                ArticleDetailActivity.launchActivity(mContext,articleDeatailInfo.wantsay.get(index).id,
                        url,articleDeatailInfo.wantsay.get(index).title,cover,articleDeatailInfo.wantsay.get(index).title);
            }
        });
        recyclerViewSpecialist.setAdapter(specialistAdapter);

        getData();
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(mContext);
        String id = getIntent().getStringExtra("id");

        HttpUtils.getPeriodArticleDetails(mContext, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                articleDeatailInfo = new Gson().fromJson(json.toString(), PeriodArticleDeatailInfo.class);
                draweeViewCover.setImageURI(articleDeatailInfo.item.cover);
                textViewTitle.setText(articleDeatailInfo.item.title);

                List<String> dirList = new ArrayList<>();
                for(PeriodArticleDeatailInfo.Items0 item : articleDeatailInfo.items_0) {
                    dirList.add(item.title);
                }

                dirAdapter.setData(dirList);
                dirAdapter.notifyDataSetChanged();

                List<String> nejmList = new ArrayList<>();
                for(PeriodArticleDeatailInfo.Items0 item : articleDeatailInfo.items_5) {
                    nejmList.add(item.title);
                }

                nejmAdapter.setData(nejmList);
                nejmAdapter.notifyDataSetChanged();

                List<String> nejm2List = new ArrayList<>();
                for(PeriodArticleDeatailInfo.Items0 item : articleDeatailInfo.items_6) {
                    nejm2List.add(item.title);
                }

                nejm2Adapter.setData(nejm2List);
                nejm2Adapter.notifyDataSetChanged();

                List<String> specialistList = new ArrayList<>();
                for(PeriodArticleDeatailInfo.Items0 item : articleDeatailInfo.wantsay) {
                    specialistList.add(item.title);
                }

                specialistAdapter.setData(specialistList);
                specialistAdapter.notifyDataSetChanged();
                if(articleDeatailInfo.video!=null){
                draweeViewVideo.setImageURI(articleDeatailInfo.video.thumb);
                textViewVideo.setText(articleDeatailInfo.video.title);
}
            }
        });
    }

    @OnClick(R.id.videoLayout)
    public void onClickVideoLayout() {
        VideoDetailActivity.launchActivity(this, articleDeatailInfo.video.id);
    }
}
