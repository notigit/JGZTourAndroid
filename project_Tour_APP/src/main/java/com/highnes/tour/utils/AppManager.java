package com.highnes.tour.utils;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

/**
 * <PRE>
 * 作用:
 *    应用管理类[单例模式]。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-08-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class AppManager {
	// Activity栈
	private static Stack<FragmentActivity> mActivityStack;
	// 本类实例
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 获得本类的实例
	 * 
	 * @return 本类的实例
	 */
	public static AppManager getAppManager() {
		if (null == instance) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加一个Activity到栈中
	 * 
	 * @param activity
	 *            Activity组件
	 */
	public void addActivity(FragmentActivity activity) {
		if (null == mActivityStack) {
			mActivityStack = new Stack<FragmentActivity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * 获取当前栈中的Activity
	 * 
	 * @return Activity组件
	 */
	public FragmentActivity currentActivity() {
		try {
			FragmentActivity activity = mActivityStack.lastElement();
			return activity;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 关闭当前(最后一个)Activity
	 */
	public void finishActivity() {
		FragmentActivity activity = mActivityStack.lastElement();
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 关闭指定的Activity
	 * 
	 * @param activity
	 *            指定的Activity
	 */
	public void finishActivity(FragmentActivity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 根据类关闭Activity
	 * 
	 * @param cls
	 *            指定的类
	 */
	public void finishActivity(Class<?> cls) {
		for (FragmentActivity activity : mActivityStack) {
			System.out.println("--准备关闭--"+cls.getClass().getSimpleName());
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 关闭所有的Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 退出应用
	 * 
	 * @param context
	 *            上下文对象
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}
}
