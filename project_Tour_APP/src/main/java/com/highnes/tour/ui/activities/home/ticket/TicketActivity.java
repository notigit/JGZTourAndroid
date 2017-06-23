package com.highnes.tour.ui.activities.home.ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.ticket.ActivitiesAdapter;
import com.highnes.tour.adapter.home.ticket.TicketAdapter;
import com.highnes.tour.beans.home.HomeAllInfo.StretchInfo;
import com.highnes.tour.beans.home.ticket.TicketInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.CityActivity;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.ui.fragment.poster.PosterFragment;
import com.highnes.tour.ui.fragment.poster.PosterFragment.OnItemClick;
import com.highnes.tour.utils.AnimationUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.SortComparator;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.layout.RippleView;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnPullListener;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.highnes.tour.view.pull.PullableScrollView;
import com.highnes.tour.view.pull.PullableScrollView.ScrollViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    首页..门票。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-08   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketActivity extends BaseActivity implements OnRefreshListener {
	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	private TextView tvCity;
	// 轮播图
	private PosterFragment mPoster;
	private PullableScrollView svRoot;
	private int maxH;
	private PullToRefreshLayout pull;

	// 菜单
	private RippleView[] rvMenu = new RippleView[4];
	private ImageView[] ivMenu = new ImageView[4];
	private TextView[] tvMenu = new TextView[4];
	List<TicketInfo.AreaTypeInfo> mThemeInfo = new ArrayList<TicketInfo.AreaTypeInfo>();
	// 重庆景点
	private TextView tvMenuTitle;

	// 推荐的门票
	private TicketAdapter ticketAdapter;
	private ListView lvTicket;

	private TicketAdapter nearbyAdapter;
	private ListView lvNearby;

	// 活动
	private ActivitiesAdapter activitiesAdapter;
	private GridView gvActivities;

	// 当前定位的城市
	private String mCity;

	@Override
	@LayoutRes
	protected int getLayoutId() {
		return R.layout.activity_home_ticket;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		lvTicket = getViewById(R.id.lv_ticket);
		lvNearby = getViewById(R.id.lv_nearby);
		gvActivities = getViewById(R.id.gv_activities);
		rvMenu[0] = getViewById(R.id.rv_menu_0);
		rvMenu[1] = getViewById(R.id.rv_menu_1);
		rvMenu[2] = getViewById(R.id.rv_menu_2);
		rvMenu[3] = getViewById(R.id.rv_menu_3);
		ivMenu[0] = getViewById(R.id.iv_menu_0);
		ivMenu[1] = getViewById(R.id.iv_menu_1);
		ivMenu[2] = getViewById(R.id.iv_menu_2);
		ivMenu[3] = getViewById(R.id.iv_menu_3);
		tvMenu[0] = getViewById(R.id.tv_menu_0);
		tvMenu[1] = getViewById(R.id.tv_menu_1);
		tvMenu[2] = getViewById(R.id.tv_menu_2);
		tvMenu[3] = getViewById(R.id.tv_menu_3);
		tvMenuTitle = getViewById(R.id.tv_item_title);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		// 滑动透明标题的高度
		maxH = DensityUtils.dip2px(mContext, 150);
		mCity = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
		SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), mCity);
		initTitle(mCity);
		//是否可以上拉
		pull.setHasPullUp(false);

		// 设置不要自动切换滑动
		lvTicket.setFocusable(false);
		lvNearby.setFocusable(false);
		svRoot.smoothScrollTo(0, 20);

		initOptions();
	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		setOnPullListener();
		setScrollViewListener();
		setTicketListener();
		setNearbyListener();
		getViewById(R.id.ll_menu0).setOnClickListener(this);
		getViewById(R.id.ll_menu1).setOnClickListener(this);
		getViewById(R.id.tv_loc_more).setOnClickListener(this);
		getViewById(R.id.tv_nearby_more).setOnClickListener(this);
		rvMenu[0].setOnClickListener(this);
		rvMenu[1].setOnClickListener(this);
		rvMenu[2].setOnClickListener(this);
		rvMenu[3].setOnClickListener(this);
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		requestByTicket();
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
		// 城市选择
		case R.id.button_forward_txt19:
		case R.id.button_down_img_right19:
		case R.id.button_forward_txt:
		case R.id.button_down_img_right:
			Intent intent = new Intent(mContext, CityActivity.class);
			startActivityForResult(intent, DefaultData.REQUEST_CODE);
			break;
		// 本地景点
		case R.id.tv_loc_more:
		case R.id.ll_menu0: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "loc");
			bundle.putString("nearbyValue", tvCity.getText().toString().trim());
			bundle.putString("themeType", "");
			bundle.putString("themeValue", "");
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		// 周边景点
		case R.id.tv_nearby_more:
		case R.id.ll_menu1: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "nearby");
			bundle.putString("nearbyValue", tvCity.getText().toString().trim());
			bundle.putString("themeType", "");
			bundle.putString("themeValue", "");
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		// 菜单1
		case R.id.rv_menu_0: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "");
			bundle.putString("nearbyValue", "");
			bundle.putString("themeType", mThemeInfo.get(0).ID);
			bundle.putString("themeValue", mThemeInfo.get(0).TypeName);
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		// 菜单2
		case R.id.rv_menu_1: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "");
			bundle.putString("nearbyValue", "");
			bundle.putString("themeType", mThemeInfo.get(1).ID);
			bundle.putString("themeValue", mThemeInfo.get(1).TypeName);
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		// 菜单3
		case R.id.rv_menu_2: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "");
			bundle.putString("nearbyValue", "");
			bundle.putString("themeType", mThemeInfo.get(2).ID);
			bundle.putString("themeValue", mThemeInfo.get(2).TypeName);
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		// 菜单4
		case R.id.rv_menu_3: {
			Bundle bundle = new Bundle();
			bundle.putString("nearbyType", "");
			bundle.putString("nearbyValue", "");
			bundle.putString("themeType", mThemeInfo.get(3).ID);
			bundle.putString("themeValue", mThemeInfo.get(3).TypeName);
			openActivity(FilterTicketActivity.class, bundle);
		}
			break;
		// 搜索
		case R.id.ll_search2_19:
		case R.id.ll_search2:
			openActivity(TicketSearchActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DefaultData.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String cityName = data.getExtras().getString(DefaultData.EXTRA_RESULT, "重庆市");
				SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), cityName);
				tvMenuTitle.setText(cityName + "景点");
				tvCity.setText(cityName);
				requestByTicket();
			}
		}
	}

	/**
	 * 设置推荐列表的点击事件
	 */
	private void setTicketListener() {
		lvTicket.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TicketInfo.SoldInfo info = (com.highnes.tour.beans.home.ticket.TicketInfo.SoldInfo) lvTicket.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mAreaID", info.AreaID);
				openActivity(TicketDetailActivity.class, bundle);
			}
		});
	}

	/**
	 * 设置周边列表的点击事件
	 */
	private void setNearbyListener() {
		lvNearby.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TicketInfo.SoldInfo info = (TicketInfo.SoldInfo) lvNearby.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mAreaID", info.AreaID);
				openActivity(TicketDetailActivity.class, bundle);
			}
		});
	}

	/**
	 * 活动的点击事件
	 */
	private void setActivitiesOnItem() {
		activitiesAdapter.setmOnCellItem(new ActivitiesAdapter.OnCellItem() {

			@Override
			public void onItem(TicketInfo.StretchInfo info) {
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 1100);
				bundle.putString("mTitle", "活动详情");
				bundle.putString("mUrl", UrlSettings.URL_H5_HOMEE_ACTIVITIES_DETAIL + info.StretchID);
				openActivity(WebViewTitleActivity.class, bundle);
			}
		});
	}

	/**
	 * 标题初始化
	 */
	private void initTitle(String city) {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		ImageView ivDown, ivBack;
		LinearLayout llSearch;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			tvCity = (TextView) vTitle.findViewById(R.id.button_forward_txt19);
			ivDown = (ImageView) vTitle.findViewById(R.id.button_down_img_right19);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search2_19);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			tvCity = (TextView) vTitle.findViewById(R.id.button_forward_txt);
			ivDown = (ImageView) vTitle.findViewById(R.id.button_down_img_right);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search2);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);
		}
		tvCity.setText(city);
		tvCity.setTextColor(ResHelper.getColorById(mContext, R.color.white));
		tvCity.setVisibility(View.VISIBLE);
		tvCity.setOnClickListener(this);
		ivBack.setVisibility(View.VISIBLE);
		ivDown.setVisibility(View.VISIBLE);
		ivDown.setOnClickListener(this);
		ivDown.setImageResource(R.drawable.ic_down_white);
		llSearch.setVisibility(View.VISIBLE);
		llSearch.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(255 / maxH, 72, 211, 194));
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		requestByTicket();
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
	 * Ticket 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByTicket() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Landmark", SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()));
		paramDatas.put("Latitude", SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()));
		paramDatas.put("NewCity", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()));
		paramDatas.put("City", SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TICKET, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				parseJsonTicket(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
				initThemeMenu(null);
				initActivities(null);
				initTicket(null);
				initTicketNea(null);
				initBanner(null);
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
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
			final TicketInfo info = GsonUtils.json2T(result, TicketInfo.class);
			initThemeMenu(info.AreaType);
			initActivities(info.Stretch);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					initTicket(info.sold);
					initTicketNea(info.Zsold);
					// 对轮播图进行排序。1/2/3
					Collections.sort(info.Advertisement, new SortComparator.TicketComparator());
					initBanner(info.Advertisement);
				}
			}, 200);
		} else {
			initThemeMenu(null);
			initActivities(null);
			initTicket(null);
			initTicketNea(null);
			initBanner(null);
		}
	}

	/**
	 * 初始化主题菜单
	 */
	private void initThemeMenu(List<TicketInfo.AreaTypeInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				mThemeInfo = info;
				getViewById(R.id.ll_theme_menu).setVisibility(View.VISIBLE);
				for (int i = 0; i < info.size(); i++) {
					rvMenu[i].setVisibility(View.VISIBLE);
					tvMenu[i].setText(info.get(i).TypeName);
					ImageLoader.getInstance().displayImage(UrlSettings.URL_HTML + info.get(i).ImageUrl, ivMenu[i], options, null);
				}
			} else {
				getViewById(R.id.ll_theme_menu).setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化活动
	 */
	private void initActivities(List<TicketInfo.StretchInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.view_show_activities).setVisibility(View.VISIBLE);
				gvActivities.setVisibility(View.VISIBLE);
				// 初始化活动
				activitiesAdapter = new ActivitiesAdapter(mContext);
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
	 * 初始化门票相关
	 */
	private void initTicket(List<TicketInfo.SoldInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.view_show_mp).setVisibility(View.VISIBLE);
				getViewById(R.id.ll_mp).setVisibility(View.VISIBLE);
				lvTicket.setVisibility(View.VISIBLE);
				// 初始化景区门票
				ticketAdapter = new TicketAdapter(mContext);
				lvTicket.setAdapter(ticketAdapter);
				ticketAdapter.setList(info);
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
	 * 初始化门票 周边相关
	 */
	private void initTicketNea(List<TicketInfo.SoldInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				getViewById(R.id.view_show_zb).setVisibility(View.VISIBLE);
				getViewById(R.id.ll_zb).setVisibility(View.VISIBLE);
				lvNearby.setVisibility(View.VISIBLE);
				// 初始化景区门票
				nearbyAdapter = new TicketAdapter(mContext);
				lvNearby.setAdapter(nearbyAdapter);
				nearbyAdapter.setList(info);
			} else {
				getViewById(R.id.view_show_zb).setVisibility(View.GONE);
				getViewById(R.id.ll_zb).setVisibility(View.GONE);
				lvNearby.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示Banner
	 */
	private void initBanner(final List<TicketInfo.AdvertisementInfo> info) {
		try {
			mPoster = new PosterFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.poster_container_ticket, mPoster).commit();
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
}
