package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.AccountInfo;
import com.android.nejm.event.FinishModifyPhoneStep1Event;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPhoneStep1Activity extends BaseActivity {
    @BindView(R.id.editTextOldPhone)
    EditText editTextOldPhone;
    @BindView(R.id.editTextVerifyCode)
    EditText editTextVerifyCode;
    @BindView(R.id.textViewSendVerifyCode)
    TextView textViewSendVerifyCode;

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, ModifyPhoneStep1Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_step1);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("修改手机号码");

        AccountInfo accountInfo = LoginUserManager.getInstance().getAccountInfo();
        if(accountInfo != null && !TextUtils.isEmpty(accountInfo.mobile)) {
            editTextOldPhone.setText(accountInfo.mobile);
            editTextOldPhone.setEnabled(false);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.textViewSendVerifyCode)
    public void onClickSendVerifyCode() {
        String mobile = editTextOldPhone.getText().toString().trim();
        if(TextUtils.isEmpty(mobile)) {
            return;
        }

        if (count == 0) {
            count = 60;
            textViewSendVerifyCode.setText("60");
            mHandler.sendEmptyMessageDelayed(0, 1000);
            HttpUtils.sendMobileVerifyCode(this, mobile, new OnNetResponseListener() {
                @Override
                public void onNetDataResponse(JSONObject json) {
                    ToastUtil.showShort(mContext, "发送手机验证码成功");
                }
            });
        }
    }

    private int count;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (count > 0) {
                    count--;
                    textViewSendVerifyCode.setText("" + count);
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    textViewSendVerifyCode.setText("发送验证码");
                }
            }
        }
    };

    @OnClick(R.id.buttonNext)
    public void onClickNext() {
        String mobile = editTextOldPhone.getText().toString().trim();
        String verifyCode = editTextVerifyCode.getText().toString().trim();
        /*if(TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showShort(mContext, "验证码不能为空");
            return;
        }*/

        ModifyPhoneStep2Activity.launchActivity(this, mobile, verifyCode);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishModifyPhoneStep1Event(FinishModifyPhoneStep1Event event) {
        finish();
    }
}
