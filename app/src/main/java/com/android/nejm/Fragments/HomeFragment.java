package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;
import com.android.nejm.data.Banner;
import com.android.nejm.utils.DisplayUtil;
import com.android.nejm.widgets.ImageIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private ImageIndicatorView mImageIndicatorView;
    private ArrayList<String> mBannerUrlList = new ArrayList<String>();
    private ArrayList<Banner>mBannerList=new ArrayList<Banner>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view = inflater.inflate(R.layout.home_fragment,container,false);
        mImageIndicatorView = view.findViewById(R.id.imageIndicatorView);
        processBanner(null);
         return view;
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
//                    Intent intent = new Intent(mContext, GoodListActivity.class);
//                    intent.putExtra("name", banner.category_name);
//                    intent.putExtra("subcate", banner.category_id);
//                    startActivity(intent);

            }
        });
        mImageIndicatorView.setBroadcastEnable(true);
        mImageIndicatorView.setBroadCastTimes(5);// 循环播放5次
        mImageIndicatorView.setBroadcastTimeIntevel(2 * 1000, 3 * 1000);// 播放启动时间及间隔
        mImageIndicatorView.loop();
        mImageIndicatorView.show(DisplayUtil.dip2px(mContext,12),DisplayUtil.dip2px(mContext,12));
    }
}
