package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;

import java.util.List;

public class ArticleDetailDirAdapter extends RecyclerView.Adapter<ArticleDetailDirAdapter.ViewHolder> {
    private Context context;
    private List<String> dirList;

    public ArticleDetailDirAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> dirList) {
        this.dirList = dirList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_detail_dir,viewGroup,false);
        return new ArticleDetailDirAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView textView = (TextView) viewHolder.itemView;
        textView.setText(dirList.get(position));
    }

    @Override
    public int getItemCount() {
        return dirList != null ? dirList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
