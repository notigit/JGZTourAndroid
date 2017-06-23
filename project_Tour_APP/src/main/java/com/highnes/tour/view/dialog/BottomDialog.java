package com.highnes.tour.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.highnes.tour.R;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.ui.activities.home.ticket.MapDisplayActivity;
import com.highnes.tour.utils.AMapUtils;
import com.highnes.tour.utils.JumpMapUtils;
import com.highnes.tour.utils.SPUtils;

public class BottomDialog extends Dialog {

    TextView baidu,gaode;
    double latitude,longitude;
    String city,street;
    public BottomDialog(Context context,double latitude,double longitude,String city,String street) {
        //给dialog定制了一个主题（透明背景，无边框，无标题栏，浮在Activity上面，模糊）
        super(context, R.style.ios_bottom_dialog);
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.street = street;
        setContentView(R.layout.ios_bottom_dialog);
        initView();
    }

    private void initView() {
        baidu = (TextView) findViewById(R.id.baidu);
        gaode = (TextView) findViewById(R.id.gaode);
        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpMapUtils.openBaiduMap(getContext(),latitude+"",longitude+"");
                dismiss();
            }
        });
        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpMapUtils.openGaoDeMap(getContext(),street,latitude+"",longitude+"");
                dismiss();
            }
        });
        findViewById(R.id.bottom_dialog_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialog.this.dismiss();
            }
        });
        //点击空白区域可以取消dialog
        this.setCanceledOnTouchOutside(true);
        //点击back键可以取消dialog
        this.setCancelable(true);
        Window window = this.getWindow();
        //让Dialog显示在屏幕的底部
        window.setGravity(Gravity.BOTTOM);
        //设置窗口出现和窗口隐藏的动画
//        window.setWindowAnimations(R.style.ios_bottom_dialog_anim);
        //设置BottomDialog的宽高属性

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }
}