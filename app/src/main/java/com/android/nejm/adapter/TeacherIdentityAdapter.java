package com.android.nejm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.RoleBean;
import com.android.nejm.data.RoleInfo;
import com.android.nejm.event.IdentitySelectedEvent;
import com.android.nejm.event.StudentIdentitySelectedEvent;
import com.android.nejm.manage.LoginUserManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherIdentityAdapter extends RecyclerView.Adapter<TeacherIdentityAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<RoleBean.IdentityInfo> roleArray;
    private OnItemClickListener itemClickListener;
    int selectionIndex = -1;

    public TeacherIdentityAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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
                if(selectionIndex >= 0) {
                    notifyItemChanged(selectionIndex);
                }

                selectionIndex = i;
                notifyItemChanged(i);

                if(itemClickListener != null) {
                    itemClickListener.onItemClicked(i);
                }
            }
        });

        if(selectionIndex == i) {
            viewHolder.textViewCareer.setTextColor(Color.WHITE);
            viewHolder.textViewCareer.setBackgroundResource(R.drawable.login_bg_shape);
        } else {
            viewHolder.textViewCareer.setTextColor(context.getResources().getColor(R.color.color_c92700));
            viewHolder.textViewCareer.setBackgroundResource(R.drawable.radio_bg_nor);
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

    public interface OnItemClickListener {
        void onItemClicked(int index);
    }
}
