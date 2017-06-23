package com.highnes.tour.view.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * <PRE>
 * 作用:
 *    解决嵌套之后ScrollView的滑动和WebView的滑动就会有冲突。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-09-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MyWebView extends WebView {
	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWebView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}

}