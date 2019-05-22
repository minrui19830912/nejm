package com.android.nejm.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.android.nejm.bean.DownloadRecord;
import com.android.nejm.db.DBManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class MyDownloadManager {
    private static final String TAG = "MyDownloadManager";

    public static void download(Context context, List<String> urlList, List<String> filePathList, DownloadCompleteListener listener) {
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        int index = 0;
        context.registerReceiver(new DownloadCompleteReceiver(urlList.size(), listener),
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        for(String url : urlList) {
            String filePath = filePathList.get(index++);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDestinationInExternalFilesDir(context,
                    null,
                    filePath);
            downloadManager.enqueue(request);

            //DownloadRecord record = new DownloadRecord();
        }
    }

    public static class DownloadCompleteReceiver extends BroadcastReceiver {
        private int fileCount;
        private int downloadCount;
        private DownloadCompleteListener completeListener;

        DownloadCompleteReceiver(int fileCount, DownloadCompleteListener listener) {
            this.fileCount = fileCount;
            this.completeListener = listener;
            downloadCount = 0;

            Log.e("TAG", "fileCount = " + fileCount);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(!action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                return;
            }

            downloadCount++;
            Log.e("TAG", "-----------------------ACTION_DOWNLOAD_COMPLETE, downloadCount = " + downloadCount);
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
            Log.e("TAG", "uri = " + uri.toString());

            if(downloadCount == fileCount && completeListener != null) {
                completeListener.downloadComplete();
                context.unregisterReceiver(this);
            }
        }
    }

    public interface DownloadCompleteListener {
        void downloadComplete();
    }

    public static String httpGetWebContext(Context context, String url) {
        String ret = "";
        HttpURLConnection conn = null;
        URL currUrl;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        try {
            currUrl = new URL(url);
        } catch (MalformedURLException e) {
            return ret;
        }
        Log.d(TAG, "currUrl : " + currUrl);
        try {
            // 判断是 http 请求还是 https 请求
            if (currUrl.getProtocol().toLowerCase().equals("https")) {
                // Don't trust all SSL, it might cause some security issue.
                // HTTPSTrustManager.allowAllSSL();
                conn = (HttpsURLConnection) currUrl.openConnection();
                Log.d(TAG, "use HTTPS protocol");
            } else {
                conn = (HttpURLConnection) currUrl.openConnection();
            }
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);

            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                br = new BufferedReader(inputStreamReader);
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                ret = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}
