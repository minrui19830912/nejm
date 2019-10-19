package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.ArticleDetailActivity;
import com.android.nejm.data.NewKnowledgeInfo;
import com.android.nejm.net.HttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class NewKnowledgeAdapter extends  RecyclerView.Adapter<NewKnowledgeAdapter.ViewHolder>{
    private Context context;
    private List<NewKnowledgeInfo.NewKnowledgeitem> newKnowledgeitems;

    public NewKnowledgeAdapter(Context context) {

        this.context = context;
    }

    public void setData(List<NewKnowledgeInfo.NewKnowledgeitem> newKnowledgeitems){
        this.newKnowledgeitems = newKnowledgeitems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_knowledge_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        NewKnowledgeInfo.NewKnowledgeitem item = newKnowledgeitems.get(position);
        viewHolder.paper_img.setImageURI(item.thumb);
        viewHolder.textViewTypeName.setText(item.typename);
        viewHolder.date.setText(item.thedate);
        viewHolder.paper_name.setText(item.title);
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.launchActivity(context,item.id,
                        HttpUtils.NEW_KNOWLEDGE_DETAIL_URL+item.id,item.title,item.thumb,item.title);
            }
        });
        if(item.show_popularize.equals("1")){
            viewHolder.adver.setText(item.popularize_title);
            viewHolder.adver.setVisibility(View.VISIBLE);
        } else{
            viewHolder.adver.setText("");
            viewHolder.adver.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if(newKnowledgeitems !=null){
            return newKnowledgeitems.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView paper_img;
        public final TextView paper_name;
        public final TextView date;
        public final TextView textViewTypeName;
        public final TextView adver;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            paper_img = (SimpleDraweeView) view.findViewById(R.id.paper_img);
            paper_name = (TextView) view.findViewById(R.id.paper_name);
            textViewTypeName = view.findViewById(R.id.textViewTypeName);
            adver = view.findViewById(R.id.adver);
            date = (TextView) view.findViewById(R.id.date);

        }
    }
}
