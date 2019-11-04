package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.nejm.R;
import com.android.nejm.adapter.TeacherIdentityAdapter;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.event.OtherIdentitySelectedEvent;
import com.android.nejm.event.SpecificIdentitySelectedEvent;
import com.android.nejm.event.StudentIdentitySelectedEvent;
import com.android.nejm.manage.LoginUserManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentiyOtherFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<RoleBean.IdentityInfo> roleArray;
    TeacherIdentityAdapter identityAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_identity_other, container, false);
        ButterKnife.bind(this, root);

        identityAdapter = new TeacherIdentityAdapter(mContext);
        identityAdapter.setOnItemClickListener(new TeacherIdentityAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
                RoleBean.IdentityInfo info = roleArray.get(index);
                roleInfo.identityId = info.id;
                roleInfo.identityName = info.name;
                EventBus.getDefault().post(new OtherIdentitySelectedEvent(index));
            }
        });

        RoleBean roleBean = LoginUserManager.getInstance().getRoleBean();
        if(roleBean != null) {
            roleArray = roleBean.otherIdentity;
            identityAdapter.setData(roleArray);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(identityAdapter);

        return root;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = 0;
        int childCount = group.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View childView = group.getChildAt(i);
            if(childView instanceof RadioButton) {
                if(checkedId == childView.getId()) {
                    break;
                }

                index++;
            }
        }
        EventBus.getDefault().post(new OtherIdentitySelectedEvent(index));
    }
}
