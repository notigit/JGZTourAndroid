package com.highnes.tour.ui.fragment.tour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.tour.Tour7Adapter;
import com.highnes.tour.beans.home.tour.Tour7Info;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7ListActivity;
import com.highnes.tour.ui.fragment.base.BaseFragment;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.highnes.tour.view.pull.PullableScrollView;

/**
 * <PRE>
 * 作用:
 *    首页..旅游度假..野营。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-09   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class Tour7Fragment extends BaseFragment implements OnRefreshListener {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_TOUR7 = "com.highnes.tour.action_callback_tour7";
	private PullableScrollView svRoot;
	private PullToRefreshLayout pull;

	private Tour7Adapter locAdapter;
	private ListView lvLoc;
	// 没有数据的时候显示
	private LinearLayout llShowData;

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unregisterReceiver();
	}

	// 注销广播
	private void unregisterReceiver() {
		mContext.unregisterReceiver(updateReceiver);
	}

	/**
	 * 注册一个广播接收器
	 */
	private void registBoradcast() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CALLBACK_TOUR7);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			performHandlePostDelayed(0, 200);
		}

	};

	@Override
	protected @LayoutRes
	int getLayoutId() {
		return R.layout.fragment_home_tour7;
	}

	@Override
	protected void findViewById() {
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		lvLoc = getViewById(R.id.lv_loc);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		lvLoc.setFocusable(false);
		svRoot.smoothScrollTo(0, 20);
		registBoradcast();
		pull.setHasPullDown(false);
		pull.setHasPullUp(false);
		locAdapter = new Tour7Adapter(mContext);
		lvLoc.setAdapter(locAdapter);

	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		getViewById(R.id.tv_loc_more).setOnClickListener(this);

		lvLoc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour7Info.CampingInfo info = (Tour7Info.CampingInfo) lvLoc.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mTourID", info.ID);
				openActivity(Tour7DetailActivity.class, bundle);
			}
		});
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 200);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 本地
		case R.id.tv_loc_more:
			openActivity(Tour7ListActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		requestByData();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		requestByData();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pull.refreshFinish(PullToRefreshLayout.SUCCEED);
		pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}

	/**
	 * Data 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByData() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("City", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_7, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonData(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.FAIL);
				pull.loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				llShowData.setVisibility(View.VISIBLE);
				initLoc(null);
			}
		}, paramDatas);
	}

	/**
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonData(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			final Tour7Info info = GsonUtils.json2T(result, Tour7Info.class);
			pull.refreshFinish(PullToRefreshLayout.SUCCEED);
			pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			initLoc(info.Camping);
			if (info.Camping == null || info.Camping.size() == 0) {
				llShowData.setVisibility(View.VISIBLE);
			} else {
				llShowData.setVisibility(View.GONE);
			}

		} else {
			pull.refreshFinish(PullToRefreshLayout.NO_DATA);
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
			initLoc(null);
		}
	}

	/**
	 * 本地
	 * 
	 * @param info
	 */
	private void initLoc(List<Tour7Info.CampingInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_loc).setVisibility(View.VISIBLE);
				getViewById(R.id.view_loc).setVisibility(View.VISIBLE);
				lvLoc.setVisibility(View.VISIBLE);
				locAdapter.setList(info);
			} else {
				getViewById(R.id.ll_loc).setVisibility(View.GONE);
				getViewById(R.id.view_loc).setVisibility(View.GONE);
				lvLoc.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
