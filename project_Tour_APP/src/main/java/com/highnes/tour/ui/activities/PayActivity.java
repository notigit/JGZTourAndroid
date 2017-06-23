package com.highnes.tour.ui.activities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alipay.share.sdk.openapi.algorithm.MD5;
import com.highnes.tour.R;
import com.highnes.tour.adapter.RedAdapter;
import com.highnes.tour.beans.PointInfo;
import com.highnes.tour.common.alipay.PayAlipaySettings;
import com.highnes.tour.common.alipay.PayResult;
import com.highnes.tour.conf.Constants;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.home.TimeDetailActivity;
import com.highnes.tour.ui.activities.home.ticket.FilterTicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailOrderActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketSearchActivity;
import com.highnes.tour.ui.activities.home.tour.FilterTour1Activity;
import com.highnes.tour.ui.activities.home.tour.FilterTour2Activity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailOrderActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailOrderActivity;
import com.highnes.tour.ui.activities.home.tour.TourActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.utils.AppManager;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * <PRE>
 * 作用:
 *    支付页面。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-10-10   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PayActivity extends BaseTitleActivity {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_PAY = "com.highnes.tour.action_callback_pay";

	private TextView tvPrice, tvInfo;
	private Button btSubmit;
	/** 支付选中 */
	private ImageView ivYl, ivAli, ivWechat;
	/** 支付方式 1.银联 2.支付宝支付 3.微信支付 */
	private int switchPay = 2;
	// 付款总额
	private String price;
	private String mInfo;
	// 订单id
	private String orderID;
	/** 支付类别 0-门票，1-旅游，2-野营，3-抢购 */
	private int payType = -1;

	// 类别id
	private String ProID;

	// 是否支付成功
	private boolean isPaySusseed = false;
	// 是否在进行支付
	private boolean isPay = true;
	// 微信支付
	private PayReq req;
	private IWXAPI api;

	// 积分
	private CheckBox cbPoint;
	// 积分抵扣的金额
	private String pointCount = "";
	private double pointPrice = 0;
	// 是否使用积分兑换
	private boolean isCheckedPoint = false;

	// 红包
	private Spinner spRed;
	private RedAdapter adapter;
	private LinearLayout llRed;
	private double redPrice = 0;
	private String CouponID = "";

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	// 注销广播
	private void unregisterReceiver() {
		unregisterReceiver(updateReceiver);
	}

	/**
	 * 注册一个广播接收器
	 */
	private void registBoradcast() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CALLBACK_PAY);
		registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.d("--aa--过滤器：" + intent.getAction());
			if (999 == Constants.WIN_PAY_CODE) {
				Constants.WIN_PAY_CODE = -999;
				LogUtils.d("--aa--微信支付成功--广播");
				// 支付成功
				paySuccessCall(1);
			}
		}

	};

	@Override
	protected void onResume() {
		LogUtils.d("--aa--onResume");
		super.onResume();
		if (-888 == Constants.WIN_PAY_CODE) {
			paySuccessCall(2);
		} else if (BaseResp.ErrCode.ERR_OK == Constants.WIN_PAY_CODE) {
			Constants.WIN_PAY_CODE = -999;
			LogUtils.d("--aa--微信支付成功--onResume");
			// 支付成功
			paySuccessCall(1);
		}

	};

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_pay;
	}

	@Override
	protected void findViewById2T() {
		ivYl = getViewById(R.id.iv_yl);
		ivAli = getViewById(R.id.iv_ali);
		ivWechat = getViewById(R.id.iv_wechat);
		tvPrice = getViewById(R.id.tv_price);
		tvInfo = getViewById(R.id.tv_info);
		btSubmit = getViewById(R.id.btn_pay);
		cbPoint = getViewById(R.id.cb_point);
		spRed = getViewById(R.id.sp_red);
		llRed = getViewById(R.id.ll_red);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("支付");
		showBackwardView("", SHOW_ICON_DEFAULT);

		price = getIntent().getExtras().getString("price", "");
		orderID = getIntent().getExtras().getString("orderID", "");
		mInfo = getIntent().getExtras().getString("mInfo", "");
		ProID = getIntent().getExtras().getString("ProID", "");
		payType = getIntent().getExtras().getInt("payType", -1);

		tvPrice.setText("￥" + price);
		tvInfo.setText(mInfo);

		Constants.WIN_PAY_CODE = -999;
		adapter = new RedAdapter(mContext);
		spRed.setAdapter(adapter);
		registBoradcast();

	}

	@Override
	protected void setListener2T() {
		ivYl.setOnClickListener(this);
		ivAli.setOnClickListener(this);
		ivWechat.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		getViewById(R.id.ll_yl).setOnClickListener(this);
		getViewById(R.id.ll_ali).setOnClickListener(this);
		getViewById(R.id.ll_wechat).setOnClickListener(this);
		cbPoint.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isCheckedPoint = isChecked;
				// 计算总金额-积分和红包
				if (isCheckedPoint) {
					double priceTotle = Double.valueOf(price) - redPrice - pointPrice;
					tvPrice.setText(ArithUtil.formatPrice(priceTotle));
				} else {
					double priceTotle = Double.valueOf(price) - redPrice;
					tvPrice.setText(ArithUtil.formatPrice(priceTotle));
				}
			}
		});

		spRed.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PointInfo.UserRedInfo info = (PointInfo.UserRedInfo) spRed.getAdapter().getItem(position);
				if ("".equals(info.CID) && "".equals(info.RID)) {
					// 空
					redPrice = 0;
					CouponID = info.CID;
				} else {
					redPrice = info.DisMoney;
					CouponID = info.CID;
				}
				// 计算总金额-积分和红包
				if (isCheckedPoint) {
					double priceTotle = Double.valueOf(price) - redPrice - pointPrice;
					tvPrice.setText(ArithUtil.formatPrice(priceTotle));
				} else {
					double priceTotle = Double.valueOf(price) - redPrice;
					tvPrice.setText(ArithUtil.formatPrice(priceTotle));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByQuery(ProID, price);
	}

	@Override
	public void onClick(View v) {
		// super.onClick(v);
		switch (v.getId()) {
		// 返回
		case R.id.ll_title_back19:
		case R.id.ll_title_back:
			proCancelOrder();
			break;
		// 银联
		case R.id.ll_yl:
		case R.id.iv_yl:
			switchPayMode(1);
			break;
		// 支付宝
		case R.id.ll_ali:
		case R.id.iv_ali:
			switchPayMode(2);
			break;
		// 微信
		case R.id.ll_wechat:
		case R.id.iv_wechat:
			switchPayMode(3);
			break;
		// 支付
		case R.id.btn_pay:
			if (AppUtils.isLogin(mContext)) {
				judgePay();
			} else {
				openActivity(LoginActivity.class);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			proCancelOrder();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 取消订单
	 */
	private void proCancelOrder() {
		showDialog("是否立即取消当前订单？", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (2 == which) {
					requestByOrderCancel(orderID);
				}
			}
		});
	}

	/**
	 * 选择支付方式
	 * 
	 * @param in
	 */
	private void switchPayMode(int in) {
		switchPay = in;
		ivYl.setImageResource(R.drawable.ic_select_normal);
		ivAli.setImageResource(R.drawable.ic_select_normal);
		ivWechat.setImageResource(R.drawable.ic_select_normal);
		switch (in) {
		case 1:// 银联支付
			ivYl.setImageResource(R.drawable.ic_select_pressred);
			break;
		case 2:// 支付宝支付
			ivAli.setImageResource(R.drawable.ic_select_pressred);
			break;
		case 3:// 微信支付
			ivWechat.setImageResource(R.drawable.ic_select_pressred);
			break;

		default:
			break;
		}
	}

	/**
	 * 点击确定后判断支付
	 */
	private void judgePay() {
		switch (switchPay) {
		case 0:
			showToast("请选择支付方式");
			break;
		// 银联
		case 1:
			showToast("银联支付还没有开通");
			break;
		// 支付宝支付
		case 2:
			// 计算总金额-积分和红包
			if (isCheckedPoint) {
				double priceTotle = Double.valueOf(price) - redPrice - pointPrice;
				requestByVerify(ArithUtil.round(priceTotle, 2) + "", orderID, CouponID, redPrice + "", pointPrice + "", pointCount);
			} else {
				double priceTotle = Double.valueOf(price) - redPrice;
				requestByVerify(ArithUtil.round(priceTotle, 2) + "", orderID, CouponID, redPrice + "", "0", "0");
			}
			break;
		// 微信支付
		case 3:
			if (isCheckedPoint) {
				double priceTotle = Double.valueOf(price) - redPrice - pointPrice;
				requestByVerify(ArithUtil.round(priceTotle, 2) + "", orderID, CouponID, redPrice + "", pointPrice + "", pointCount);
			} else {
				double priceTotle = Double.valueOf(price) - redPrice;
				requestByVerify(ArithUtil.round(priceTotle, 2) + "", orderID, CouponID, redPrice + "", "0", "0");
			}
			break;
		default:
			btSubmit.setEnabled(true);
			break;
		}
	}

	// -----------------------支付宝支付--开始---------------------
	/**
	 * 支付宝支付
	 */
	private void payOrder(String payID, String notify_url, String ActualAmount) {
		if (isPay) {
			isPay = false;
			// TODO TEST
			// PayAlipaySettings.startPayAlipay(this, mHandler, payID, "金龟子旅行",
			// mInfo, "0.01", notify_url);
			PayAlipaySettings.startPayAlipay(this, mHandler, payID, "金龟子旅行", mInfo, ActualAmount, notify_url);
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
				btSubmit.setEnabled(true);
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

	// -----------------------支付宝支付--结束---------------------

	// -----------------------微信支付--开始---------------------
	/**
	 * 微信支付
	 * 
	 * @param prepay_id
	 *            预定单ID
	 */
	private void payWeChat(String prepay_id) {
		Constants.WIN_PAY_CODE = -888;
		Constants.WIN_PAY_TYPE = 0;
		// 注册微信支付
		req = new PayReq();
		api = WXAPIFactory.createWXAPI(this, "");
		api.registerApp(Constants.WIN_ID);
		// 判断微信是否安装
		if (api.isWXAppInstalled()) {
			genPayReq(prepay_id);
		} else {
			showToast("还没有安装微信");
			final Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse("http://weixin.qq.com/d");
			intent.setData(uri);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("下载提示").setMessage("\n现在就下载微信APP吗？\n").setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					startActivity(intent);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			builder.create().show();
		}
	}

	/**
	 * 微信支付
	 */
	private void genPayReq(String prepay_id) {
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
		req.sign = genAppSign(signParams);
		api.registerApp(Constants.WIN_ID);
		api.sendReq(req);
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

	// -----------------------微信支付--结束---------------------

	/**
	 * 支付成功后判断类型，回调后台接口
	 * 
	 * @param type
	 *            0表示支付宝，1表示微信
	 */
	private void paySuccessCall(int type) {
		// 支付成功
		try {
			// 门票
			AppManager.getAppManager().finishActivity(TicketDetailOrderActivity.class);
			AppManager.getAppManager().finishActivity(TicketDetailActivity.class);
			AppManager.getAppManager().finishActivity(TicketSearchActivity.class);
			AppManager.getAppManager().finishActivity(FilterTicketActivity.class);
			AppManager.getAppManager().finishActivity(TicketActivity.class);

			// 旅游/野营
			AppManager.getAppManager().finishActivity(Tour1DetailOrderActivity.class);
			AppManager.getAppManager().finishActivity(Tour1DetailActivity.class);
			AppManager.getAppManager().finishActivity(Tour7DetailOrderActivity.class);
			AppManager.getAppManager().finishActivity(Tour7DetailActivity.class);
			AppManager.getAppManager().finishActivity(FilterTour1Activity.class);
			AppManager.getAppManager().finishActivity(FilterTour2Activity.class);
			AppManager.getAppManager().finishActivity(TourActivity.class);
			// 抢购
			AppManager.getAppManager().finishActivity(TimeDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("mOrderID", orderID);
			openActivity(PaySuccessActivity.class, bundle);
			finishActivity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -----------------------网络请求-----------------------

	/**
	 * * Verify 网络请求
	 * 
	 * @param ActualAmount
	 *            实付金额
	 * @param OrderID
	 *            主订单ID
	 * @param CouponID
	 *            代金券编号CID
	 * @param VoucherAmount
	 *            代金券优惠金额
	 * @param PonitMoney
	 *            积分抵扣金额
	 * @param Ponit
	 *            使用积分
	 */
	private void requestByVerify(final String ActualAmount, String OrderID, String CouponID, String VoucherAmount, String PonitMoney, String Ponit) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("ActualAmount", ActualAmount);
		paramDatas.put("OrderID", OrderID);
		paramDatas.put("CouponID", CouponID);
		paramDatas.put("VoucherAmount", VoucherAmount);
		paramDatas.put("PonitMoney", PonitMoney);
		paramDatas.put("Ponit", Ponit);
		String url = "";
		switch (payType) {
		// 门票
		case 0:
			paramDatas.put("Target", "金龟子旅行-门票");
			if (switchPay == 2)
				url = UrlSettings.URL_PAY_TICKET_ZFB;
			else
				url = UrlSettings.URL_PAY_TICKET_WX;
			break;
		// 旅游
		case 1:
			paramDatas.put("Target", "金龟子旅行-旅游度假");
			if (switchPay == 2)
				url = UrlSettings.URL_PAY_TOUR_ZFB;
			else
				url = UrlSettings.URL_PAY_TOUR_WX;
			break;
		// 野营
		case 2:
			paramDatas.put("Target", "金龟子旅行-野营");
			if (switchPay == 2)
				url = UrlSettings.URL_PAY_YEYING_ZFB;
			else
				url = UrlSettings.URL_PAY_YEYING_WX;
			break;
		// 抢购
		case 3:
			paramDatas.put("Target", "金龟子旅行-抢购");
			if (switchPay == 2)
				url = UrlSettings.URL_PAY_QIANGGOU_ZFB;
			else
				url = UrlSettings.URL_PAY_QIANGGOU_WX;
			break;
		default:
			break;
		}
		new NETConnection(mContext, url, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonVerify(result, ActualAmount);
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
	 * Verify JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonVerify(String result, String ActualAmount) {
		try {
			if (switchPay == 2) {
				// 支付宝
				if (StringUtils.isEquals("0", JsonUtils.getString(result, "state", "-1"))) {
					// 支付宝回调地址
					String notifyUrl = JsonUtils.getString(result, "notify_url", "");
					// 支付ID
					String payID = JsonUtils.getString(result, "points", "");
					if (StringUtils.isEmpty(payID) || StringUtils.isEmpty(notifyUrl)) {
						showToast("支付失败，请重新支付");
					} else {
						isPaySusseed = false;
						payOrder(payID, notifyUrl, ActualAmount);
					}
				} else {
					showToast("支付失败，请重新支付");
				}
			} else {
				// 微信支付
				if (StringUtils.isEquals("0", JsonUtils.getString(result, "state", "-1"))) {
					// 支付ID
					String payID = JsonUtils.getString(result, "points", "");
					if (StringUtils.isEmpty(payID)) {
						showToast("支付失败，请重新支付");
					} else {
						payWeChat(payID);
					}
				} else {
					showToast("支付失败，请重新支付");
				}
			}
		} catch (Exception e) {
			showToast("支付失败，请重新支付");
			e.printStackTrace();
		}
	}

	/**
	 * Query 查询积分和红包
	 * 
	 * @param ProID
	 * @param Money
	 *            支付的原价格
	 */
	private void requestByQuery(String ProID, String Money) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("ProID", ProID);
		paramDatas.put("Money", Money);
		new NETConnection(mContext, UrlSettings.URL_PAY_QUERY, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonQuery(result);
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
	 * Query JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonQuery(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				PointInfo info = GsonUtils.json2T(result, PointInfo.class);
				String Multiply = info.Multiply;
				String UserPonit = info.UserPonit;
				// 判断积分
				if (Integer.valueOf(UserPonit) <= 0) {
					// 没有积分
					pointPrice = 0;
					pointCount = "0";
					cbPoint.setText("可用0积分抵￥0");

				} else {
					double price = Integer.valueOf(UserPonit) * Double.valueOf(Multiply);
					// 只留2位小数
					pointPrice = ((int) (price * 100)) / 100.0;
					// 计算花费的积分数量
					pointCount = (int) (pointPrice / Double.valueOf(Multiply)) + "";
					cbPoint.setText("可用" + pointCount + "积分抵￥" + pointPrice);
				}

				// 判断红包
				if (info.UserRed != null && info.UserRed.size() > 0) {
					PointInfo.UserRedInfo red = new PointInfo.UserRedInfo("", "不使用红包兑换", 0.0, "");
					info.UserRed.add(0, red);
					adapter.setList(info.UserRed);
					redPrice = 0;
				} else {
					PointInfo.UserRedInfo red = new PointInfo.UserRedInfo("", "不使用红包兑换", 0.0, "");
					adapter.add(red);
					redPrice = 0;
				}

			} else {
				pointPrice = 0;
				pointCount = "0";
				redPrice = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			pointPrice = 0;
			pointCount = "0";
			redPrice = 0;
		}
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrderCancel(final String OrderID) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("OrderID", OrderID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_CANCEL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonCancel(result);
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
	private void parseJsonCancel(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				// 删除成功
				showToast("取消订单成功");
				finishActivity();
			} else {
				showToast("取消订单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
