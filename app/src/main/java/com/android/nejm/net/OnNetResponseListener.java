package com.android.nejm.net;

import android.content.Context;
import android.content.Intent;

import com.android.nejm.MyApplication;
import com.android.nejm.activitys.LoginActivity;
import com.android.nejm.utils.AppManager;
import com.android.nejm.utils.SPUtils;
import com.android.nejm.utils.ToastUitl;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

/**
 * Created by malone on 2017/1/20.
 */

public abstract class OnNetResponseListener {
    public abstract  void onNetDataResponse(JSONObject json);
    public  void onNetFailResponse(Context context, String msg, String msgCode){
        LoadingDialog.cancelDialogForLoading();
        if (msgCode.equals("00007") || msgCode.equals("00008") || msgCode.equals( "00009")){//如果需要重新登录
            Intent intentLogin = new Intent(context, LoginActivity.class);
            intentLogin.putExtra("reLogin",true);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentLogin);
            MyApplication.mToken="";
            MyApplication.client_id="";
            SPUtils.setSharedStringData(context,"access_token", MyApplication.mToken);
            SPUtils.setSharedStringData(context,"client_id", MyApplication.client_id);
            SPUtils.setSharedBooleanData(context,"hasPhone",false);
            AppManager.getAppManager().finishAllActivity();
        }else {
            ToastUitl.showShort(context, msg);
        }
    }
    public  void onNetErrorResponse(Context context, String error){
        LoadingDialog.cancelDialogForLoading();
//        ToastUitl.showShort(context,error);
    }
}
