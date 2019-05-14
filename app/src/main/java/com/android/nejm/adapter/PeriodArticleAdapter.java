package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.Paper;
import com.android.nejm.widgets.NoScrollGridView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class PeriodArticleAdapter extends  RecyclerView.Adapter<PeriodArticleAdapter.ViewHolder>{
    private Context context;
    private List<PeriodArticleItem> mPaperList;

    public PeriodArticleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<PeriodArticleItem> paperList){
        this.mPaperList = paperList;
    }

    @NonNull
    @Override
    public PeriodArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.period_article_item,viewGroup,false);
        return new PeriodArticleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodArticleAdapter.ViewHolder viewHolder, int position) {
        //viewHolder.paper_img.setImageURI("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2121206715,2955288754&fm=27&gp=0.jpg");
        PeriodArticleItem periodArticleItem = mPaperList.get(position);
        viewHolder.month.setText(periodArticleItem.month);
        viewHolder.year.setText(periodArticleItem.year);
        viewHolder.period_gridview.setAdapter(new MonthArticleAdapter(periodArticleItem.articleItems));
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
        //public final SimpleDraweeView paper_img;
        public final TextView year;
        public final TextView month;
        private final NoScrollGridView period_gridview;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            period_gridview = (NoScrollGridView) view.findViewById(R.id.period_gridview);
            year = (TextView) view.findViewById(R.id.year);
            month = (TextView) view.findViewById(R.id.month);

        }
    }

    class MonthArticleAdapter extends BaseAdapter {
        private List<PeriodArticleItem.ArticleItem> articleItems;
        MonthArticleAdapter(List<PeriodArticleItem.ArticleItem> articleItems) {
            this.articleItems = articleItems;
        }

        @Override
        public int getCount() {
            return articleItems != null ? articleItems.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View view = LayoutInflater.from(context).inflate(R.layout.month_article_item,null);
            PeriodArticleItem.ArticleItem articleItem = articleItems.get(position);
            //((SimpleDraweeView)view.findViewById(R.id.paper_img)).setImageURI(articleItem.cover);
            MonthArticleAdapterViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.month_article_item,parent, false);
                holder = new MonthArticleAdapterViewHolder(convertView);
            } else {
                holder = (MonthArticleAdapterViewHolder)convertView.getTag();
            }

            holder.paper_img.setImageURI(articleItem.cover);
            holder.paper_name.setText(articleItem.thedate);
            return convertView;
        }

        class MonthArticleAdapterViewHolder {
            SimpleDraweeView paper_img;
            TextView paper_name;

            public MonthArticleAdapterViewHolder(View convertView) {
                paper_img = convertView.findViewById(R.id.paper_img);
                paper_name = convertView.findViewById(R.id.paper_name);
                convertView.setTag(this);
            }
        }
    }
}
