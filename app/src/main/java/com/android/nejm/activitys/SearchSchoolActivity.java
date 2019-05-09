package com.android.nejm.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.android.nejm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchSchoolActivity extends BaseActivity {
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);
        showBack();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textViewConfirm)
    public void onClickConfirm() {
        finish();
    }
}
