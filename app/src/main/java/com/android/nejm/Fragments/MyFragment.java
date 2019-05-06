package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;
import com.android.nejm.activitys.WebViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.textViewContactUs)
    public void onClickContactUs() {
        WebViewActivity.launchActivity(getActivity(), "联系我们", "http://www.nejmqianyan.cn/index.php?c=singlepage&m=contactus");
    }
}
