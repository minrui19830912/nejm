package com.android.nejm.manage;

import android.text.TextUtils;

import com.android.nejm.MyApplication;
import com.android.nejm.data.AccountInfo;
import com.android.nejm.data.LoginBean;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.db.DBManager;
import com.android.nejm.utils.SPUtils;

public class LoginUserManager {
    public String access_token;
    public String client_id;
    public String uid;
    public String roleid;
    public String lastzipid;

    public AccountInfo accountInfo;
    public RoleBean roleBean;
    public RoleInfo roleInfo;

    public long lastDownloadtime;

    public boolean isLogin = false;

    private static LoginUserManager userManager;

    private boolean enablePush = true;

    private LoginUserManager() {
        access_token = SPUtils.getStringPreference("access_token", "");
        client_id = SPUtils.getStringPreference("client_id", "");
        uid = SPUtils.getStringPreference("uid", "");
        roleid = SPUtils.getStringPreference("roleid", "");
        lastzipid = SPUtils.getStringPreference("lastzipid", "0");
        lastDownloadtime = SPUtils.getLongPreference("last_download_time", 0);
        enablePush = SPUtils.getBooleanPreference("enable_push", true);

        MyApplication.mToken = access_token;
        MyApplication.client_id = client_id;
        MyApplication.uid = uid;

        roleInfo = new RoleInfo();
        roleBean = new RoleBean();
        accountInfo = new AccountInfo();

        if(!TextUtils.isEmpty(uid)) {
            isLogin = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBManager.init(MyApplication.getApplication(), uid);
                    AnnouceRecordManager.getInstance().query();
                }
            }).start();
        }
    }

    public static LoginUserManager getInstance() {
        if(userManager == null) {
            userManager = new LoginUserManager();
        }

        return userManager;
    }

    public void quitLogin() {
        access_token = "";
        client_id = "";
        uid = "";
        roleid = "";

        SPUtils.putStringPreference("access_token", access_token);
        SPUtils.putStringPreference("client_id", client_id);
        SPUtils.putStringPreference("uid", uid);
        SPUtils.putStringPreference("roleid", roleid);

        MyApplication.mToken = access_token;
        MyApplication.client_id = client_id;
        MyApplication.uid = uid;

        isLogin = false;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public RoleBean getRoleBean() {
        return roleBean;
    }

    public void setRoleBean(RoleBean roleBean) {
        this.roleBean = roleBean;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    public String getLastzipid() {
        return lastzipid;
    }

    public void setLastzipid(String lastzipid) {
        this.lastzipid = lastzipid;
        SPUtils.putStringPreference("lastzipid", lastzipid);
    }

    public long getLastDownloadtime() {
        return lastDownloadtime;
    }

    public void setLastDownloadtime(long lastDownloadtime) {
        this.lastDownloadtime = lastDownloadtime;
        SPUtils.getLongPreference("last_download_time", lastDownloadtime);
    }

    public boolean isEnablePush() {
        return enablePush;
    }

    public void setEnablePush(boolean enablePush) {
        this.enablePush = enablePush;
        SPUtils.putBooleanPreference("enable_push", enablePush);
    }

    public void login(LoginBean loginBean) {
        access_token = loginBean.access_token;
        client_id = loginBean.client_id;
        uid = loginBean.uid;
        roleid = loginBean.roleid;

        isLogin = true;
        MyApplication.mToken = loginBean.access_token;
        MyApplication.client_id = loginBean.client_id;
        MyApplication.uid = loginBean.uid;

        SPUtils.putStringPreference("access_token", loginBean.access_token);
        SPUtils.putStringPreference("client_id", loginBean.client_id);
        SPUtils.putStringPreference("uid", loginBean.uid);
        SPUtils.putStringPreference("roleid", loginBean.roleid);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager.init(MyApplication.getApplication(), uid);
            }
        }).start();*/
    }

    public void register(LoginBean loginBean) {
        access_token = loginBean.access_token;
        client_id = loginBean.client_id;
        uid = loginBean.uid;

        isLogin = false;

        MyApplication.mToken = loginBean.access_token;
        MyApplication.client_id = loginBean.client_id;
        MyApplication.uid = loginBean.uid;
    }
}
