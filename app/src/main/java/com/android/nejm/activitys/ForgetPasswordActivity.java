package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.android.nejm.Fragments.FindPwdByEmailFragment;
import com.android.nejm.Fragments.FindPwdByPhoneFragment;
import com.android.nejm.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        showBack();
        setCommonTitle("密码找回");

        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.radioButtonFindByPhone);

        fragmentList.add(new FindPwdByPhoneFragment());
        fragmentList.add(new FindPwdByEmailFragment());
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(R.id.radioButtonFindByPhone == checkedId) {
            viewPager.setCurrentItem(0);
        } else {
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
