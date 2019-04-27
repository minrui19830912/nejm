package com.android.nejm.net;

import android.content.Context;
import android.util.Log;

import com.android.nejm.widgets.LoadingDialog;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by malone on 2017/1/20.
 */

public abstract class StringNetCallback extends StringCallback {
    private Context mContext;
    public  StringNetCallback(Context context){
        super();
        mContext =context;
    }
    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        LoadingDialog.cancelDialogForLoading();
        if(response!=null){
//        ToastUitl.showShort(mContext,response.toString());
            Log.e("minrui","response="+response.toString());
        }else{
//            ToastUitl.showShort(mContext,e.toString());
            Log.e("minrui","Exception="+e.toString());
        }

    }
}
