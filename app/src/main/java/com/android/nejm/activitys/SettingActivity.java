package com.android.nejm.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.nejm.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        showBack();
    }

    @OnClick(R.id.textViewAbout)
    public void onClickAbout() {
        WebViewActivity.launchActivity(this, "关于我们", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=aboutus");
    }

    @OnClick(R.id.textViewUsage)
    public void onClickUsage() {
        //WebViewActivity.launchActivity(this, "");
    }

    @OnClick(R.id.textViewService)
    public void onClickService() {
        WebViewActivity.launchActivity(this, "服务条款", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=terms");
    }

    @OnClick(R.id.textViewPrivacy)
    public void onClickPrivacy() {
        WebViewActivity.launchActivity(this, "隐私政策", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=privacy");
    }

    @OnClick(R.id.textViewVersion)
    public void onClickVersion() {

    }

    @OnClick(R.id.textViewClean)
    public void onClickClean() {

    }

    @OnClick(R.id.textViewPush)
    public void onClickPush() {

    }

    @OnClick(R.id.textViewQuit)
    public void onClickQuit() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
