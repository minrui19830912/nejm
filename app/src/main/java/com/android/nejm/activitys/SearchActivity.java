package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.SearchArticleAdapter;
import com.android.nejm.data.Paper;
import com.android.nejm.data.Source;
import com.android.nejm.data.SpecialFieldArticleInfo;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.search_box)
    EditText editTextSearch;
    @BindView(R.id.search_content)
    View mSearchContent;
    @BindView(R.id.recyler_view)
    RecyclerView mRecylerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.grdiview)
    GridView grdiview;
    @BindView(R.id.imageViewClear)
    ImageView imageViewClear;


    private ArrayList<Paper> mPaperList = new ArrayList<>();
    private int page = 1;
    private SearchArticleAdapter articleAdapter;
    private SpecialFieldArticleInfo articleInfo;
    public List<SpecialFieldArticleInfo.ArtitleItem> artitleItems = new ArrayList<>();
    private ArrayList<Source> mSourceList = new ArrayList<>();
    private int totalCount;
    private int mCheckIndex = 0;
    private String id;
    private ClassesGridAdapter mClassesGridAdapter = new ClassesGridAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setCommonTitle("搜索", true);
        ButterKnife.bind(this);
        articleAdapter = new SearchArticleAdapter(this, true, new SearchArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                SpecialFieldArticleInfo.ArtitleItem artitleItem = artitleItems.get(index);
                if(mCheckIndex >= 0 && mSourceList.size() > mCheckIndex) {
                    Source source = mSourceList.get(mCheckIndex);
                    if(TextUtils.equals(source.sourcename, "其他文章")) {
                        ArticleDetailActivity.launchActivity(mContext, artitleItem.id, HttpUtils.NEW_KNOWLEDGE_DETAIL_URL+artitleItem.id,artitleItem.show_wantsay,artitleItem.thumb,artitleItem.title);
                        return;
                    }
                }

                if(artitleItem.is_video) {
                    VideoDetailActivity.launchActivity(mContext, artitleItem.id);
                } else {
                    ArticleDetailActivity.launchActivity(mContext, artitleItem.id, HttpUtils.ARTICLE_DETAIL_URL+artitleItem.id,artitleItem.show_wantsay,artitleItem.thumb,artitleItem.title);
                }
            }
        });
//        SearchKnowledgeAdapter adapter = new SearchKnowledgeAdapter(mContext);
//        for (int i = 0; i < 6; i++) {
//            Paper paper = new Paper();
//            mPaperList.add(paper);
//        }
//        adapter.setData(mPaperList);
        mRecylerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        mRecylerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mRecylerView.setAdapter(articleAdapter);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (artitleItems.size() < totalCount) {
                    page++;
                    search(false, true);
                } else {
                    ToastUtil.showShort(mContext, "没有更多数据");
                }
            }
        });
        grdiview.setAdapter(mClassesGridAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    imageViewClear.setVisibility(View.VISIBLE);
                } else {
                    imageViewClear.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnEditorAction(R.id.search_box)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search(true, false);
            return true;
        }

        return false;
    }

    @OnClick(R.id.imageViewClear)
    public void onClickClear() {
        editTextSearch.setText("");
        imageViewClear.setVisibility(View.GONE);
    }

    private void search(boolean showLoading, final boolean loadMore) {
        if (showLoading) {
            LoadingDialog.showDialogForLoading(this);
        }

        final String keyword = editTextSearch.getText().toString();
        recordEvent(keyword);
        HttpUtils.search(this, keyword, id, page, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                refreshLayout.finishRefresh();
                if (!loadMore) {
                    artitleItems.clear();
                }
                mSourceList.clear();
                JSONArray sources = json.optJSONArray("sources");
                for (int i = 0; sources != null && i < sources.length(); i++) {
                    JSONObject sourceJsonObject = sources.optJSONObject(i);
                    Source source = new Source();
                    source.id = sourceJsonObject.optString("id");
                    source.sourcename = sourceJsonObject.optString("sourcename");
                    mSourceList.add(source);
                }

                totalCount = json.optInt("total_count");
                articleInfo = new Gson().fromJson(json.toString(), SpecialFieldArticleInfo.class);


                artitleItems.addAll(articleInfo.items);
                if(artitleItems.size() >= totalCount) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.finishLoadMore();
                }

                articleAdapter.setData(artitleItems,keyword);
                articleAdapter.notifyDataSetChanged();
                mClassesGridAdapter.notifyDataSetChanged();
                mSearchContent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void recordEvent(String keyword) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("keyword", keyword);
        if(LoginUserManager.getInstance().isLogin()) {
            //map.put("user", LoginUserManager.getInstance().uid);
        }
        MobclickAgent.onEvent(mContext.getApplicationContext(), "ID_EVENT_SEARCH_KEYWORD", map);
    }

    private class ClassesGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSourceList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.search_grid_item, parent,false);
            }

            Source source = mSourceList.get(position);
            final int pos = position;
            TextView textView = (TextView) convertView;
            if (mCheckIndex == pos) {
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.drawable.home_radio_checked);
            } else {
                textView.setTextColor(getResources().getColor(R.color.nejm_text_unselect));
                textView.setBackgroundResource(R.drawable.home_radio_nor);
            }

            if (source != null) {
                textView.setText(source.sourcename);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCheckIndex = pos;
                    id = source.id;
                    page = 1;
                    refreshLayout.resetNoMoreData();
                    mRecylerView.scrollToPosition(0);
                    notifyDataSetChanged();
                    search(true, false);
                }
            });
            return convertView;
        }
    }
}
