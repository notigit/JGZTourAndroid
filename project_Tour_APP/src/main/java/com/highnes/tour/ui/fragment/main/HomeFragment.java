package com.highnes.tour.ui.fragment.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.highnes.tour.R;
import com.highnes.tour.adapter.home.HomeActivitiesAdapter;
import com.highnes.tour.adapter.home.HomeHotelAdapter;
import com.highnes.tour.adapter.home.HomeTicketAdapter;
import com.highnes.tour.adapter.home.HomeTimesAdapter;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.beans.home.HomeAllInfo.BenefitInfo;
import com.highnes.tour.beans.home.HomeAllInfo.NewsInfo;
import com.highnes.tour.beans.home.HomeAllInfo.SoldInfo;
import com.highnes.tour.beans.home.HomeAllInfo.StretchInfo;
import com.highnes.tour.beans.home.HomeFastInfo;
import com.highnes.tour.beans.home.HotelInfo;
import com.highnes.tour.beans.home.HotelInfo.DataInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.CityActivity;
import com.highnes.tour.ui.activities.PHPWebViewActivity;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.home.MsgActivity;
import com.highnes.tour.ui.activities.home.NewsActivity;
import com.highnes.tour.ui.activities.home.SosActivity;
import com.highnes.tour.ui.activities.home.TimeDetailActivity;
import com.highnes.tour.ui.activities.home.TimeListActivity;
import com.highnes.tour.ui.activities.home.ticket.FilterTicketActivity;
import com.highnes.tour.ui.activities.home.ticket.MapRoadActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketSearchActivity;
import com.highnes.tour.ui.activities.home.tour.TourActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.fragment.base.BaseFragment;
import com.highnes.tour.ui.fragment.poster.PosterFragment;
import com.highnes.tour.ui.fragment.poster.PosterFragment.OnItemClick;
import com.highnes.tour.utils.AMapHelper;
import com.highnes.tour.utils.AMapHelper.MyOnReceiveLocation;
import com.highnes.tour.utils.AnimationUtils;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.SortComparator;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.dialog.BottomDialog;
import com.highnes.tour.view.gridview.MyGridView;
import com.highnes.tour.view.layout.RippleView;
import com.highnes.tour.view.listview.MyListView;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnPullListener;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.highnes.tour.view.pull.PullableScrollView;
import com.highnes.tour.view.pull.PullableScrollView.ScrollViewListener;
import com.highnes.tour.view.textview.AutoTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    首页。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class HomeFragment extends BaseFragment implements OnRefreshListener {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_HOME = "com.highnes.tour.action_callback_home";
	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	// 轮播图
	private PosterFragment mPoster;

	private PullableScrollView svRoot;
	private int maxH;
	private PullToRefreshLayout pull;

	// 中间的菜单
	private RippleView[] rvMenu = new RippleView[8];
	private LinearLayout llTab;

	// 滚动的头条
	private AutoTextView tvNews;

	// 活动
	private HomeActivitiesAdapter activitiesAdapter;
	private MyGridView gvActivities;
	// 限时秒杀
	private HomeTimesAdapter timesAdapter;
	private MyGridView gvTimes;
	public static List<HomeAllInfo.BenefitInfo> timeData = new ArrayList<HomeAllInfo.BenefitInfo>();
	// 景区门票
	private HomeTicketAdapter ticketAdapter;
	private MyListView lvTicket;
	// 热门酒店
	private HomeHotelAdapter hotelAdapter;
	private MyGridView gvHotel;

	// 定位
	private TextView tvCity;
	// 当前的定位信息
	private BDLocation mBdLocation;
	// 头条的线程
	private Thread thread;

	// 有消息的红点
	private ImageView ivDot;

	private List<HomeAllInfo.AdvertisementInfo> mBanner;
	@Override
	public void onResume() {
		super.onResume();
		if (mPoster != null && mBanner!=null) {
			initBanner(mBanner);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if (thread != null) {
				thread.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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
		intentFilter.addAction(ACTION_CALLBACK_HOME);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			performHandlePostDelayed(3, 100);
		}

	};

	@Override
	protected @LayoutRes
	int getLayoutId() {
		return R.layout.fragment_tab_home;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		tvNews = getViewById(R.id.tv_news);
		gvActivities = getViewById(R.id.gv_activities);
		gvTimes = getViewById(R.id.gv_times);
		lvTicket = getViewById(R.id.lv_ticket);
		gvHotel = getViewById(R.id.gv_hotel);
		rvMenu[0] = getViewById(R.id.rv_menu_0);
		rvMenu[1] = getViewById(R.id.rv_menu_1);
		rvMenu[2] = getViewById(R.id.rv_menu_2);
		rvMenu[3] = getViewById(R.id.rv_menu_3);
		rvMenu[4] = getViewById(R.id.rv_menu_4);
		rvMenu[5] = getViewById(R.id.rv_menu_5);
		rvMenu[6] = getViewById(R.id.rv_menu_6);
		rvMenu[7] = getViewById(R.id.rv_menu_7);
		llTab = getViewById(R.id.ll_tab);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		// 滑动透明标题的高度
		maxH = DensityUtils.dip2px(mContext, 150);
		String city = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
		SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), city);
		initTitle(city);

		initOptions();
		//设置是否可以上拉
		pull.setHasPullUp(false);

		// 设置不要自动切换滑动
		gvActivities.setFocusable(false);
		gvTimes.setFocusable(false);
		lvTicket.setFocusable(false);
		gvHotel.setFocusable(false);
		svRoot.smoothScrollTo(0, 20);
		initTab(getFastData());
		registBoradcast();
	}

	/**
	 * 便民服务的菜单数据
	 * 
	 * @return
	 */
	private List<HomeFastInfo> getFastData() {
		List<HomeFastInfo> list = new ArrayList<HomeFastInfo>();
//		list.add(new HomeFastInfo("电子导游", "drawable://" + R.drawable.ic_home_menu8, ""));
		list.add(new HomeFastInfo("违章查询", "drawable://" + R.drawable.ic_home_menu9, ""));
		list.add(new HomeFastInfo("ETC查询", "drawable://" + R.drawable.ic_home_menu10, ""));
		list.add(new HomeFastInfo("路况查询", "drawable://" + R.drawable.ic_home_menu11, ""));
		list.add(new HomeFastInfo("天气查询", "drawable://" + R.drawable.ic_home_menu12, ""));
		list.add(new HomeFastInfo("资费查询", "drawable://" + R.drawable.ic_home_menu13, ""));
		list.add(new HomeFastInfo("航班动态", "drawable://" + R.drawable.ic_home_menu14, ""));
		list.add(new HomeFastInfo("道路救援", "drawable://" + R.drawable.ic_home_menu15, ""));
		return list;
	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		setOnPullListener();
		setScrollViewListener();
		tvNews.setOnClickListener(this);
		for (int i = 0; i < rvMenu.length; i++) {
			rvMenu[i].setOnClickListener(this);
		}
		getViewById(R.id.tv_ticket_more).setOnClickListener(this);
		getViewById(R.id.tv_hotel_more).setOnClickListener(this);
		getViewById(R.id.tv_time_more).setOnClickListener(this);
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 200); // 首页接口
		performHandlePostDelayed(1, 800); // 定位
		performHandlePostDelayed(2, 1000); // 酒店
		performHandlePostDelayed(3, 1200); // 消息
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		if (0 == type) {
			String jsonHome = SPUtils.get(mContext, DefaultData.JSON_HOME, "").toString();
			if (!StringUtils.isEmpty(jsonHome)) {
				parseJsonHomeAll(jsonHome);
			}
		} else if (1 == type) {
			super.performHandlePostDelayedCallBack(type);
			AMapHelper helper = new AMapHelper();
			helper.startLocation(mContext, new MyOnReceiveLocation() {

				@Override
				public void onReceiveLocation(BDLocation location) {
					mBdLocation = location;
					if (mBdLocation != null) {
						LogUtils.d("--定位成功！经度：" + mBdLocation.getLongitude() + "，纬度：" + mBdLocation.getLatitude() + ",城市：" + mBdLocation.getCity());
						SPUtils.put(mContext, PreSettings.USER_LAT.getId(), mBdLocation.getLatitude());
						SPUtils.put(mContext, PreSettings.USER_LNG.getId(), mBdLocation.getLongitude());
						String city = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
						if (!StringUtils.isEmpty(mBdLocation.getCity()) && !city.equals(mBdLocation.getCity())) {
							// 当前城市不同
							displayLocDialog(mBdLocation.getCity());
						} else {
							requestByHomeAll(true, city);
						}
					} else {
						LogUtils.e("--定位失败！");
						String cityName = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
						requestByHomeAll(false, cityName);
					}
				}
			});
		} else if (2 == type) {
			requestByHotel();
		} else {
			proLogicMsg();
		}
	}

	/**
	 * 初始化tab
	 * 
	 * @param channelList
	 *            菜单
	 */
	@SuppressLint("InflateParams")
	private void initTab(List<HomeFastInfo> channelList) {
		for (int i = 0; i < channelList.size(); i++) {
			View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_fast_item, null);
			RippleView rvFast = (RippleView) view.findViewById(R.id.rv_root);
			TextView tvName = (TextView) view.findViewById(R.id.tv_item_name);
			ImageView ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			rvFast.setTag(i);
			tvName.setText(channelList.get(i).name);
			Log.e("TAG", "initTab: "+channelList.get(i).imgUrl );
			ImageLoader.getInstance().displayImage(channelList.get(i).imgUrl, ivImg, options, null);
			rvFast.setOnClickListener(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.width = (int) DensityUtils.dp2pxF(mContext, 70);
			lp.height = lp.width;
			view.setLayoutParams(lp);
			llTab.addView(view);
		}
	}

	/**
	 * 显示位置不同的提示框
	 */
	private void displayLocDialog(final String city) {
		showDialog("定位到您当前的位置是" + city + "\n是否立即切换？", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (2 == which) {
					tvCity.setText(city);
					SPUtils.put(mContext, PreSettings.USER_CITY.getId(), city);
					requestByHomeAll(true, city);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 门票
		case R.id.rv_menu_0:
			openActivity(TicketActivity.class);
			break;
		// 旅游度假
		case R.id.rv_menu_1:
			openActivity(TourActivity.class);
			break;
		// 城市选择
		case R.id.button_backward_txt19:
		case R.id.button_down_img19:
		case R.id.button_backward_txt:
		case R.id.button_down_img:
			Intent intent = new Intent(mContext, CityActivity.class);
			startActivityForResult(intent, DefaultData.REQUEST_CODE);
			break;
		// 点击头条
		case R.id.tv_news:
			openActivity(NewsActivity.class);
			break;
		// 便民菜单
		case R.id.rv_root:
			proLogicFast((int) (v).getTag());
			break;
		// 门票更多
		case R.id.tv_ticket_more: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "");
			bundle.putString("nearbyValue", "");
			bundle.putString("themeType", "");
			bundle.putString("themeValue", "");
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		// 酒店、酒店更多
		case R.id.rv_menu_2:
		case R.id.tv_hotel_more: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 10001);
			bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_HOTEL_MORE+AppUtils.getTemp());
			openActivity(PHPWebViewActivity.class, bundle);
		}
			break;
		// 消息
		case R.id.button_down_img_right19:
		case R.id.button_down_img_right:
			if (AppUtils.isLogin(mContext)) {
				openActivity(MsgActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 美食
		case R.id.rv_menu_3: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 10002);
			bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_MEISHI+AppUtils.getTemp());
			openActivity(PHPWebViewActivity.class, bundle);
		}
			break;
		// 土特产
		case R.id.rv_menu_6: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 10003);
			bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_TECHAN+AppUtils.getTemp());
			openActivity(PHPWebViewActivity.class, bundle);
		}
			break;
		// 娱乐
		case R.id.rv_menu_7: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 10004);
			bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_YULE+AppUtils.getTemp());
			openActivity(PHPWebViewActivity.class, bundle);
		}
			break;
		// 飞机票
		case R.id.rv_menu_4:
