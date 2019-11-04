package com.android.nejm.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.HospitalSchoolAdapter;
import com.android.nejm.data.HospitalSchool;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class SearchSchoolActivity extends BaseActivity {
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HospitalSchoolAdapter hospitalSchoolAdapter;
    HospitalSchool hospitalSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);
        showBack();
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        hospitalSchoolAdapter = new HospitalSchoolAdapter(this);
        recyclerView.setAdapter(hospitalSchoolAdapter);
    }

    @OnEditorAction(R.id.editTextSearch)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            String keyword = v.getText().toString().trim();
            searchSchool(keyword);
            return true;
        }

        return false;
    }


    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {
        int index = hospitalSchoolAdapter.getSelectIndex();
        if(index >= 0) {
            HospitalSchool.School school = hospitalSchool.items.get(index);

            RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
            roleInfo.hospitalId = school.id;
            roleInfo.hospitalName = school.name;
        }

        finish();
    }

    private void searchSchool(String keyword) {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.searchHospitalSchool(this, keyword, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
                hospitalSchool = new Gson().fromJson(json.toString(), HospitalSchool.class);
                hospitalSchoolAdapter.setData(hospitalSchool.items);
                hospitalSchoolAdapter.notifyDataSetChanged();
            }
        });
    }
}
