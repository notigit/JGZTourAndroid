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

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.tour.Tour2ChinaAdapter;
import com.highnes.tour.adapter.home.tour.Tour2TopAdapter;
import com.highnes.tour.beans.home.tour.Tour2Info;
import com.highnes.tour.beans.home.tour.Tour2Info.HotAreaInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.home.tour.FilterTour2Activity;
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
 *    首页..旅游度假..国外游。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-05   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class Tour3Fragment extends BaseFragment implements OnRefreshListener {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_TOUR3 = "com.highnes.tour.action_callback_tour3";
	private PullableScrollView svRoot;
	private PullToRefreshLayout pull;

	// 人气排行榜
	private Tour2TopAdapter topAdapter;
	private GridView gvTop;

	// 热门国内游
	private Tour2ChinaAdapter chinaAdapter;
	private ListView lvChina;

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
		intentFilter.addAction(ACTION_CALLBACK_TOUR3);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 暂不需要处理选择了城市
			// performHandlePostDelayed(0, 200);
		}

	};

	@Override
	protected @LayoutRes
	int getLayoutId() {
		return R.layout.fragment_home_tour3;
	}

	@Override
	protected void findViewById() {
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		gvTop = getViewById(R.id.gv_hot_top);
		lvChina = getViewById(R.id.lv_loc);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		gvTop.setFocusable(false);
		lvChina.setFocusable(false);
		svRoot.smoothScrollTo(0, 20);
		registBoradcast();
		pull.setHasPullUp(false);
		pull.setHasPullDown(false);

		topAdapter = new Tour2TopAdapter(mContext);
		gvTop.setAdapter(topAdapter);

		chinaAdapter = new Tour2ChinaAdapter(mContext);
		lvChina.setAdapter(chinaAdapter);

	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		getViewById(R.id.tv_hot_top_more).setOnClickListener(this);
		getViewById(R.id.tv_hot_china_more).setOnClickListener(this);
		gvTop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour2Info.HotAreaInfo info = (HotAreaInfo) gvTop.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("nearbyType", "top");
				bundle.putString("nearbyName", "境外游");
				bundle.putString("nearbyValue", info.AreaName);
				openActivity(FilterTour2Activity.class, bundle);
			}
		});
		lvChina.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour2Info.HotTourismInfo info = (Tour2Info.HotTourismInfo) lvChina.getAdapter().getItem(position);
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
		// 热门境外游
		case R.id.tv_hot_china_more: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "list");
			bundle.putString("nearbyName", "境外游");
			bundle.putString("nearbyValue", SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString());
			openActivity(FilterTour2Activity.class, bundle);
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
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pull.refreshFinish(PullToRefreshLayout.SUCCEED);
		pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByData() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_3, new NETConnection.SuccessCallback() {

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
				initTop(null);
				initChina(null);
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
			final Tour2Info info = GsonUtils.json2T(result, Tour2Info.class);
			pull.refreshFinish(PullToRefreshLayout.SUCCEED);
			pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			initTop(info.HotArea);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					initChina(info.HotTourism);
				}
			}, 200);
			if ((info.HotArea == null || info.HotArea.size() == 0) && (info.HotTourism == null || info.HotTourism.size() == 0)) {
				llShowData.setVisibility(View.VISIBLE);
			} else {
				llShowData.setVisibility(View.GONE);
			}

		} else {
			pull.refreshFinish(PullToRefreshLayout.NO_DATA);
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
			initTop(null);
			initChina(null);
		}
	}

	/**
	 * 人气排行榜
	 * 
	 * @param info
	 */
	private void initTop(List<Tour2Info.HotAreaInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_hot_top).setVisibility(View.VISIBLE);
				gvTop.setVisibility(View.VISIBLE);
				topAdapter.setList(info);
			} else {
				getViewById(R.id.ll_hot_top).setVisibility(View.GONE);
				gvTop.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 国内游
	 * 
	 * @param info
	 */
	private void initChina(List<Tour2Info.HotTourismInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_hot_china).setVisibility(View.VISIBLE);
				lvChina.setVisibility(View.VISIBLE);
				chinaAdapter.setList(info);
			} else {
				getViewById(R.id.ll_hot_china).setVisibility(View.GONE);
				lvChina.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
