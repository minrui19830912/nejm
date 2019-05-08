package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.android.nejm.R;
import com.android.nejm.event.IdentitySelectedEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentityFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_career, container, false);
        ButterKnife.bind(this, view);
        radioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        EventBus.getDefault().post(new IdentitySelectedEvent(checkedId));
    }
}
