package com.android.nejm.db;

import android.content.Context;
import android.os.Looper;

import com.android.nejm.bean.DaoMaster;
import com.android.nejm.bean.DaoSession;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;


public class DBManager {
    private static DaoSession daoSession;

    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, String str) {
        DaoMaster.DevOpenHelper devOpenHelper;
        if (str != null) {
            devOpenHelper = new DaoMaster.DevOpenHelper(context, String.format("nejm_%s.db", str));
        } else {
            devOpenHelper = new DaoMaster.DevOpenHelper(context, "nejm.db");
        }
        daoSession = new DaoMaster(devOpenHelper.getWritableDb()).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static <T> void insert(T t) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            daoSession.insert(t);
        } else {
            Observable.just(t).observeOn(Schedulers.io()).subscribe(new Consumer<T>() {
                public void accept(T t) throws Exception {
                    DBManager.daoSession.insert(t);
                }
            }, new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                }
            });
        }
    }

    public static <T> void update(T t) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            daoSession.update(t);
        } else {
            Observable.just(t).observeOn(Schedulers.io()).subscribe(new Consumer<T>() {
                public void accept(T t) throws Exception {
                    DBManager.daoSession.update(t);
                }
            }, new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                }
            });
        }
    }

    public static <T> void delete(T t) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            daoSession.delete(t);
        } else {
            Observable.just(t).observeOn(Schedulers.io()).subscribe(new Consumer<T>() {
                public void accept(T t) throws Exception {
                    DBManager.daoSession.delete(t);
                }
            }, new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                }
            });
        }
    }

    public static <T> List<T> queryAll(Class<T> cls) {
        return daoSession.queryBuilder(cls).list();
    }
}
