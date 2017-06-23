package com.highnes.tour.ui.activities.home.tour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.tour.TourMenuAdapter;
import com.highnes.tour.beans.home.tour.TourBannerInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.CityActivity;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.ui.fragment.poster.PosterFragment;
import com.highnes.tour.ui.fragment.poster.PosterFragment.OnItemClick;
import com.highnes.tour.ui.fragment.tour.Tour0Fragment;
import com.highnes.tour.ui.fragment.tour.Tour1Fragment;
import com.highnes.tour.ui.fragment.tour.Tour2Fragment;
import com.highnes.tour.ui.fragment.tour.Tour3Fragment;
import com.highnes.tour.ui.fragment.tour.Tour4Fragment;
import com.highnes.tour.ui.fragment.tour.Tour5Fragment;
import com.highnes.tour.ui.fragment.tour.Tour6Fragment;
import com.highnes.tour.ui.fragment.tour.Tour7Fragment;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.FragmentUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    首页..旅游度假。
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
public class TourActivity extends BaseActivity {
	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	private TextView tvCity;
	// 轮播图
	private PosterFragment mPoster;
	// 当前定位的城市
	private String mCity;
	boolean isCurrCity = true;
	// 菜单列表
	private ListView lvMenu;
	private TourMenuAdapter menuAdapter;

	private FragmentManager mFragmentManager;
	/** 当前的页面 */
	private Fragment mCurrentFragment;
	private int mCurrent = 0;

	@Override
	@LayoutRes
	protected int getLayoutId() {
		return R.layout.activity_home_tour;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		lvMenu = getViewById(R.id.lv_menu);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		mCity = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
		SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), mCity);
		initTitle(mCity);
		initOptions();
		menuAdapter = new TourMenuAdapter(mContext);
		lvMenu.setAdapter(menuAdapter);
		menuAdapter.setList(getMenuData());

		mCurrentFragment = new Tour0Fragment();
		mFragmentManager = getSupportFragmentManager();
		FragmentUtils.replaceFragment(mFragmentManager, R.id.layout_content, Tour0Fragment.class, null, false);
	}

	@Override
	protected void setListener() {

		// 菜单点击事件
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// String info = (String) lvMenu.getAdapter().getItem(position);
				menuAdapter.setIndex(position);
				mCurrent = position;
				// TODO 新页面
				switch (position) {
				// 景区门票
				case 0:
					replaceFragment(Tour0Fragment.class);
					break;
				// 周边游
				case 1:
					replaceFragment(Tour1Fragment.class);
					break;
				// 国内游
				case 2:
					replaceFragment(Tour2Fragment.class);
					break;
				// 国外游
				case 3:
					replaceFragment(Tour3Fragment.class);
					break;
				// 自驾游
				case 4:
					replaceFragment(Tour4Fragment.class);
					break;
				// 自由行
				case 5:
					replaceFragment(Tour5Fragment.class);
					break;
				// 跟团游
				case 6:
					replaceFragment(Tour6Fragment.class);
					break;
				// 野营
				case 7:
					replaceFragment(Tour7Fragment.class);
					break;
				// 当地玩乐
				case 8: {
					Bundle bundle = new Bundle();
					bundle.putInt("mType", 1202);
					bundle.putString("mTitle", "当地玩乐");
					bundle.putString("mUrl", UrlSettings.URL_PHP_H5_NEARBY_INDEX+AppUtils.getTemp());
					openActivity(WebViewTitleActivity.class, bundle);
				}

					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		requestByBanner();
	}

	/**
	 * 选择城市后的广播
	 */
	private void proLogicCity() {
		// TODO
		Intent mIntent = new Intent();
		// 发送广播
		switch (mCurrent) {
		// 景区门票
		case 0:
			mIntent.setAction(Tour0Fragment.ACTION_CALLBACK_TOUR0);
			break;
		// 周边游
		case 1:
			mIntent.setAction(Tour1Fragment.ACTION_CALLBACK_TOUR1);
			break;
		// 国内游
		case 2:
			mIntent.setAction(Tour2Fragment.ACTION_CALLBACK_TOUR2);
			break;
		// 国外游
		case 3:
			mIntent.setAction(Tour3Fragment.ACTION_CALLBACK_TOUR3);
			break;
		// 自驾游
		case 4:
			mIntent.setAction(Tour4Fragment.ACTION_CALLBACK_TOUR4);
			break;
		// 自由行
		case 5:
			mIntent.setAction(Tour5Fragment.ACTION_CALLBACK_TOUR5);
			break;
		// 跟团游
		case 6:
			mIntent.setAction(Tour6Fragment.ACTION_CALLBACK_TOUR6);
			break;
		// 野营
		case 7:
			mIntent.setAction(Tour7Fragment.ACTION_CALLBACK_TOUR7);
			break;
		default:
			break;
		}
		sendBroadcast(mIntent);
	}

	/**
	 * 替换Fragment页面。
	 * 
	 * @param newFragment
	 *            新的Fragment页面
	 */
	private void replaceFragment(Class<? extends Fragment> newFragment) {
		mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager, R.id.layout_content, mCurrentFragment, newFragment, null, false);
	}

	/**
	 * 菜单数据
	 * 
	 * @return
	 */
	private List<String> getMenuData() {
		List<String> list = new ArrayList<String>();
		list.add("景区门票");
		list.add("周边游");
		list.add("国内游");
		list.add("境外游");
		list.add("自驾游");
		list.add("自由行");
		list.add("跟团游");
		list.add("野营");
		list.add("当地玩乐");
		return list;
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
		// 搜索
		case R.id.ll_search2_19:
		case R.id.ll_search2:
			// openActivity(TicketSearchActivity.class);
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
				tvCity.setText(cityName);
				SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), cityName);
				proLogicCity();
			}
		}
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
		llSearch.setVisibility(View.GONE);
		llSearch.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(0, 72, 211, 194));
	}

	// -----------------------网络请求-----------------------
	/**
	 * Banner 网络请求
	 * 
	 * 
	 */
	private void requestByBanner() {

		Map<String, Object> paramDatas = new HashMap<String, Object>();
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_BANNER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonBanner(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
				initBanner(null);
			}
		}, paramDatas);
	}

	/**
	 * Banner JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonBanner(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final TourBannerInfo info = GsonUtils.json2T(result, TourBannerInfo.class);
				initBanner(info.Advertisement);
			} else {
				initBanner(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示Banner
	 */
	private void initBanner(final List<TourBannerInfo.AdvertisementInfo> info) {
		try {
			mPoster = new PosterFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.poster_container_tour, mPoster).commit();
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
								// TODO 点击跳转
								// Bundle bundle = new Bundle();
								// bundle.putInt("mType", 1001);
								// bundle.putString("mTitle", "详情");
								// bundle.putString("mUrl",
								// info.get(position).JumpUrl);
								// openActivity(WebViewActivity.class, bundle);
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
