package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.android.nejm.Fragments.HomeFragment;
import com.android.nejm.R;

public class MainActivity extends BaseActivity {
    private HomeFragment mHomeFragment;
    private int mIndex = -1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(0);
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(0);
    }

    private void showFragment(int index) {

        mIndex = index;
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        switch (index) {
            case 0:
//                if (mMainFragment == null) {
//                    mMainFragment = new MainFragment();
//                    trans.add(R.id.content, mMainFragment);
//                } else {
//                    trans.show(mMainFragment);
//                }
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    trans.add(R.id.content, mHomeFragment);
                } else {
                    trans.show(mHomeFragment);
                }
                break;
//            case 1:
//                if (mStoreFragment == null) {
//                    mStoreFragment = new StoreFragment();
//                    trans.add(R.id.content, mStoreFragment);
//                } else {
//                    trans.show(mStoreFragment);
//                }
//                break;
//            case 2:
//                if (mFindFragment == null) {
//                    mFindFragment = new FindFragment();
//                    trans.add(R.id.content, mFindFragment);
//                } else {
//                    trans.show(mFindFragment);
//                }
//                break;
//            case 3:
//
//                if (mShopcarFragment == null) {
//                    mShopcarFragment = new ShopcarFragment();
//                    trans.add(R.id.content, mShopcarFragment);
//                } else {
//                    trans.show(mShopcarFragment);
//                }
//                break;
//            case 4:
//
//                if (mMyFragment == null) {
//                    mMyFragment = new MyFragment();
//                    trans.add(R.id.content, mMyFragment);
//                } else {
//                    trans.show(mMyFragment);
//                }
//                break;
        }

        trans.commitAllowingStateLoss();
    }

    private void hideAllFragment() {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//        if (mMainFragment != null) {
//            trans.hide(mMainFragment);
//        }
        if (mHomeFragment != null) {
            trans.hide(mHomeFragment);
        }
//        if (mStoreFragment != null) {
//            trans.hide(mStoreFragment);
//        }
//        if (mFindFragment != null) {
//            trans.hide(mFindFragment);
//        }
//        if (mShopcarFragment != null) {
//            trans.hide(mShopcarFragment);
//        }
//        if (mMyFragment != null) {
//            trans.hide(mMyFragment);
//        }

        trans.commitAllowingStateLoss();
    }

}
