package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.ArticleDetailActivity;
import com.android.nejm.data.RelatedArticle;
import com.android.nejm.data.SpecialFieldArticleInfo;
import com.android.nejm.net.HttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelatedArticleAdapter extends RecyclerView.Adapter<RelatedArticleAdapter.ViewHolder> {
    private Context context;
    private List<RelatedArticle> relatedArticles;

    public RelatedArticleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<RelatedArticle> relatedArticles) {
        this.relatedArticles = relatedArticles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.speical_field_article_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RelatedArticle relatedArticle = relatedArticles.get(i);
//        Log.e("article", i + ": " + artitleItem.thumb + ", size = " + artitleItemList.size());
//        for(SpecialFieldArticleInfo.ArtitleItem item : artitleItemList) {
//            Log.e("test", "thumb = " + item.thumb);
//        }
        viewHolder.draweeViewCover.setImageURI(relatedArticle.thumb);
        viewHolder.textViewType.setText(String.format(Locale.CHINA, "%s：%s", relatedArticle.sourcename, relatedArticle.typename));
        viewHolder.textViewAuthor.setText(relatedArticle.author);
        viewHolder.textViewDate.setText(relatedArticle.postdate);
        if(relatedArticle.specialties != null && relatedArticle.specialties.size() > 0) {
            viewHolder.textViewField.setText(relatedArticle.specialties.get(0).classname);
        } else {
            viewHolder.textViewField.setText("");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.launchActivity(context, relatedArticle.id, HttpUtils.ARTICLE_DETAIL_URL+relatedArticle.id,relatedArticle.show_wantsay,relatedArticle.thumb,relatedArticle.title);
            }
        });

        viewHolder.textViewArticleTitle.setText(relatedArticle.title);
        if(TextUtils.equals(relatedArticle.show_wantsay, "1")) {
            viewHolder.textViewComment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewComment.setVisibility(View.INVISIBLE);
        }
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
