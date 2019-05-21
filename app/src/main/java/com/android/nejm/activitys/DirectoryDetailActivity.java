package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.nejm.R;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

public class DirectoryDetailActivity extends AppCompatActivity {

    public static void launchActivity(Context context, String id) {
        Intent intent = new Intent(context, DirectoryDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_detail);

        String id = getIntent().getStringExtra("id");
        loadData(id);
    }

    private void loadData(String id) {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.getDirectoryDetails(this, id, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {

            }
        });
    }
}
