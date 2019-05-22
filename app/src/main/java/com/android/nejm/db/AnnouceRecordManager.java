package com.android.nejm.db;

import com.android.nejm.bean.AnnounceRecord;
import com.android.nejm.bean.DownloadRecord;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class AnnouceRecordManager {
    public List<AnnounceRecord> query() {
        QueryBuilder<AnnounceRecord> queryBuilder = DBManager.getDaoSession().queryBuilder(AnnounceRecord.class);
        return queryBuilder.list();
    }

    public void insert(AnnounceRecord record) {
        DBManager.insert(record);
    }

    public void update(AnnounceRecord record) {
        DBManager.update(record);
    }
}
