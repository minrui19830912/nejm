package com.android.nejm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.BuildConfig;
import com.android.nejm.R;
import com.android.nejm.activitys.LoginActivity;
import com.android.nejm.activitys.RegisterActivity;
import com.android.nejm.activitys.SettingActivity;
import com.android.nejm.activitys.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyUnloginFragment extends BaseFragment {
    @BindView(R.id.textViewVersion)
    TextView textViewVersion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my_unlogin, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        textViewVersion.setText("V" + BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.textViewLogin)
    public void onClickLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewRegister)
    public void onClickRegister() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textViewContactUs)
    public void onClickContactUs() {
        WebViewActivity.launchActivity(getActivity(), "联系我们", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=contactus");
    }
}


