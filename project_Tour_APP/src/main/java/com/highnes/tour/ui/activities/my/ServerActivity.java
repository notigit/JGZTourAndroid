package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.ServerAdapter;
import com.highnes.tour.beans.find.FindListInfo;
import com.highnes.tour.beans.my.ServerInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.find.FindDetailActivity;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 * 客服。
 * 注意事项:
 * 无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-09-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ServerActivity extends BaseTitleActivity {
    TextView serverPhone;

    @Override
    @LayoutRes
    protected int getLayoutId2T() {
        return R.layout.activity_find_list;
    }

    @Override
    protected void findViewById2T() {
    }

    @Override
    protected void initView2T(Bundle savedInstanceState) {
        setTitle("客服");
        showBackwardView("", SHOW_ICON_DEFAULT);
        serverPhone = getViewById(R.id.server_phone);
    }

    @Override
    protected void setListener2T() {
        serverPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPhoneDialog();
            }
        });
    }

    @Override
    protected void processLogic2T(Bundle savedInstanceState) {
        performHandlePostDelayed(0, 200);

    }

    @Override
    protected void performHandlePostDelayedCallBack(int type) {
        super.performHandlePostDelayedCallBack(type);
    }

    /**
     * 拨打电话
     */
    private void displayPhoneDialog() {
//		if (TextUtils.isEmpty(mInfo.PayPhone)) {
//			showToast("没有获取到商家的电话");
//			return;
//		}
        showDialog("是否立即拨打400-011-0101？", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (2 == which) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-011-0101"));
                    startActivity(intent);
                }
            }
        });
    }
}
