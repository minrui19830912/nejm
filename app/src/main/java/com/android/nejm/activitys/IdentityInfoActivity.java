package com.android.nejm.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.nejm.Fragments.IdentityDoctorFragment;
import com.android.nejm.Fragments.IdentityFragment;
import com.android.nejm.Fragments.IdentityStudentFragment;
import com.android.nejm.Fragments.IdentiyOtherFragment;
import com.android.nejm.Fragments.PersonalInfoDoctorFragment;
import com.android.nejm.Fragments.PersonalInfoOtherFragment;
import com.android.nejm.Fragments.PersonalInfoStudentFragment;
import com.android.nejm.R;
import com.android.nejm.data.RoleBean;
import com.android.nejm.event.DoctorIdentitySelectedEvent;
import com.android.nejm.event.IdentitySelectedEvent;
import com.android.nejm.event.OtherIdentitySelectedEvent;
import com.android.nejm.event.StudentIdentitySelectedEvent;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentityInfoActivity extends BaseActivity {
    @BindView(R.id.radioButtonIdentity)
    RadioButton radioButtonIdentity;
    @BindView(R.id.radioButtonProfession)
    RadioButton radioButtonProfession;
    @BindView(R.id.radioButtonPersonalInfo)
    RadioButton radioButtonPersonalInfo;

    List<Fragment> fragmentList = new ArrayList<>();
    int curFragmentIndex = 0;

    boolean fromLogin = false;

    public static void launchActivity(Context context, boolean fromLogin) {
        Intent intent = new Intent(context, IdentityInfoActivity.class);
        intent.putExtra("fromLogin", fromLogin);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_info);
        showBack();
        ButterKnife.bind(this);

        fromLogin = getIntent().getBooleanExtra("fromLogin", false);

        fragmentList.add(new IdentityFragment());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragmentList.get(0));
        transaction.commitAllowingStateLoss();

        curFragmentIndex = 0;

        radioButtonIdentity.setChecked(true);
        radioButtonProfession.setChecked(false);
        radioButtonPersonalInfo.setChecked(false);
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        FragmentManager  fragmentManager = getSupportFragmentManager();
        Log.e("TAG", "onBackPressed, fragments.size = " + fragmentList.size());

        if(curFragmentIndex > 0) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            for(int i = fragmentList.size() - 1; i >= curFragmentIndex; i--) {
                transaction.remove(fragmentList.get(i));
                fragmentList.remove(i);
            }

            curFragmentIndex--;
            transaction.show(fragmentList.get(curFragmentIndex));
            transaction.commitAllowingStateLoss();

            radioButtonIdentity.setBackgroundResource(R.drawable.step_one);
            radioButtonProfession.setBackgroundResource(R.drawable.step_two);
            radioButtonPersonalInfo.setBackgroundResource(R.drawable.step_three);

            radioButtonIdentity.setChecked(curFragmentIndex == 0);
            radioButtonProfession.setChecked(curFragmentIndex == 1);
            radioButtonPersonalInfo.setChecked(curFragmentIndex == 2);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        if(fromLogin) {

        }

        super.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIdentitySelectedEvent(IdentitySelectedEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        if(TextUtils.equals(event.id, "1")
            || TextUtils.equals(event.id, "2")
            || TextUtils.equals(event.id, "4")) {
            fragment = new IdentityDoctorFragment();
            radioButtonProfession.setText("专业");
        } else if(TextUtils.equals(event.id, "5")) {
            fragment = new IdentityStudentFragment();
            radioButtonProfession.setText("身份");
        } else {
            fragment = new IdentiyOtherFragment();
            radioButtonProfession.setText("身份");
        }

        for(Fragment fragment1 : fragmentList) {
            transaction.hide(fragment1);
        }

        fragmentList.add(fragment);
        curFragmentIndex = 1;

        transaction.add(R.id.content, fragment);
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
        radioButtonProfession.setChecked(true);
        radioButtonIdentity.setBackgroundResource(R.drawable.step_one_after_checked);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoctorIdentitySelectedEvent(DoctorIdentitySelectedEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment fragment1 : fragmentList) {
            transaction.hide(fragment1);
        }

        Fragment fragment = new PersonalInfoDoctorFragment();
        fragmentList.add(fragment);
        curFragmentIndex = 2;

        transaction.add(R.id.content, fragment);
        transaction.commitAllowingStateLoss();

        radioButtonPersonalInfo.setChecked(true);
        radioButtonProfession.setBackgroundResource(R.drawable.step_two_after_checked);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStudentIdentitySelectedEvent(StudentIdentitySelectedEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment fragment1 : fragmentList) {
            transaction.hide(fragment1);
        }

        Fragment fragment = new PersonalInfoStudentFragment();
        fragmentList.add(fragment);
        curFragmentIndex = 2;

        transaction.add(R.id.content, fragment);
        transaction.commitAllowingStateLoss();

        radioButtonPersonalInfo.setChecked(true);
        radioButtonProfession.setBackgroundResource(R.drawable.step_two_after_checked);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOtherIdentitySelectedEvent(OtherIdentitySelectedEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment fragment1 : fragmentList) {
            transaction.hide(fragment1);
        }

        Fragment fragment = new PersonalInfoOtherFragment();
        fragmentList.add(fragment);
        curFragmentIndex = 2;

        transaction.add(R.id.content, fragment);
        transaction.commitAllowingStateLoss();

        radioButtonPersonalInfo.setChecked(true);
        radioButtonProfession.setBackgroundResource(R.drawable.step_two_after_checked);
    }
}
