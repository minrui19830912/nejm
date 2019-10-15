package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.android.nejm.bean.FiltModel;
import com.android.nejm.data.SpecialFieldArticleInfo;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.FiltPopuWindow;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
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
    String type_array;

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
        HttpUtils.getWholeCategory(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                JSONArray items = json.optJSONArray("items");
                if(items!=null){
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.optJSONObject(i);
                        String sourcename = item.optString("sourcename");
                        JSONArray typelist = item.optJSONArray("typelist");
                        for (int j = 0; typelist!=null&&j < typelist.length(); j++) {
                            JSONObject typeObj = typelist.optJSONObject(j);
                            typeObj.optString("id");
                            typeObj.optString("typename");
                        }
                    }
                }
            }
        });
        initView();
    }

    private void getData(boolean showLoadingDialog, final boolean clearList) {
        if(showLoadingDialog) {
            LoadingDialog.showDialogForLoading(this);
        }

        HttpUtils.getArticleClassList(this, id,type_array, page, new OnNetResponseListener() {
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


private void initView(){
    String[] titles = getArray(R.array.fit_titles);
    String[][] tabs = new String[][]{
            getArray(R.array.fit_sex_tabs)
            ,getArray(R.array.fit_age_tabs)
            ,getArray(R.array.fit_money_tabs)
    };
    final List<FiltModel> listData = new ArrayList<>();
    for (int i = 0; i < titles.length; i++){
        FiltModel model = new FiltModel();
        model.setTypeName(titles[i]);
        model.setType(i);//筛选类型可以自己定义
        List<FiltModel.TableMode> tabsList = new ArrayList<>();
        for(int j = 0; j < tabs[i].length; j++){
            FiltModel.TableMode mod = new FiltModel.TableMode();
            mod.id = j;//id可以自己定义 看服务器需要什么
            mod.name = tabs[i][j];
            tabsList.add(mod);
        }
        model.setTabs(tabsList);
        listData.add(model);
        //默认选中第一项
        model.setTab(model.getTabs().get(0));
    }
    //数据组装稍微有点麻烦哈，如果您更好的方式请联系我，我学习学习。
    //直接看结果吧
    findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new FiltPopuWindow.Builder(mContext).setColumnCount(3)//设置列数，测试2.3.4.5没问题
                    .setDataSource(listData)
                    .setColorBg(R.color.color_f8f8f8)
                    //所有的属性设置必须在build之前，不然无效
                    .build()
                    .createPop()
                    .showAsDropDown(findViewById(R.id.top_divider));
            //我这里头方便这样写了，pop对象可以拿出来存放，不需要每次都去创建
        }
    });
}


    private String[] getArray(int id){
        return getResources().getStringArray(id);
    }
}
