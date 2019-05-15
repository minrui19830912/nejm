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

import com.android.nejm.R;
import com.android.nejm.data.SpecialFieldIconInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class SpecialFieldGridAdapter extends BaseAdapter {
    private Context context;
    private List<SpecialFieldIconInfo> iconInfoList;
    private String focusId;

    public SpecialFieldGridAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<SpecialFieldIconInfo> iconInfoList, String focusId) {
        this.iconInfoList = iconInfoList;
        this.focusId = focusId;
    }

    public void setFocusId(String id) {
        this.focusId = id;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.special_field_grid_item, parent, false);
        }

        SimpleDraweeView draweeView = (SimpleDraweeView)convertView;
        SpecialFieldIconInfo info = iconInfoList.get(position);
        if(TextUtils.isEmpty(info.icon)) {
            draweeView.setBackgroundResource(0);
        } else {
            if(TextUtils.equals(focusId, info.id)) {
                draweeView.setBackgroundResource(R.drawable.home_classes_bg);
            } else {
                draweeView.setBackgroundResource(R.drawable.home_classes_nor);
            }

            draweeView.setImageURI(info.icon);
        }

        return convertView;
    }
}
