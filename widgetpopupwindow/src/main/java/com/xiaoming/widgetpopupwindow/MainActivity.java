package com.xiaoming.widgetpopupwindow;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

//使用PopupWindow控件弹窗，自定义弹窗内容
public class MainActivity extends AppCompatActivity {
    private PopupWindow popupWindow;
    private View contentView;
    private Button btClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btClick = findViewById(R.id.bt_click);
        btClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupWindow(v);
            }
        });
    }

    private void openPopupWindow(View parent) {
        if(popupWindow == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            //将xml解析成View
            contentView = layoutInflater.inflate(R.layout.popupwindow, null);
            //自定义弹窗的内容contentView放进PopupWindow
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ColorDrawable colorDrawable = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(colorDrawable);
        //产生背景变暗效果
        //获取窗口的参数
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        //显示位置在底部
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }
}
