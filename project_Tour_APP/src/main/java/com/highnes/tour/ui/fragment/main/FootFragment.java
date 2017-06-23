package com.highnes.tour.ui.fragment.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.foot.FootAdapter;
import com.highnes.tour.beans.foot.FootGroupInfo;
import com.highnes.tour.beans.foot.FootGroupInfo.TribuneInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.foot.FootGroupDetailActivity;
import com.highnes.tour.ui.activities.foot.FootIssueActivity;
import com.highnes.tour.ui.fragment.base.BaseFragment;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DensityUtils;
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
public class FootFragment extends BaseFragment implements OnRefreshListener {
	// 是否加载
	public static boolean isLoad = true;
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_FOOT = "com.highnes.tour.action_callback_foot";
	/** 改变第1个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_ONE = 0;
	/** 改变第2个菜单切换的背景颜色 */
	private final static int CHANGE_MENU_TWO = 1;
	// 标题
	private LinearLayout llTitle;
	private View vTitle;

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
	private FootAdapter[] adapter = new FootAdapter[2];
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
		intentFilter.addAction(ACTION_CALLBACK_FOOT);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (isLoad) {
				isLoad = false;
				performHandlePostDelayed(0, 200);
			}
		}

	};

	@Override
	protected @LayoutRes
	int getLayoutId() {
		return R.layout.fragment_tab_foot;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		mTabPager = getViewById(R.id.pager);
		tvMenu[0] = getViewById(R.id.tv_menu_0);
		tvMenu[1] = getViewById(R.id.tv_menu_1);
		viewLine = getViewById(R.id.line);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		initTitle("足迹");
		initPager();
		registBoradcast();
		page[0] = 1;
		page[1] = 1;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setListener() {
		tvMenu[0].setOnClickListener(this);
		tvMenu[1].setOnClickListener(this);
		getViewById(R.id.iv_add).setOnClickListener(this);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		if (currPager == CHANGE_MENU_ONE) {
			page[0] = 1;
			requestByMy();
		} else {
			page[1] = 1;
			requestByGroup();
		}
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
		// 发布
		case R.id.iv_add:
			openActivity(FootIssueActivity.class);
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
		View view1 = mLi.inflate(R.layout.include_foot_my, null);
		View view2 = mLi.inflate(R.layout.include_foot_my, null);
		// 每个页面的view数据
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		// 计算滑动条的宽度
		lineWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth() / views.size();
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
		// lvForum[index].addHeaderView(View.inflate(mContext,
		// R.layout.include_foot_head, null));
		adapter[index] = new FootAdapter(mContext);
		lvForum[index].setAdapter(adapter[index]);
		setOnItemClickListener(index);
	}

	private void setOnItemClickListener(final int index) {
		adapter[index].setOnItemCall(new FootAdapter.OnItemCall() {

			@Override
			public void onCall(TribuneInfo info) {
				Bundle bundle = new Bundle();
				bundle.putString("mID", info.ID);
				openActivity(FootGroupDetailActivity.class, bundle);
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
					requestByMy();
				}
				break;
			// 选中第2页
			case CHANGE_MENU_TWO:
				currPager = CHANGE_MENU_TWO;
				selectPage(CHANGE_MENU_TWO);
				if (isFrist[selectPage]) {
					page[1] = 1;
					isFrist[selectPage] = false;
					requestByGroup();
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

	/**
	 * 标题初始化
	 */
	private void initTitle(String title) {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		TextView tvTitle;
		if (isShow) {
			// 加载v19类型的标题
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title19);
			// 计算标题的高度
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 18F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 0F);
			llTitle.setLayoutParams(p);
		}

//		tvTitle.setText(title);
		// 设置背景透明度
		root.setBackgroundResource(R.color.transparent);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		switch (currPager) {
		case CHANGE_MENU_ONE:
			page[currPager] = 1;
			requestByMy();
			break;
		case CHANGE_MENU_TWO:
			page[currPager] = 1;
			requestByGroup();
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
				requestByMy();
			} else {
				pull[currPager].loadmoreFinish(PullToRefreshLayout.NO_DATA);
			}

			break;
		case CHANGE_MENU_TWO:
			if (page[currPager] + 1 <= pageTotle[currPager]) {
				++page[currPager];
				requestByGroup();
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
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByGroup() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("page", page[currPager]);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FOOT_GROUP, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonGroup7My(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
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
	private void parseJsonGroup7My(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final FootGroupInfo info = GsonUtils.json2T(result, FootGroupInfo.class);
				int pages = Integer.valueOf(info.size) / pagesize;
				pageTotle[currPager] = Integer.valueOf(info.size) % pagesize == 0 ? pages : pages + 1;
				if (info.Tribune != null && info.Tribune.size() > 0) {
					pull[currPager].refreshFinish(PullToRefreshLayout.SUCCEED);
					pull[currPager].loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page[currPager]) {
						adapter[currPager].removeAll();
						adapter[currPager].setList(info.Tribune);
					} else {
						adapter[currPager].addAll(info.Tribune);
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
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByMy() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("page", page[currPager]);
		paramDatas.put("rows", pagesize);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FOOT_MY, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonGroup7My(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull[currPager].refreshFinish(PullToRefreshLayout.FAIL);
				pull[currPager].loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				llNotData[currPager].setVisibility(View.VISIBLE);
			}
		}, paramDatas);
	}
}
