package com.highnes.tour.ui.activities.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.CouponAdapter;
import com.highnes.tour.beans.my.CouponInfo;
import com.highnes.tour.beans.my.CouponInfo.RedPacketInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.fragment.main.FootFragment.MyOnPageChangeListener;
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
 *    优惠券。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-08   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class CouponActivity extends BaseTitleActivity implements OnRefreshListener {
	/** 改变第1个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_ONE = 0;
	/** 改变第2个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_TWO = 1;
	private int currPager = CHANGE_MENU_ONE;
	private ViewPager mTabPager; // 当前卡片页内容
	// 所有的页面
	private List<View> views;
	// 菜单下划线宽度
	private int lineWidth;
	// 菜单下划线
	private View viewLine;

	// 菜单
	private TextView[] tvMenu = new TextView[2];
	// 商评论
	private ListView[] lvForum = new ListView[2];
	private CouponAdapter[] adapter = new CouponAdapter[2];
	private PullToRefreshLayout[] pull = new PullToRefreshLayout[2];
	// 没有数据
	private LinearLayout[] llNotData = new LinearLayout[2];

	// 当前的分页页数
	private int[] page = new int[2];
	// 分页总页数
	private int[] pageTotle = new int[2];
	// 是否第一次加载
	private boolean[] isFrist = new boolean[] { false, true };
	// 每页的条数
	private int pagesize = 20;

	private EditText etCode;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_coupon;
	}

	@Override
	protected void findViewById2T() {
		mTabPager = getViewById(R.id.pager);
		tvMenu[0] = getViewById(R.id.tv_menu_0);
		tvMenu[1] = getViewById(R.id.tv_menu_1);
		viewLine = getViewById(R.id.line);
		etCode = getViewById(R.id.et_code);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("红包优惠券");
		showBackwardView("", SHOW_ICON_DEFAULT);
		page[0] = 1;
		page[1] = 1;
		initPager();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setListener2T() {
		tvMenu[0].setOnClickListener(this);
		tvMenu[1].setOnClickListener(this);
		getViewById(R.id.tv_code).setOnClickListener(this);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 100);
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
		// 兑换
		case R.id.tv_code:
			String code = etCode.getText().toString().trim();
			if (StringUtils.isEmpty(code)) {
				showToast("请输入兑换码");
				return;
			} else {
				requestByCode(code);
			}
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
		View view1 = mLi.inflate(R.layout.include_my_coupon, null);
		View view2 = mLi.inflate(R.layout.include_my_coupon, null);
		// 每个页面的view数据
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
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
		pull[index].setHasPullDown(false);
		pull[index].setHasPullUp(false);
		if (0 == index) {
			adapter[index] = new CouponAdapter(mContext, true);
		} else {
			adapter[index] = new CouponAdapter(mContext, false);
		}
		lvForum[index].setAdapter(adapter[index]);
		setOnItemClickListener(index);
	}

	private void setOnItemClickListener(final int index) {
		adapter[index].setOnItemCall(new CouponAdapter.OnItemCall() {

			@Override
			public void itemCall(RedPacketInfo info) {
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 4001);
				bundle.putString("mTitle", "优惠券说明");
				bundle.putString("mUrl", UrlSettings.URL_H5_USER_COUPON_INFO + info.CouID);
				openActivity(WebViewTitleActivity.class, bundle);
			}
		});
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
					requestByCoupon();
				}
				break;
			// 选中第2页
			case CHANGE_MENU_TWO:
				currPager = CHANGE_MENU_TWO;
				selectPage(CHANGE_MENU_TWO);
				if (isFrist[selectPage]) {
					page[1] = 1;
					isFrist[selectPage] = false;
					requestByCoupon();
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
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		page[0] = 1;
		requestByCoupon();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		switch (currPager) {
		case CHANGE_MENU_ONE:
			page[currPager] = 1;
			requestByCoupon();
			break;
		case CHANGE_MENU_TWO:
			page[currPager] = 1;
			requestByCoupon();
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
				requestByCoupon();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}

			break;
		case CHANGE_MENU_TWO:
			if (page[currPager] + 1 <= pageTotle[currPager]) {
				++page[currPager];
				requestByCoupon();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 优惠券 网络请求
	 * 
	 */
	private void requestByCoupon() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_COUPON, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonCoupon(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull[currPager].refreshFinish(PullToRefreshLayout.NO_DATA);
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
				llNotData[currPager].setVisibility(View.VISIBLE);
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
	private void parseJsonCoupon(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final CouponInfo info = GsonUtils.json2T(result, CouponInfo.class);
				int pages = Integer.valueOf(info.PCount) / pagesize;
				pageTotle[currPager] = Integer.valueOf(info.PCount) % pagesize == 0 ? pages : pages + 1;
				if (currPager == CHANGE_MENU_ONE) {
					if (info.RedPacket != null && info.RedPacket.size() > 0) {
						pull[currPager].refreshFinish(PullToRefreshLayout.SUCCEED);
						pull[currPager].loadmoreFinish(PullToRefreshLayout.SUCCEED);
						if (1 == page[currPager]) {
							adapter[currPager].removeAll();
							adapter[currPager].setList(info.RedPacket);
						} else {
							adapter[currPager].addAll(info.RedPacket);
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
					if (info.OutRedPacket != null && info.OutRedPacket.size() > 0) {
						pull[currPager].refreshFinish(PullToRefreshLayout.SUCCEED);
						pull[currPager].loadmoreFinish(PullToRefreshLayout.SUCCEED);
						if (1 == page[currPager]) {
							adapter[currPager].removeAll();
							adapter[currPager].setList(info.OutRedPacket);
						} else {
							adapter[currPager].addAll(info.OutRedPacket);
						}
						llNotData[currPager].setVisibility(View.GONE);
					} else {
						pull[currPager].refreshFinish(PullToRefreshLayout.NO_DATA);
						pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
						adapter[currPager].removeAll();
						// 显示如果没有数据的时候的处理
						llNotData[currPager].setVisibility(View.VISIBLE);
					}
				}
			} else {
				pull[currPager].refreshFinish(PullToRefreshLayout.NO_DATA);
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 兑换 网络请求
	 * 
	 */
	private void requestByCode(String value) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		paramDatas.put("CodeNum", value);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_COUPON_CODE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonCode(result);
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
	private void parseJsonCode(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				showToast("兑换成功");
				etCode.setText("");
				requestByCoupon();
			} else {
				showToast(JsonUtils.getString(result, "message", "兑换失败！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
