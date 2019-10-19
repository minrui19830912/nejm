package com.android.nejm.activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.android.nejm.R;
import com.android.nejm.adapter.NotifyMessageAdapter;
import com.android.nejm.bean.AnnounceRecord;
import com.android.nejm.data.AnnounceMessage;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    AnnounceMessage announceMessage;
    NotifyMessageAdapter messageAdapter;

    List<AnnounceRecord> recordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        showBack();
        setCommonTitle("通知");
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        messageAdapter = new NotifyMessageAdapter(this, new NotifyMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                AnnounceMessage.MessageItem item = announceMessage.items.get(index);
                NotifyDetailActivity.launchActivity(mContext, item.id);
                item.read = true;
                messageAdapter.notifyItemChanged(index);

                for(AnnounceRecord record : recordList) {
                    if(TextUtils.equals(record.msgId, item.id)) {
                        record.read = true;
                        AnnouceRecordManager.getInstance().update(record);
                        break;
                    }
                }
            }

            @Override
            public void onItemLongClicked(View view,int index) {
                showPopMenu(view,index);
            }
        });

        recyclerView.setAdapter(messageAdapter);

        getData();
    }

    public void showPopMenu(View view, final int pos){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.delete_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                AnnounceMessage.MessageItem msgitem = announceMessage.items.get(pos);
                HttpUtils.delNoticeById(mContext,msgitem.id,null);
                announceMessage.items.remove(pos);
                messageAdapter.notifyDataSetChanged();

                for(AnnounceRecord record : recordList) {
                    if(TextUtils.equals(record.msgId, msgitem.id)) {

                        AnnouceRecordManager.getInstance().delete(record);
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }



    private void getData() {
        LoadingDialog.showDialogForLoading(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                recordList = AnnouceRecordManager.getInstance().getRecordList();
                HttpUtils.getMessageList(mContext, new OnNetResponseListener() {
                    @Override
                    public void onNetDataResponse(JSONObject json) {
                        announceMessage = new Gson().fromJson(json.toString(), AnnounceMessage.class);

                        new AsyncTask<Void,Void,Void>() {

                            @Override
                            protected Void doInBackground(Void... voids) {
                                List<AnnounceRecord> list = new ArrayList<>();
                                for(AnnounceMessage.MessageItem item : announceMessage.items) {
                                    boolean exist = false;
                                    for(AnnounceRecord record : recordList) {
                                        if(TextUtils.equals(item.id, record.msgId)) {
                                            item.read = record.read;
                                            exist = true;
                                            break;
                                        }
                                    }

                                    if(!exist) {
                                        list.add(new AnnounceRecord(null, false, item.id));
                                    }
                                }
                                AnnouceRecordManager.getInstance().insert(list);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                LoadingDialog.cancelDialogForLoading();
                                messageAdapter.setData(announceMessage.items);
                                messageAdapter.notifyDataSetChanged();
                            }
                        }.execute();
                    }
                });
            }
        }).start();
    }
}
