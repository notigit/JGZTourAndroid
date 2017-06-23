package com.highnes.tour.ui.activities.home.tour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.ticket.TicketFilterListAdapter;
import com.highnes.tour.adapter.home.tour.Tour1FilterHotAdapter;
import com.highnes.tour.adapter.home.tour.Tour1FilterListAdapter;
import com.highnes.tour.adapter.home.tour.filter.FilterListFilterAdapter;
import com.highnes.tour.adapter.home.tour.filter.FilterListLeftAdapter;
import com.highnes.tour.adapter.home.tour.filter.FilterListRightAdapter;
import com.highnes.tour.adapter.home.tour.filter.FilterListSortAdapter;
import com.highnes.tour.beans.home.tour.Tour1FilterInfo;
import com.highnes.tour.beans.home.tour.Tour1Info;
import com.highnes.tour.beans.home.tour.Tour1FilterInfo.SiftingsInfo.SiftingInfo;
import com.highnes.tour.beans.home.tour.Tour1FilterListInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketSearchActivity;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    首页..旅游度假..周边游..筛选列表。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-15   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FilterTour1Activity extends BaseActivity implements OnRefreshListener {

	// 传递过来的标志 空表示没有，nearby表示周边，loc表示本地
	private String nearbyType = "";
	// nearbyType的值为nearby表示周边的值，loc表示本地的值
	private String nearbyValue = "";

	// 标题
	private LinearLayout llTitle;
	private View vTitle;

	private PullToRefreshLayout pull;
	private Tour1FilterListAdapter ticketAdapter;
	private Tour1FilterHotAdapter hotAdapter;
	private ListView lvTicket;

	private TextView[] tvMenu = new TextView[3];
	// 没有数据的时候显示
	private LinearLayout llShowData;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;

	// -------------------附近-------------------
	private PopupWindow pwNearby;
	private ListView lvNearbyLeft;
	private FilterListLeftAdapter nearbyLeftAdapter;

	private ListView lvNearbyRight;
	private FilterListRightAdapter nearbyRightAdapter;
	List<Tour1FilterInfo.SiftingsInfo.SiftingInfo> nearby0Data;
	List<Tour1FilterInfo.SiftingsInfo.SiftingInfo> nearby1Data;

	// -------------------智能排序---------------
	private PopupWindow pwSort;
	private ListView lvSort;
	private FilterListSortAdapter sortAdapter;
	List<Tour1FilterInfo.SiftingsInfo.SiftingInfo> sortData;
	// -------------------筛选---------------
	private PopupWindow pwFilter;
	private ListView lvFilter;
	private FilterListFilterAdapter filterAdapter;
	List<Tour1FilterInfo.SiftingsInfo.SiftingInfo> filterData;
	// -------------------值---------------
	// 当前选择的数据 附近/城市/排序/主题
	private String mNumber = "0", mCity = "重庆市", mOrder = "智能排序";
	// 筛选条件
	private ArrayList<String> mSift = new ArrayList<String>();
	// 对应的默认值
	private String dNumber = "0", dCity = "重庆市", dOrder = "智能排序", dSift = "";

	// -------------------中间值---------------
	// 附近选择的左侧菜单的下标
	private int mNearbyLeftIndex = 0;

	@Override
	@LayoutRes
	protected int getLayoutId() {
		return R.layout.activity_home_tour_1_filter;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		tvMenu[0] = getViewById(R.id.tv_menu_0);
		tvMenu[1] = getViewById(R.id.tv_menu_1);
		tvMenu[2] = getViewById(R.id.tv_menu_2);
		pull = getViewById(R.id.pull);
		lvTicket = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		initTitle();
		nearbyType = getIntent().getExtras().getString("nearbyType", "");
		nearbyValue = getIntent().getExtras().getString("nearbyValue", "");

		// 初始化默认的城市
		mCity = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
		dCity = mCity;

		if ("hot".equals(nearbyType)) {
			// 推荐的门票
			hotAdapter = new Tour1FilterHotAdapter(mContext);
			lvTicket.setAdapter(hotAdapter);
		} else {
			// 推荐的门票
			ticketAdapter = new Tour1FilterListAdapter(mContext);
			lvTicket.setAdapter(ticketAdapter);
		}
	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		setTicketListener();
		tvMenu[0].setOnClickListener(this);
		tvMenu[1].setOnClickListener(this);
		tvMenu[2].setOnClickListener(this);
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 0);
		performHandlePostDelayed(1, 200);

	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		if (0 == type) {
			requestByMenu();
		} else {
			initType();
		}
	}

	private void initType() {
		if (StringUtils.isEmpty(nearbyType)) {
			// 普通进入的筛选列表
			page = 1;
			requestByTicket();
		} else {
			page = 1;
			dismissPwAll(0);
			// 附近条件
			if ("hot".equals(nearbyType)) {
				tvMenu[0].setText("推荐");
				requestByTicketHot();
			} else if ("loc".equals(nearbyType)) {
				tvMenu[0].setText("本地");
				requestByTicketLoc();
			} else {
				tvMenu[0].setText("热门");
				requestByTicketLOC7Nearby();
			}

		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 返回
		case R.id.button_backward_img19:
		case R.id.button_backward_img:
			finishActivity();
			break;
		// 菜单1
		case R.id.tv_menu_0:
			dismissPwAll(0);
			displayPwNearby(v);
			break;
		// 菜单2
		case R.id.tv_menu_1:
			dismissPwAll(1);
			displaySort(v);
			break;
		// 菜单3
		case R.id.tv_menu_2:
			dismissPwAll(2);
			displayFilter(v);
			break;
		// 筛选中的确定按钮
		case R.id.btn_submit:
			page = 1;
			requestByTicket();
			if (pwFilter != null && pwFilter.isShowing()) {
				pwFilter.dismiss();
			}
			break;
		// 搜索
		case R.id.ll_search19:
		case R.id.ll_search:
			// TODO 搜索
			openActivity(TicketSearchActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置推荐列表的点击事件
	 */
	private void setTicketListener() {
		lvTicket.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Tour1FilterListInfo.TourismsInfo info = (Tour1FilterListInfo.TourismsInfo) lvTicket.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mTourID", info.ID);
				openActivity(Tour1DetailActivity.class, bundle);
			}
		});
	}

	/**
	 * 标题初始化
	 */
	private void initTitle() {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		ImageView ivBack;
		LinearLayout llSearch;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search19);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);
		}
		ivBack.setVisibility(View.VISIBLE);
		llSearch.setVisibility(View.GONE);
		llSearch.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(255, 72, 211, 194));
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		initType();
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

	// ------------------------------ 菜单 ------------------------------

	/**
	 * 判断显示附近菜单
	 */
	private void displayPwNearby(View view) {
		try {
			if (pwNearby == null) {
				initPwNearby();
				pwNearby.showAsDropDown(view, 0, 0);
				nearbyLeftAdapter = new FilterListLeftAdapter(mContext);
				String[] stringLeft = getResources().getStringArray(R.array.ticket_menu_nearby_left_hot);
				lvNearbyLeft.setAdapter(nearbyLeftAdapter);
				nearbyLeftAdapter.setList(Arrays.asList(stringLeft));
				if (nearbyLeftAdapter.getCount() > 0) {
					nearbyLeftAdapter.setIndex(0);
				}
				nearbyRightAdapter = new FilterListRightAdapter(mContext);
				lvNearbyRight.setAdapter(nearbyRightAdapter);
				nearbyRightAdapter.setList(nearby0Data);
			} else {
				if (pwNearby.isShowing()) {
					pwNearby.dismiss();
				} else {
					pwNearby.showAsDropDown(view, 0, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 初始化显示附近菜单
	 */
	private void initPwNearby() {
		try {
			// 获取自定义布局文件pop.xml的视图
			View customView = getLayoutInflater().inflate(R.layout.pw_ticket_menu_list, null, false);
			/** 在这里可以实现自定义视图的功能 */
			lvNearbyLeft = (ListView) customView.findViewById(R.id.lv_left);
			lvNearbyRight = (ListView) customView.findViewById(R.id.lv_right);
			// 创建PopupWindow实例
			pwNearby = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			pwNearby.setAnimationStyle(R.style.AnimationFade);
			// 自定义view添加触摸事件
			customView.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (pwNearby != null && pwNearby.isShowing()) {
						pwNearby.dismiss();
					}
					return false;
				}
			});
			lvNearbyLeft.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					nearbyLeftAdapter.setIndex(position);
					if (0 == position) {
						nearbyRightAdapter.setList(nearby0Data);
						mCity = dCity;
					} else {
						nearbyRightAdapter.setList(nearby1Data);
						mNumber = dNumber;
					}
					mNearbyLeftIndex = position;
				}
			});
			lvNearbyRight.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					Tour1FilterInfo.SiftingsInfo.SiftingInfo item = (Tour1FilterInfo.SiftingsInfo.SiftingInfo) lvNearbyRight.getAdapter().getItem(position);
					pwNearby.dismiss();
					nearbyRightAdapter.setIndex(position);
					if (0 == mNearbyLeftIndex) {
						// 选择附近
						if ("-1".equals(item.ID)) {
							tvMenu[0].setText("附近");
							mNumber = dNumber;
						} else {
							tvMenu[0].setText(item.Name);
							mNumber = item.Name;
						}
						page = 1;
						requestByTicket();
					} else {
						// 选择周边
						tvMenu[0].setText(item.Name);
						mCity = item.Name;
						page = 1;
						requestByTicket();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断显示排序
	 */
	private void displaySort(View view) {
		try {
			if (pwSort == null) {
				initpwSort();
				pwSort.showAsDropDown(view, 0, 0);
				sortAdapter = new FilterListSortAdapter(mContext);
				lvSort.setAdapter(sortAdapter);
				sortAdapter.setList(sortData);
				if (sortAdapter.getCount() > 0) {
					sortAdapter.setIndex(0);
				}
			} else {
				if (pwSort.isShowing()) {
					pwSort.dismiss();
				} else {
					pwSort.showAsDropDown(view, 0, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 初始化显示智能排序菜单
	 */
	private void initpwSort() {
		try {
			// 获取自定义布局文件pop.xml的视图
			View customView = getLayoutInflater().inflate(R.layout.pw_ticket_menu_list, null, false);
			/** 在这里可以实现自定义视图的功能 */
			lvSort = (ListView) customView.findViewById(R.id.lv_type);
			customView.findViewById(R.id.ll_single).setVisibility(View.VISIBLE);
			customView.findViewById(R.id.ll_double).setVisibility(View.GONE);
			// 创建PopupWindow实例
			pwSort = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			pwSort.setAnimationStyle(R.style.AnimationFade);
			// 自定义view添加触摸事件
			customView.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (pwSort != null && pwSort.isShowing()) {
						pwSort.dismiss();
					}
					return false;
				}
			});
			lvSort.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					sortAdapter.setIndex(position);
					Tour1FilterInfo.SiftingsInfo.SiftingInfo info = (SiftingInfo) lvSort.getAdapter().getItem(position);
					tvMenu[1].setText(info.Name);
					pwSort.dismiss();
					// 排序
					if ("-1".equals(info.ID)) {
						mOrder = dOrder;
					} else {
						mOrder = info.Name;
					}
					page = 1;
					requestByTicket();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断显示主题
	 */
	private void displayFilter(View view) {
		try {
			if (pwFilter == null) {
				initpwFilter();
				pwFilter.showAsDropDown(view, 0, 0);
				filterAdapter = new FilterListFilterAdapter(mContext);
				lvFilter.setAdapter(filterAdapter);
				filterAdapter.setList(filterData);
				// 这一步的作用是，筛选条件要按顺序传递
				for (int i = 0; i < filterData.size(); i++) {
					mSift.add("");
				}
				// 点击筛选的列表
				filterAdapter.setOnChecked(new FilterListFilterAdapter.OnChecked() {

					@Override
					public void onChecked(Tour1FilterInfo.SiftingsInfo.SiftingInfo info, int position) {
						if (info.isSelect()) {
							mSift.set(position, info.Name);
						} else {
							mSift.set(position, "");
						}
					}
				});
			} else {
				if (pwFilter.isShowing()) {
					pwFilter.dismiss();
				} else {
					pwFilter.showAsDropDown(view, 0, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 初始化显示主题
	 */
	private void initpwFilter() {
		try {
			// 获取自定义布局文件pop.xml的视图
			View customView = getLayoutInflater().inflate(R.layout.pw_ticket_menu_list, null, false);
			/** 在这里可以实现自定义视图的功能 */
			lvFilter = (ListView) customView.findViewById(R.id.lv_filter);
			customView.findViewById(R.id.ll_single).setVisibility(View.GONE);
			customView.findViewById(R.id.ll_double).setVisibility(View.GONE);
			customView.findViewById(R.id.ll_filter).setVisibility(View.VISIBLE);
			customView.findViewById(R.id.btn_submit).setOnClickListener(this);
			// 创建PopupWindow实例
			pwFilter = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			pwFilter.setAnimationStyle(R.style.AnimationFade);
			// 自定义view添加触摸事件
			customView.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (pwFilter != null && pwFilter.isShowing()) {
						pwFilter.dismiss();
					}
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭所有的pw
	 */
	private void dismissPwAll(int index) {
		// 修改字体颜色和箭头的方向
		Drawable navDown = getResources().getDrawable(R.drawable.ic_menu_down);
		navDown.setBounds(0, 0, navDown.getMinimumWidth(), navDown.getMinimumHeight());
		for (int i = 0; i < tvMenu.length; i++) {
			tvMenu[i].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_8a8a8a));
			tvMenu[i].setCompoundDrawables(null, null, navDown, null);
		}
		tvMenu[index].setTextColor(ResHelper.getColorById(mContext, R.color.titlec));
		Drawable navUp = getResources().getDrawable(R.drawable.ic_menu_up);
		navUp.setBounds(0, 0, navUp.getMinimumWidth(), navUp.getMinimumHeight());
		tvMenu[index].setCompoundDrawables(null, null, navUp, null);

		// 隐藏PW
		if (pwNearby != null && pwNearby.isShowing()) {
			pwNearby.dismiss();
		}
		if (pwSort != null && pwSort.isShowing()) {
			pwSort.dismiss();
		}
		if (pwFilter != null && pwFilter.isShowing()) {
			pwFilter.dismiss();
		}
	}

	// -----------------------网络请求-----------------------
	/**
	 * Menu 网络请求
	 * 
	 */
	private void requestByMenu() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_1_FILTER_MENU, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonMenu(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Menu JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonMenu(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			final Tour1FilterInfo info = GsonUtils.json2T(result, Tour1FilterInfo.class);
			if (info != null) {
				// 排序数据处理
				sortData = new ArrayList<Tour1FilterInfo.SiftingsInfo.SiftingInfo>();
				sortData.add(new Tour1FilterInfo.SiftingsInfo.SiftingInfo("-1", "智能排序"));
				// 筛选
				filterData = new ArrayList<Tour1FilterInfo.SiftingsInfo.SiftingInfo>();
				// 附近
				nearby0Data = new ArrayList<Tour1FilterInfo.SiftingsInfo.SiftingInfo>();
				nearby0Data.add(new Tour1FilterInfo.SiftingsInfo.SiftingInfo("-1", "附近(智能范围)"));
				// 热门
				nearby1Data = new ArrayList<Tour1FilterInfo.SiftingsInfo.SiftingInfo>();
				for (int i = 0; i < info.Siftings.size(); i++) {
					if ("排序".equals(info.Siftings.get(i).Type)) {
						sortData.addAll(info.Siftings.get(i).sifting);
					} else if ("筛选".equals(info.Siftings.get(i).Type)) {
						filterData.addAll(info.Siftings.get(i).sifting);
					} else if ("附近".equals(info.Siftings.get(i).Type)) {
						nearby0Data.addAll(info.Siftings.get(i).sifting);
					}
				}
				for (int i = 0; i < info.HotCity.size(); i++) {
					nearby1Data.add(new Tour1FilterInfo.SiftingsInfo.SiftingInfo(info.HotCity.get(i).HID, info.HotCity.get(i).CtiyName));
				}
			}
		}
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
		// 距离
		if (mNumber.equals(dNumber)) {
			paramDatas.put("Number", dNumber);
		} else {
			paramDatas.put("Number", mNumber);
		}
		// 城市
		if (mCity.equals(dCity)) {
			paramDatas.put("City", dCity);
			paramDatas.put("Latitude", SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()));
			paramDatas.put("Landmark", SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()));
		} else {
			paramDatas.put("City", mCity);
			paramDatas.put("Latitude", "0.0");
			paramDatas.put("Landmark", "0.0");
		}
		// 排序
		if (mOrder.equals(dOrder)) {
			paramDatas.put("Order", dOrder);
		} else {
			paramDatas.put("Order", mOrder);
		}
		// 筛选
		if (mSift.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mSift.size(); i++) {
				sb.append(mSift.get(i));
			}
			paramDatas.put("Sift", sb.toString());
		} else {
			paramDatas.put("Sift", dSift);
		}
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_1_FILTER_LIST, new NETConnection.SuccessCallback() {

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
				// 显示如果没有数据的时候的处理
				llShowData.setVisibility(View.VISIBLE);
			}
		}, paramDatas);
	}

	/**
	 * Ticket 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByTicketLOC7Nearby() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("City", nearbyValue);
		paramDatas.put("Latitude", SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()));
		paramDatas.put("Landmark", SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()));
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_1_FILTER_LIST_NEARBY, new NETConnection.SuccessCallback() {

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
				// 显示如果没有数据的时候的处理
				llShowData.setVisibility(View.VISIBLE);
			}
		}, paramDatas);
	}

	/**
	 * Ticket 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByTicketHot() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("AreaName", nearbyValue);
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_1_FILTER_LIST_AREA, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonTicketHot(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.FAIL);
				pull.loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				// 显示如果没有数据的时候的处理
				llShowData.setVisibility(View.VISIBLE);
			}
		}, paramDatas);
	}

	/**
	 * Ticket 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByTicketLoc() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("City", nearbyValue);
		paramDatas.put("NewCity", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()));
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_1_FILTER_LIST_LOC, new NETConnection.SuccessCallback() {

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
				// 显示如果没有数据的时候的处理
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
		try {

			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final Tour1FilterListInfo info = GsonUtils.json2T(result, Tour1FilterListInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (ticketAdapter == null) {
					ticketAdapter = new Tour1FilterListAdapter(mContext);
					lvTicket.setAdapter(ticketAdapter);
				}
				if (info.Tourisms != null && info.Tourisms.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						ticketAdapter.removeAll();
						ticketAdapter.setList(info.Tourisms);
					} else {
						ticketAdapter.addAll(info.Tourisms);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					ticketAdapter.removeAll();
					// 显示如果没有数据的时候的处理
					llShowData.setVisibility(View.VISIBLE);
				}
			} else {
				pull.refreshFinish(PullToRefreshLayout.NO_DATA);
				pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ticket JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonTicketHot(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final Tour1FilterListInfo info = GsonUtils.json2T(result, Tour1FilterListInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Tourism != null && info.Tourism.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						hotAdapter.removeAll();
						hotAdapter.setList(info.Tourism);
					} else {
						hotAdapter.addAll(info.Tourism);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					hotAdapter.removeAll();
					// 显示如果没有数据的时候的处理
					llShowData.setVisibility(View.VISIBLE);
				}
			} else {
				pull.refreshFinish(PullToRefreshLayout.NO_DATA);
				pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
