package com.highnes.tour.view.webview;

import android.content.Context;
import android.net.http.SslError;
import android.os.Handler;
import android.util.AttributeSet;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.highnes.tour.R;

/**
 * <PRE>
 * 作用:
 *    自定义有进度条的WebView。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-12-09   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

	private ProgressBar progressbar;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 设置进度条的样式与位置显示
		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		progressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.webview_style));
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
		addView(progressbar);
		setWebChromeClient(new WebChromeClient());
		// setWebViewClient(new WebViewClient() {
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return true;
		// }
		// });
		setWebViewClient(new MyWebViewClient());
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress >= 100) {
				progressbar.setVisibility(GONE);
				if (oProgressSucceed != null) {
					oProgressSucceed.onSucceed();
				}
			} else {
				progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

		// 配置权限（同样在WebChromeClient中实现）
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}
	}

	// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * 
	 * 自定义的WebViewClient
	 * 
	 * @author FUNY
	 * @date 2016/07/05 14:40:57
	 * 
	 */
	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			/**
			 * 解决4.4+的问题,这是个大坑!!为了能兼容所有该版本.我在这里通过 webview 与js交互的方式来实现: 首先通过
			 * js的语法来循环遍历接收到的数据,提取出带有”img”标签的内容,高度自适应,设置它的宽度占屏幕的100%:
			 */
			view.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName('img'); " + "for(var i=0;i<objs.length;i++)  " + "{"
					+ "var img = objs[i];   " + "    " + "img.style.maxWidth = '100%';   " + "img.style.height = 'auto';   " + "}" + "})()");
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		//		设置WebView接受所有网站的证书
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			super.onReceivedSslError(view, handler, error); //必须注释否则无效
			// handler.cancel();// Android默认的处理方式
			handler.proceed();// 接受所有网站的证书
			// handleMessage(Message msg);// 进行其他处理
		}

	}

	/**
	 * 进度条加载成功后的的回调
	 */
	public interface onProgressSucceed {
		void onSucceed();
	}

	private onProgressSucceed oProgressSucceed;

	public void setoProgressSucceed(onProgressSucceed oProgressSucceed) {
		this.oProgressSucceed = oProgressSucceed;
	}

	/**
	 * Java调用JS的方法
	 * 
	 * @param urlCentent
	 *            例如："javascript:obtainPhoneType('android')"
	 */
	public void loadJava2JS(final String urlCentent) {
		ProgressWebView.this.post(new Runnable() {

			@Override
			public void run() {
				ProgressWebView.this.loadUrl(urlCentent);
			}
		});
	}
}