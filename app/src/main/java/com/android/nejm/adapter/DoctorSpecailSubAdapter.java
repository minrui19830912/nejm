package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.RoleBean;

import java.util.List;

public class DoctorSpecailSubAdapter extends RecyclerView.Adapter<DoctorSpecailSubAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<RoleBean.SubListItem> sublist;
    private OnItemClickListener itemClickListener;

    public DoctorSpecailSubAdapter(Context context, OnItemClickListener listener) {
       this.context = context;
       inflater = LayoutInflater.from(context);
       itemClickListener = listener;
    }

    public void setList(List<RoleBean.SubListItem> sublist) {
        this.sublist = sublist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.doctor_special_item_sub, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RoleBean.SubListItem item = sublist.get(i);
        TextView textView = (TextView) viewHolder.itemView;
        textView.setText(item.title);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onItemClicked(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sublist != null ? sublist.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
