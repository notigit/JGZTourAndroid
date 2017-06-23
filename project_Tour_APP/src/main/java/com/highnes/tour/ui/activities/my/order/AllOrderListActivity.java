package com.highnes.tour.ui.activities.my.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.AllOrderAdapter;
import com.highnes.tour.beans.my.AllOrderInfo;
import com.highnes.tour.beans.my.AllOrderInfo.OrderInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.PayActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.home.TimeDetailActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailActivity;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * <PRE>
 * 作用:
 *    足迹。
 * 注意事项:
 *    .。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-13   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class AllOrderListActivity extends BaseTitleActivity implements OnRefreshListener {
	// 是否加载
	public static boolean isLoad = true;
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK = "com.highnes.tour.action_callback";

	/** 改变第1个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_ONE = 0;
	/** 改变第2个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_TWO = 1;
	/** 改变第3个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_THREE = 2;
	/** 改变第4个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_FOUR = 3;
	/** 改变第5个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_FIVE = 4;

	private int currPager = CHANGE_MENU_ONE;
	private ViewPager mTabPager; // 当前卡片页内容
	// 所有的页面
	private List<View> views;
	// 菜单下划线宽度
	private int lineWidth;
	// 菜单下划线
	private View viewLine;

	// 菜单
	private TextView[] tvMenu = new TextView[5];
	// 商评论
	private ListView[] lvForum = new ListView[5];
	private AllOrderAdapter[] adapter = new AllOrderAdapter[5];
	private PullToRefreshLayout[] pull = new PullToRefreshLayout[5];
	// 没有数据
	private LinearLayout[] llNotData = new LinearLayout[5];

	// 当前的分页页数
	private int[] page = new int[5];
	// 分页总页数
	private int[] pageTotle = new int[5];
	// 是否第一次加载
	private boolean[] isFrist = new boolean[] { true, true, true, true, true };
	// 每页的条数
	private int pagesize = 20;

	// 当前的订单名称
	private String mName;
	
	@Override
	protected void onResume() {
		super.onResume();
		page[currPager] = 1;
		requestByOrder();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		intentFilter.addAction(ACTION_CALLBACK);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			page[currPager] = 1;
			requestByOrder();
		}

	};

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_all_order_list;
	}

	@Override
	protected void findViewById2T() {
		mTabPager = getViewById(R.id.pager);
		tvMenu[0] = getViewById(R.id.tv_menu_0);
		tvMenu[1] = getViewById(R.id.tv_menu_1);
		tvMenu[2] = getViewById(R.id.tv_menu_2);
		tvMenu[3] = getViewById(R.id.tv_menu_3);
		tvMenu[4] = getViewById(R.id.tv_menu_4);
		viewLine = getViewById(R.id.line);

	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mName = getIntent().getExtras().getString("name", "订单");
		setTitle(mName + "订单");
		showBackwardView("", SHOW_ICON_DEFAULT);
		initPager();
		page[0] = 1;
		page[1] = 1;
		page[2] = 1;
		page[3] = 1;
		page[4] = 1;
		registBoradcast();

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setListener2T() {
		tvMenu[0].setOnClickListener(this);
		tvMenu[1].setOnClickListener(this);
		tvMenu[2].setOnClickListener(this);
		tvMenu[3].setOnClickListener(this);
		tvMenu[4].setOnClickListener(this);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
//		requestByOrder();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 菜单1
		case R.id.tv_menu_0:
			currPager = CHANGE_MENU_ONE;
			selectPage(CHANGE_MENU_ONE);
			mTabPager.setCurrentItem(0);
			break;
		// 菜单2
		case R.id.tv_menu_1:
			currPager = CHANGE_MENU_TWO;
			selectPage(CHANGE_MENU_TWO);
			mTabPager.setCurrentItem(1);
			break;
		// 菜单3
		case R.id.tv_menu_2:
			currPager = CHANGE_MENU_THREE;
			selectPage(CHANGE_MENU_THREE);
			mTabPager.setCurrentItem(2);
			break;
		// 菜单4
		case R.id.tv_menu_3:
			currPager = CHANGE_MENU_FOUR;
			selectPage(CHANGE_MENU_FOUR);
			mTabPager.setCurrentItem(3);
			break;
		// 菜单5
		case R.id.tv_menu_4:
			currPager = CHANGE_MENU_FIVE;
			selectPage(CHANGE_MENU_FIVE);
			mTabPager.setCurrentItem(4);
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化pager
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private void initPager() {
		LayoutInflater mLi = LayoutInflater.from(mContext);
		View view1 = mLi.inflate(R.layout.include_my_all_order, null);
		View view2 = mLi.inflate(R.layout.include_my_all_order, null);
		View view3 = mLi.inflate(R.layout.include_my_all_order, null);
		View view4 = mLi.inflate(R.layout.include_my_all_order, null);
		View view5 = mLi.inflate(R.layout.include_my_all_order, null);
		// 每个页面的view数据
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		// 计算滑动条的宽度
		lineWidth = getWindowManager().getDefaultDisplay().getWidth() / views.size();
		viewLine.getLayoutParams().width = lineWidth;
		viewLine.requestLayout();

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		initPagerView(0, view1);
		initPagerView(1, view2);
		initPagerView(2, view3);
		initPagerView(3, view4);
		initPagerView(4, view5);
	}

	/**
	 * 初始化Pager
	 * 
	 * @param view
	 */
	private void initPagerView(int index, View view) {
		pull[index] = (PullToRefreshLayout) view.findViewById(R.id.pull_list);
		lvForum[index] = (ListView) view.findViewById(R.id.lv_pull);
		llNotData[index] = (LinearLayout) view.findViewById(R.id.ll_class_hint);
		pull[index].setOnRefreshListener(this);
		adapter[index] = new AllOrderAdapter(mContext);
		lvForum[index].setAdapter(adapter[index]);
		setOnItemClickListener(index);
	}

	/**
	 * 列表点击事件处理
	 * 
	 * @param index
	 */
	private void setOnItemClickListener(final int index) {
		adapter[index].setmOnItemCall(new AllOrderAdapter.OnItemCall() {

			@Override
			public void onItem(int type, OrderInfo info) {
				switch (type) {
				// item
				case 0:
					// 跳转各自的详情页面,判断类型即可
					proItem(info);
					break;
				// 第一个按钮
				case 1:
					// 查看订单等操作 ,判断类型即可
					proOrder(info);
					break;
				// 第二个按钮
				case 2:
					// 业务处理等操作，判断类型和状态。
					proHandler(info);
					break;

				default:
					break;
				}
			}

		});
	}

	/**
	 * 处理跳转的页面详情
	 * 
	 * @param info
	 */
	private void proItem(OrderInfo info) {
		if ("门票".equals(mName)) {
			Bundle bundle = new Bundle();
			bundle.putString("mAreaID", info.AreaID);
			openActivity(TicketDetailActivity.class, bundle);
		} else if ("旅游".equals(mName)) {
			Bundle bundle = new Bundle();
			bundle.putString("mTourID", info.TouID);
			openActivity(Tour1DetailActivity.class, bundle);
		} else if ("野营".equals(mName)) {
			Bundle bundle = new Bundle();
			bundle.putString("mTourID", info.CamID);
			openActivity(Tour7DetailActivity.class, bundle);
		} else if ("抢购".equals(mName)) {
			Bundle bundle = new Bundle();
			bundle.putString("mSoldID", info.SoldID);
			bundle.putString("mID", info.mID);
			bundle.putString("mNewPrice", info.NewPrice);
			//此处要注意时间的格式，时间中间有T字符
			bundle.putString("mOverTime", info.OTime);
			openActivity(TimeDetailActivity.class, bundle);
		}
	}

	/**
	 * 订单处理1的个按钮
	 * 
	 * @param info
	 */
	private void proOrder(final OrderInfo info) {
		if ("待付款".equals(info.Status)) {
			// 取消订单
			showDialog("是否删除订单？", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (2 == which) {
						requestByOrderCancel(info);
					}
				}
			});
		} else {
			// 查看订单
			Bundle bundle = new Bundle();
			bundle.putString("mOrderID", info.OrderID);
			openActivity(OrderDetailActivity.class, bundle);
		}
	}

	/**
	 * 订单处理2的个按钮
	 * 
	 * @param info
	 */
	private void proHandler(final OrderInfo info) {
		if ("待评论".equals(info.Status)) {
			Bundle bundle = new Bundle();
			bundle.putString("mOrderID", info.OrderID);
			openActivity(AddCommentActivity.class, bundle);
		} else if ("待消费".equals(info.Status)) {
			// 时间等因素判断
			showDialog("是否立即申请退款？", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (2 == which) {
						requestByOrderRefundsOrder(info);
					}
				}
			});
		} else if ("待付款".equals(info.Status)) {
			// 去付款
			Bundle bundle = new Bundle();
			bundle.putString("price", info.Price);
			bundle.putString("orderID", info.OrderID);
			bundle.putString("mInfo", info.Title);
			bundle.putString("ProID", info.ProID);
			/** 支付类别 0-门票，1-旅游，2-野营，3-抢购 */
			if ("门票".equals(mName)) {
				bundle.putInt("payType", 0);
			} else if ("旅游".equals(mName)) {
				bundle.putInt("payType", 1);
			} else if ("野营".equals(mName)) {
				bundle.putInt("payType", 2);
			} else if ("抢购".equals(mName)) {
				bundle.putInt("payType", 3);
			}
			openActivity(PayActivity.class, bundle);
		}
	}

	/**
	 * 选择的页面，修改字体颜色
	 * 
	 * @param selectPage
	 *            选择的页
	 */
	private void selectPage(int selectPage) {
		tvMenu[0].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[1].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[2].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[3].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[4].setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		tvMenu[selectPage].setTextColor(ResHelper.getColorById(mContext, R.color.font_green));
	}

	/*
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int selectPage) {
			switch (selectPage) {
			// 选中第1页
			case CHANGE_MENU_ONE:
				currPager = CHANGE_MENU_ONE;
				selectPage(CHANGE_MENU_ONE);
				if (isFrist[selectPage]) {
					page[0] = 1;
					isFrist[selectPage] = false;
					requestByOrder();
				}
				break;
			// 选中第2页
			case CHANGE_MENU_TWO:
				currPager = CHANGE_MENU_TWO;
				selectPage(CHANGE_MENU_TWO);
				if (isFrist[selectPage]) {
					page[1] = 1;
					isFrist[selectPage] = false;
					requestByOrder();
				}
				break;
			// 选中第3页
			case CHANGE_MENU_THREE:
				currPager = CHANGE_MENU_THREE;
				selectPage(CHANGE_MENU_THREE);
				if (isFrist[selectPage]) {
					page[2] = 1;
					isFrist[selectPage] = false;
					requestByOrder();
				}
				break;
			// 选中第4页
			case CHANGE_MENU_FOUR:
				currPager = CHANGE_MENU_FOUR;
				selectPage(CHANGE_MENU_FOUR);
				if (isFrist[selectPage]) {
					page[3] = 1;
					isFrist[selectPage] = false;
					requestByOrder();
				}
				break;
			// 选中第5页
			case CHANGE_MENU_FIVE:
				currPager = CHANGE_MENU_FIVE;
				selectPage(CHANGE_MENU_FIVE);
				if (isFrist[selectPage]) {
					page[4] = 1;
					isFrist[selectPage] = false;
					requestByOrder();
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// 移动滑动条
			float tagerX = arg0 * lineWidth + arg2 / views.size();
			ViewPropertyAnimator.animate(viewLine).translationX(tagerX).setDuration(0);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		switch (currPager) {
		case CHANGE_MENU_ONE:
			page[currPager] = 1;
			requestByOrder();
			break;
		case CHANGE_MENU_TWO:
			page[currPager] = 1;
			requestByOrder();
			break;
		case CHANGE_MENU_THREE:
			page[currPager] = 1;
			requestByOrder();
			break;
		case CHANGE_MENU_FOUR:
			page[currPager] = 1;
			requestByOrder();
			break;
		case CHANGE_MENU_FIVE:
			page[currPager] = 1;
			requestByOrder();
			break;

		default:
			break;
		}

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		switch (currPager) {
		case CHANGE_MENU_ONE:
			if (page[currPager] + 1 <= pageTotle[currPager]) {
				++page[currPager];
				requestByOrder();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}

			break;
		case CHANGE_MENU_TWO:
			if (page[currPager] + 1 <= pageTotle[currPager]) {
				++page[currPager];
				requestByOrder();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
			break;
		case CHANGE_MENU_THREE:
			if (page[currPager] + 1 <= pageTotle[currPager]) {
				++page[currPager];
				requestByOrder();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
			break;
		case CHANGE_MENU_FOUR:
			if (page[currPager] + 1 <= pageTotle[currPager]) {
				++page[currPager];
				requestByOrder();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
			break;
		case CHANGE_MENU_FIVE:
			if (page[currPager] + 1 <= pageTotle[currPager]) {
				++page[currPager];
				requestByOrder();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrder() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("Name", mName);
		paramDatas.put("page", page[currPager]);
		paramDatas.put("rows", pagesize);
		switch (currPager) {
		case CHANGE_MENU_ONE:
			paramDatas.put("Type", "待付款");
			break;
		case CHANGE_MENU_TWO:
			paramDatas.put("Type", "待消费");
			break;
		case CHANGE_MENU_THREE:
			paramDatas.put("Type", "待评论");
			break;
		case CHANGE_MENU_FOUR:
			paramDatas.put("Type", "交易完成");
			break;
		case CHANGE_MENU_FIVE:
			paramDatas.put("Type", "退款中");
			break;
		default:
			break;
		}
		if (isFrist[currPager]) {
			showLoading();
		}
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_ALL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				if (isFrist[currPager]) {
					stopLoading();
					isFrist[currPager] = false;
				}
				parseJsonGroup(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				if (isFrist[currPager]) {
					stopLoading();
					isFrist[currPager] = false;
				}
				pull[currPager].refreshFinish(PullToRefreshLayout.FAIL);
				pull[currPager].loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				llNotData[currPager].setVisibility(View.VISIBLE);
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
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final AllOrderInfo info = GsonUtils.json2T(result, AllOrderInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle[currPager] = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Order != null && info.Order.size() > 0) {
					pull[currPager].refreshFinish(PullToRefreshLayout.SUCCEED);
					pull[currPager].loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page[currPager]) {
						adapter[currPager].removeAll();
						adapter[currPager].setList(info.Order);
					} else {
						adapter[currPager].addAll(info.Order);
					}
					llNotData[currPager].setVisibility(View.GONE);
				} else {
					pull[currPager].refreshFinish(PullToRefreshLayout.NO_DATA);
					pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
					adapter[currPager].removeAll();
					// 显示如果没有数据的时候的处理
					llNotData[currPager].setVisibility(View.VISIBLE);
				}
			} else {
				pull[currPager].refreshFinish(PullToRefreshLayout.NO_DATA);
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
				llNotData[currPager].setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrderCancel(final OrderInfo info) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("OrderID", info.OrderID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_CANCEL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonCancel(result, info);
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
	private void parseJsonCancel(String result, OrderInfo info) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				// 删除成功
				showToast("删除成功");
				adapter[currPager].remove(info);
			} else {
				showToast("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrderRefundsOrder(final OrderInfo info) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("OrderID", info.OrderID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_REFUNDS, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonRefunds(result, info);
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
	private void parseJsonRefunds(String result, OrderInfo info) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				showToast("申请退款成功");
				adapter[currPager].remove(info);
			} else {
				showToast(JsonUtils.getString(result, "points", "申请退款失败"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