//			LogUtils.d("--飞机票");
//			showNoData();
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 10002);
			bundle.putString("mUrl", UrlSettings.URL_PHP_H5_HOME_MEISHI+AppUtils.getTemp());
			openActivity(PHPWebViewActivity.class, bundle);
			break;
		// 火车票
		case R.id.rv_menu_5:
			showNoData();
			break;
		// 限时抢购更多
		case R.id.tv_time_more:
			openActivity(TimeListActivity.class);
			break;
		// 搜索
		case R.id.ll_search19:
		case R.id.ll_search:
			openActivity(TicketSearchActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 处理便民菜单
	 * 
	 * @param tag
	 */
	private void proLogicFast(int tag) {

		switch (tag) {
		// 电子导游
//		case 0:
//			// TODO 电子导游
//			showNoData();
//			break;
		// 违章查询
		case 0: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 1004);
			bundle.putString("mTitle", "违章查询");
			bundle.putString("mUrl", UrlSettings.URL_H5_HOME_WZCX);
			openActivity(WebViewTitleActivity.class, bundle);
		}
			break;
		// ETC查询
		case 1: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 1005);
			bundle.putString("mTitle", "ETC查询");
			bundle.putString("mUrl", UrlSettings.URL_H5_HOME_ETC);
			openActivity(WebViewTitleActivity.class, bundle);
		}
			break;
		// 路况查询
		case 2:
