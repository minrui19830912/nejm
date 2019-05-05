package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.nejm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdByEmailFragment extends BaseFragment {
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_find_by_email, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.buttonConfirm)
    public void onClickConfirm() {
        String email = editTextEmail.getText().toString().trim();
    }
}
