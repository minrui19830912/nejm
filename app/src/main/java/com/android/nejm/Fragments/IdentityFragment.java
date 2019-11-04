package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.android.nejm.R;
import com.android.nejm.adapter.CareerAdapter;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.event.IdentitySelectedEvent;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentityFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<RoleBean.IdentityInfo> roleArray;
    CareerAdapter careerAdapter;

    RoleBean roleBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_career, container, false);
        ButterKnife.bind(this, view);

        careerAdapter = new CareerAdapter(mContext);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(careerAdapter);

        loadData();

        return view;
    }

    private void loadData() {
        LoadingDialog.showDialogForLoading(mContext);
        HttpUtils.getRole(mContext, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                roleBean = new Gson().fromJson(json.toString(), RoleBean.class);
                roleBean.roleArray = new ArrayList<>();
                roleBean.doctorJobs = new ArrayList<>();
                roleBean.teacherJobs = new ArrayList<>();
                roleBean.teacherIdentity = new ArrayList<>();
                roleBean.otherIdentity = new ArrayList<>();
                LoginUserManager.getInstance().setRoleBean(roleBean);

                JSONObject role_array = json.optJSONObject("role_array");
                Iterator<String> keys = role_array.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    roleBean.roleArray.add(new RoleBean.IdentityInfo(key, role_array.optString(key)));
                }

                JSONObject doctor_jobs = json.optJSONObject("doctor_jobs");
                keys = doctor_jobs.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    roleBean.doctorJobs.add(new RoleBean.IdentityInfo(key, doctor_jobs.optString(key)));
                }

                JSONObject teacher_jobs = json.optJSONObject("teacher_jobs");
                keys = teacher_jobs.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    roleBean.teacherJobs.add(new RoleBean.IdentityInfo(key, teacher_jobs.optString(key)));
                }

                JSONObject teacher_identity = json.optJSONObject("teacher_identity");
                keys = teacher_identity.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    roleBean.teacherIdentity.add(new RoleBean.IdentityInfo(key, teacher_identity.optString(key)));
                }

                JSONObject other_identity = json.optJSONObject("other_identity");
                keys = other_identity.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    roleBean.otherIdentity.add(new RoleBean.IdentityInfo(key, other_identity.optString(key)));
                }

                RoleInfo roleInfo = new RoleInfo();
                LoginUserManager.getInstance().setRoleInfo(roleInfo);
                roleInfo.roleid =roleBean.roleid;
                roleInfo.name = roleBean.name;
                roleInfo.divisionId = roleBean.division;
                roleInfo.company = roleBean.company;
                roleInfo.hospitalId = roleBean.hospital;
                roleInfo.jobnameId = roleBean.jobname;
                roleInfo.identityId = roleBean.identity;

                roleArray = roleBean.roleArray;
                careerAdapter.setData(roleArray);
                careerAdapter.notifyDataSetChanged();
            }
        });
    }
}
