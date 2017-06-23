package com.highnes.tour.ui.activities.my;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;

/**
 * <PRE>
 * 作用:
 *    收藏。
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
public class CollectActivity extends BaseTitleActivity {

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_collect;
	}

	@Override
	protected void findViewById2T() {
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("全部收藏");
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
			bundle.putInt("mType", 0);
			bundle.putString("mTitle", "门票收藏");
			openActivity(CollectListActivity.class, bundle);
		}
			break;
		// 旅游
		case R.id.ll_tour: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 1);
			bundle.putString("mTitle", "旅游收藏");
			openActivity(CollectListActivity.class, bundle);
		}
			break;
		// 野营
		case R.id.ll_yeying: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 2);
			bundle.putString("mTitle", "野营收藏");
			openActivity(CollectListActivity.class, bundle);
		}
			break;
		// 酒店
		case R.id.ll_hotel: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 2);
			bundle.putString("mTitle", "酒店收藏");
			openActivity(PHPCollectListActivity.class, bundle);
		}
			break;
		// 特产
		case R.id.ll_shop: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 1);
			bundle.putString("mTitle", "土特产收藏");
			openActivity(PHPCollectListActivity.class, bundle);
		}
			break;
		// 美食
		case R.id.ll_nearby: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 3);
			bundle.putString("mTitle", "美食收藏");
			openActivity(PHPCollectListActivity.class, bundle);
		}
			break;
		// 娱乐
		case R.id.ll_yule: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 4);
			bundle.putString("mTitle", "休闲娱乐收藏");
			openActivity(PHPCollectListActivity.class, bundle);
		}
			break;
		default:
			break;
		}
	}

}
