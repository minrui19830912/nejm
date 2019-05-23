package com.android.nejm.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.ArticleDetailActivity;
import com.android.nejm.activitys.MainActivity;
import com.android.nejm.activitys.NotificationActivity;
import com.android.nejm.activitys.OtherArticleListActivity;
import com.android.nejm.activitys.SearchActivity;
import com.android.nejm.activitys.SpecialFieldListActivity;
import com.android.nejm.adapter.HorizontalPaperListAdapter;
import com.android.nejm.data.HomeBean;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.DisplayUtil;
import com.android.nejm.widgets.HorizontalDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.android.nejm.widgets.NoScrollGridView;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    Banner banner;
    RadioGroup radioGroup1;
    RadioGroup radioGroupField;
    ImageView notification;

    private ArrayList<String> mBannerUrlList = new ArrayList<String>();
    private NoScrollGridView mGridView;
    private RecyclerView mRecyclerView;
    private HorizontalPaperListAdapter mHorizontalPaperListAdapter;
    //private GridAdapter mGridAdapter;
    //private String[] gridArray = {"fads", "fads", "fda", "fad", "fads", "fads", "fda", "fad"};
    //private ArrayList<Paper> mPaperList = new ArrayList<>();
    private HomeBean homeBean;

    private ClassesGridAdapter classesGridAdapter;
    private FilterOneGridAdapter filterOneGridAdapter;
    private FilterTwoGridAdapter filterTwoGridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        banner = view.findViewById(R.id.bannerView);
        radioGroup1 = view.findViewById(R.id.radioGroup1);
        radioGroup1.check(R.id.latest);

        classesGridAdapter = new ClassesGridAdapter();
        filterOneGridAdapter = new FilterOneGridAdapter();
        filterTwoGridAdapter = new FilterTwoGridAdapter();

        radioGroupField = view.findViewById(R.id.radioGroup2);
        radioGroupField.check(R.id.major_field);
        radioGroupField.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.major_field:
                        mGridView.setAdapter(classesGridAdapter);
                        break;
                    case R.id.nejm:
                        mGridView.setAdapter(filterOneGridAdapter);
                        break;
                    case R.id.nejm_hot:
                        mGridView.setAdapter(filterTwoGridAdapter);
                        break;
                }
            }
        });

        mGridView = view.findViewById(R.id.grdiview);
        mRecyclerView = view.findViewById(R.id.horizontal_paper_list);
        //mGridAdapter = new GridAdapter();
        //mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(mContext, ArticleDetailActivity.class);
