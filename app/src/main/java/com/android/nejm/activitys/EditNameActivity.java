package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.nejm.R;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditNameActivity extends BaseActivity {
    @BindView(R.id.editTextName)
    EditText editTextName;

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, EditNameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        showBack();
        setCommonTitle("编辑姓名");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonConfirm)
    public void onClickConfirm() {
        String name = editTextName.getText().toString();
        if(TextUtils.isEmpty(name)) {
            ToastUtil.showShort(this, "姓名不能为空");
            return;
        }
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.editName(this, name, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                ToastUtil.showShort(EditNameActivity.this, "编辑姓名成功");
                LoginUserManager.getInstance().accountInfo.truename = name;
                finish();
            }
        });
    }
}
