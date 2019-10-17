package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.nejm.R;
import com.android.nejm.adapter.DownloadArticleAdapter;
import com.android.nejm.bean.DownloadRecord;
import com.android.nejm.db.DownloadRecordManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OfflineArticleListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String id;
    private int page = 1;

    DownloadArticleAdapter articleAdapter;
    List<DownloadRecord> downloadRecordList = new ArrayList<>();

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, OfflineArticleListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_article_list);
        ButterKnife.bind(this);
        setCommonTitle("下载文章列表",true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        articleAdapter = new DownloadArticleAdapter(this);
        articleAdapter.setOnItemLongClickListener(new DownloadArticleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(View view, int index) {
                showPopMenu(view,index);
            }
        });
        recyclerView.setAdapter(articleAdapter);

        loadData(true);
    }

    public void showPopMenu(View view, final int pos){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.delete_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                DownloadRecordManager.delete(downloadRecordList.get(pos));
               Log.e("minrui","count="+ DownloadRecordManager.getRecordCount());
                articleAdapter.removeItem(pos);


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

    private void loadData(boolean showLoadingDialog) {
        if (showLoadingDialog) {
            LoadingDialog.showDialogForLoading(this);
        }

        Observable.create(new ObservableOnSubscribe<List<DownloadRecord>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DownloadRecord>> emitter) throws Exception {
                List<DownloadRecord> list = DownloadRecordManager.query();
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DownloadRecord>>() {
                    @Override
                    public void accept(List<DownloadRecord> downloadRecords) throws Exception {
                        LoadingDialog.cancelDialogForLoading();
                        downloadRecordList.addAll(downloadRecords);
                        articleAdapter.setData(downloadRecordList);
                        articleAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
