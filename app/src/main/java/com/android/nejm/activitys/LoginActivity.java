package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.nejm.MyApplication;
import com.android.nejm.R;
import com.android.nejm.bean.AnnounceRecord;
import com.android.nejm.data.AnnounceMessage;
import com.android.nejm.data.LoginBean;
import com.android.nejm.db.AnnouceRecordManager;
import com.android.nejm.db.DBManager;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.textViewShowPwd)
    TextView textViewShowPwd;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    private boolean justFinish = false;

    public static void launchActivity(Context context, boolean just_finish) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("just_finish", just_finish);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        justFinish =  getIntent().getBooleanExtra("just_finish",false);
    }

    @OnClick(R.id.textViewOr)
    public void onClickRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewForgetPwd)
    public void onClickForgetPwd() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewShowPwd)
    public void onClickShowPwd() {
        if(editTextPassword.getInputType() == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editTextPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_CLASS_TEXT);
            textViewShowPwd.setText("显示密码");
        } else {
            editTextPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            textViewShowPwd.setText("隐藏密码");
        }

        editTextPassword.setText(editTextPassword.getText());
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    @OnClick(R.id.textViewLogin)
    public void onClickLogin() {
        String name = editTextName.getText().toString().trim();
        String pwd = editTextPassword.getText().toString().trim();
        //name = "13912345678";
        //pwd = "123456";
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            ToastUtil.showShort(this, "用户名或密码不能为空");
            return;
        }
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.loginSystem(this, name, pwd, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoginBean loginBean = new Gson().fromJson(json.toString(), LoginBean.class);
                LoginUserManager.getInstance().login(loginBean);
                updateAnnouceRecord();
                new AsyncTask<Void,Void,Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        DBManager.init(MyApplication.getApplication(), LoginUserManager.getInstance().uid);
                        AnnouceRecordManager.getInstance().query();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        LoadingDialog.cancelDialogForLoading();
                        if(justFinish ){
                            finish();
                        } else {
                            startActivity(new Intent(mContext,MainActivity.class));
                            finish();
                        }
                    }
                }.execute();
            }

            @Override
            public void onNetFailResponse(Context context, String msg, String msgCode) {
               // super.onNetFailResponse(context, msg, msgCode);
                LoadingDialog.cancelDialogForLoading();
                ToastUtil.showShort(mContext,msg);
            }
        });
    }

    private void updateAnnouceRecord() {
        List<AnnounceRecord> recordList = AnnouceRecordManager.getInstance().getRecordList();
        HttpUtils.getMessageList(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                AnnounceMessage announceMessage = new Gson().fromJson(json.toString(), AnnounceMessage.class);

                new AsyncTask<Void,Void,Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        List<AnnounceRecord> list = new ArrayList<>();
                        for(AnnounceMessage.MessageItem item : announceMessage.items) {
                            boolean exist = false;
                            for(AnnounceRecord record : recordList) {
                                if(TextUtils.equals(item.id, record.msgId)) {
                                    item.read = record.read;
                                    exist = true;
                                    break;
                                }
                            }

                            if(!exist) {
                                list.add(new AnnounceRecord(null, false, item.id));
                            }
                        }
                        AnnouceRecordManager.getInstance().insert(list);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {

                    }
                }.execute();
            }
        });
    }

    @OnClick(R.id.textViewSkip)
    public void onClickSkip() {
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }
}
