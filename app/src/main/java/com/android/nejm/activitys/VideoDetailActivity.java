package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.nejm.R;
import com.android.nejm.data.VideoDetail;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

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

    @OnClick(R.id.textViewEnglishTitle)
    public void onClickOutLink() {
        /*if(videoDetail.item != null && !TextUtils.isEmpty(videoDetail.item.outlink)) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(videoDetail.item.outlink);
            target.setData(uri);
            Intent intent = Intent.createChooser(target, "选择浏览器");
            startActivity(intent);
        }*/
        onClickVideo();
    }

    @OnClick(R.id.videoView)
    public void onClickVideo() {
        videoView.setVideoPath(videoDetail.item.video_url);
        videoView.requestFocus();
        videoView.start();
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
