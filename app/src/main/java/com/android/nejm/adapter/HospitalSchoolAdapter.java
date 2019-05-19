package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.HospitalSchool;

import java.util.List;

public class HospitalSchoolAdapter extends RecyclerView.Adapter<HospitalSchoolAdapter.ViewHolder> {
    private Context context;
    private List<HospitalSchool.School> schoolList;
    private int selectIndex = -1;

    public HospitalSchoolAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<HospitalSchool.School> list) {
        this.schoolList = list;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.hospital_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        TextView textView = (TextView) viewHolder.itemView;
        HospitalSchool.School school = schoolList.get(i);
        textView.setText(school.name);
        if(selectIndex == i) {
            textView.setTextColor(context.getResources().getColor(R.color.color_c92700));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.horizontal_line_red);
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.color_5c));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.horizontal_line_nor);
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectIndex);
                selectIndex = i;
                notifyItemChanged(selectIndex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolList != null ? schoolList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
