package com.android.nejm.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.nejm.R;
import com.android.nejm.event.IdentitySelectedEvent;
import com.android.nejm.event.OtherIdentitySelectedEvent;
import com.android.nejm.event.SpecificIdentitySelectedEvent;
import com.android.nejm.event.StudentIdentitySelectedEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentityStudentFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_career_2, container, false);
        ButterKnife.bind(this, root);
        radioGroup.setOnCheckedChangeListener(this);
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
        EventBus.getDefault().post(new StudentIdentitySelectedEvent(index));
    }
}