//                    intent.putExtra("name", banner.category_name);
//                    intent.putExtra("subcate", banner.category_id);
                //startActivity(intent);
                switch (radioGroupField.getCheckedRadioButtonId()) {
                    case R.id.major_field: {
                        JSONArray jsonArray = new JSONArray();
                        for(HomeBean.Classes clazz : homeBean.classes) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.putOpt("id", clazz.id);
                                jsonObject.putOpt("title", clazz.classname);
                                jsonObject.putOpt("icon", clazz.icon);
                                jsonObject.putOpt("xcx_icon_se", clazz.xcx_icon_se);
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        HomeBean.Classes classes = homeBean.classes.get(position);
                        SpecialFieldListActivity.launchActivity(mContext, classes.classname, "专业领域", classes.id, jsonArray.toString());
                    }
                        break;
                    case R.id.nejm: {
                        JSONArray jsonArray = new JSONArray();
                        for(HomeBean.Filter filter : homeBean.filter_1) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.putOpt("id", filter.id);
                                jsonObject.putOpt("title", filter.filtername);
                                jsonObject.putOpt("icon", "");
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        HomeBean.Filter filter = homeBean.filter_1.get(position);
                        OtherArticleListActivity.launchActivity(mContext, filter.filtername, "NEJM", filter.id, jsonArray.toString());
                    }
                        break;
                    case R.id.nejm_hot:
                    {
                        JSONArray jsonArray = new JSONArray();
                        for(HomeBean.Filter filter : homeBean.filter_2) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.putOpt("id", filter.id);
                                jsonObject.putOpt("title", filter.filtername);
                                jsonObject.putOpt("icon", "");
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        HomeBean.Filter filter2 = homeBean.filter_2.get(position);
                        OtherArticleListActivity.launchActivity(mContext, filter2.filtername, "NEJM期刊荟萃", filter2.id, jsonArray.toString());
                    }
                        break;
                }
            }
        });

        //processBanner(null);
        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(intent);
            }
        });
        getData();
        view.findViewById(R.id.nejm_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).showTab(3);
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

    private void getData() {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getMainData(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                homeBean = new Gson().fromJson(json.toString(), HomeBean.class);
                //JSONArray articleList = json.optJSONArray("weekly");
                mHorizontalPaperListAdapter = new HorizontalPaperListAdapter(mContext);
                /*for (int i = 0; i < articleList.length(); i++) {
                    JSONObject article = articleList.optJSONObject(i);
                    Paper paper = new Paper();
                    paper.id = article.optString("id");
                    paper.title = article.optString("title");
                    paper.date = article.optString("thedate");
                    paper.url = article.optString("cover");
                    mPaperList.add(paper);
                }
                //mHorizontalPaperListAdapter.setData(mPaperList);*/
                mHorizontalPaperListAdapter.setData(homeBean.weekly);
                mRecyclerView.addItemDecoration(new HorizontalDecoration(DisplayUtil.dip2px(mContext, 7)));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                mRecyclerView.setAdapter(mHorizontalPaperListAdapter);
                mHorizontalPaperListAdapter.notifyDataSetChanged();

                processBanner();

                mGridView.setAdapter(classesGridAdapter);
                classesGridAdapter.notifyDataSetChanged();
            }
        });

    }

    private class ClassesGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(homeBean != null && homeBean.classes != null) {
                return homeBean.classes.size();
            }

            return 0;
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
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_item, null);
                holder = new ViewHolder();
                holder.simpleDraweeView = convertView.findViewById(R.id.category_img);
                holder.textView = convertView.findViewById(R.id.category_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            HomeBean.Classes classes = homeBean.classes.get(position);
            holder.simpleDraweeView.setBackgroundResource(R.drawable.home_classes_bg);
            holder.simpleDraweeView.setImageURI(classes.icon);
            Log.e("grid", "icon = " + classes.icon);
            holder.textView.setText(classes.classname);

            return convertView;
        }
    }

    static class ViewHolder {
        SimpleDraweeView simpleDraweeView;
        TextView textView;
    }

    private class FilterOneGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(homeBean != null && homeBean.filter_1 != null) {
                return homeBean.filter_1.size();
            }

            return 0;
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
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_item, null);
                holder = new ViewHolder();
                holder.simpleDraweeView = convertView.findViewById(R.id.category_img);
                holder.textView = convertView.findViewById(R.id.category_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            HomeBean.Filter filter = homeBean.filter_1.get(position);
            holder.simpleDraweeView.setBackgroundResource(R.drawable.icon_article);
            holder.textView.setText(filter.filtername);

            return convertView;
        }
    }

    private class FilterTwoGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(homeBean != null && homeBean.filter_2 != null) {
                return homeBean.filter_2.size();
            }

            return 0;
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
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_item, null);
                holder = new ViewHolder();
                holder.simpleDraweeView = convertView.findViewById(R.id.category_img);
                holder.textView = convertView.findViewById(R.id.category_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            HomeBean.Filter filter = homeBean.filter_2.get(position);
            holder.simpleDraweeView.setBackgroundResource(R.drawable.icon_article);
            holder.textView.setText(filter.filtername);

            return convertView;
        }
    }

    private void processBanner() {
        /*mBannerUrlList.add("http://www.windlinker.com/uploads/common/index_banner.jpg");
        mBannerUrlList.add("http://www.windlinker.com/uploads/common/2.png");
        mBannerUrlList.add("http://www.windlinker.com/uploads/common/3.png");*/

        if (homeBean.banner == null || homeBean.banner.size() == 0) {
            return;
        }

        List<String> titleList = new ArrayList<>();

        for (HomeBean.Banner banner : homeBean.banner) {
            mBannerUrlList.add(banner.pic);
            titleList.add(banner.title);
            Log.e("banner", banner.title + ", " + banner.pic);
        }

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(mBannerUrlList);
        banner.setBannerTitles(titleList);
        banner.isAutoPlay(true);
        banner.setDelayTime(3000);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                HomeBean.Banner bannr = homeBean.banner.get(position);
                ArticleDetailActivity.launchActivity(mContext,bannr.articleid,
                        HttpUtils.ARTICLE_DETAIL_URL+bannr.articleid,bannr.intro,bannr.pic,bannr.title);
            }
        });

        banner.start();

        /*mImageIndicatorView.setIndicatorIconDrawable(R.drawable.circle_chang_focus_drawable,R.drawable.circle_chang_normal_drawable);
        mImageIndicatorView.setupLayoutByImageUrl(mBannerUrlList,
                R.mipmap.default_banner);
        //轮播图点击事件
        mImageIndicatorView.setOnItemClickListener(new ImageIndicatorView.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Log.e("mImageIndicatorView","OnItemClick"+position);

//                    Banner banner = mBannerList.get(position);
                    Intent intent = new Intent(mContext, ArticleDetailActivity.class);
//                    intent.putExtra("name", banner.category_name);
//                    intent.putExtra("subcate", banner.category_id);
                    startActivity(intent);
            }
        });
        mImageIndicatorView.setBroadcastEnable(true);
        mImageIndicatorView.setBroadCastTimes(5);// 循环播放5次
        mImageIndicatorView.setBroadcastTimeIntevel(2 * 1000, 3 * 1000);// 播放启动时间及间隔
        mImageIndicatorView.loop();
        mImageIndicatorView.show(DisplayUtil.dip2px(mContext,12),DisplayUtil.dip2px(mContext,12));*/
    }

    static class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
}
