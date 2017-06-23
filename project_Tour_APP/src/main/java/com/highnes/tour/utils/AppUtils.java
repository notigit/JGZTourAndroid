package com.highnes.tour.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.highnes.tour.conf.PreSettings;

/**
 * <PRE>
 * 作用:
 *    常用信息工具类。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-05   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class AppUtils {
	public static String getTemp() {
		return "?temptemptemp=" + System.currentTimeMillis();
	}
	
	public static String getTempAnd() {
		return "&temptemptemp=" + System.currentTimeMillis();
	}

	/**
	 * 判断用户是否登录
	 * 
	 * @param mContext
	 * @return true是普通用户，false是游客
	 */
	public static boolean isLogin(Context mContext) {
		int mUserState = (Integer) SPUtils.get(mContext, PreSettings.USER_TYPE.getId(), PreSettings.USER_TYPE.getDefaultValue());
		// 1是普通用户
		return 1 == mUserState;
	}

	/**
	 * 判断某个界面是否在前台 使用次方法，必须加上以下权限否则会报错 <uses-permission
	 * android:name="android.permission.GET_TASKS"/>
	 * 
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	@SuppressWarnings({ "deprecation" })
	private static boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	// 版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	// 版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}

	/**
	 * 根据包名判断是否安装某个App
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkApp(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (PackageInfo pi : packs) {
			if (pi.applicationInfo.packageName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}
}
