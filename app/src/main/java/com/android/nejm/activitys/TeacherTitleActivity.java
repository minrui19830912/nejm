package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.nejm.R;
import com.android.nejm.adapter.TeacherTitleAdapter;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.manage.LoginUserManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeacherTitleActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    TeacherTitleAdapter teacherTitleAdapter;
    List<RoleBean.IdentityInfo> teacherJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_title);
        ButterKnife.bind(this);
        showBack();

        teacherTitleAdapter = new TeacherTitleAdapter(this);

        RoleBean roleBean = LoginUserManager.getInstance().getRoleBean();
        teacherJobs = roleBean.teacherJobs;
        teacherTitleAdapter.setData(teacherJobs);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(teacherTitleAdapter);
    }

    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {
        int index = teacherTitleAdapter.getSelectIndex();
        RoleBean.IdentityInfo identityInfo = teacherJobs.get(index);

        RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
        roleInfo.jobnameId = identityInfo.id;
        roleInfo.jobnameName = identityInfo.name;

        finish();
    }
}
