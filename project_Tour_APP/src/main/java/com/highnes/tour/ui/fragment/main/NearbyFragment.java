package com.highnes.tour.ui.fragment.main;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.share.sdk.openapi.algorithm.MD5;
import com.highnes.tour.R;
import com.highnes.tour.common.alipay.PayAlipaySettings;
import com.highnes.tour.common.alipay.PayResult;
import com.highnes.tour.conf.Constants;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.fragment.base.BaseFragment;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.view.webview.ProgressWebView;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * <PRE>
 * 作用:
 *    周边。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-05   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class NearbyFragment extends BaseFragment {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_NEARBY = "com.highnes.tour.action_callback_nearby";
	public static final String ACTION_CALLBACK_PAY_SUCCEED = "com.highnes.tour.action_callback_pay_succeed";
	public static boolean isLoad = true;
	// 标题
	private LinearLayout llTitle;
	private View vTitle;

	private ProgressWebView web;

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
		intentFilter.addAction(ACTION_CALLBACK_NEARBY);
		intentFilter.addAction(ACTION_CALLBACK_PAY_SUCCEED);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.d("--周边过滤器：" + intent.getAction());
			if (ACTION_CALLBACK_NEARBY.equals(intent.getAction())) {
				// 加载网页
				if (isLoad) {
					isLoad = false;
					performHandlePostDelayed(0, 100);
				}
			} else {
				// 微信支付成功
				paySuccessCall(1);
			}
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		if (BaseResp.ErrCode.ERR_OK == Constants.WIN_PAY_CODE) {
			LogUtils.d("--微信支付成功");
			// 支付成功
			paySuccessCall(1);
		}
	};

	@Override
	protected @LayoutRes
	int getLayoutId() {
		return R.layout.fragment_tab_nearby;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		web = getViewById(R.id.webview);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		initTitle("周边");
		registBoradcast();
		initWebView();
		initWeiApi();
	}

	@Override
	protected void setListener() {
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 刷新
		case R.id.button_backward_img19:
		case R.id.button_backward_img:
			isLoad = true;
			performHandlePostDelayed(0, 100);
			break;

		default:
			break;
		}
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {

	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		LogUtils.i("--加载" + UrlSettings.URL_PHP_H5_NEARBY_INDEX);
		web.loadUrl(UrlSettings.URL_PHP_H5_NEARBY_INDEX);
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

			web.addJavascriptInterface(this, "tour");

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

	/**
	 * 标题初始化
	 */
	private void initTitle(String title) {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		TextView tvTitle;
		ImageView ivSX;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title19);
			ivSX = (ImageView) vTitle.findViewById(R.id.button_backward_img19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title);
			ivSX = (ImageView) vTitle.findViewById(R.id.button_backward_img);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);

		}
		ivSX.setImageResource(R.drawable.ic_shuaxin);
		ivSX.setVisibility(View.VISIBLE);
		ivSX.setOnClickListener(this);
		tvTitle.setText(title);
		// 设置背景透明度
		root.setBackgroundResource(R.color.titlec);
	}

	// -------------------------------Java与JS交互-------------------------------

	/**
	 * 使用Java调用JS
	 * 
	 * @param orderId
	 *            订单id
	 */
	public void getAppUserID(String userID) {
		LogUtils.d("--已发送PHP用户ID");
		web.loadJava2JS("javascript:getAppUserID('" + userID + "')");
	}

	/**
	 * JS调用Java的方法 funFromAndroid是JS中定义的方法 android sdk api >= 17 时需要加@JavascriptInterface
	 * 支付的JS
	 * 
	 */
	@JavascriptInterface
	public void funFromUserID() {
		try {
			LogUtils.d("--PHP请求用户ID");
			if (AppUtils.isLogin(mContext)) {
				String userId = SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
				getAppUserID(userId);
			} else {
				openActivity(LoginActivity.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * JS调用Java的方法 funFromAndroid是JS中定义的方法 android sdk api >= 17 时需要加@JavascriptInterface
	 * 支付的JS
	 * 
	 * @param pay_type
	 *            支付类型
	 * @param prepay_id
	 *            预定单id
	 */
	@JavascriptInterface
	public void funFromAndroid(final String pay_type, final String prepay_id) {
		try {
			LogUtils.d("点击支付--支付类型是：" + pay_type + "--支付的信息是：" + prepay_id);
			if ("wechat".equals(pay_type)) {
				// 微信
				genPayReq(prepay_id);
			} else if ("alipay".equals(pay_type)) {
				// 支付宝
				isPaySusseed = false;
				isPay = true;
				payOrder(prepay_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------微信支付-----------------------------------
	// 微信支付
	private PayReq req;
	private IWXAPI msgApi;

	private void initWeiApi() {
		// 注册微信支付
		req = new PayReq();
		msgApi = WXAPIFactory.createWXAPI(mContext, "");
		msgApi.registerApp(Constants.WIN_ID);
	}

	/**
	 * 微信支付
	 */
	private void genPayReq(String prepay_id) {
		Constants.WIN_PAY_CODE = -999;
		Constants.WIN_PAY_TYPE = 1;
		req.appId = Constants.WIN_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = prepay_id;
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		req.extData = "app data"; // optional
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(signParams);
		msgApi.registerApp(Constants.WIN_ID);
		msgApi.sendReq(req);
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 生成签名
	 */
	@SuppressLint("DefaultLocale")
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.MCH_KEY);
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return appSign;
	}

	// -------------------------------支付宝支付-------------------------------
	// 是否支付成功
	private boolean isPaySusseed = false;
	// 是否在进行支付
	private boolean isPay = true;

	/**
	 * 支付宝支付
	 */
	private void payOrder(String jsonStr) {
		if (isPay) {
			isPay = false;
			String payID = JsonUtils.getString(jsonStr, "code", "");//支付ID
			String title = JsonUtils.getString(jsonStr, "title", "");//商品标题
			String info = JsonUtils.getString(jsonStr, "body", "");//商品描述
			Double price = JsonUtils.getDouble(jsonStr, "money", 0.0);//商品价格
			String notify_url = JsonUtils.getString(jsonStr, "notifyurl", "");//支付回调地址
//			PayAlipaySettings.startPayAlipay(mContext, mHandler, payID, "金龟子旅行", info, "0.01", notify_url);
			PayAlipaySettings.startPayAlipay(mContext, mHandler, payID, title, info, price+"", notify_url);
		}
	}

	/**
	 * 该Handler在下单界面编写
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			stopLoading();
			switch (msg.what) {
			case PayAlipaySettings.SDK_PAY_FLAG: {
				if (isPaySusseed) {
					return;
				}
				// 同步返回需要验证的信息
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					isPaySusseed = true;
					paySuccessCall(0);
				} else {
					isPay = true;
					isPaySusseed = false;
					if (TextUtils.equals(resultStatus, "6001")) {
						showToast("您已取消支付");
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					} else if (TextUtils.equals(resultStatus, "8000")) {
						showToast("等待商家验证");
					} else {
						showToast("支付失败");
					}
				}
				break;
			}
			case PayAlipaySettings.SDK_CHECK_FLAG: {
				LogUtils.i("检查结果为：" + msg.obj);
				break;
			}
			default:
				stopLoading();
				break;
			}
		};
	};

	// -----------------------支付结果---------------------

	/**
	 * 支付成功后判断类型，回调后台接口
	 * 
	 * @param type
	 *            0表示支付宝，1表示微信
	 */
	private void paySuccessCall(int type) {
		// TODO 支付成功
		try {
			LogUtils.d("去成功页面--微信支付");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
