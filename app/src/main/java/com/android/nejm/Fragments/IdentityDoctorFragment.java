package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.nejm.R;
import com.android.nejm.widgets.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentityDoctorFragment extends BaseFragment {
    @BindView(R.id.recyclerViewProfession)
    RecyclerView recyclerViewProfession;
    @BindView(R.id.recyclerViewOffice)
    RecyclerView recyclerViewOffice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_identity_doctor, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        recyclerViewProfession.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerViewProfession.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        recyclerViewOffice.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerViewOffice.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

    }
}
