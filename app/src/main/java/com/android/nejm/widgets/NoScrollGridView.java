package com.android.nejm.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

public class NoScrollGridView extends GridView {

    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        int height = getMeasuredHeight();
        Log.e("TAG", "NoScrollGridView, onMeasure, height = " + height);
    }

}
