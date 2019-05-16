package com.android.nejm.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.adapter.HospitalAdapter;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class SearchHospitalActivity extends BaseActivity {
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HospitalAdapter hospitalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hospital);
        showBack();
        ButterKnife.bind(this);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = v.getText().toString().trim();
                    searchHospital(keyword);
                    return true;
                }

                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        hospitalAdapter = new HospitalAdapter(this);
        recyclerView.setAdapter(hospitalAdapter);
    }

    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {
        finish();
    }

    private void searchHospital(String keyword) {
        LoadingDialog.showDialogForLoading(this);
        HttpUtils.searchHospital(this, keyword, new OnNetResponseListener() {
            @Override
            public void onNetDataResponse(JSONObject json) {
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }
}
