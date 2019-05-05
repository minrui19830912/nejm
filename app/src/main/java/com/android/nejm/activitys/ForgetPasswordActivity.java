package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.android.nejm.Fragments.FindPwdByEmailFragment;
import com.android.nejm.Fragments.FindPwdByPhoneFragment;
import com.android.nejm.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        fragmentList.add(new FindPwdByPhoneFragment());
        fragmentList.add(new FindPwdByEmailFragment());
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
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
