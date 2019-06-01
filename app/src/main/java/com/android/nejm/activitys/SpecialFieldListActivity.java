package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.SpecialFieldGridAdapter;
import com.android.nejm.adapter.SpeicalFieldArticleAdapter;
import com.android.nejm.data.SpecialFieldArticleInfo;
import com.android.nejm.data.SpecialFieldIconInfo;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialFieldListActivity extends BaseActivity {
    @BindView(R.id.textViewSpecailFieldTitle)
    TextView textViewSpecailFieldTitle;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String id;
    private int page = 1;
    private SpecialFieldArticleInfo articleInfo;
    public List<SpecialFieldArticleInfo.ArtitleItem> artitleItems = new ArrayList<>();

    SpeicalFieldArticleAdapter articleAdapter;
    SpecialFieldGridAdapter gridAdapter;

    public static void launchActivity(Context context, String title, String id) {
        Intent intent = new Intent(context, SpecialFieldListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_field_list);
        showBack();
        ButterKnife.bind(this);

        String title = getIntent().getStringExtra("title");
        setCommonTitle(title);

        textViewSpecailFieldTitle.setText(title);

        this.id = getIntent().getStringExtra("id");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpecialFieldListActivity.this.id = articleInfo.classes.get(position).id;
                page = 1;
                recyclerView.scrollToPosition(0);
                getData(true, true);
            }
        });

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        articleAdapter = new SpeicalFieldArticleAdapter(this);
        recyclerView.setAdapter(articleAdapter);

        getData(true, true);
    }

    private void getData(boolean showLoadingDialog, final boolean clearList) {
        if(showLoadingDialog) {
            LoadingDialog.showDialogForLoading(this);
        }

        HttpUtils.getArticleClassList(this, id, page, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                refreshLayout.finishRefresh();

                articleInfo = new Gson().fromJson(json.toString(), SpecialFieldArticleInfo.class);
                if(clearList) {
                    artitleItems.clear();
                }

                artitleItems.addAll(articleInfo.items);
                if(artitleItems.size() >= articleInfo.total_count) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                }

                articleAdapter.setData(artitleItems);
                articleAdapter.notifyDataSetChanged();

                if(gridAdapter == null) {
                    gridAdapter = new SpecialFieldGridAdapter(mContext);
                    gridAdapter.setData(articleInfo.classes, id);
                    gridView.setAdapter(gridAdapter);
                } else {
                    gridAdapter.setFocusId(id);
                }

                if(articleInfo.classes != null) {
                    for(SpecialFieldArticleInfo.Classes clazz : articleInfo.classes) {
                        if(TextUtils.equals(clazz.id, id)) {
                            textViewSpecailFieldTitle.setText(clazz.classname);
                            setCommonTitle(clazz.classname);
                            break;
                        }
                    }
                }
            }
        });
    }
}
