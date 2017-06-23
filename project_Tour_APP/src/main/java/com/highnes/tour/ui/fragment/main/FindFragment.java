package com.highnes.tour.ui.fragment.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.find.FindAdapter;
import com.highnes.tour.adapter.find.FindAdapter.OnItemCall;
import com.highnes.tour.beans.find.FindInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.CityActivity;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.find.FindDetailActivity;
import com.highnes.tour.ui.activities.find.FindListActivity;
import com.highnes.tour.ui.activities.find.FindTacticsActivity;
import com.highnes.tour.ui.fragment.base.BaseFragment;
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
import com.highnes.tour.view.listview.MyListView;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnPullListener;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;
import com.highnes.tour.view.pull.PullableScrollView;
import com.highnes.tour.view.pull.PullableScrollView.ScrollViewListener;

/**
 * <PRE>
 * 作用:
 *    发现。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-29   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FindFragment extends BaseFragment implements OnRefreshListener {
	public static boolean isLoad = true;
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_FIND = "com.highnes.tour.action_callback_find";
	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	private int maxH;
	private TextView tvCity;

	// 轮播图
	private PosterFragment mPoster;

	private PullableScrollView svRoot;
	private PullToRefreshLayout pull;

	// 景色
	private MyListView lvList;
	private FindAdapter adapter;

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
		intentFilter.addAction(ACTION_CALLBACK_FIND);
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
		return R.layout.fragment_tab_find;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		pull = getViewById(R.id.pull);
		svRoot = getViewById(R.id.sv_shops);
		lvList = getViewById(R.id.lv_list);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		// 滑动透明标题的高度
		maxH = DensityUtils.dip2px(mContext, 150);
		String city = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
		SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), city);
		initTitle(city, "发现");
		pull.setHasPullUp(false);
		// 景色
		adapter = new FindAdapter(mContext);
		lvList.setAdapter(adapter);

		lvList.setFocusable(false);
		svRoot.smoothScrollTo(0, 20);
		// TODO 可能把顶部做到列表中
		// lvList2.addFooterView(View.inflate(mContext,
		// R.layout.include_find_more, null));
		registBoradcast();
	}

	@Override
	protected void setListener() {
		pull.setOnRefreshListener(this);
		setOnPullListener();
		setScrollViewListener();
		getViewById(R.id.ll_tact).setOnClickListener(this);
		adapter.setmOnItemCall(new OnItemCall() {

			@Override
			public void onCall(String mTypeID, String mNoteID, int type, String mTitle) {
				if (0 == type) { // 更多
					Bundle bundle = new Bundle();
					bundle.putString("mTypeID", mTypeID);
					bundle.putString("mTitle", mTitle);
					openActivity(FindListActivity.class, bundle);
				} else {// 单个Item
					Bundle bundle = new Bundle();
					bundle.putString("mTypeID", mTypeID);
					bundle.putString("mNoteID", mNoteID);
					bundle.putString("mTitle", mTitle);
					openActivity(FindDetailActivity.class, bundle);
				}
			}
		});
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {

	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		requestByFind();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 城市选择
		case R.id.button_backward_txt19:
		case R.id.button_down_img19:
		case R.id.button_backward_txt:
		case R.id.button_down_img:
			Intent intent = new Intent(mContext, CityActivity.class);
			startActivityForResult(intent, DefaultData.REQUEST_CODE);
			break;
		// 攻略
		case R.id.ll_tact:
			openActivity(FindTacticsActivity.class);
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
				tvCity.setText(cityName);
				SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), cityName);
				requestByFind();
			}
		}
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

	/**
	 * 标题初始化
	 */
	private void initTitle(String city, String title) {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		TextView tvTitle;
		ImageView ivRight, ivLeft;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			ivRight = (ImageView) vTitle.findViewById(R.id.button_down_img_right19);
			ivLeft = (ImageView) vTitle.findViewById(R.id.button_down_img19);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title19);
			tvCity = (TextView) vTitle.findViewById(R.id.button_backward_txt19);

			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			ivRight = (ImageView) vTitle.findViewById(R.id.button_down_img_right);
			ivLeft = (ImageView) vTitle.findViewById(R.id.button_down_img);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title);
			tvCity = (TextView) vTitle.findViewById(R.id.button_backward_txt);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);

		}
		tvCity.setText(city);
		tvCity.setTextColor(ResHelper.getColorById(mContext, R.color.white));
		tvCity.setVisibility(View.VISIBLE);
		tvCity.setOnClickListener(this);
		tvTitle.setText(title);
		tvTitle.setVisibility(View.VISIBLE);
		ivLeft.setVisibility(View.VISIBLE);
		ivRight.setVisibility(View.INVISIBLE);
		ivRight.setImageResource(R.drawable.ic_msg_white);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(255 / maxH, 72, 211, 194));
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		requestByFind();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pull.refreshFinish(PullToRefreshLayout.SUCCEED);
		pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}

	// -----------------------网络请求-----------------------
	/**
	 * Find 网络请求
	 */
	private void requestByFind() {

		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("City", SPUtils.get(mContext, PreSettings.USER_CITY_SELECT.getId(), PreSettings.USER_CITY_SELECT.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FIND, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				parseJsonFind(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.NO_DATA);
				pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Find JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonFind(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final FindInfo info = GsonUtils.json2T(result, FindInfo.class);
				initFindInfo(info.NotesType);
				// 对轮播图进行排序。1/2/3
				Collections.sort(info.Advertisement, new SortComparator.FindComparator());
				initBanner(info.Advertisement);
			} else {
				initBanner(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列表内容
	 * 
	 * @param info
	 */
	private void initFindInfo(List<FindInfo.NotesTypeInfo> info) {
		if (info != null) {
			adapter.setList(info);
		}
	}

	/**
	 * 显示Banner
	 */
	private void initBanner(final List<FindInfo.AdvertisementInfo> info) {
		try {
			mPoster = new PosterFragment();
			getFragmentManager().beginTransaction().replace(R.id.poster_container_find, mPoster).commit();
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
								proLogicBanner(info.get(position));
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
	 * 处理轮播图事件
	 * 
	 * @param info
	 */
	private void proLogicBanner(FindInfo.AdvertisementInfo info) {
		if (info.Type) {// 原生跳转
			// TODO 原生的规则确定
		} else { // H5跳转
			if (!StringUtils.isEmpty(info.JumpUrl)) {
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 3001);
				bundle.putString("mTitle", "详情");
				bundle.putString("mUrl", info.JumpUrl);
				openActivity(WebViewTitleActivity.class, bundle);
			}
		}
	}

}
