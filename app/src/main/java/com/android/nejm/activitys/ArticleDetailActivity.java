package com.android.nejm.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.android.nejm.R;
import com.android.nejm.utils.AppUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class ArticleDetailActivity extends BaseActivity {
    private WebView mWebView;

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_URL = "url";
    private static final String EXTRA_CONTENT = "content";
    private static final String EXTRA_COVER = "cover";
    private static final String EXTRA_TITLE = "title";
    private String mTitle;

    private String mContent;
    private String mShareContent="NEJM医学前沿";
    private String cover;
    private String url;
    public static void launchActivity(Context context, String url,String content,String cover,String title) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_CONTENT, content);
        intent.putExtra(EXTRA_COVER, cover);
        intent.putExtra(EXTRA_TITLE, title);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_activity);
        setCommonTitle("NEJM医学前沿");
        showBack();
        initWebView();

        url = getIntent().getStringExtra(EXTRA_URL);
        //https://dev.nejmqianyan.com/?c=article&m=app&id=163&uid=341
        //String url = HttpUtils.ARTICLE_DETAIL_URL+id;
        //if login url+="&uid=341"
        mWebView.loadUrl(url);
//        mWebView.loadUrl("file:///android_asset/articles.html");
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings()
                .setJavaScriptCanOpenWindowsAutomatically(true);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            // 返回键退回
            // if (isProgressShow()) {
            // closeProgress();
            // }
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private void showShareDialog() {


        View view = LayoutInflater.from(mContext).inflate(R.layout.share_dlg, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(this, R.style.common_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.view_share_weixin:
                        // 分享到微信
                        // onShare2Weixin();
                        AppUtil.shareToFriend(mContext, mTitle, mShareContent, url,cover, new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(Platform platform, int i) {

                            }
                        });
                        break;

                    case R.id.view_share_pengyou:
                        // 分享到朋友圈
                        // onShare2Weixin();
                        AppUtil.shareToCircel(mContext, mTitle, mShareContent, url,cover,  new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(Platform platform, int i) {

                            }
                        });
                        break;

                    case R.id.share_cancel_btn:
                        // 取消
                        break;
                    case R.id.view_share_email:

                        break;
                    case R.id.view_share_weibo:
                        AppUtil.shareToSinaWeibo(mContext, mTitle, mContent, url,cover, new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(Platform platform, int i) {

                            }
                        });
                        break;


                }

                dialog.dismiss();
            }

        };
        ViewGroup mViewWeixin = (ViewGroup) view.findViewById(R.id.view_share_weixin);
        ViewGroup mViewPengyou = (ViewGroup) view.findViewById(R.id.view_share_pengyou);
        ViewGroup mViewWeibo = (ViewGroup) view.findViewById(R.id.view_share_weibo);
        Button mBtnCancel = (Button) view.findViewById(R.id.share_cancel_btn);

        mViewWeixin.setOnClickListener(listener);
        mViewPengyou.setOnClickListener(listener);
        mViewWeibo.setOnClickListener(listener);
        mBtnCancel.setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }

}
