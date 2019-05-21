package com.android.nejm.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nejm.R;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.event.DoctorIdentitySelectedEvent;
import com.android.nejm.manage.LoginUserManager;
import com.android.nejm.net.HttpUtils;
import com.android.nejm.net.OnNetResponseListener;
import com.android.nejm.utils.ToastUtil;
import com.android.nejm.widgets.DividerItemDecoration;
import com.android.nejm.widgets.LoadingDialog;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentityDoctorFragment extends BaseFragment {
    /*@BindView(R.id.recyclerViewProfession)
    RecyclerView recyclerViewProfession;
    @BindView(R.id.recyclerViewOffice)
    RecyclerView recyclerViewOffice;*/
    @BindView(R.id.layoutContainer)
    FrameLayout layoutContainer;

    RoleBean roleBean;

    OptionsPickerView pvOptions;

    List<OptionItem> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_identity_doctor, container, false);
        ButterKnife.bind(this, root);
        //initView();
        initOptionPicker();
        loadData();
        return root;
    }

    /*private void initView() {
        recyclerViewProfession.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerViewProfession.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        recyclerViewOffice.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        recyclerViewOffice.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }*/

    private void loadData() {
        roleBean = LoginUserManager.getInstance().roleBean;
        if(roleBean.dpts != null) {
            for(RoleBean.DPTS dpts : roleBean.dpts) {
                OptionItem item = new OptionItem();
                item.id = dpts.id;
                item.title = dpts.title;
                options1Items.add(item);

                List<String> list = new ArrayList<>();
                options2Items.add(list);
                if(dpts.sublist != null) {
                    for(RoleBean.SubListItem subListItem : dpts.sublist) {
                        list.add(subListItem.title);
                    }
                }
            }

            pvOptions.setPicker(options1Items, options2Items);
            pvOptions.show();
        }
    }

    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
                roleInfo.divisionId = roleBean.dpts.get(options1).sublist.get(options2).id;
                roleInfo.divisionName = roleBean.dpts.get(options1).sublist.get(options2).title;

                EventBus.getDefault().post(new DoctorIdentitySelectedEvent(options1, options2));
            }
        })
                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                .isDialog(false)
                .setDecorView(layoutContainer)
                .setOutSideCancelable(false)
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvOptions.returnData();
                                //pvCustomOptions.dismiss();
                                //EventBus.getDefault().post(new DoctorIdentitySelectedEvent(options1, options2));
                            }
                        });
                    }
                })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        //ToastUtil.showShort(mContext, str);
                    }
                })
                .build();
        pvOptions.setKeyBackCancelable(false);
    }

    static class OptionItem implements IPickerViewData {
        public String id;
        public String title;

        @Override
        public String getPickerViewText() {
            return title;
        }
    }
}
