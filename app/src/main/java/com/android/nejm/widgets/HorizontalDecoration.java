package com.android.nejm.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by malone on 2017/1/7.
 */

public class HorizontalDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public HorizontalDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;

    }
}