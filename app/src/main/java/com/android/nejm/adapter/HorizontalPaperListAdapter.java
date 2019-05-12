package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.MainActivity;
import com.android.nejm.data.Paper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class HorizontalPaperListAdapter extends RecyclerView.Adapter<HorizontalPaperListAdapter.ViewHolder> {
    private ArrayList<Paper> mPaperList;
    private Context context;

    public HorizontalPaperListAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<Paper> paperList){
        this.mPaperList = paperList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.paper_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(i==mPaperList.size()){
            viewHolder.paper_img.setImageResource(R.mipmap.icon_img_more);
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)context).showTab(2);
                }
            });
        } else {
        Paper list = mPaperList.get(i);
        viewHolder.paper_img.setImageURI(list.url);
        viewHolder.paper_date.setText(list.date);
        viewHolder.paper_name.setText(list.title);
        }
    }

    @Override
    public int getItemCount() {
        if(mPaperList!=null){
        return mPaperList.size()+1;
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView paper_img;
        public final TextView paper_name;
        public final TextView paper_date;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            paper_img = (SimpleDraweeView) view.findViewById(R.id.paper_img);
            paper_name = (TextView) view.findViewById(R.id.paper_name);
            paper_date = (TextView) view.findViewById(R.id.paper_date);

        }


    }
}
