package com.android.nejm.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.BuildConfig;
import com.android.nejm.R;
import com.android.nejm.activitys.EditPersonalInfoActivity;
import com.android.nejm.activitys.FavoriteActivity;
import com.android.nejm.activitys.FeedbackActivity;
import com.android.nejm.activitys.NotificationActivity;
import com.android.nejm.activitys.ReadHistoryActivity;
import com.android.nejm.activitys.SettingActivity;
import com.android.nejm.activitys.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.textViewReadCount)
    TextView textViewReadCount;
    @BindView(R.id.textViewFavoriteCount)
    TextView textViewFavoriteCount;
    @BindView(R.id.textViewDownloadCount)
    TextView textViewDownloadCount;
    @BindView(R.id.textViewNotifyCount)
    TextView textViewNotifyCount;
    @BindView(R.id.textViewVersion)
    TextView textViewVersion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        textViewVersion.setText("V" + BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.textViewEmail)
    public void onClickEmail() {
        SpannableString msg = new SpannableString("您已开启邮件订阅，是否要关闭邮件订阅？");
        msg.setSpan(new ForegroundColorSpan(Color.parseColor("#C92700")), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        new AlertDialog.Builder(getActivity())
                .setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("关闭订阅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    @OnClick(R.id.textViewMicroMsg)
    public void onClickMicroMsg() {
        new AlertDialog.Builder(getActivity())
                .setMessage("立刻前往NEJM医学前沿官方微信。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("立刻前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    @OnClick(R.id.textViewEditPersonalInfo)
    public void onClickEditPersonalInfo() {
        Intent intent = new Intent(getActivity(), EditPersonalInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewFeedback)
    public void onClickFeedback() {
        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewSetting)
    public void onClickSetting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewContactUs)
    public void onClickContactUs() {
        WebViewActivity.launchActivity(getActivity(), "联系我们", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=contactus");
    }

    @OnClick(R.id.readLayout)
    public void onClickReadLayout() {
        startActivity(new Intent(getActivity(), ReadHistoryActivity.class));
    }

    @OnClick(R.id.favoriteLayout)
    public void onClickFavoriteLayout() {
        startActivity(new Intent(getActivity(), FavoriteActivity.class));
    }

    @OnClick(R.id.downloadLayout)
    public void onClickDownloadLayout() {

    }

    @OnClick(R.id.notifyLayout)
    public void onClickNotifyLayout() {
        startActivity(new Intent(getActivity(), NotificationActivity.class));
    }

    @OnClick(R.id.textViewDownload)
    public void onDownload() {

    }
}
