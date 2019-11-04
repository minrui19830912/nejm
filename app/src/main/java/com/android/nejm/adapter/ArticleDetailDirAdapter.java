package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;

import java.util.List;

public class ArticleDetailDirAdapter extends RecyclerView.Adapter<ArticleDetailDirAdapter.ViewHolder> {
    private Context context;
    private List<String> dirList;
    private OnItemClickListener itemClickListener;

    public ArticleDetailDirAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        itemClickListener = listener;
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
        textView.setText(Html.fromHtml(dirList.get(position)));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null) {
                    itemClickListener.onItemClicked(position);
                }
            }
        });
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

    public interface OnItemClickListener {
        void onItemClicked(int index);
    }
}
