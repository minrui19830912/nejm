package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.nejm.MyApplication;
import com.android.nejm.R;
import com.android.nejm.bean.AnnounceRecord;
import com.android.nejm.data.AnnounceMessage;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.DisplayUtil;
import com.android.nejm.utils.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AdvertActivity extends BaseActivity {
    private CountDownTimer mCountDownTimer;
//    SimpleDraweeView sdvSplash ;//显示启动图
    ImageView ivSplash;
    TextView tvNum;
    LinearLayout rlNum;
    private String mLinkUrl;
    long mTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        sdvSplash = (SimpleDraweeView) findViewById(R.id.sdvSplash);
       if(LoginUserManager.getInstance().isLogin()){
           List<AnnounceRecord> recordList = AnnouceRecordManager.getInstance().getRecordList();
           HttpUtils.getMessageList(mContext, new OnNetResponseListener() {
               @Override
               public void onNetDataResponse(JSONObject json) {
                   AnnounceMessage announceMessage = new Gson().fromJson(json.toString(), AnnounceMessage.class);

                   new AsyncTask<Void,Void,Void>() {

                       @Override
                       protected Void doInBackground(Void... voids) {
                           List<AnnounceRecord> list = new ArrayList<>();
                           for(AnnounceMessage.MessageItem item : announceMessage.items) {
                               boolean exist = false;
                               for(AnnounceRecord record : recordList) {
                                   if(TextUtils.equals(item.id, record.msgId)) {
                                       item.read = record.read;
                                       exist = true;
                                       break;
                                   }
                               }

                               if(!exist) {
                                   list.add(new AnnounceRecord(null, false, item.id));
                               }
                           }
                           AnnouceRecordManager.getInstance().insert(list);
                           return null;
                       }

                       @Override
                       protected void onPostExecute(Void aVoid) {

                       }
                   }.execute();
               }
           });
       }

        ivSplash = (ImageView) findViewById(R.id.ivSplash);
        tvNum = (TextView) findViewById(R.id.tvNum);
        rlNum = (LinearLayout) findViewById(R.id.rlNum);
       String path= SPUtils.getSharedStringData(mContext,"adver_path");
       if(!TextUtils.isEmpty(path)){
        Bitmap bmp= BitmapFactory.decodeFile(path);
           ivSplash.setImageBitmap(bmp);
       } else{
           ivSplash.setBackgroundResource(R.mipmap.splash);
       }
        HttpUtils.getAdverstyData(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                String imageUrl = json.optString("img");
                mLinkUrl = json.optString("url");
                new getImageCacheAsyncTask(mContext).execute(imageUrl);
            }
        });
        ivSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mLinkUrl)){
                Uri uri = Uri.parse(mLinkUrl);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            }
        });

        //使用Glide缓存网络图片，
//        long start = SPUtils.getSharedlongData(mContext,"startTime");
//        long end = SPUtils.getSharedlongData(mContext,"endTime");
//        if (start != 0l && end != 0l) {
//            long current = Calendar.getInstance().getTimeInMillis();
//            if (current >start && current<end){//显示缓存图片
//                Glide.with(getApplicationContext()).load(url)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.drawable.splash)
//                        .into(ivSplash);
////                new getImageCacheAsyncTask(this).execute(SPUtils.url);
//
//            }else {//显示默认图片
////                ivSplash.setImageURI(Uri.parse("res://" + getPackageName() + "/" + R.drawable.splash));
//            }
//        }else {//显示默认图片
////            ivSplash.setImageURI(Uri.parse("res://" + getPackageName() + "/" + R.drawable.splash));
//        }
//        msgFromServer();

        //使用fresco加载动画
//        DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
//                    .setAutoPlayAnimations(true)
//        //加载drawable里的一张gif图
//                    .setUri(Uri.parse("res://"+getPackageName()+"/"+R.drawable.splash))//设置uri
//                    .build();
//        //设置Controller
//        sdvSplash.setController(mDraweeController);
//                mCountDownTimer = new CountDownTimer(4 * 1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                //每隔countDownInterval秒会回调一次onTick()方法
//                tvNum.setText((millisUntilFinished / 1000)+"");
//            }
//
//            @Override
//            public void onFinish() {
//                //startActivity(new Intent(mContext,MainActivity.class));
//                if(TextUtils.isEmpty(MyApplication.mToken)){
//                    startActivity(new Intent(mContext,LoginActivity.class));
//                    finish();
//                } else {
//                    startActivity(new Intent(mContext,MainActivity.class));
//                    finish();
//                }
//            }
//        };
       // mCountDownTimer.start();// 开始计时
        rlNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(mContext,MainActivity.class));
                if(TextUtils.isEmpty(MyApplication.mToken)){
                    startActivity(new Intent(mContext,LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(mContext,MainActivity.class));
                    finish();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCountDownTimer!=null){
        mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        long time = 4 * 1000;
        if(mCountDownTimer!=null){
            time =mTime;
        }
        mCountDownTimer= new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                mTime = millisUntilFinished;
                tvNum.setText((millisUntilFinished / 1000)+"");
            }

            @Override
            public void onFinish() {
                //startActivity(new Intent(mContext,MainActivity.class));
                if(TextUtils.isEmpty(MyApplication.mToken)){
                    startActivity(new Intent(mContext,LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(mContext,MainActivity.class));
                    finish();
                }
            }
        };
        mCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
        }
    }



    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public getImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl =  params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(DisplayUtil.dip2px(mContext,1080),DisplayUtil.dip2px(mContext,1920))
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            SPUtils.setSharedStringData(mContext,"adver_path",path);
            Log.e("path", path);


        }
    }




}
