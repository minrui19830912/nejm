package com.android.nejm.widgets;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by malone on 2017/1/7.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        //outRect.left = space;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
            if(linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                RecyclerView.Adapter adapter = parent.getAdapter();
                if(adapter != null) {
                    int lastPosition = adapter.getItemCount() - 1;
                    //Log.e("TAG", "index = " + parent.getChildPosition(view) + ", lastPosition = " + lastPosition);
                    if(parent.getChildPosition(view) < lastPosition) {
                        outRect.bottom = space;
                    }
                }
            } else {
                outRect.right = space;
            }
        } else {
            outRect.right = space;
        }

       // outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildPosition(view) == 0)
//            outRect.top = space;
    }
}