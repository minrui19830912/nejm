package com.android.nejm.activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;

import com.android.nejm.R;
import com.android.nejm.adapter.DownloadArticleAdapter;
import com.android.nejm.db.DownloadRecordManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.manage.PushManager;
import com.android.nejm.utils.FileUtils1;
import com.android.nejm.widgets.LoadingDialog;

import org.apache.commons.io.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.switchButton)
    SwitchCompat switchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("设置");

        if(LoginUserManager.getInstance().isEnablePush()) {
            switchButton.setChecked(true);
        } else {
            switchButton.setChecked(false);
        }

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    PushManager.getInstance().openPush();
                } else {
                    PushManager.getInstance().closePush();
                }
            }
        });
    }

    @OnClick(R.id.textViewAbout)
    public void onClickAbout() {
        WebViewActivity.launchActivity(this, "关于我们", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=aboutus");
    }

    @OnClick(R.id.textViewUsage)
    public void onClickUsage() {
        WebViewActivity.launchActivity(this, "使用说明", "https://dev.nejmqianyan.com/index.php?c=singlepage&m=instructions");
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
        WebViewActivity.launchActivity(this, "版权说明", "https://dev.nejmqianyan.com/index.php?c=singlepage&m=copyright");
    }

    @OnClick(R.id.textViewClean)
    public void onClickClean() {
        LoadingDialog.showDialogForLoading(this);
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                LoadingDialog.cancelDialogForLoading();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    File file = new File(getExternalFilesDir(null), "/html");
                    FileUtils.deleteDirectory(file);
                } catch (Exception e) {

                }

                DownloadRecordManager.deleteAll();
                LoginUserManager.getInstance().setLastzipid("0");
                LoginUserManager.getInstance().setLastDownloadtime(0);

                copyAssetsFiles();

                return null;
            }
        }.execute();
    }

    private void copyAssetsFiles() {
        File filePath = new File(getExternalFilesDir(""), "/html");
        FileUtils1.getInstance(this).copyAssetsToSD("", filePath.getAbsolutePath());
    }

    @OnClick(R.id.textViewPush)
    public void onClickPush() {

    }

    @OnClick(R.id.textViewQuit)
    public void onClickQuit() {
        LoginUserManager.getInstance().quitLogin();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
