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

public class DoctorSpecailAdapter extends RecyclerView.Adapter<DoctorSpecailAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener itemClickListener;
    private List<RoleBean.DPTS> dptsList;
    private int selectionIndex = 0;

    public DoctorSpecailAdapter(Context context, OnItemClickListener listener) {
       this.context = context;
       inflater = LayoutInflater.from(context);
       itemClickListener = listener;
    }

    public void setList(List<RoleBean.DPTS> dptsList) {
        this.dptsList = dptsList;
    }

    public int getSelectionIndex() {
        return selectionIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.doctor_special_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RoleBean.DPTS dpts = dptsList.get(i);
        TextView textView = (TextView)viewHolder.itemView;
        textView.setText(dpts.title);

        if(selectionIndex == i) {
            textView.setTextColor(context.getResources().getColor(R.color.color_c92700));
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.horizontal_line_red);
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_5c));
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.horizontal_line_nor);
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectionIndex != -1) {
                    notifyItemChanged(selectionIndex);
                }

                selectionIndex = i;
                notifyItemChanged(i);

                if(itemClickListener != null) {
                    itemClickListener.onItemClicked(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dptsList != null ? dptsList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
