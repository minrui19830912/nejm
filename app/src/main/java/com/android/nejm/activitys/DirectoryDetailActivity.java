package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.DirectoryBean;
import com.android.nejm.net.Constant;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectoryDetailActivity extends BaseActivity {
    @BindView(R.id.draweeViewLogo)
    SimpleDraweeView draweeViewLogo;
    @BindView(R.id.textViewDirName)
    TextView textViewDirName;
    @BindView(R.id.textViewDirName2)
    TextView textViewDirName2;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
//    @BindView(R.id.textViewContent)
//    TextView textViewContent;
    @BindView(R.id.webview)
    WebView mWebView;

    DirectoryBean directoryBean;

    public static void launchActivity(Context context, String id) {
        Intent intent = new Intent(context, DirectoryDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_detail);
        showBack();
        setCommonTitle("NEJM医学前沿");
        ButterKnife.bind(this);
        initWebView();
        String id = getIntent().getStringExtra("id");
        loadData(id);
    }
    private void initWebView() {

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
    private void loadData(String id) {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.getDirectoryDetails(this, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                directoryBean = new Gson().fromJson(json.toString(), DirectoryBean.class);
//                setControllerListener(draweeViewLogo, directoryBean.logo);
                draweeViewLogo.setImageURI(directoryBean.logo);
                textViewDirName.setText(directoryBean.item.title);
                textViewDirName2.setText(directoryBean.item.stitle);
                textViewDate.setText(directoryBean.item.postdate);
//                textViewContent.setText(Html.fromHtml(directoryBean.item.keyword));
                mWebView.loadDataWithBaseURL(HttpUtils.BASE_URL, Constant.WEBVIEW_START + directoryBean.item.keyword + Constant.WEBVIEW_END, "text/html",  "utf-8", null);
            }
        });
    }

//    public static void setControllerListener(final SimpleDraweeView simpleDraweeView, String imagePath) {
//        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)simpleDraweeView.getLayoutParams();
//        BaseControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
//            @Override
//            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
//                if (imageInfo == null) {
//                    return;
//                }
//                int imageWidth = simpleDraweeView.getWidth();
//                int height = imageInfo.getHeight();
//                int width = imageInfo.getWidth();
//                layoutParams.width = imageWidth;
//                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
//                simpleDraweeView.setLayoutParams(layoutParams);
//            }
//
//            @Override
//            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
//                Log.d("TAG", "Intermediate image received");
//            }
//
//            @Override
//            public void onFailure(String id, Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        };
//        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(Uri.parse(imagePath)).build();
//        simpleDraweeView.setController(controller);
//    }


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
//                    String id = url.substring(length, index);
//                    SpecialFieldListActivity.launchActivity(mContext, "", id);
                    Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                    return true;
                }
            }

//            else if(url.startsWith("http://www.nejm.outlink")) {
//                Intent intent= new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(shareContent.outlink);
//                intent.setData(content_url);
//                startActivity(intent);
//                return true;
//            }

            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }

    }
}
