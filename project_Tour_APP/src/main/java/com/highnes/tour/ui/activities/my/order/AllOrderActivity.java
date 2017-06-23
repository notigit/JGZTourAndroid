package com.highnes.tour.ui.activities.my.order;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;

/**
 * <PRE>
 * 作用:
 *    所有订单。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-18   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class AllOrderActivity extends BaseTitleActivity {

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_all_order;
	}

	@Override
	protected void findViewById2T() {
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("所有订单");
		showBackwardView("", SHOW_ICON_DEFAULT);

	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_ticket).setOnClickListener(this);
		getViewById(R.id.ll_tour).setOnClickListener(this);
		getViewById(R.id.ll_yeying).setOnClickListener(this);
		getViewById(R.id.ll_hotel).setOnClickListener(this);
		getViewById(R.id.ll_shop).setOnClickListener(this);
		getViewById(R.id.ll_nearby).setOnClickListener(this);
		getViewById(R.id.ll_qianggou).setOnClickListener(this);
		getViewById(R.id.ll_yule).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 门票
		case R.id.ll_ticket: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "门票");
			openActivity(AllOrderListActivity.class, bundle);
		}
			break;
		// 旅游
		case R.id.ll_tour: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "旅游");
			openActivity(AllOrderListActivity.class, bundle);
		}
			break;
		// 野营
		case R.id.ll_yeying: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "野营");
			openActivity(AllOrderListActivity.class, bundle);
		}
			break;
		// 抢购
		case R.id.ll_qianggou: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "抢购");
			openActivity(AllOrderListActivity.class, bundle);
		}
			break;
		// 酒店
		case R.id.ll_hotel: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "酒店");
			bundle.putString("types", "2");
			openActivity(PHPOrderListActivity.class, bundle);
		}
			break;
		// 特产
		case R.id.ll_shop: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "土特产");
			bundle.putString("types", "1");
			openActivity(PHPOrderListActivity.class, bundle);
		}
			break;
		// 美食
		case R.id.ll_nearby: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "美食");
			bundle.putString("types", "3");
			openActivity(PHPOrderListActivity.class, bundle);
		}
			break;
		// 休闲娱乐
		case R.id.ll_yule: {
			Bundle bundle = new Bundle();
			bundle.putString("name", "休闲娱乐");
			bundle.putString("types", "4");
			openActivity(PHPOrderListActivity.class, bundle);
		}
			break;
		default:
			break;
		}
	}
}
