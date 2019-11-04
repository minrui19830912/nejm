package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.VideoDetailActivity;
import com.android.nejm.data.VideoInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class VideoListAdapter extends  RecyclerView.Adapter<VideoListAdapter.ViewHolder>{
    private Context context;
    private List<VideoInfo.Videoitem> videoitems;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<VideoInfo.Videoitem> paperList){
        this.videoitems = paperList;
    }
    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item,viewGroup,false);
        return new VideoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListAdapter.ViewHolder viewHolder, int position) {
        VideoInfo.Videoitem videoitem = videoitems.get(position);
        viewHolder.paper_img.setImageURI(videoitem.thumb);
        viewHolder.paper_name.setText(videoitem.title);
        viewHolder.textViewTypeName.setText(videoitem.typename);
        viewHolder.textViewDate.setText(videoitem.postdate);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoDetailActivity.launchActivity(context, videoitem.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(videoitems !=null){
            return videoitems.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView paper_img;
        public final TextView paper_name;
        public final TextView textViewTypeName;
        public final TextView textViewDate;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            paper_img = (SimpleDraweeView) view.findViewById(R.id.paper_img);
            paper_name = (TextView) view.findViewById(R.id.paper_name);
            textViewTypeName = (TextView) view.findViewById(R.id.textViewTypeName);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        }
    }
}
