package com.android.nejm.manage;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.android.nejm.BuildConfig;
import com.android.nejm.constant.Constant;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import java.io.File;

public class UploadManager {
    private static UploadManager uploadManager;
    private String apkFilePath = "/newversion.apk";

    public static UploadManager getInstance() {
        if(uploadManager == null) {
            uploadManager = new UploadManager();
        }

        return uploadManager;
    }

    private UploadManager() {
    }

    public void checkUpdate(Activity context) {
        LoadingDialog.showDialogForLoading(context);
        HttpUtils.updateVersion(context, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();

                JSONObject jsonObject = json.optJSONObject("android");
                String version = jsonObject.optString("ver");
                String update = jsonObject.optString("update");
                String link = jsonObject.optString("link");
                if(BuildConfig.VERSION_NAME.compareToIgnoreCase(version) >= 0) {
                    return;
                }

                if(TextUtils.equals(update, "1")) {
                    new AlertDialog.Builder(context)
                            .setMessage("检测到新版本, 请更新")
                            .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    download(context, link);
                                }
                            })
                            .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Process.killProcess(Process.myPid());
                                }
                            }).create().show();
                } else {
                    new AlertDialog.Builder(context)
                            .setMessage("检测到新版本, 请更新")
                            .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    download(context, link);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
            }
        });
    }

    private void download(Context context, String url) {
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                installApk(context);
            }},
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalFilesDir(context,
                null,
                apkFilePath);
        downloadManager.enqueue(request);
    }

    private void installApk(Context context) {
        File file = new File(context.getExternalFilesDir(null), apkFilePath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(context, Constant.fileProvider, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }

            context.startActivity(intent);
        }
    }
}
