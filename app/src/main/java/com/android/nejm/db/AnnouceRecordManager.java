package com.android.nejm.db;

import android.text.TextUtils;

import com.android.nejm.bean.AnnounceRecord;
import com.android.nejm.bean.AnnounceRecordDao;
import com.android.nejm.bean.DaoSession;
import com.android.nejm.event.AnnouceRecordUpdatedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class AnnouceRecordManager {
    private static AnnouceRecordManager sAnnouceRecordManager;
    private boolean isReady = false;
    private List<AnnounceRecord> recordList;

    public static AnnouceRecordManager getInstance() {
        if(sAnnouceRecordManager == null) {
            sAnnouceRecordManager = new AnnouceRecordManager();
        }

        return sAnnouceRecordManager;
    }

    private AnnouceRecordManager() {
        recordList = new ArrayList<>();
    }

    public List<AnnounceRecord> getRecordList() {
        return recordList;
    }

    public void query() {
        if(!isReady) {
            QueryBuilder<AnnounceRecord> queryBuilder = DBManager.getDaoSession().queryBuilder(AnnounceRecord.class);
            recordList = queryBuilder.list();
            isReady = true;
            EventBus.getDefault().postSticky(new AnnouceRecordUpdatedEvent(hasUnread()));
        }
    }

    public boolean hasUnread() {
        if(isReady) {
            for(AnnounceRecord record : recordList) {
                if(!record.read) {
                    return true;
                }
            }
        }

        return false;
    }

    public void insert(AnnounceRecord record) {
        for(AnnounceRecord item : recordList) {
            if(TextUtils.equals(item.msgId, record.msgId)) {
                return;
            }
        }

        recordList.add(record);
        DBManager.insert(record);
        EventBus.getDefault().postSticky(new AnnouceRecordUpdatedEvent(hasUnread()));
    }

    public void update(AnnounceRecord record) {
        for(AnnounceRecord item : recordList) {
            if(TextUtils.equals(item.msgId, record.msgId)) {
                item.read = record.read;
                DBManager.update(record);
                EventBus.getDefault().postSticky(new AnnouceRecordUpdatedEvent(hasUnread()));
                return;
            }
        }
    }

    public void insert(List<AnnounceRecord> records) {
        DaoSession daoSession = DBManager.getDaoSession();
        AnnounceRecordDao recordDao = daoSession.getAnnounceRecordDao();
        recordDao.insertOrReplaceInTx(records);
        isReady = false;
        query();
        EventBus.getDefault().postSticky(new AnnouceRecordUpdatedEvent(hasUnread()));
    }
}
