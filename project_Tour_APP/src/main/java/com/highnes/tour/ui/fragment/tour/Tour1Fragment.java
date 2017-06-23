package com.highnes.tour.ui.fragment.tour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.tour.Tour1AreaAdapter;
import com.highnes.tour.adapter.home.tour.Tour1HotAdapter;
import com.highnes.tour.beans.home.tour.Tour1Info;
import com.highnes.tour.beans.home.tour.Tour1Info.TouAreaInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.home.tour.FilterTour1Activity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailActivity;
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
 *    首页..旅游度假..周边游。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-31   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class Tour1Fragment extends BaseFragment implements OnRefreshListener {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_TOUR1 = "com.highnes.tour.action_callback_tour1";
	private PullableScrollView svRoot;
	private PullToRefreshLayout pull;
	// 标题
	private TextView tvLoc;

	private Tour1HotAdapter hotAdapter;
	private GridView gvHot;
	private Tour1AreaAdapter locAdapter, nearbyAdapter;
	private ListView lvLoc, lvNearby;
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
		intentFilter.addAction(ACTION_CALLBACK_TOUR1);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String city = SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()).toString();
			tvLoc.setText(city + "景点");
			performHandlePostDelayed(0, 200);
		}

	};

	@Override
	protected @LayoutRes
	int getLayoutId() {
		return R.layout.fragment_home_tour1;
	}

	@Override
	protected void findViewById() {
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		gvHot = getViewById(R.id.gv_hot);
		lvLoc = getViewById(R.id.lv_loc);
		lvNearby = getViewById(R.id.lv_nearby);
		llShowData = getViewById(R.id.ll_class_hint);
		tvLoc = getViewById(R.id.tv_loc);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		lvLoc.setFocusable(false);
		lvNearby.setFocusable(false);
		svRoot.smoothScrollTo(0, 20);
		registBoradcast();
		pull.setHasPullDown(false);
		pull.setHasPullUp(false);
		String city = SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()).toString();
		tvLoc.setText(city + "景点");

		hotAdapter = new Tour1HotAdapter(mContext);
		gvHot.setAdapter(hotAdapter);

		locAdapter = new Tour1AreaAdapter(mContext);
		lvLoc.setAdapter(locAdapter);

		nearbyAdapter = new Tour1AreaAdapter(mContext);
		lvNearby.setAdapter(nearbyAdapter);
	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		getViewById(R.id.tv_loc_more).setOnClickListener(this);
		getViewById(R.id.tv_nearby_more).setOnClickListener(this);

		gvHot.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour1Info.TouAreaInfo info = (TouAreaInfo) gvHot.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("nearbyType", "hot");
				bundle.putString("nearbyValue", info.AreaName);
				openActivity(FilterTour1Activity.class, bundle);
			}
		});

		lvLoc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour1Info.TourismsInfo info = (Tour1Info.TourismsInfo) lvLoc.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mTourID", info.ID);
				openActivity(Tour1DetailActivity.class, bundle);
			}
		});
		lvNearby.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour1Info.TourismsInfo info = (Tour1Info.TourismsInfo) lvNearby.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mTourID", info.ID);
				openActivity(Tour1DetailActivity.class, bundle);
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
		case R.id.tv_loc_more: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "loc");
			bundle.putString("nearbyValue", SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString());
			openActivity(FilterTour1Activity.class, bundle);
		}
			break;
		// 周边
		case R.id.tv_nearby_more: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "nearby");
			bundle.putString("nearbyValue", SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString());
			openActivity(FilterTour1Activity.class, bundle);
		}
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
		paramDatas.put("Landmark", SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()));
		paramDatas.put("Latitude", SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()));
		paramDatas.put("NewCity", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()));
		paramDatas.put("City", SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_1, new NETConnection.SuccessCallback() {

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
				initHot(null);
				initLoc(null);
				initNearby(null);
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
			final Tour1Info info = GsonUtils.json2T(result, Tour1Info.class);
			pull.refreshFinish(PullToRefreshLayout.SUCCEED);
			pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			initHot(info.TouArea);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					initLoc(info.Tourisms);
				}
			}, 200);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					initNearby(info.ZBTourisms);
				}
			}, 400);
			if ((info.TouArea == null || info.TouArea.size() == 0) && (info.Tourisms == null || info.Tourisms.size() == 0)
					&& (info.ZBTourisms == null || info.ZBTourisms.size() == 0)) {
				llShowData.setVisibility(View.VISIBLE);
			} else {
				llShowData.setVisibility(View.GONE);
			}

		} else {
			pull.refreshFinish(PullToRefreshLayout.NO_DATA);
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
			initHot(null);
			initLoc(null);
			initNearby(null);
		}
	}

	/**
	 * 目的地
	 * 
	 * @param info
	 */
	private void initHot(List<Tour1Info.TouAreaInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_tuijian).setVisibility(View.VISIBLE);
				getViewById(R.id.view_hot).setVisibility(View.VISIBLE);
				gvHot.setVisibility(View.VISIBLE);
				hotAdapter.setList(info);
			} else {
				getViewById(R.id.ll_tuijian).setVisibility(View.GONE);
				getViewById(R.id.view_hot).setVisibility(View.GONE);
				gvHot.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 本地
	 * 
	 * @param info
	 */
	private void initLoc(List<Tour1Info.TourismsInfo> info) {
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

	/**
	 * 周边
	 * 
	 * @param info
	 */
	private void initNearby(List<Tour1Info.TourismsInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_nearby).setVisibility(View.VISIBLE);
				lvNearby.setVisibility(View.VISIBLE);
				nearbyAdapter.setList(info);
			} else {
				getViewById(R.id.ll_nearby).setVisibility(View.GONE);
				lvNearby.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
