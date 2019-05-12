package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.Paper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class VideoListAdapter extends  RecyclerView.Adapter<VideoListAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Paper> mPaperList;

    public VideoListAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<Paper> paperList){
        this.mPaperList = paperList;
    }
    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item,viewGroup,false);
        return new VideoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.paper_img.setImageURI("https://www.nejmqianyan.cn/data/upload/20190114/1547428095876430.png");
    }

    @Override
    public int getItemCount() {
        if(mPaperList!=null){
            return mPaperList.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView paper_img;
        public final TextView paper_name;
        public final TextView date;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            paper_img = (SimpleDraweeView) view.findViewById(R.id.paper_img);
            paper_name = (TextView) view.findViewById(R.id.paper_name);
            date = (TextView) view.findViewById(R.id.date);

        }
    }
}
