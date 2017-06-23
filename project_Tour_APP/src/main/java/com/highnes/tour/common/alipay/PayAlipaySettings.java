package com.highnes.tour.common.alipay;

/**
 * 支付宝支付集成
 * 
 * @author Administrator
 * 
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.highnes.tour.conf.Constants;
import com.highnes.tour.utils.ToastHelper;

/**
 * <PRE>
 * 作用:
 *    支付宝支付集成
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-10-29   QINX        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PayAlipaySettings {
	public static final int SDK_PAY_FLAG = 1;
	public static final int SDK_CHECK_FLAG = 2;

	/** TODO 页面下单成功后，付款商品的信息 */
	private static Activity payActivity = null;
	// 订单号 */
	private static String PAY_ORDER_NUMBER = "";
	/** 支付宝回调接口 */
	private static String PAY_CALLBACK = "";
	/** 支付标题 */
	private static String PAY_TITLE = "";
	/** 支付介绍 */
	private static String PAY_CONTENT = "";
	/** 支付总金额 */
	private static String PAY_PRICE = "";
	/** 支付结果 */
	private static Handler PAY_Handler = null;

	/**
	 * 
	 * @param activity
	 *            有提示，绝不能为空
	 * @param handler
	 *            监听支付状态情况必要
	 * @param order
	 *            订单号
	 * @param title
	 *            商品标题
	 * @param content
	 *            商品介绍
	 * @param price
	 *            商品总价格
	 * @param callback
	 *            支付宝回调链接
	 */
	public static void startPayAlipay(Activity activity, Handler handler, String order, String title, String content, String price, String callback) {
		payActivity = activity;
		PAY_Handler = handler;
		PAY_ORDER_NUMBER = order;
		PAY_TITLE = title;
		PAY_CONTENT = content;
		PAY_PRICE = price;
		PAY_CALLBACK = callback;
		if (payActivity == null || PAY_Handler == null || "".equals(PAY_ORDER_NUMBER) || "".equals(PAY_TITLE) || "".equals(PAY_CONTENT) || "".equals(PAY_PRICE)
				|| "".equals(PAY_CALLBACK)) {
			ToastHelper.showToast(payActivity, "数据不能为空");
		} else {
			payAlipay();
		}
	}

	/**
	 * 
	 * @param context
	 *            有提示，绝不能为空
	 * @param handler
	 *            监听支付状态情况必要
	 * @param order
	 *            订单号
	 * @param title
	 *            商品标题
	 * @param content
	 *            商品介绍
	 * @param price
	 *            商品总价格
	 * @param callback
	 *            支付宝回调链接
	 */
	public static void startPayAlipay(Context context, Handler handler, String order, String title, String content, String price, String callback) {
		startPayAlipay((Activity) context, handler, order, title, content, price, callback);
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public static void payAlipay() {
		if (TextUtils.isEmpty(Constants.ALIPAY_PARTNER) || TextUtils.isEmpty(Constants.ALIPAY_PRIVATE) || TextUtils.isEmpty(Constants.ALIPAY_SELLER)) {
			new AlertDialog.Builder(payActivity).setTitle("警告").setMessage("需要配置PARTNER | PAY_PRIVATE| SELLER")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							payActivity.finish();
						}
					}).show();
			return;
		}
		// 订单
		String orderInfo = getOrderInfo(PAY_TITLE, PAY_CONTENT, PAY_PRICE);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
		System.out.println("--payInfo"+payInfo);
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(payActivity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				PAY_Handler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 创建订单信息
	 * 
	 */
	public static String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Constants.ALIPAY_PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Constants.ALIPAY_SELLER + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + PAY_ORDER_NUMBER + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + PAY_CALLBACK + "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return SignUtils.sign(content, Constants.ALIPAY_PRIVATE);
	}

	/**
	 * 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
