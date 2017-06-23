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
import android.widget.LinearLayout;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.tour.Tour0HotAdapter;
import com.highnes.tour.adapter.home.tour.Tour0TicketAdapter;
import com.highnes.tour.beans.home.tour.Tour0Info;
import com.highnes.tour.beans.home.tour.Tour0Info.SoldInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.home.ticket.FilterTicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.fragment.base.BaseFragment;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.listview.MyListView;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.highnes.tour.view.pull.PullableScrollView;

/**
 * <PRE>
 * 作用:
 *    首页..旅游度假..景区门票。
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
public class Tour0Fragment extends BaseFragment implements OnRefreshListener {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_TOUR0 = "com.highnes.tour.action_callback_tour0";
	private PullableScrollView svRoot;
	private PullToRefreshLayout pull;
	// 景区门票
	private Tour0TicketAdapter ticketAdapter;
	private MyListView lvTicket;
	// 人气门票
	private Tour0HotAdapter hotAdapter;
	private MyListView lvHot;

	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;
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
		intentFilter.addAction(ACTION_CALLBACK_TOUR0);
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
		return R.layout.fragment_home_tour0;
	}

	@Override
	protected void findViewById() {
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		lvTicket = getViewById(R.id.lv_ticket);
		lvHot = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		lvTicket.setFocusable(false);
		svRoot.smoothScrollTo(0, 20);
		pull.setHasPullDown(false);
		pull.setHasPullUp(false);
		registBoradcast();

		ticketAdapter = new Tour0TicketAdapter(mContext);
		lvTicket.setAdapter(ticketAdapter);

		hotAdapter = new Tour0HotAdapter(mContext);
		lvHot.setAdapter(hotAdapter);
	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		getViewById(R.id.tv_ticket0_more).setOnClickListener(this);
		getViewById(R.id.tv_ticket1_more).setOnClickListener(this);
		setTicketOnItem();
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 200);

	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		page = 1;
		requestByTicket();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 更多
		case R.id.tv_ticket0_more:
		case R.id.tv_ticket1_more: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "");
			bundle.putString("nearbyValue", "");
			bundle.putString("themeType", "");
			bundle.putString("themeValue", "");
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		requestByTicket();

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		if (page + 1 <= pageTotle) {
			++page;
			requestByTicket();
		} else {
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
		}

	}

	/**
	 * 门票的点击事件
	 */
	private void setTicketOnItem() {
		ticketAdapter.setmOnCellItem(new Tour0TicketAdapter.OnCellItem() {

			@Override
			public void onItem(Tour0Info.HotSoldInfo info) {
				Bundle bundle = new Bundle();
				bundle.putString("mAreaID", info.AreaID);
				openActivity(TicketDetailActivity.class, bundle);
			}
		});
		lvHot.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour0Info.SoldInfo info = (SoldInfo) lvHot.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mAreaID", info.AreaID);
				openActivity(TicketDetailActivity.class, bundle);
			}
		});
	}

	/**
	 * Ticket 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByTicket() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("City", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()));
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_0, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonTicket(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.FAIL);
				pull.loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				initTicket(null);
				initHot(null);
				llShowData.setVisibility(View.VISIBLE);
			}
		}, paramDatas);
	}

	/**
	 * Ticket JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonTicket(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			final Tour0Info info = GsonUtils.json2T(result, Tour0Info.class);
			int pages = Integer.valueOf(info.SoldConut) / pagesize;
			pageTotle = Integer.valueOf(info.SoldConut) % pagesize == 0 ? pages : pages + 1;
			pull.refreshFinish(PullToRefreshLayout.SUCCEED);
			pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			initTicket(info.HotSold);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					initHot(info.Sold);
				}
			}, 200);
			if ((info.HotSold == null || info.HotSold.size() == 0) && (info.Sold == null || info.Sold.size() == 0)) {
				llShowData.setVisibility(View.VISIBLE);
			} else {
				llShowData.setVisibility(View.GONE);
			}

		} else {
			pull.refreshFinish(PullToRefreshLayout.NO_DATA);
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
			initTicket(null);
			initHot(null);
		}
	}

	private void initTicket(List<Tour0Info.HotSoldInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_ticket).setVisibility(View.VISIBLE);
				lvTicket.setVisibility(View.VISIBLE);
				if (1 == page) {
					ticketAdapter.setList(info);
				}
			} else {
				getViewById(R.id.ll_ticket).setVisibility(View.GONE);
				lvTicket.setVisibility(View.GONE);
				ticketAdapter.removeAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHot(List<Tour0Info.SoldInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_hot).setVisibility(View.VISIBLE);
				lvHot.setVisibility(View.VISIBLE);
				if (1 == page) {
					hotAdapter.removeAll();
					hotAdapter.setList(info);
				} else {
					hotAdapter.addAll(info);
				}
			} else {
				getViewById(R.id.ll_hot).setVisibility(View.GONE);
				lvHot.setVisibility(View.GONE);
				hotAdapter.removeAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
