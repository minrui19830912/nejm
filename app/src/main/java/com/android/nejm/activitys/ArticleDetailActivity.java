package com.android.nejm.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.bean.DownloadRecord;
import com.android.nejm.data.ArticleShareContent;
import com.android.nejm.db.DownloadRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.AppUtil;
import com.android.nejm.utils.MyDownloadManager;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ArticleDetailActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private WebView mWebView;
    private TextView textViewDownload;

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
    private String mId;
    private TextView storage;

    ArticleShareContent shareContent;
    private String outLinkUrl;

    private static final int PERMISSION_STORGE_REQUEST_CODE = 1;

    public static void launchActivity(Context context,String id, String url,String content,String cover,String title) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_ID, id);
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
        mId= getIntent().getStringExtra(EXTRA_ID);
        url = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mContent = getIntent().getStringExtra(EXTRA_CONTENT);
        cover = getIntent().getStringExtra(EXTRA_COVER);

        checkLogin();
        /*findViewById(R.id.unlogin_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,LoginActivity.class);
                intent.putExtra("just_finish",true);
                startActivityForResult(intent,1001);
            }
        });*/

//        mWebView.loadUrl("file:///android_asset/articles.html");
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
        storage =findViewById(R.id.storage);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showDialogForLoading(mContext);
                HttpUtils.saveArticle(mContext, mId, new OnNetResponseListener() {
                    @Override
                    public void onNetDataResponse(JSONObject json) {
                        LoadingDialog.cancelDialogForLoading();
                        ToastUtil.showShort(mContext,"收藏成功");
                        Drawable storageDrawable = getResources().getDrawable(R.mipmap.icon_collect_selected);
                        storage.setCompoundDrawablesWithIntrinsicBounds(storageDrawable,null,null,null);
                    }
                });
            }
        });
        findViewById(R.id.relate_article).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ReleatedArticleActivity.class);
                intent.putExtra("id",mId);
                startActivity(intent);
            }
        });

        if(url != null && url.startsWith(HttpUtils.NEW_KNOWLEDGE_DETAIL_URL)) {
            findViewById(R.id.operate_layout).setVisibility(View.GONE);
        } else {
            loadShareContent();
        }

        textViewDownload = findViewById(R.id.download);
        textViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    downloadArticle();
                } else {
                    EasyPermissions.requestPermissions(ArticleDetailActivity.this, "NEJM需要使用存储权限，您是否同意？", PERMISSION_STORGE_REQUEST_CODE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
        if(LoginUserManager.getInstance().isLogin()&&DownloadRecordManager.hasDownLoad(mId)){
            textViewDownload.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_img_download_selected, 0, 0, 0);
            textViewDownload.setTextColor(getResources().getColor(R.color.color_c92700));
            textViewDownload.setClickable(false);
        }

        recordEvent();
    }

    private void recordEvent() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("title", mTitle);
        if(LoginUserManager.getInstance().isLogin()) {
            map.put("isLogin", "true");
            //map.put("user", LoginUserManager.getInstance().uid);
        } else {
            map.put("isLogin", "false");
        }

        if(url != null && url.startsWith(HttpUtils.NEW_KNOWLEDGE_DETAIL_URL)) {
            MobclickAgent.onEvent(mContext.getApplicationContext(), "ID_EVENT_NEW_KNOWLEDGE", map);
        } else {
            MobclickAgent.onEvent(mContext.getApplicationContext(), "ID_EVENT_ARTICLE", map);
        }
    }

    private void downloadArticle() {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getArticleInfo(mContext, mId, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {

                JSONObject item = json.optJSONObject("item");

                DownloadRecord downloadRecord = new DownloadRecord();
                downloadRecord.articleId = item.optString("id");
                downloadRecord.title = item.optString("title");
                downloadRecord.filtername = item.optString("filtername");
                downloadRecord.thumb = item.optString("thumb");
                downloadRecord.postdate = item.optString("postdate");
                downloadRecord.show_wantsay = item.optString("show_wantsay");
                downloadRecord.author = item.optString("author");
                downloadRecord.sourcename = item.optString("sourcename");
                downloadRecord.typename = item.optString("typename");

                JSONArray jsonArray = item.optJSONArray("specialties");
                if(jsonArray != null && jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    downloadRecord.classname = jsonObject.optString("classname");
                }

                File file = new File(mContext.getExternalFilesDir(null), String.format(Locale.CHINA, "/html/%s.html", mId));
                downloadRecord.filePath = file.getAbsolutePath();
                DownloadRecordManager.insert(downloadRecord);
            }
        });


        List<String> urlList = new ArrayList<>();
        String loginUrl = url;
        loginUrl+="&ac=download&uid=";
        loginUrl+=LoginUserManager.getInstance().uid;
        urlList.add(loginUrl);

        String filePath = String.format(Locale.CHINA, "/html/%s.html", mId);
        List<String> filePathList = new ArrayList<>();
        filePathList.add(filePath);

        LoadingDialog.showDialogForLoading(mContext);
        MyDownloadManager.download(mContext, urlList, filePathList, new ArrayList<>(),
                new ArrayList<>(),
                new MyDownloadManager.DownloadCompleteListener() {
                    @Override
                    public void downloadComplete() {
                        Log.e("TAG", "downloadComplete");
                        ToastUtil.showShort(mContext,"下载成功");
                        LoadingDialog.cancelDialogForLoading();
                        textViewDownload.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_img_download_selected, 0, 0, 0);
                        textViewDownload.setTextColor(getResources().getColor(R.color.color_c92700));
                    }
                });
    }

    private void loadShareContent() {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.getArticleShareContent(this, mId, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                String isFav = json.optString("isfav");
                if(isFav.equals("1")){
                    Drawable storageDrawable = getResources().getDrawable(R.mipmap.icon_collect_selected);
                    storage.setCompoundDrawablesWithIntrinsicBounds(storageDrawable,null,null,null);
                }
                shareContent = new Gson().fromJson(json.optJSONObject("item").toString(), ArticleShareContent.class);
            }
        });
    }

    private void checkLogin() {
        String appendUrl=url;
        //https://dev.nejmqianyan.com/?c=article&m=app&id=163&uid=341
        //String url = HttpUtils.ARTICLE_DETAIL_URL+id;
        //if login url+="&uid=341"
        if(LoginUserManager.getInstance().isLogin()){
            appendUrl+="&uid=";
            appendUrl+=LoginUserManager.getInstance().uid;
            findViewById(R.id.operate_layout).setVisibility(View.VISIBLE);
            //findViewById(R.id.unlogin_text).setVisibility(View.GONE);

        } else {
            findViewById(R.id.operate_layout).setVisibility(View.GONE);
            //findViewById(R.id.unlogin_text).setVisibility(View.VISIBLE);
        }
        mWebView.loadUrl(appendUrl);
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == PERMISSION_STORGE_REQUEST_CODE) {
            downloadArticle();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtil.showShort(this, "用户授权失败");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("NEJM", "shouldOverrideUrlLoading, url = " + url);
            if(TextUtils.equals(url, "http://www.nejm.login/")) {
                Intent intent = new Intent(mContext,LoginActivity.class);
                intent.putExtra("just_finish",true);
                startActivityForResult(intent,1001);
                return true;
            } else if (url.startsWith("http://www.nejm.com/?videoId=")) {
                int length = "http://www.nejm.com/?videoId=".length();
                if(url.length() > length) {
                    String videoId = url.substring(length);
                    VideoDetailActivity.launchActivity(mContext, videoId);
                    return true;
                }
            } else if (url.startsWith("http://www.nejm.com/?classid=")) {
                int length = "http://www.nejm.com/?classid=".length();
                int index = url.indexOf('&');
                if(url.length() > length && index > length) {
                    String id = url.substring(length, index);
                    SpecialFieldListActivity.launchActivity(mContext, "", id);
                    return true;
                }
            } else if(url.startsWith("http://www.nejm.outlink")) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(shareContent.outlink);
                intent.setData(content_url);
                startActivity(intent);
                return true;
            }

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
                if(shareContent != null) {
                    mTitle = shareContent.title;
                    mShareContent = shareContent.keyword;
                    cover = shareContent.thumb;
                }

                String url = "https://www.nejmqianyan.cn/article/" + mId;
                switch (v.getId()) {

                    case R.id.view_share_weixin:
                        // 分享到微信
                        // onShare2Weixin();
                        AppUtil.shareToFriend(mContext, mTitle, mShareContent, url,cover, new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                recordShareEvent("微信");
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
                                recordShareEvent("微信朋友圈");
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
                        sendEmail( mTitle, mContent, url);
                        break;
                    case R.id.view_share_weibo:
                        AppUtil.shareToSinaWeibo(mContext, mTitle, mContent, url,cover, new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                recordShareEvent("微博");
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
        ViewGroup mViewEmail = (ViewGroup) view.findViewById(R.id.view_share_email);
        Button mBtnCancel = (Button) view.findViewById(R.id.share_cancel_btn);

        mViewWeixin.setOnClickListener(listener);
        mViewPengyou.setOnClickListener(listener);
        mViewWeibo.setOnClickListener(listener);
        mViewEmail.setOnClickListener(listener);
        mBtnCancel.setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }

    private void recordShareEvent(String channel) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("title", mTitle);
        map.put("channel", channel);

        MobclickAgent.onEvent(mContext.getApplicationContext(), "ID_EVENT_SHARE", map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001){
            checkLogin();
        }
    }

    public  void sendEmail(String title, String content, String emailUrl) {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setType("plain/text");
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, emailUrl);
        //邮件主题
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        //邮件内容
        email.putExtra(android.content.Intent.EXTRA_TEXT, content);

        try {
            Intent intent = Intent.createChooser(email,  "请选择邮件发送内容" );
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showShort(this, "您没有安装邮件客户端");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_STORGE_REQUEST_CODE) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }
}
