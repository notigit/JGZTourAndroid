package com.highnes.tour.ui.activities.find;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.CityActivity;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.floatingactionbutton.FloatingActionButton;
import com.highnes.tour.view.floatingactionbutton.FloatingActionsMenu;
import com.highnes.tour.view.webview.ProgressWebView;

/**
 * <PRE>
 * 作用:
 *    发现..攻略。
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
public class FindTacticsActivity extends BaseActivity {
	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	private TextView tvCity;

	// 当前定位的城市
	private String mCity;
	private ProgressWebView web;
	private FloatingActionButton btnMenu[] = new FloatingActionButton[6];
	//最后点击的按钮
	private FloatingActionButton btnMenuLast;
	//菜单
	private FloatingActionsMenu btnRoot;;
	
	private int indexFlag = 0;

	@Override
	@LayoutRes
	protected int getLayoutId() {
		return R.layout.activity_find_tack;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		web = getViewById(R.id.webview);
		btnMenu[0] = getViewById(R.id.ib_0);
		btnMenu[1] = getViewById(R.id.ib_1);
		btnMenu[2] = getViewById(R.id.ib_2);
		btnMenu[3] = getViewById(R.id.ib_3);
		btnMenu[4] = getViewById(R.id.ib_4);
		btnMenu[5] = getViewById(R.id.ib_5);
		btnRoot = getViewById(R.id.multiple_actions_down);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		mCity = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
		SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), mCity);
		initTitle(mCity, "攻略");
		web.loadUrl(UrlSettings.URL_H5_FIND_TACK0 + mCity);
		indexFlag = 0;
		btnMenuLast = btnMenu[0];
	}

	@Override
	protected void setListener() {
		for (int i = 0; i < btnMenu.length; i++) {
			btnMenu[i].setOnClickListener(this);
		}
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		// requestByTicket();
	}

	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		try {
			cleanWebDB();
			WebSettings webSettings = web.getSettings();
			webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
			webSettings.setUseWideViewPort(true);// 关键点
			webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 适应屏幕，内容将自动缩放
			webSettings.setDisplayZoomControls(false);
			webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
			webSettings.setAllowFileAccess(true); // 允许访问文件
			webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
			webSettings.setSupportZoom(true); // 支持缩放
			webSettings.setLoadWithOverviewMode(true);
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除WebView的缓存
	 */
	private void cleanWebDB() {
		mContext.deleteDatabase("webview.db");
		mContext.deleteDatabase("webviewCache.db");

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
		// 菜单
		case R.id.ib_0:
			proLogicMenu(0);
			break;
		case R.id.ib_1:
			proLogicMenu(1);
			break;
		case R.id.ib_2:
			proLogicMenu(2);
			break;
		case R.id.ib_3:
			proLogicMenu(3);
			break;
		case R.id.ib_4:
			proLogicMenu(4);
			break;
		case R.id.ib_5:
			proLogicMenu(5);
			break;
		default:
			break;
		}
	}

	/**
	 * 菜单点击
	 * 
	 * @param ResID
	 */
	private void proLogicMenu(int index) {
		indexFlag = index;
		btnMenuLast.setColorNormalResId(R.color.btn_menu_normal);
		btnMenuLast.setColorPressedResId(R.color.btn_menu_pressed);
		btnMenu[index].setColorNormalResId(R.color.theme_orange);
		btnMenu[index].setColorPressedResId(R.color.theme_orange);
		btnMenuLast = btnMenu[index];
		// TODO 业务逻辑处理
		switch (index) {
		case 0:
			web.loadUrl(UrlSettings.URL_H5_FIND_TACK0 + mCity);
			break;
		case 1:
			web.loadUrl(UrlSettings.URL_H5_FIND_TACK1 + mCity);
			break;
		case 2:
			web.loadUrl(UrlSettings.URL_H5_FIND_TACK2 + mCity);
			break;
		case 3:
			web.loadUrl(UrlSettings.URL_H5_FIND_TACK3 + mCity);
			break;
		case 4:
			web.loadUrl(UrlSettings.URL_H5_FIND_TACK4 + mCity);
			break;
		case 5:
			showNoData();
			break;
		default:
			break;
		}
		btnRoot.toggle();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DefaultData.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String cityName = data.getExtras().getString(DefaultData.EXTRA_RESULT, "重庆市");
				SPUtils.put(mContext, PreSettings.USER_CITY_SELECT.getId(), cityName);
				tvCity.setText(cityName);
				mCity = cityName;
				proLogicMenu(indexFlag);
			}
		}
	}

	/**
	 * 标题初始化
	 */
	private void initTitle(String city, String mTitle) {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		ImageView ivDown, ivBack;
		TextView tvTitle;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			tvCity = (TextView) vTitle.findViewById(R.id.button_forward_txt19);
			ivDown = (ImageView) vTitle.findViewById(R.id.button_down_img_right19);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img19);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			tvCity = (TextView) vTitle.findViewById(R.id.button_forward_txt);
			ivDown = (ImageView) vTitle.findViewById(R.id.button_down_img_right);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);
		}
		tvCity.setText(city);
		tvCity.setTextColor(ResHelper.getColorById(mContext, R.color.white));
		tvCity.setVisibility(View.VISIBLE);
		tvCity.setOnClickListener(this);
		tvTitle.setText(mTitle);
		tvTitle.setVisibility(View.VISIBLE);
		ivBack.setVisibility(View.VISIBLE);
		ivDown.setVisibility(View.VISIBLE);
		ivDown.setOnClickListener(this);
		ivDown.setImageResource(R.drawable.ic_down_white);
		ivBack.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(255, 72, 211, 194));
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
				parseJsonTicket(result);
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
	 * Ticket JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonTicket(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			// final TicketInfo info = GsonUtils.json2T(result,
			// TicketInfo.class);
		} else {
		}
	}

}
