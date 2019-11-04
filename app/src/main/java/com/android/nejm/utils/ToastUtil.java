package com.android.nejm.utils;


import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nejm.R;


/**
 * Toast统一管理类
 */
public class ToastUtil {


    private static Toast toast;
    private static Toast toast2;

    private static Toast initToast(Context context,CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(Context context,CharSequence message) {
        initToast(context,message, Toast.LENGTH_SHORT).show();
    }

    public static void showShortCenter(Context context,CharSequence message) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        //设置显示时间
        toast.setDuration(Toast.LENGTH_SHORT);
View view = LayoutInflater.from(context).inflate(R.layout.toast_layout,null);
        ((TextView)view.findViewById(R.id.text1)).setText(message);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        toast.setView(view);

        toast.show();

    }
    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(Context context,int strResId) {
//		Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show();
        initToast(context,context.getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(Context context,CharSequence message) {
        initToast(context,message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    public static void showLong(Context context,int strResId) {
        initToast(context,context.getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(Context context,CharSequence message, int duration) {
        initToast(context,message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param strResId
     * @param duration
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context,context.getResources().getText(strResId), duration).show();
    }

    /**
     * 显示有image的toast
     *
     * @param tvStr
     * @param imageResource
     * @return
     */
    public static Toast showToastWithImg(Context context,final String tvStr, final int imageResource) {
        if (toast2 == null) {
            toast2 = new Toast(context);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast_custom, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
        tv.setText(TextUtils.isEmpty(tvStr) ? "" : tvStr);
        ImageView iv = (ImageView) view.findViewById(R.id.toast_custom_iv);
        if (imageResource > 0) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(imageResource);
        } else {
            iv.setVisibility(View.GONE);
        }
        toast2.setView(view);
        toast2.setGravity(Gravity.CENTER, 0, 0);
        toast2.show();
        return toast2;

    }
}
