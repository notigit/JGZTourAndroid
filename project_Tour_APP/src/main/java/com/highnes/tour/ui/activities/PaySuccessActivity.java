package com.highnes.tour.ui.activities;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.AppManager;
import com.highnes.tour.utils.JsonUtils;

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
public class PaySuccessActivity extends BaseTitleActivity {

	private TextView tvCode, tvName;
	private String mOrderID;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_pay_success;
	}

	@Override
	protected void findViewById2T() {
		tvCode = getViewById(R.id.tv_code);
		tvName = getViewById(R.id.tv_name);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("支付成功");
		showBackwardView("", SHOW_ICON_DEFAULT);
		mOrderID = getIntent().getExtras().getString("mOrderID", "");
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.btn_pay_success).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		showLoading();
		performHandlePostDelayed(0, 8000);
		
	}
	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		requestByOrder();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 支付成功
		case R.id.btn_pay_success:
			AppManager.getAppManager().finishActivity(PayActivity.class);
			finishActivity();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onBackward(View backwardView) {
		super.onBackward(backwardView);
		AppManager.getAppManager().finishActivity(PayActivity.class);
		finishActivity();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			AppManager.getAppManager().finishActivity(PayActivity.class);
			finishActivity();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	// -----------------------网络请求-----------------------

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrder() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("OrderID", mOrderID);
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_DETAIL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonGroup(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonGroup(String result) {
		try {
			proCode(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 门票处理
	 * 
	 * @param result
	 */
	private void proCode(String result) {
		String GoodsName = JsonUtils.getString(result, "GoodsName", "");
		String SoldNum = JsonUtils.getString(result, "SoldNum", "");
		tvCode.setText(SoldNum);
		tvName.setText(GoodsName);
	}

}
