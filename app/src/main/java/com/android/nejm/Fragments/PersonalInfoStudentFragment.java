package com.android.nejm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;
import com.android.nejm.activitys.TitleActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoStudentFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_personal_info_student, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.textViewTitle)
    public void onClickTitle() {
        startActivity(new Intent(getActivity(), TitleActivity.class));
    }
}
