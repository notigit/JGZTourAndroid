package com.highnes.tour.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;

/**
 * <PRE>
 * 作用:
 *    常用单位转换的辅助类。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0        2015-01-15   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class DensityUtils {
	private DensityUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 *            上下文对象
	 * @param val
	 *            要转换的dp值
	 * @return 返回转换后的px值
	 */
	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 *            上下文对象
	 * @param val
	 *            要转换的dp值
	 * @return 返回转换后的px值
	 */
	public static float dp2pxF(Context context, float dpVal) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
				context.getResources().getDisplayMetrics());
	}

	/**
	 * sp转px
	 * 
	 * @param context
	 *            上下文对象
	 * @param val
	 *            要转换的sp值
	 * @return 返回转换后的px值
	 */
	public static int sp2px(Context context, float spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * px转dp
	 * 
	 * @param context
	 *            上下文对象
	 * @param pxVal
	 *            要转换的px值
	 * @return 返回转换后的dp值
	 */
	public static float px2dp(Context context, float pxVal) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxVal / scale);
	}

	/**
	 * px转sp
	 * 
	 * @param context
	 *            上下文对象
	 * @param pxVal
	 *            要转换的px值
	 * @return 返回转换后的sp值
	 */
	public static float px2sp(Context context, float pxVal) {
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}

	/**
	 * 屏幕宽高
	 * 
	 */
	public static class Dimension {
		private int mWidth;
		private int mHeight;

		public Dimension(int mWidth, int mHeight) {
			super();
			this.mWidth = mWidth;
			this.mHeight = mHeight;
		}

		public int getmWidth() {
			return mWidth;
		}

		public void setmWidth(int mWidth) {
			this.mWidth = mWidth;
		}

		public int getmHeight() {
			return mHeight;
		}

		public void setmHeight(int mHeight) {
			this.mHeight = mHeight;
		}

		public Dimension() {
		}
	}

	/**
	 * 获取区域一（屏幕区域）的高度与宽度
	 * 
	 * @param activity
	 * @return Dimension对象。高度宽度单位为px
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static Dimension getAreaOne(Activity activity) {
		Point dims = new Point();
		if (android.os.Build.VERSION.SDK_INT >= 13) {
			activity.getWindowManager().getDefaultDisplay().getSize(dims);
		} else if (android.os.Build.VERSION.SDK_INT < 13) {
			dims.x = activity.getWindowManager().getDefaultDisplay().getWidth();
			dims.y = activity.getWindowManager().getDefaultDisplay()
					.getHeight();
		}
		Dimension dimen = new Dimension();
		// Display disp = activity.getWindowManager().getDefaultDisplay();
		// Point outP = new Point();
		// disp.getSize(outP);
		dimen.setmWidth(dims.x);
		dimen.setmHeight(dims.y);
		return dimen;
	}

	/**
	 * 获取区域二（应用区域）的高度与宽度
	 * 
	 * @param activity
	 * @return Dimension对象。高度宽度单位为px
	 */
	public static Dimension getAreaTwo(Activity activity) {
		Dimension dimen = new Dimension();
		Rect outRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(outRect);
		dimen.setmWidth(outRect.width());
		dimen.setmHeight(outRect.height());
		return dimen;
	}

	/**
	 * 获取区域三（View绘制区域）的高度与宽度
	 * 
	 * @param activity
	 * @return Dimension对象。高度宽度单位为px
	 */
	public static Dimension getAreaThree(Activity activity) {
		Dimension dimen = new Dimension();
		// 用户绘制区域
		Rect outRect = new Rect();
		activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
				.getDrawingRect(outRect);
		dimen.setmWidth(outRect.width());
		dimen.setmHeight(outRect.height());
		// end
		return dimen;
	}

	/**
	 * 获取控件的宽高
	 * 
	 * @param view
	 *            控件
	 * @return Dimension
	 */
	public static Dimension getViewDimension(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		int height = view.getMeasuredHeight();
		int width = view.getMeasuredWidth();
		return new Dimension(width, height);
	}
}
