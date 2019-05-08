package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.nejm.Fragments.IdentityDoctorFragment;
import com.android.nejm.Fragments.IdentityFragment;
import com.android.nejm.Fragments.IdentityStudentFragment;
import com.android.nejm.Fragments.IdentiyOtherFragment;
import com.android.nejm.Fragments.PersonalInfoOtherFragment;
import com.android.nejm.Fragments.PersonalInfoStudentFragment;
import com.android.nejm.R;
import com.android.nejm.event.IdentitySelectedEvent;
import com.android.nejm.event.OtherIdentitySelectedEvent;
import com.android.nejm.event.StudentIdentitySelectedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentityInfoActivity extends BaseActivity {
    @BindView(R.id.radioButtonIdentity)
    RadioButton radioButtonIdentity;
    @BindView(R.id.radioButtonProfession)
    RadioButton radioButtonProfession;
    @BindView(R.id.radioButtonPersonalInfo)
    RadioButton radioButtonPersonalInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_info);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, new IdentityFragment());
        transaction.commitAllowingStateLoss();

        radioButtonIdentity.setChecked(true);
        radioButtonProfession.setChecked(false);
        radioButtonPersonalInfo.setChecked(false);
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
        switch (event.id) {
            case R.id.radioButtonDoctor:
            case R.id.radioButtonResident:
            case R.id.radioButtonOtherDoctor:
                transaction.replace(R.id.content, new IdentityDoctorFragment());
                break;
            case R.id.radioButtonStudent:
                transaction.replace(R.id.content, new IdentityStudentFragment());
                break;
            case R.id.radioButtonOther:
                transaction.replace(R.id.content, new IdentiyOtherFragment());
                break;
        }

        transaction.commitAllowingStateLoss();
        radioButtonProfession.setChecked(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStudentIdentitySelectedEvent(StudentIdentitySelectedEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new PersonalInfoStudentFragment());
        transaction.commitAllowingStateLoss();

        radioButtonPersonalInfo.setChecked(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOtherIdentitySelectedEvent(OtherIdentitySelectedEvent event) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new PersonalInfoOtherFragment());
        transaction.commitAllowingStateLoss();

        radioButtonPersonalInfo.setChecked(true);
    }
}
