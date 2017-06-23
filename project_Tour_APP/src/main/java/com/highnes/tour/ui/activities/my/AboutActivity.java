package com.highnes.tour.ui.activities.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.AppUtils;

/**
 * <PRE>
 * 作用:
 *    设置页面..关于我们。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-24   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class AboutActivity extends BaseTitleActivity {
	// 拨打电话
	private LinearLayout llTelPhone;
	// 版本信息
	private TextView tvVersion;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_setup_about;
	}

	@Override
	protected void findViewById2T() {
		llTelPhone = (LinearLayout) findViewById(R.id.ll_tel_phone);
		tvVersion = (TextView) findViewById(R.id.tv_version);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("关于我们");
		showBackwardView("", SHOW_ICON_DEFAULT);
		tvVersion.setText("金龟子V" + AppUtils.getVersionName(mContext));
	}

	@Override
	protected void setListener2T() {
		llTelPhone.setOnClickListener(this);

	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 拨打电话
		case R.id.ll_tel_phone:
			displayPhoneDialog("4000110101");
			break;
		default:
			break;
		}
	}

	/**
	 * 显示拨打电话的对话框
	 */
	private void displayPhoneDialog(final String tel) {
		try {
			showDialog("立即拨打" + tel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (2 == which) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
						startActivity(intent);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
