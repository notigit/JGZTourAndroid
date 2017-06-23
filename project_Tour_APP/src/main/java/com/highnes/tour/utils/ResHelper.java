package com.highnes.tour.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;

/**
 * <PRE>
 * 作用:
 *    资源文件帮助类。
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
@SuppressWarnings("ResourceType")
public class ResHelper {

	/**
	 * 获取Drawable资源
	 * 
	 * @param context
	 *            上下文
	 * @param resId
	 *            资源ID
	 * @return Drawable对象/错误返回null
	 */
	public static Drawable getDrawableById(Context context, @DrawableRes int resId) {
		if (context == null) {
			LogUtils.e("get drawable, resId " + resId + ", but context is null");
			return null;
		}
		return context.getResources().getDrawable(resId);
	}

	/**
	 * 获取Color的值
	 * 
	 * @param context
	 *            上下文
	 * @param resId
	 *            资源ID
	 * @return Color的值/错误返回-1
	 */
	public static int getColorById(Context context, @ColorRes int resId) {
		if (context == null) {
			LogUtils.e("get Color, resId " + resId + ", but context is null");
			return -1;
		}
		return context.getResources().getColor(resId);
	}
	/**
	 * 获取Color的值
	 * 
	 * @param context
	 *            上下文
	 * @param resId
	 *            资源ID
	 * @return Color的值/错误返回-1
	 */
	public static float getDimenById(Context context, @DimenRes int resId) {
		if (context == null) {
			LogUtils.e("get Color, resId " + resId + ", but context is null");
			return -1;
		}
		return context.getResources().getDimension(resId);
	}

	/**
	 * 获取Array的值
	 * 
	 * @param context
	 *            上下文
	 * @param resourceIdArray
	 *            资源ID
	 * @return Array的值/错误返回null
	 */
	public static String[] getArrayById(Context context, @ColorRes int resourceIdArray) {
		if (context == null) {
			LogUtils.e("get Color, resId " + resourceIdArray + ", but context is null");
			return null;
		}
		return context.getResources().getStringArray(resourceIdArray);
	}
}
