package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.ArticleDetailActivity;
import com.android.nejm.activitys.DirectoryDetailActivity;
import com.android.nejm.activitys.VideoDetailActivity;
import com.android.nejm.data.OtherArticleInfo;
import com.android.nejm.net.HttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherArticleAdapter extends RecyclerView.Adapter<OtherArticleAdapter.ViewHolder> {
    private Context context;
    private List<OtherArticleInfo.ArtitleItem> artitleItemList;

    public OtherArticleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<OtherArticleInfo.ArtitleItem> artitleItemList) {
        this.artitleItemList = artitleItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.speical_field_article_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        OtherArticleInfo.ArtitleItem artitleItem = artitleItemList.get(i);
        Log.e("article", i + ": " + artitleItem.thumb + ", size = " + artitleItemList.size());
        for(OtherArticleInfo.ArtitleItem item : artitleItemList) {
            Log.e("test", "thumb = " + item.thumb);
        }
        viewHolder.draweeViewCover.setImageURI(artitleItem.thumb);
        viewHolder.textViewType.setText(String.format(Locale.CHINA, "%s：%s", artitleItem.sourcename, artitleItem.filtername));
        viewHolder.textViewAuthor.setText(artitleItem.author);
        viewHolder.textViewDate.setText(artitleItem.postdate);
        if(artitleItem.specialties != null && artitleItem.specialties.size() > 0) {
            viewHolder.textViewField.setText(artitleItem.specialties.get(0).classname);
        } else {
            viewHolder.textViewField.setText("");
        }

        viewHolder.textViewArticleTitle.setText(artitleItem.title);
        if(TextUtils.equals(artitleItem.show_wantsay, "1")) {
            viewHolder.textViewComment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewComment.setVisibility(View.INVISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(artitleItem.article_type == 2) {//video
                    VideoDetailActivity.launchActivity(context, artitleItem.id);
                } else if(artitleItem.article_type == 3) {//directory
                    DirectoryDetailActivity.launchActivity(context, artitleItem.id);
                } else {
                    ArticleDetailActivity.launchActivity(context, artitleItem.id, HttpUtils.ARTICLE_DETAIL_URL+artitleItem.id,artitleItem.show_wantsay,artitleItem.thumb,artitleItem.title);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return artitleItemList != null ? artitleItemList.size() : 0;
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
