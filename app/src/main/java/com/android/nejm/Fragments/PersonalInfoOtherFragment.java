package com.android.nejm.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.nejm.R;
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

public class PersonalInfoOtherFragment extends BaseFragment {
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextOrganization)
    EditText editTextOrganization;
    @BindView(R.id.editTextPosition)
    EditText editTextPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_personal_info_other, container, false);
        ButterKnife.bind(this, root);
        return root;
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
        roleInfo.jobnameName = AppUtil.getTextContent(editTextOrganization);
        roleInfo.company = AppUtil.getTextContent(editTextPosition);

        Map<String, String> params = new HashMap<>();
        params.put("roleid", roleInfo.roleid);
        params.put("name", roleInfo.name);
        params.put("identity", roleInfo.identityName);
        params.put("jobname", roleInfo.jobnameName);
        params.put("company", roleInfo.company);

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
