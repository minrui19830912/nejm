package com.android.nejm.manage;

import android.content.Context;

import com.android.nejm.MyApplication;

import cn.jpush.android.api.JPushInterface;

public class PushManager {
    private static PushManager pushManager;
    private boolean isPushInited = false;

    public static PushManager getInstance() {
        if(pushManager == null) {
            pushManager = new PushManager();
        }

        return pushManager;
    }

    public void init() {
        if(!isPushInited && LoginUserManager.getInstance().isEnablePush()) {
            isPushInited = true;
            JPushInterface.setDebugMode(true);
            JPushInterface.init(MyApplication.getApplication());
            //String rid = JPushInterface.getRegistrationID(MyApplication.getApplication());
        }
    }

    public void openPush() {
        if(!isPushInited) {
            init();
        } else {
            JPushInterface.resumePush(MyApplication.getApplication());
        }

        LoginUserManager.getInstance().setEnablePush(true);
    }

    public void closePush() {
        if(isPushInited) {
            JPushInterface.stopPush(MyApplication.getApplication());
        }

        LoginUserManager.getInstance().setEnablePush(false);
    }
}
