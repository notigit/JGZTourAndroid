package com.highnes.tour.ui.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;

/**
 * <PRE>
 * 作用:
 *    支付..成功。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-10-10   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PHPPaySuccessActivity extends BaseTitleActivity {

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_pay_success;
	}

	@Override
	protected void findViewById2T() {
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("支付成功");
		showBackwardView("", SHOW_ICON_DEFAULT);
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.btn_pay_success).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 支付成功
		case R.id.btn_pay_success:
			finishActivity();
			break;
		default:
			break;
		}
	}
}
