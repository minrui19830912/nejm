package com.android.nejm.activitys;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.nejm.R;
import com.android.nejm.data.VideoDetail;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.AppUtil;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class VideoDetailActivity extends BaseActivity {
    @BindView(R.id.draweeViewLogo)
    SimpleDraweeView draweeViewLogo;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewEnglishTitle)
    TextView textViewEnglishTitle;
    @BindView(R.id.textViewAuthor)
    TextView textViewAuthor;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewField)
    TextView textViewField;
    @BindView(R.id.layoutRelatedArticle)
    ConstraintLayout layoutRelatedArticle;
    @BindView(R.id.textViewRelatedArticleTitle)
    TextView textViewRelatedArticleTitle;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.draweeViewVideoCover)
    SimpleDraweeView draweeViewVideoCover;
    @BindView(R.id.textViewContent)
    TextView textViewContent;
    @BindView(R.id.textViewUnlogin)
    TextView textViewUnlogin;
    @BindView(R.id.bottomLayout)
    ConstraintLayout bottomLayout;
    @BindView(R.id.textViewFavorite)
    TextView textViewFavorite;

    private String id;
    private VideoDetail videoDetail;

    public static void launchActivity(Context context, String id) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        showBack();
        setCommonTitle("NEJM医学前沿");
        ButterKnife.bind(this);

        id = getIntent().getStringExtra("id");
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(LoginUserManager.getInstance().isLogin()) {
            textViewUnlogin.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.VISIBLE);
        } else {
            textViewUnlogin.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.textViewEnglishTitle)
    public void onClickOutLink() {
        if(videoDetail.item != null && !TextUtils.isEmpty(videoDetail.item.outlink)) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(videoDetail.item.outlink);
            target.setData(uri);
            Intent intent = Intent.createChooser(target, "选择浏览器");
            startActivity(intent);
        }
    }

    @OnClick(R.id.draweeViewVideoCover)
    public void onClickVideoCover() {
        if(LoginUserManager.getInstance().isLogin()) {
            draweeViewVideoCover.setVisibility(ViewGroup.GONE);
            videoView.setVideoPath(videoDetail.item.video_url);
            videoView.requestFocus();
            videoView.start();
        } else {
            ToastUtil.showShort(this, "请登录");
        }
    }

    @OnClick(R.id.textViewUnlogin)
    public void onClickUnLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewFavorite)
    public void onClickFavorite() {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.saveArticle(mContext, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                if(TextUtils.equals(videoDetail.isfav, "1")) {
                    ToastUtil.showShort(mContext,"已取消收藏");
                    videoDetail.isfav = "0";
                    textViewFavorite.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_collect_normal,0,0,0);
                    textViewFavorite.setTextColor(getResources().getColor(R.color.color_444));
                } else {
                    ToastUtil.showShort(mContext,"收藏成功");
                    videoDetail.isfav = "1";
                    textViewFavorite.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_collect_selected,0,0,0);
                    textViewFavorite.setTextColor(getResources().getColor(R.color.color_c92700));
                }
            }
        });
    }

    @OnClick(R.id.textViewShare)
    public void onClickShare() {
        showShareDialog();
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
                String mShareContent="NEJM医学前沿";
                String mTitle = AppUtil.getTextContent(textViewTitle);
                String mContent = AppUtil.getTextContent(textViewContent);
                String cover = videoDetail.item.thumb;
                String url = "https://www.nejmqianyan.cn/article/" + id;

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
                        sendEmail( mTitle, mContent, url);
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

    private void getData() {
        LoadingDialog.cancelDialogForLoading();
        HttpUtils.getVideoDetails(this, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                videoDetail = new Gson().fromJson(json.toString(), VideoDetail.class);
                setControllerListener(draweeViewLogo, videoDetail.logo);
                textViewTitle.setText(videoDetail.item.title);
                textViewAuthor.setText(videoDetail.item.author);
                textViewDate.setText(videoDetail.item.postdate);
                textViewEnglishTitle.setText(videoDetail.item.outtitle);
                draweeViewVideoCover.setImageURI(videoDetail.item.thumb);
                textViewContent.setText(Html.fromHtml(videoDetail.item.content));
                if(TextUtils.equals(videoDetail.isfav, "1")) {
                    textViewFavorite.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_collect_selected,0,0,0);
                    textViewFavorite.setTextColor(getResources().getColor(R.color.color_c92700));
                } else {
                    textViewFavorite.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_collect_normal,0,0,0);
                    textViewFavorite.setTextColor(getResources().getColor(R.color.color_444));
                }
            }
        });
    }

    public static void setControllerListener(final SimpleDraweeView simpleDraweeView, String imagePath) {
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)simpleDraweeView.getLayoutParams();
        BaseControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int imageWidth = simpleDraweeView.getWidth();
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = imageWidth;
                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                simpleDraweeView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                Log.d("TAG", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(Uri.parse(imagePath)).build();
        simpleDraweeView.setController(controller);
    }
}
