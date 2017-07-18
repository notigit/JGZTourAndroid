package com.highnes.tour.ui.activities;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

import com.alipay.share.sdk.openapi.algorithm.MD5;
import com.highnes.tour.R;
import com.highnes.tour.common.alipay.PayAlipaySettings;
import com.highnes.tour.common.alipay.PayResult;
import com.highnes.tour.conf.Constants;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.utils.AppUtils;
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
 * 所有H5页面。
 * 注意事项:
 * 无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-08-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PHPWebViewActivity extends BaseTitleActivity {
    public static final String ACTION_CALLBACK_PAY_SUCCEED = "com.highnes.tour.action_callback_pay_succeed";
    private ProgressWebView web;
    // url地址
    private String mUrl;
    // 传递过来的类型
    private int mType;

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
        intentFilter.addAction(ACTION_CALLBACK_PAY_SUCCEED);
        mContext.registerReceiver(updateReceiver, intentFilter);
    }

    /**
     * 自定义广播接收器
     */
    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//			LogUtils.d("--aa--过滤器：" + intent.getAction());
            if (999 == Constants.WIN_PAY_CODE) {
//				LogUtils.d("--aa--微信支付成功--广播");
                Constants.WIN_PAY_CODE = -999;
                // 支付成功
                paySuccessCall(1);
            }
        }

    };

    @Override
    public void onResume() {
        super.onResume();
//		LogUtils.d("--aa--onResume");
        if (BaseResp.ErrCode.ERR_OK == Constants.WIN_PAY_CODE) {
//			LogUtils.d("--aa--微信支付成功--onResume");
            Constants.WIN_PAY_CODE = -999;
            // 支付成功
            paySuccessCall(1);
        }
    }

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
        mType = getIntent().getExtras().getInt("mType", 0);
        mUrl = getIntent().getExtras().getString("mUrl", "");
        showBackwardView("取消", SHOW_ICON_GONE);
        setTitle("");
        initWebView();
        initWeiApi();
        registBoradcast();
    }

    @Override
    protected void setListener2T() {
    }

    @Override
    protected void processLogic2T(Bundle savedInstanceState) {
        loadUrl();
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
            //访问https网站 启用mixed content
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            web.addJavascriptInterface(this, "tour");

            // 启用数据库
            webSettings.setDatabaseEnabled(true);
            String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
            // 启用地理定位
            webSettings.setGeolocationEnabled(true);
            // 设置定位的数据库路径
            webSettings.setGeolocationDatabasePath(dir);
            // 最重要的方法，一定要设置，这就是出不来的主要原因
            webSettings.setDomStorageEnabled(true);

//            web.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                @SuppressWarnings("deprecation")
//                @Override
//                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                    LogUtils.d("--伟哥--" + url);
//                    // 这里处理为了就是让一般的http和https协议开头的走正常的页面，而其他的URL则会开启一个Acitity然后去调用原生APP应用
//                    if (url.startsWith("http") || url.startsWith("https")) {
//                        Log.e("TAG", "shouldInterceptRequest: "+url );
//                        return super.shouldInterceptRequest(view, url);
//                    } else {
//                        try {
//                            Log.e("TAG", "shouldInterceptRequests: "+url );
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            startActivity(intent);
//                            mHandlerPhone.sendEmptyMessage(0);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//                }
//
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandlerPhone = new Handler() {
        public void handleMessage(android.os.Message msg) {
            web.goBack();
        }

    };

    private void loadUrl() {
        String urlStr = mUrl;
        switch (mType) {
            // 首页..酒店详情
            case 10000:
                break;
            // 首页..酒店..更多
            case 10001:
                break;
            // 首页..美食
            case 10002:
                break;
            // 首页..土特产
            case 10003:
                break;
            // 首页..娱乐
            case 10004:
                break;
            // 首页..当地玩乐
            case 10005:
                break;
            // 周边
            case 20000:
                break;
            // 我的..购物车
            case 50001:
                break;
            // 我的收藏
            case 50002:
                break;
            // 订单详情
            case 60001:
                break;
            default:
                break;
        }
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

    // -------------------------------Java与JS交互-------------------------------

    /**
     * 使用Java调用JS
     *
     *
     */
    public void getAppUserID(String userID) {
        LogUtils.d("--已发送PHP用户ID" + userID);
        web.loadJava2JS("javascript:getAppUserID('" + userID + "')");
    }

    /**
     * JS调用Java的方法 funFromUserID 是JS中定义的方法 android sdk api >= 17 时需要加@JavascriptInterface
     * 支付的JS
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
     * JS调用Java的方法 funFromLatLng 是JS中定义的方法 android sdk api >= 17 时需要加@JavascriptInterface
     * 支付的JS
     */
    @JavascriptInterface
    public void funFromLatLng() {
        try {
            LogUtils.d("--PHP请求经纬度");
            String lat = SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()).toString();
            String lng = SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()).toString();
            LogUtils.d("--响应PHP" + "javascript:getAppLatLng('" + lat + "','" + lng + "')");
            web.loadJava2JS("javascript:getAppLatLng('" + lat + "','" + lng + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JS调用Java的方法 funFromAndroid是JS中定义的方法 android sdk api >= 17 时需要加@JavascriptInterface
     * 支付的JS
     *
     * @param pay_type  支付类型
     * @param prepay_id 预定单id
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
            String payID = JsonUtils.getString(jsonStr, "code", "");// 支付ID
            String title = JsonUtils.getString(jsonStr, "title", "");// 商品标题
            String info = JsonUtils.getString(jsonStr, "body", "");// 商品描述
            String price = JsonUtils.getString(jsonStr, "money", "0");// 商品价格
            String notify_url = JsonUtils.getString(jsonStr, "notifyurl", "");// 支付回调地址
            PayAlipaySettings.startPayAlipay(mContext, mHandler, payID, title, info, price + "", notify_url);
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
        }

        ;
    };

    // -----------------------支付结果---------------------

    /**
     * 支付成功后判断类型，回调后台接口
     *
     * @param type 0表示支付宝，1表示微信
     */
    private void paySuccessCall(int type) {
        try {
            LogUtils.d("去成功页面--微信支付");
            openActivity(PHPPaySuccessActivity.class);
            finishActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
