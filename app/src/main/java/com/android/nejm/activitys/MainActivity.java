package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.android.nejm.Fragments.HomeFragment;
import com.android.nejm.Fragments.NewKnowledgeFragment;
import com.android.nejm.R;
import com.android.nejm.utils.AppUtil;
import com.android.nejm.utils.ToastUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class MainActivity extends BaseActivity {
    private HomeFragment mHomeFragment;
    private NewKnowledgeFragment mNewKnowledgeFragment;
    private int mIndex = -1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hideAllFragment();
                    showFragment(0);
                    return true;
                case R.id.navigation_my_knowledge:
                    hideAllFragment();
                    showFragment(1);
                    AppUtil.shareToFriend(mContext, "abc", "fdasfd", "www.baidu.com", new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                            ToastUtil.showShort(mContext,"onComplete");
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                            ToastUtil.showShort(mContext,"onError");
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {
                            ToastUtil.showShort(mContext,"onCancel");
                        }
                    });
                    return true;
                case R.id.navigation_papers:
                    hideAllFragment();
                    showFragment(2);
                    return true;
                case R.id.navigation_videos:
                    hideAllFragment();
                    showFragment(3);
                    return true;
                case R.id.navigation_my:
                    hideAllFragment();
                    showFragment(4);
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
            case 1:
                if (mNewKnowledgeFragment == null) {
                    mNewKnowledgeFragment = new NewKnowledgeFragment();
                    trans.add(R.id.content, mNewKnowledgeFragment);
                } else {
                    trans.show(mNewKnowledgeFragment);
                }
                break;
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
        if (mNewKnowledgeFragment != null) {
            trans.hide(mNewKnowledgeFragment);
        }
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
