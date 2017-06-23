package com.highnes.tour.view.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <PRE>
 * 作用:
 *    自定义ViewPager，用于是否可以滑动页面。默认可以。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-13   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class CustomViewPager extends ViewPager {
	private boolean enabled;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	// 触摸没有反应就可以了
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	/**
	 * 设置是否打开滑动
	 * 
	 * @param enabled
	 *            true可以滑动/false不可以滑动。
	 */
	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
