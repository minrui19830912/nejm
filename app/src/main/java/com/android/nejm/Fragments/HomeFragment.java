package com.android.nejm.Fragments;

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

import com.android.nejm.R;
import com.android.nejm.activitys.ArticleDetailActivity;
import com.android.nejm.activitys.SearchActivity;
import com.android.nejm.adapter.HorizontalPaperListAdapter;
import com.android.nejm.data.Banner;
import com.android.nejm.data.Paper;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.DisplayUtil;
import com.android.nejm.widgets.ImageIndicatorView;
import com.android.nejm.widgets.LoadingDialog;
import com.android.nejm.widgets.NoScrollGridView;
import com.android.nejm.widgets.SpacesItemDecoration;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private ImageIndicatorView mImageIndicatorView;
    private ArrayList<String> mBannerUrlList = new ArrayList<String>();
    private ArrayList<Banner>mBannerList=new ArrayList<Banner>();
    private NoScrollGridView mGridView;
    private RecyclerView mRecyclerView;
    private HorizontalPaperListAdapter mHorizontalPaperListAdapter;
    private GridAdapter mGridAdapter;
    private String[]gridArray={"fads","fads","fda","fad","fads","fads","fda","fad"};
    private ArrayList<Paper> mPaperList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view = inflater.inflate(R.layout.home_fragment,container,false);
        mImageIndicatorView = view.findViewById(R.id.imageIndicatorView);
        mGridView = view.findViewById(R.id.grdiview);
        mRecyclerView = view.findViewById(R.id.horizontal_paper_list);
        mGridAdapter  =new GridAdapter();
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
//                    intent.putExtra("name", banner.category_name);
//                    intent.putExtra("subcate", banner.category_id);
                startActivity(intent);
            }
        });
        mHorizontalPaperListAdapter = new HorizontalPaperListAdapter(mContext);
        for (int i = 0; i < 6; i++) {
            Paper paper = new Paper();
            mPaperList.add(paper);
        }
        mHorizontalPaperListAdapter.setData(mPaperList);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration( DisplayUtil.dip2px(mContext,4)));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.setAdapter(mHorizontalPaperListAdapter);
        mHorizontalPaperListAdapter.notifyDataSetChanged();
        processBanner(null);
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
//        LoadingDialog.showDialogForLoading(mContext);
//        HttpUtils.getMainData(mContext,"app","index", new OnNetResponseListener() {
//            @Override
//            public void onNetDataResponse(JSONObject json) {
//                LoadingDialog.cancelDialogForLoading();
//
//            }
//        });
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getMainData(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();

            }
        });
    }

    private class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return gridArray.length;
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
            if(convertView==null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.category_grid_item,null);
            }
            ((SimpleDraweeView)convertView.findViewById(R.id.category_img)).setImageURI("https://publish-pic-cpu.baidu.com/4c121c86-7b68-4922-a87c-bc23052516d1.jpeg@q_90,w_450|f_webp");

            return convertView;
        }
    }

    private void processBanner(JSONObject json){
//        JSONArray banner = json.optJSONArray("rotate");
//        for(int i=0;i<banner.length();i++){
//            JSONObject banObject = banner.optJSONObject(i);
//            Banner ban = new Banner();
//            ban.img_url = banObject.optString("img_url");
//            ban.url=banObject.optString("url");
//            ban.title=banObject.optString("title");
//            ban.category_name = banObject.optString("category_name");
//            ban.category_id = banObject.optString("category_id");
//            mBannerList.add(ban);
//            mBannerUrlList.add(ban.img_url);
//        }
                    mBannerUrlList.add("http://www.windlinker.com/uploads/common/index_banner.jpg");
                    mBannerUrlList.add("http://www.windlinker.com/uploads/common/2.png");
                    mBannerUrlList.add("http://www.windlinker.com/uploads/common/3.png");
        mImageIndicatorView.setIndicatorIconDrawable(R.drawable.circle_chang_focus_drawable,R.drawable.circle_chang_normal_drawable);
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
        mImageIndicatorView.show(DisplayUtil.dip2px(mContext,12),DisplayUtil.dip2px(mContext,12));
    }
}