//			openActivity(MapRoadActivity.class);
			BottomDialog bottomDialog = new BottomDialog(getActivity(),mBdLocation.getLatitude(),mBdLocation.getLongitude(),mBdLocation.getCity(),mBdLocation.getStreet());
			bottomDialog.show();
			break;
		// 天气查询
		case 3: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 1007);
			bundle.putString("mTitle", "天气查询");
			bundle.putString("mUrl", UrlSettings.URL_H5_HOME_TQCX);
			openActivity(WebViewTitleActivity.class, bundle);
		}

			break;
		// 资费查询
		case 4: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 1008);
			bundle.putString("mTitle", "资费查询");
			bundle.putString("mUrl", UrlSettings.URL_H5_HOME_ZCCX);
			openActivity(WebViewTitleActivity.class, bundle);
		}
			break;
		// 航班动态
		case 5: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 1009);
			bundle.putString("mTitle", "航班动态");
			bundle.putString("mUrl", UrlSettings.URL_H5_HOME_HBDT);
			openActivity(WebViewTitleActivity.class, bundle);
		}
			break;
		// 道路救援
		case 6:
			openActivity(SosActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DefaultData.REQUEST_CODE) {
			getActivity();
			if (resultCode == Activity.RESULT_OK) {
				String cityName = data.getExtras().getString(DefaultData.EXTRA_RESULT, "重庆市");
				String currCity = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
				tvCity.setText(cityName);
				if (currCity.equals(cityName)) {
					// 当前的城市
					requestByHomeAll(true, cityName);
				} else {
					SPUtils.put(mContext, PreSettings.USER_CITY.getId(), cityName);
					SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), cityName);
					requestByHomeAll(true, cityName);
				}

			}
		}
	}

	private void proLogicMsg() {
		if (AppUtils.isLogin(mContext)) {
			requestByOrderMsg();
		} else {
			ivDot.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 抢购的点击事件
	 */
	private void setTimeOnItem() {
		timesAdapter.setmOnCellItem(new HomeTimesAdapter.OnCellItem() {

			@Override
			public void onItem(BenefitInfo info) {
				Bundle bundle = new Bundle();
				bundle.putString("mSoldID", info.SoldID);
				bundle.putString("mID", info.ID);
				bundle.putString("mNewPrice", info.NewPrice + "");
				bundle.putString("mOverTime", info.Odate);
				openActivity(TimeDetailActivity.class, bundle);
			}
		});
	}

	/**
	 * 活动的点击事件
	 */
	private void setActivitiesOnItem() {
		activitiesAdapter.setmOnCellItem(new HomeActivitiesAdapter.OnCellItem() {

			@Override
			public void onItem(StretchInfo info) {
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 1002);
				bundle.putString("mTitle", "活动详情");
				bundle.putString("mUrl", UrlSettings.URL_H5_HOMEE_ACTIVITIES_DETAIL + info.StretchID);
				openActivity(WebViewTitleActivity.class, bundle);
			}
		});
	}

	/**
	 * 门票的点击事件
	 */
	private void setTicketOnItem() {
		ticketAdapter.setmOnCellItem(new HomeTicketAdapter.OnCellItem() {

			@Override
			public void onItem(SoldInfo info) {
				Bundle bundle = new Bundle();
				bundle.putString("mAreaID", info.AreaID);
				openActivity(TicketDetailActivity.class, bundle);
			}
		});
	}

	/**
	 * 酒店的点击事件
	 */
	private void setHotelOnItem() {
		// TODO
		hotelAdapter.setmOnCellItem(new HomeHotelAdapter.OnCellItem() {

			@Override
			public void onItem(DataInfo info) {
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 10000);
				bundle.putString("mUrl", info.url);
				openActivity(PHPWebViewActivity.class, bundle);
			}
		});
	}

	/**
	 * 标题初始化
	 */
	private void initTitle(String city) {
		//当前sdk版本是否大于19 4.4.4
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		ImageView ivDown, ivRight;
		LinearLayout llSearch;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			tvCity = (TextView) vTitle.findViewById(R.id.button_backward_txt19);
			ivDown = (ImageView) vTitle.findViewById(R.id.button_down_img19);
			ivRight = (ImageView) vTitle.findViewById(R.id.button_down_img_right19);
			ivDot = (ImageView) vTitle.findViewById(R.id.button_dot_right19);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			tvCity = (TextView) vTitle.findViewById(R.id.button_backward_txt);
			ivDown = (ImageView) vTitle.findViewById(R.id.button_down_img);
			ivRight = (ImageView) vTitle.findViewById(R.id.button_down_img_right);
			ivDot = (ImageView) vTitle.findViewById(R.id.button_dot_right);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);
		}
		tvCity.setText(city);
		tvCity.setTextColor(ResHelper.getColorById(mContext, R.color.white));
		tvCity.setVisibility(View.VISIBLE);
		tvCity.setOnClickListener(this);
		ivDown.setOnClickListener(this);
		ivDown.setVisibility(View.VISIBLE);
		ivRight.setVisibility(View.VISIBLE);
		ivRight.setImageResource(R.drawable.ic_msg_white);
		ivDot.setVisibility(View.INVISIBLE);
		llSearch.setVisibility(View.VISIBLE);
		llSearch.setOnClickListener(this);
		ivRight.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(255 / maxH, 72, 211, 194));
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		performHandlePostDelayed(1, 800); // 定位
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pull.refreshFinish(PullToRefreshLayout.SUCCEED);
		pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}

	/**
	 * 设置下拉的监听
	 */
	private void setOnPullListener() {
		pull.setOnPullListener(new OnPullListener() {

			@Override
			public void onPull(boolean isPull) {
				if (isPull) {
					// 隐藏title
					llTitle.setAnimation(AnimationUtils.setAnimationZeroToUp(500));
					llTitle.setVisibility(View.GONE);
				} else {
					// 显示title
					llTitle.setAnimation(AnimationUtils.setAnimationUpToZero(500));
					llTitle.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * 设置滑动的监听
	 */
	private void setScrollViewListener() {
		svRoot.setScrollViewListener(new ScrollViewListener() {

			@Override
			public void onScrollChanged(PullableScrollView scrollView, int x, int y, int oldx, int oldy) {
				int alpha = 255 * y / maxH;
				if (alpha >= 0 && alpha <= 255) {
					// 48d3c2
					vTitle.setBackgroundColor(Color.argb(alpha, 72, 211, 194));
				}
				if (y > DensityUtils.dip2px(mContext, 150)) {
					vTitle.setBackgroundColor(Color.argb(255, 72, 211, 194));
				}
			}
		});
	}

	// -----------------------网络请求-----------------------
	/**
	 * HomeAll 网络请求
	 * 
	 * @param isLoc
	 *            是否能定位到数据
	 * @param cityName
	 *            查询的城市
	 * 
	 */
	private void requestByHomeAll(boolean isLoc, String cityName) {

		Map<String, Object> paramDatas = new HashMap<String, Object>();
		String url = "";
		if (isLoc) {
			// 定位到数据
			url = UrlSettings.URL_HOME_ALL_CITY;
			paramDatas.put("City", cityName);
		} else {
			// 定位不到数据
			url = UrlSettings.URL_HOME_ALL;
		}

		showLoading();
		new NETConnection(mContext, url, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				parseJsonHomeAll(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.FAIL);
				pull.loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				initActivities(null);
				initNews(null);
				initTimes(null);
				initTicket(null);
				initBanner(null);
			}
		}, paramDatas);
	}

	/**
	 * HomeAll JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonHomeAll(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final HomeAllInfo info = GsonUtils.json2T(result, HomeAllInfo.class);
				initActivities(info.Stretch);
				initNews(info.News);
				initTimes(info.Benefit);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						initTicket(info.Sold);
						// 对轮播图进行排序。1/2/3
						Collections.sort(info.Advertisement, new SortComparator.HomeTicketComparator());
						mBanner = info.Advertisement;
						initBanner(info.Advertisement);
					}
				}, 200);
				SPUtils.put(mContext, DefaultData.JSON_HOME, result);
			} else {
				initActivities(null);
				initNews(null);
				initTimes(null);
				initTicket(null);
				initBanner(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化门票相关
	 */
	private void initTicket(List<HomeAllInfo.SoldInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.view_show_mp).setVisibility(View.VISIBLE);
				getViewById(R.id.ll_mp).setVisibility(View.VISIBLE);
				lvTicket.setVisibility(View.VISIBLE);
				// 初始化景区门票
				ticketAdapter = new HomeTicketAdapter(mContext);
				lvTicket.setAdapter(ticketAdapter);
				ticketAdapter.setList(info);
				setTicketOnItem();
			} else {
				getViewById(R.id.view_show_mp).setVisibility(View.GONE);
				getViewById(R.id.ll_mp).setVisibility(View.GONE);
				lvTicket.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化活动
	 */
	private void initActivities(List<HomeAllInfo.StretchInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.view_show_activities).setVisibility(View.VISIBLE);
				gvActivities.setVisibility(View.VISIBLE);
				// 初始化活动
				activitiesAdapter = new HomeActivitiesAdapter(mContext);
				gvActivities.setAdapter(activitiesAdapter);
				activitiesAdapter.setList(info);
				setActivitiesOnItem();
			} else {
				getViewById(R.id.view_show_activities).setVisibility(View.GONE);
				gvActivities.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化限时秒杀
	 */
	private void initTimes(List<HomeAllInfo.BenefitInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.view_show_time).setVisibility(View.VISIBLE);
				getViewById(R.id.ll_time).setVisibility(View.VISIBLE);
				gvTimes.setVisibility(View.VISIBLE);
				timeData.clear();
				timeData = info;
				// 初始化限时抢购
				timesAdapter = new HomeTimesAdapter(mContext);
				gvTimes.setAdapter(timesAdapter);
				timesAdapter.setList(timeData);
				handlerTime.sendEmptyMessage(1);
				setTimeOnItem();
			} else {
				getViewById(R.id.view_show_time).setVisibility(View.GONE);
				getViewById(R.id.ll_time).setVisibility(View.GONE);
				gvTimes.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateView(int itemIndex, HomeAllInfo.BenefitInfo info) {
		// 得到第一个可显示控件的位置，
		int visiblePosition = gvTimes.getFirstVisiblePosition();
		// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
		if (itemIndex - visiblePosition >= 0) {
			// 得到要更新的item的view
			View view = gvTimes.getChildAt(itemIndex - visiblePosition);
			// 调用adapter更新界面
			timesAdapter.updateView(view, itemIndex, info);
		}
	}

	boolean isNeedCountTime = false;
	private Handler handlerTime = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				new Thread(new Runnable() {

					@Override
					public void run() {
						isNeedCountTime = false;
						// ①：其实在这块需要精确计算当前时间
						for (int index = 0; index < timeData.size(); index++) {
							HomeAllInfo.BenefitInfo goods = timeData.get(index);
							long time = goods.getRestTime();
							if (time > 1000) {// 判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
								isNeedCountTime = true;
								goods.setRestTime(time - 1000);
							} else {
								goods.setRestTime(0L);
							}
						}
						handlerTime.sendEmptyMessageDelayed(2, 0);
					}
				}).start();

				break;
			case 2:
				// ②：for循环执行的时间
				for (int i = 0; i < timeData.size(); i++) {
					updateView(i, timeData.get(i));
				}
				if (isNeedCountTime) {
					// TODO 然后用1000-（②-①），就赢延迟的时间
					handlerTime.sendEmptyMessageDelayed(1, 1000);
				}
				break;
			}
		}
	};

	/**
	 * 显示Banner
	 */
	private void initBanner(final List<HomeAllInfo.AdvertisementInfo> info) {
		try {
			mPoster = new PosterFragment();
			getFragmentManager().beginTransaction().replace(R.id.poster_container, mPoster).commit();
			if (info != null && info.size() > 0) {
				// 轮播图
				final List<String> list = new ArrayList<String>();
				for (int i = 0; i < info.size(); i++) {
					list.add(UrlSettings.URL_IMAGE + info.get(i).PhotoUrl);
				}
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mPoster.initPoster(list);
						mPoster.setOnItemClick(new OnItemClick() {

							@Override
							public void onItemClick(int position) {
								Bundle bundle = new Bundle();
								bundle.putInt("mType", 1001);
								bundle.putString("mTitle", "详情");
								bundle.putString("mUrl", info.get(position).JumpUrl);
								openActivity(WebViewTitleActivity.class, bundle);

							}
						});
					}
				}, 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化头条的滚动
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void initNews(final List<HomeAllInfo.NewsInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.ll_show_tt).setVisibility(View.VISIBLE);
				getViewById(R.id.view_show_tt).setVisibility(View.VISIBLE);
				if (thread != null) {
					thread.stop();
				}
				thread = new Thread(new Runnable() {
					@Override
					public void run() {
						int currTTIndex = 0;
						while (true) {
							Message msg = new Message();
							msg.what = 0;
							msg.obj = info.get(currTTIndex);
							mHandler.sendMessage(msg);
							currTTIndex = ++currTTIndex % info.size();
							try {
								Thread.sleep(4000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				thread.start();
			} else {
				getViewById(R.id.ll_show_tt).setVisibility(View.GONE);
				getViewById(R.id.view_show_tt).setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		if (0 == msg.what) {
			HomeAllInfo.NewsInfo item = (NewsInfo) msg.obj;
			tvNews.setText(item.NewsType + "~" + item.NewsTitle);
		}
		return super.handleMessage(msg);
	}

	// -----------------------网络请求-----------------------

	/**
	 * Msg 网络请求
	 * 
	 */
	private void requestByOrderMsg() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		new NETConnection(mContext, UrlSettings.URL_HOME_MSG_COUNT, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonOrderMsg(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
			}
		}, paramDatas);
	}

	/**
	 * Order JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonOrderMsg(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				int count = Integer.valueOf(JsonUtils.getString(result, "Count", "0"));
				ivDot.setVisibility(count > 0 ? View.VISIBLE : View.INVISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hotel 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByHotel() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("page", 1);
		paramDatas.put("limit", 3);
		paramDatas.put("lng", SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()));
		paramDatas.put("lat", SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()));
		paramDatas.put("distance", 15000);
		new NETConnection(mContext, UrlSettings.URL_PHP_HOME_HOTEL_LIST, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonHotal(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				getViewById(R.id.view_hotel).setVisibility(View.GONE);
				getViewById(R.id.ll_hotel).setVisibility(View.GONE);
				gvHotel.setVisibility(View.GONE);
			}
		}, paramDatas);
	}

	/**
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonHotal(String result) {
		try {
			if (1 == JsonUtils.getInt(result, "errcode", 0)) {
				final HotelInfo info = GsonUtils.json2T(result, HotelInfo.class);
				if (info.data != null && info.data.size() > 0) {
					getViewById(R.id.view_hotel).setVisibility(View.VISIBLE);
					getViewById(R.id.ll_hotel).setVisibility(View.VISIBLE);
					gvHotel.setVisibility(View.VISIBLE);
					// 热门酒店
					hotelAdapter = new HomeHotelAdapter(mContext);
					gvHotel.setAdapter(hotelAdapter);
					hotelAdapter.setList(info.data);
					setHotelOnItem();
				} else {
					getViewById(R.id.view_hotel).setVisibility(View.GONE);
					getViewById(R.id.ll_hotel).setVisibility(View.GONE);
					gvHotel.setVisibility(View.GONE);
				}
			} else {
				getViewById(R.id.view_hotel).setVisibility(View.GONE);
				getViewById(R.id.ll_hotel).setVisibility(View.GONE);
				gvHotel.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
