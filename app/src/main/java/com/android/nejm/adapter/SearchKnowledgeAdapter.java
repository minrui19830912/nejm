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

public class SearchKnowledgeAdapter extends  RecyclerView.Adapter<SearchKnowledgeAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Paper> mPaperList;

    public SearchKnowledgeAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<Paper> paperList){
        this.mPaperList = paperList;
    }
    @NonNull
    @Override
    public SearchKnowledgeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_knowledge_item,viewGroup,false);
        return new SearchKnowledgeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchKnowledgeAdapter.ViewHolder viewHolder, int i) {
        viewHolder.paper_img.setImageURI("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2121206715,2955288754&fm=27&gp=0.jpg");
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
