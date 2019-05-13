package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.android.nejm.Fragments.FindPwdByEmailFragment;
import com.android.nejm.Fragments.FindPwdByPhoneFragment;
import com.android.nejm.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class ForgetPasswordActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.radioButtonFindByPhone)
    RadioButton radioButtonFindByPhone;
    @BindView(R.id.radioButtonFindByEmail)
    RadioButton radioButtonFindByEmail;

    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("密码找回");

        radioButtonFindByPhone.setChecked(true);

        fragmentList.add(new FindPwdByPhoneFragment());
        fragmentList.add(new FindPwdByEmailFragment());
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
    }

    @OnCheckedChanged({R.id.radioButtonFindByPhone, R.id.radioButtonFindByEmail})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked) {
            return;
        }

        if(buttonView.getId() == R.id.radioButtonFindByPhone) {
            radioButtonFindByEmail.setChecked(false);
            viewPager.setCurrentItem(0);
        } else {
            radioButtonFindByPhone.setChecked(false);
            viewPager.setCurrentItem(1);
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
