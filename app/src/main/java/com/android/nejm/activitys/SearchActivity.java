package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.SpeicalFieldArticleAdapter;
import com.android.nejm.data.Paper;
import com.android.nejm.data.Source;
import com.android.nejm.data.SpecialFieldArticleInfo;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private SpeicalFieldArticleAdapter articleAdapter;
    private SpecialFieldArticleInfo articleInfo;
    public List<SpecialFieldArticleInfo.ArtitleItem> artitleItems = new ArrayList<>();
    private ArrayList<Source> mSourceList = new ArrayList<>();
    private int totalCount;
    private CheckBox mCurrentCheckBox;
    private int mCheckIndex = 0;
    private String id;
    private ClassesGridAdapter mClassesGridAdapter = new ClassesGridAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setCommonTitle("搜索", true);
        ButterKnife.bind(this);
        articleAdapter = new SpeicalFieldArticleAdapter(this);
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

        refreshLayout.autoLoadMore();
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

        String keyword = editTextSearch.getText().toString();
        HttpUtils.search(this, keyword, id, page, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                refreshLayout.finishRefresh(100);
                refreshLayout.finishLoadMore(100);
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

                articleAdapter.setData(artitleItems);
                articleAdapter.notifyDataSetChanged();
                mClassesGridAdapter.notifyDataSetChanged();
                mSearchContent.setVisibility(View.VISIBLE);
            }
        });
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
            CheckBox checkBox = (CheckBox) convertView;
            if (mCheckIndex == pos) {
                mCurrentCheckBox = checkBox;
                mCurrentCheckBox.setChecked(true);

            } else {
                checkBox.setChecked(false);
            }
            if (source != null) {
                checkBox.setText(source.sourcename);
            }
            CompoundButton.OnCheckedChangeListener mCheckListener=new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        if (mCurrentCheckBox != null) {
                            mCurrentCheckBox.setChecked(false);
                        }
                        mCurrentCheckBox = (CheckBox) buttonView;
                        mCheckIndex = pos;
                        id = source.id;
                        search(true, false);
                    }
                }
            };
            checkBox.setOnCheckedChangeListener(mCheckListener);
            return convertView;
        }
    }


}
