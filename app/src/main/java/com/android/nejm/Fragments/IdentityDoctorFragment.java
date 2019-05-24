package com.android.nejm.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nejm.R;
import com.android.nejm.adapter.DoctorSpecailAdapter;
import com.android.nejm.adapter.DoctorSpecailSubAdapter;
import com.android.nejm.adapter.OnItemClickListener;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.event.DoctorIdentitySelectedEvent;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.DisplayUtil;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.android.nejm.widgets.SpacesItemDecoration;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentityDoctorFragment extends BaseFragment {
    @BindView(R.id.recyclerViewProfession)
    RecyclerView recyclerViewProfession;
    @BindView(R.id.recyclerViewOffice)
    RecyclerView recyclerViewOffice;
    @BindView(R.id.layoutContainer)
    FrameLayout layoutContainer;

    RoleBean roleBean;

    DoctorSpecailAdapter doctorSpecailAdapter;
    DoctorSpecailSubAdapter doctorSpecailSubAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_identity_doctor, container, false);
        ButterKnife.bind(this, root);
        initView();
        loadData();
        return root;
    }

    private void initView() {
        recyclerViewProfession.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerViewProfession.addItemDecoration(new SpacesItemDecoration(DisplayUtil.dip2px(mContext, 18)));

        recyclerViewOffice.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerViewOffice.addItemDecoration(new SpacesItemDecoration(DisplayUtil.dip2px(mContext, 6)));

        doctorSpecailAdapter = new DoctorSpecailAdapter(mContext, new OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                doctorSpecailSubAdapter.setList(roleBean.dpts.get(index).sublist);
                doctorSpecailSubAdapter.notifyDataSetChanged();
            }
        });
        doctorSpecailSubAdapter = new DoctorSpecailSubAdapter(mContext, new OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                int dptsIndex = doctorSpecailAdapter.getSelectionIndex();
                RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
                roleInfo.divisionId = roleBean.dpts.get(dptsIndex).sublist.get(index).id;
                roleInfo.divisionName = roleBean.dpts.get(dptsIndex).sublist.get(index).title;

                EventBus.getDefault().post(new DoctorIdentitySelectedEvent(dptsIndex, index));
            }
        });

        recyclerViewProfession.setAdapter(doctorSpecailAdapter);
        recyclerViewOffice.setAdapter(doctorSpecailSubAdapter);
    }

    private void loadData() {
        roleBean = LoginUserManager.getInstance().roleBean;
        if(roleBean.dpts != null) {
            doctorSpecailAdapter.setList(roleBean.dpts);
            doctorSpecailAdapter.notifyDataSetChanged();

            if(roleBean.dpts.size() > 0) {
                doctorSpecailSubAdapter.setList(roleBean.dpts.get(0).sublist);
                doctorSpecailSubAdapter.notifyDataSetChanged();
            }
        }
    }
}
