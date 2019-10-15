package com.android.nejm.widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.nejm.R;

public class MenuPopupWin extends PopupWindow {

    private Context m_context;
    private TextView m_tvMenu1;
    private TextView m_tvMenu2;
    private View m_view;
    private Window m_window;

    public MenuPopupWin(Context context, final OnMenuItemClickListener listener) {
        super(context);
        this.m_context = context;
        // 初始化view
        m_view = LayoutInflater.from(context).inflate(R.layout.lay_menupopwin, null);
        m_tvMenu1 = m_view.findViewById(R.id.text1);
        m_tvMenu2 = m_view.findViewById(R.id.text2);
        m_tvMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        m_tvMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, 1);
                }
                dismiss();
            }
        });
        // 设置自定义PopupWindow的View
        this.setContentView(m_view);
        // 设置自定义PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置自定义PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置自定义PopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置自定义PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popWinAnimBot);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置自定义PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // popupwindow消失，恢复透明度
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = m_window.getAttributes();
                lp.alpha = 1f;
                m_window.setAttributes(lp);
            }
        });
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        m_view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = m_view.findViewById(R.id.lay_spinnerdlg).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                // setOutsideTouchable(true)则点击PopupWindow之外的地方PopupWindow会消失
//                return true;
//            }
//        });
    }

    /**
     * 显示dlg，window用来设置背景变暗
     *
     * @param parent
     */
    public void show(View parent, Window window) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        m_window = window;
        // 设置背景变暗
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);

    }

    /**
     * 定义接口
     */
    public interface OnMenuItemClickListener {
        void onItemClick(View view, int position);
    }
}
