package com.android.nejm.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.LoginActivity;
import com.android.nejm.activitys.SearchSchoolActivity;
import com.android.nejm.activitys.TeacherTitleActivity;
import com.android.nejm.activitys.TitleActivity;
import com.android.nejm.adapter.TeacherTitleAdapter;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.AppUtil;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoStudentFragment extends BaseFragment {
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.textViewSchool)
    TextView textViewSchool;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_personal_info_student, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
        if(roleInfo != null) {
            textViewSchool.setText(roleInfo.hospitalName);
            textViewTitle.setText(roleInfo.jobnameName);
        }
    }

    @OnClick(R.id.textViewTitle)
    public void onClickTitle() {
        startActivity(new Intent(getActivity(), TeacherTitleActivity.class));
    }

    @OnClick(R.id.textViewSchool)
    public void onClickSchool() {
        startActivity(new Intent(getActivity(), SearchSchoolActivity.class));
    }

    @OnClick(R.id.buttonConfirm)
    public void onClickConfirm() {
        /*new AlertDialog.Builder(getActivity())
                .setMessage("您已经成功注册。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();*/
        LoadingDialog.showDialogForLoading(mContext);

        RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
        roleInfo.name = AppUtil.getTextContent(editTextName);

        Map<String, String> params = new HashMap<>();
        params.put("roleid", roleInfo.roleid);
        params.put("name", roleInfo.name);
        params.put("hospital", roleInfo.hospitalId);
        params.put("identity", roleInfo.identityId);
        params.put("jobname", roleInfo.jobnameId);

        HttpUtils.editRole(mContext, params, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                ToastUtil.showShort(mContext, "编辑身份成功");
                if(mContext != null && mContext instanceof Activity) {
                    ((Activity)mContext).finish();
                }
            }
        });
    }

    @OnClick(R.id.textViewSkip)
    public void onClickSkip() {
        if(mContext != null) {
            ((Activity)mContext).finish();
        }
    }
}
