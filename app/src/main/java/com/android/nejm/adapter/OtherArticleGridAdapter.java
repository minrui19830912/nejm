package com.android.nejm.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.nejm.R;
import com.android.nejm.data.OtherArticleInfo;
import com.android.nejm.data.SpecialFieldIconInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class OtherArticleGridAdapter extends BaseAdapter {
    private Context context;
    private List<OtherArticleInfo.FilterItem> iconInfoList;
    private String focusId;

    public OtherArticleGridAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<OtherArticleInfo.FilterItem> iconInfoList, String focusId) {
        this.iconInfoList = iconInfoList;
        this.focusId = focusId;
    }

    public void setFocusId(String focusId) {
        this.focusId = focusId;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return iconInfoList != null ? iconInfoList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return iconInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.other_article_grid_item, parent, false);
        }

        TextView textView = (TextView) convertView;
        OtherArticleInfo.FilterItem info = iconInfoList.get(position);
        textView.setText(info.filtername);

        if(TextUtils.equals(focusId, info.id)) {
            textView.setBackgroundResource(R.drawable.grid_item_round_rectangle_selected);
            textView.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            textView.setBackgroundResource(R.drawable.grid_item_round_rectangle_unselected);
            textView.setTextColor(context.getResources().getColor(R.color.nejm_text_unselect));
        }

        return convertView;
    }
}
