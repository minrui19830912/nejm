package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.nejm.R;
import com.android.nejm.adapter.DoctorTitleAdapter;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.manage.LoginUserManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorTitleActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    DoctorTitleAdapter doctorTitleAdapter;

    List<RoleBean.IdentityInfo> doctorJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_title);
        ButterKnife.bind(this);
        showBack();

        doctorTitleAdapter = new DoctorTitleAdapter(this);

        RoleBean roleBean = LoginUserManager.getInstance().getRoleBean();
        doctorJobs = roleBean.doctorJobs;
        doctorTitleAdapter.setData(doctorJobs);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(doctorTitleAdapter);
    }

    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {
        int index = doctorTitleAdapter.getSelectIndex();
        RoleBean.IdentityInfo identityInfo = doctorJobs.get(index);

        RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
        roleInfo.jobnameId = identityInfo.id;
        roleInfo.jobnameName = identityInfo.name;

        finish();
    }
}
