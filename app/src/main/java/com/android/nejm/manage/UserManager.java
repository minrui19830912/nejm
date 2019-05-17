package com.android.nejm.manage;

import com.android.nejm.data.LoginBean;
import com.android.nejm.utils.SPUtils;

public class UserManager {
    public String access_token;
    public String client_id;
    public String uid;
    public String roleid;

    private static UserManager userManager;

    private UserManager() {
        access_token = SPUtils.getStringPreference("access_token", "");
        client_id = SPUtils.getStringPreference("client_id", "");
        uid = SPUtils.getStringPreference("uid", "");
        roleid = SPUtils.getStringPreference("roleid", "");
    }

    public static UserManager getInstance() {
        if(userManager == null) {
            userManager = new UserManager();
        }

        return userManager;
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

    public void save(LoginBean loginBean) {
        access_token = loginBean.access_token;
        client_id = loginBean.client_id;
        uid = loginBean.uid;
        roleid = loginBean.roleid;

        SPUtils.putStringPreference("access_token", loginBean.access_token);
        SPUtils.putStringPreference("client_id", loginBean.client_id);
        SPUtils.putStringPreference("uid", loginBean.uid);
        SPUtils.putStringPreference("roleid", loginBean.roleid);
    }
}
