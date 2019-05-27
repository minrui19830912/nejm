package com.android.nejm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.event.IdentitySelectedEvent;
import com.android.nejm.manage.LoginUserManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CareerAdapter extends RecyclerView.Adapter<CareerAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<RoleBean.IdentityInfo> roleArray;
    int selectIndex = -1;

    public CareerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<RoleBean.IdentityInfo> roleArray) {
        this.roleArray = roleArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.career_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RoleBean.IdentityInfo info = roleArray.get(i);
        viewHolder.textViewCareer.setText(info.name);
        viewHolder.textViewCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectIndex >= 0) {
                    notifyItemChanged(selectIndex);
                }

                selectIndex = i;
                notifyItemChanged(i);
                RoleInfo roleInfo = LoginUserManager.getInstance().roleInfo;
                roleInfo.roleid = info.id;
                roleInfo.roleName = info.name;
                EventBus.getDefault().post(new IdentitySelectedEvent(info.id));
            }
        });

        if(selectIndex == i) {
            viewHolder.textViewCareer.setBackgroundResource(R.drawable.login_bg_shape);
            viewHolder.textViewCareer.setTextColor(Color.WHITE);
        } else {
            viewHolder.textViewCareer.setBackgroundResource(R.drawable.radio_bg_nor);
            viewHolder.textViewCareer.setTextColor(context.getResources().getColor(R.color.radio_text_color));
        }
    }

    @Override
    public int getItemCount() {
        return roleArray != null ? roleArray.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewCareer)
        TextView textViewCareer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
