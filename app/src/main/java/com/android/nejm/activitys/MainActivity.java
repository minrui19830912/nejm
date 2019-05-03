package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import com.android.nejm.Fragments.HomeFragment;
import com.android.nejm.Fragments.NewKnowledgeFragment;
import com.android.nejm.R;
import com.android.nejm.utils.AppUtil;
import com.android.nejm.utils.ToastUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private HomeFragment mHomeFragment;
    private NewKnowledgeFragment mNewKnowledgeFragment;
    private int mIndex = -1;
    private int[]mTabArray={R.id.indicator_one,R.id.indicator_two,R.id.indicator_three,R.id.indicator_four,R.id.indicator_five};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroup navView = findViewById(R.id.indicator_group);

        for(int i=0;i<mTabArray.length;i++){
            findViewById(mTabArray[i]).setOnClickListener(this);
        }
        findViewById(mTabArray[0]).performClick();
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

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.indicator_one:
                hideAllFragment();
                showFragment(0);

                break;
            case R.id.indicator_two:

                    hideAllFragment();
                    showFragment(1);

                break;
            case R.id.indicator_three:

                hideAllFragment();
                showFragment(2);

                break;
            case R.id.indicator_four:

                    hideAllFragment();
                    showFragment(3);
//                startActivity(new Intent(mContext,LoginActivity.class));
                break;
            case R.id.indicator_five:
                hideAllFragment();
                showFragment(4);
                break;

        }
    }
}
