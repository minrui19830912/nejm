package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.DirectoryBean;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
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
    @BindView(R.id.textViewContent)
    TextView textViewContent;

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

        String id = getIntent().getStringExtra("id");
        loadData(id);
    }

    private void loadData(String id) {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.getDirectoryDetails(this, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                directoryBean = new Gson().fromJson(json.toString(), DirectoryBean.class);
                setControllerListener(draweeViewLogo, directoryBean.logo);
                textViewDirName.setText(directoryBean.item.title);
                textViewDirName2.setText(directoryBean.item.stitle);
                textViewDate.setText(directoryBean.item.postdate);
                textViewContent.setText(Html.fromHtml(directoryBean.item.content));
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
