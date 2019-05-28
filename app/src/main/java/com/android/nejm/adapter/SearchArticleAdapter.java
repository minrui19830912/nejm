package com.android.nejm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.activitys.ArticleDetailActivity;
import com.android.nejm.activitys.VideoDetailActivity;
import com.android.nejm.data.SpecialFieldArticleInfo;
import com.android.nejm.net.HttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchArticleAdapter extends RecyclerView.Adapter<SearchArticleAdapter.ViewHolder> {
    private Context context;
    private boolean displayMark = false;
    private List<SpecialFieldArticleInfo.ArtitleItem> artitleItemList;
    private String keyWord;


    public SearchArticleAdapter(Context context) {
        this.context = context;
    }

    public SearchArticleAdapter(Context context, boolean displayMark) {
        this.context = context;
        this.displayMark = displayMark;
    }

    public void setData(List<SpecialFieldArticleInfo.ArtitleItem> artitleItemList) {
        this.artitleItemList = artitleItemList;
    }
    public void setData(List<SpecialFieldArticleInfo.ArtitleItem> artitleItemList,String keyWord) {
        this.artitleItemList = artitleItemList;
        this.keyWord = keyWord;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_article_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SpecialFieldArticleInfo.ArtitleItem artitleItem = artitleItemList.get(i);
//        Log.e("article", i + ": " + artitleItem.thumb + ", size = " + artitleItemList.size());
//        for(SpecialFieldArticleInfo.ArtitleItem item : artitleItemList) {
//            Log.e("test", "thumb = " + item.thumb);
//        }
        viewHolder.draweeViewCover.setImageURI(artitleItem.thumb);
        if(artitleItem.sourcename == null) {
            artitleItem.sourcename = "";
        }
        if(artitleItem.typename == null) {
            artitleItem.typename = "";
        }

        viewHolder.textViewType.setText(String.format(Locale.CHINA, "%s：%s", artitleItem.sourcename, artitleItem.typename));
        viewHolder.textViewAuthor.setText(artitleItem.author);
        viewHolder.textViewDate.setText(artitleItem.postdate);
        if(artitleItem.specialties != null && artitleItem.specialties.size() > 0) {
            viewHolder.textViewField.setText(artitleItem.specialties.get(0).classname);
        } else {
            viewHolder.textViewField.setText("");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(artitleItem.is_video) {
                    VideoDetailActivity.launchActivity(context, artitleItem.id);
                } else {
                    ArticleDetailActivity.launchActivity(context, artitleItem.id, HttpUtils.ARTICLE_DETAIL_URL+artitleItem.id,artitleItem.show_wantsay,artitleItem.thumb,artitleItem.title);
                }
            }
        });

        if(displayMark&&(!TextUtils.isEmpty(artitleItem.intro))&&(!TextUtils.isEmpty(keyWord))){
            int markColor = context.getResources().getColor(R.color.color_search_mark);
            int index = artitleItem.title.indexOf(keyWord);
            int introIndex = artitleItem.intro.indexOf(keyWord);
            Log.e("minrui","index="+index);
            SpannableStringBuilder titleStyle=new SpannableStringBuilder(artitleItem.title);
            SpannableStringBuilder introStyle=new SpannableStringBuilder(artitleItem.intro);
            if(index>=0){
                titleStyle.setSpan(new BackgroundColorSpan(markColor),index, index+keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(introIndex>=0){
                introStyle.setSpan(new BackgroundColorSpan(markColor),introIndex, introIndex+keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            viewHolder.textViewArticleIntro.setText(introStyle);
            viewHolder.textViewArticleTitle.setText(titleStyle);
        }else{
            viewHolder.textViewArticleIntro.setText(artitleItem.intro);
            viewHolder.textViewArticleTitle.setText(artitleItem.title);
        }

        if(TextUtils.equals(artitleItem.show_wantsay, "1")) {
            viewHolder.textViewComment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textViewComment.setVisibility(View.INVISIBLE);
        }
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
        @BindView(R.id.textViewArticleIntro)
        TextView textViewArticleIntro;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
