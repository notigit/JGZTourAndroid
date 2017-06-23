package com.highnes.tour.ui.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.view.webview.ProgressWebView;

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
public class WebViewTitleActivity extends BaseTitleActivity {
    private ProgressWebView web;
    // url地址
    private String mUrl;
    // 传递过来的类型
    private int mType;
    // 传递过来的标题
    private String mTitle;

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
        mTitle = getIntent().getExtras().getString("mTitle", "");
        setTitle(mTitle);
        showBackwardView("", SHOW_ICON_DEFAULT);
        initWebView();
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
            //访问https网站
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl() {
        String urlStr = mUrl;
        switch (mType) {
            // Banner
            case 1001:
                break;
            // 首页..活动详情
            case 1002:
                break;
            // 首页..违章查询
            case 1004:
                break;
            // 首页..ETC查询
            case 1005:
                break;
            // 首页..天气查询
            case 1007:
                break;
            // 首页..资费查询
            case 1008:
                break;
            // 首页..航班动态
            case 1009:
                break;

            // 首页..门票..活动详情
            case 1100:
                break;
            // 首页..门票..详情..票型说明
            case 1101:
                break;
            // 首页..门票..详情..保险说明
            case 1102:
                break;
            // 首页..旅游度假..详情..单房差
            case 1201:
                break;
            // 首页..旅游度假..当地玩乐
            case 1202:
                break;

            // 首页..头条详情
            case 1901:
                break;
            // 首页..消息
            case 1902:
                break;
            // 发现..轮播图
            case 3001:
                break;
            // 优惠券说明
            case 4001:
                break;
            // 注册协议
            case 5001:
                break;
            // 积分说明
            case 5002:
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
}
