package com.android.nejm.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.OfflineArticleDetailActivity;
import com.android.nejm.bean.DownloadRecord;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadArticleAdapter extends RecyclerView.Adapter<DownloadArticleAdapter.ViewHolder> {
    private Context context;
    private List<DownloadRecord> relatedArticles;
    private OnItemLongClickListener onItemLongClickListener;
    public interface OnItemLongClickListener {
        void onItemLongClicked(View view, int index);
    }
    public DownloadArticleAdapter(Context context) {
        this.context = context;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public void setData(List<DownloadRecord> relatedArticles) {
        this.relatedArticles = relatedArticles;
    }
    public void removeItem(int pos){
        relatedArticles.remove(pos);
        notifyItemRemoved(pos);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.speical_field_article_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DownloadRecord relatedArticle = relatedArticles.get(i);
//        Log.e("article", i + ": " + artitleItem.thumb + ", size = " + artitleItemList.size());
//        for(SpecialFieldArticleInfo.ArtitleItem item : artitleItemList) {
//            Log.e("test", "thumb = " + item.thumb);
//        }
        viewHolder.draweeViewCover.setImageURI(relatedArticle.thumb);
        viewHolder.textViewType.setText(String.format(Locale.CHINA, "%s：%s", relatedArticle.sourcename, relatedArticle.typename));
        viewHolder.textViewAuthor.setText(relatedArticle.author);
        viewHolder.textViewDate.setText(relatedArticle.postdate);
        viewHolder.textViewField.setText(relatedArticle.classname);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(relatedArticle.filePath);
                Log.e("dpp", "filePath = " + file.getAbsolutePath());
                Log.e("dpp", "url = " + Uri.fromFile(file).toString());
                String url = Uri.fromFile(file).toString();
                OfflineArticleDetailActivity.launchActivity(context, relatedArticle.articleId, url,relatedArticle.show_wantsay,relatedArticle.thumb,relatedArticle.title);
            }
        });

        viewHolder.textViewArticleTitle.setText(relatedArticle.title);
        if(TextUtils.equals(relatedArticle.show_wantsay, "1")) {
            viewHolder.textViewComment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewComment.setVisibility(View.INVISIBLE);
        }
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemLongClickListener!=null){
                    onItemLongClickListener.onItemLongClicked(view,i);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return relatedArticles != null ? relatedArticles.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewType)
        TextView textViewType;
        @BindView(R.id.textViewComment)
        TextView textViewComment;
        @BindView(R.id.draweeViewCover)
        SimpleDraweeView draweeViewCover;
        @BindView(R.id.textViewArticleTitle)
        TextView textViewArticleTitle;
        @BindView(R.id.textViewAuthor)
        TextView textViewAuthor;
        @BindView(R.id.textViewField)
        TextView textViewField;
        @BindView(R.id.textViewDate)
        TextView textViewDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
