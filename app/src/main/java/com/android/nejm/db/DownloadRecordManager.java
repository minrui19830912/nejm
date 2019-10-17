package com.android.nejm.db;

import com.android.nejm.bean.DownloadRecord;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DownloadRecordManager {
    public static List<DownloadRecord> query() {
        QueryBuilder<DownloadRecord> queryBuilder = DBManager.getDaoSession().queryBuilder(DownloadRecord.class);
        return queryBuilder.list();
    }

    public static void deleteAll() {
        DBManager.deleteAll(DownloadRecord.class);
    }

    public static long getRecordCount() {
        QueryBuilder<DownloadRecord> queryBuilder = DBManager.getDaoSession().queryBuilder(DownloadRecord.class);
        return queryBuilder.count();
    }

    public static void insert(DownloadRecord record) {
        DBManager.insert(record);
    }
    public static void delete(DownloadRecord record) {
        DBManager.delete(record);
    }

    public static void update(DownloadRecord record) {
        DBManager.update(record);
    }

    public static boolean hasDownLoad(String articleId){
        QueryBuilder<DownloadRecord> queryBuilder = DBManager.getDaoSession().queryBuilder(DownloadRecord.class);
        List<DownloadRecord> list = queryBuilder.list();
        for(DownloadRecord record:list){
            if(record.articleId.equals(articleId)){
                return true;
            }
        }
        return false ;
    }
}
