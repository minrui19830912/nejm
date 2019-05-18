package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.editTextInfo)
    EditText editTextInfo;
    @BindView(R.id.editTextPhone)
    EditText editTextPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("意见与反馈");
    }

    @OnClick(R.id.buttonSubmit)
    public void onClickSubmit() {
        String content = editTextInfo.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.feedback(this, content, phone, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                ToastUtil.showShort(mContext, "提交成功");
                finish();
            }
        });
    }
}
