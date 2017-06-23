package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;

import com.highnes.tour.R;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.view.webview.ProgressWebView;

/**
 * <PRE>
 * 作用:
 *    客服详情。
 * 注意事项:
 * 	  无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ServerDetailActivity extends BaseTitleActivity {
	private ProgressWebView web;
	// 传递过来的类型
	private String mID;
	private String isZan;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_webview_title;
	}

	@Override
	protected void findViewById2T() {
		web = getViewById(R.id.webview);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mID = getIntent().getExtras().getString("mID", "");
		isZan = getIntent().getExtras().getString("isZan", "");
		setTitle("详情");
		showBackwardView("", SHOW_ICON_DEFAULT);
		if (isZan.equals("True")) {
			showForwardView("", R.drawable.ic_zan_red);
		} else {
			showForwardView("", R.drawable.ic_zan_g);
		}
		initWebView();
	}

	@Override
	protected void setListener2T() {
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		loadUrl();
	}
	
	@Override
	protected void onForward(View forwardView) {
		super.onForward(forwardView);
		if (!isZan.equals("True")) {
			if(AppUtils.isLogin(mContext)){
				requestByHealths(mID);
			}else{
				openActivity(LoginActivity.class);
			}
		}
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
			// webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadUrl() {
		String urlStr = String.format(UrlSettings.URL_H5_USER_SERVICE_Detail, mID);
		LogUtils.i("--加载H5--" + urlStr);
		web.loadUrl(urlStr);
	}

	/**
	 * 清除WebView的缓存
	 */
	private void cleanWebDB() {
		mContext.deleteDatabase("webview.db");
		mContext.deleteDatabase("webviewCache.db");

	}
	
	// -----------------------网络请求-----------------------

	/**
	 * Healths 网络请求
	 * 
	 * @param page
	 *            当前页面
	 * @param rows
	 *            页容量
	 * 
	 */
	private void requestByHealths(String id) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("AdviceID", id);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_SERVICE_ZAN, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonHealths(result);
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
	 * Healths JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonHealths(String result) {
		String status = JsonUtils.getString(result, "status", "0");
		switch (Integer.valueOf(status)) {
		case 0:
			break;
		case 1:
			showForwardView("", R.drawable.ic_zan_red);
			isZan = "True";
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		default:
			break;
		}
	}
}
