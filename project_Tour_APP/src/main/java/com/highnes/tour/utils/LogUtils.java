package com.highnes.tour.utils;

import android.util.Log;

import com.highnes.tour.BuildConfig;

/**
 * <PRE>
 * 作用:
 *    简单的封装Log类,便于控制是否打印Log信息。
 * 注意事项:
 *    默认msg前面添加"--"，便于控制台查看。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0        2015-08-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public final class LogUtils {
	// // 控制是否打印Log信息
	// private static boolean openLog = true;
	//
	// public static void d(String tag, String msg) {
	// if (openLog) {
	// Log.d(tag, "--" + msg);
	// }
	// }
	//
	// public static void w(String tag, String msg) {
	// if (openLog) {
	// Log.w(tag, "--" + msg);
	// }
	// }
	//
	// public static void e(String tag, String msg) {
	// if (openLog) {
	// Log.e(tag, "--" + msg);
	// }
	// }
	//
	// public static void i(String tag, String msg) {
	// if (openLog) {
	// Log.i(tag, "--" + msg);
	// }
	// }
	// -----------------------------------------------------------------
	private static String className; // 所在的类名
	private static String methodName; // 所在的方法名
	private static int lineNumber; // 所在行号

	public static final int VERBOSE = 1; // 显示Verbose及以上的Log
	public static final int DEBUG = 2; // 显示Debug及以上的Log
	public static final int INFO = 3; // 显示Info及以上的Log
	public static final int WARN = 4; // 显示Warn及以上的Log
	public static final int ERROR = 5; // 显示Error及以上的Log
	public static final int NOTHING = 6; // 全部不显示

	public static final int LEVEL = VERBOSE; // 控制显示的级别

	private LogUtils() {
	}

	public static boolean isDebuggable() {
		return BuildConfig.DEBUG;
	}

	private static String createLog(String log) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("--[");
		buffer.append(methodName);
		buffer.append(":");
		buffer.append(lineNumber);
		buffer.append("]");
		buffer.append(log);

		return buffer.toString();
	}

	private static void getMethodNames(StackTraceElement[] sElements) {
		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}

	public static void v(String message) {
		if (!isDebuggable()) {
			return;
		}

		if (LEVEL <= VERBOSE) {
			getMethodNames(new Throwable().getStackTrace());
			Log.v(className, createLog(message));
		}
	}

	public static void d(String message) {
		if (!isDebuggable()) {
			return;
		}

		if (LEVEL <= DEBUG) {
			getMethodNames(new Throwable().getStackTrace());
			Log.d(className, createLog(message));
		}
	}

	public static void i(String message) {
		if (!isDebuggable()) {
			return;
		}

		if (LEVEL <= INFO) {
			getMethodNames(new Throwable().getStackTrace());
			Log.i(className, createLog(message));
		}
	}

	public static void w(String message) {
		if (!isDebuggable()) {
			return;
		}

		if (LEVEL <= WARN) {
			getMethodNames(new Throwable().getStackTrace());
			Log.w(className, createLog(message));
		}
	}

	public static void e(String message) {
		if (!isDebuggable()) {
			return;
		}

		if (LEVEL <= ERROR) {
			getMethodNames(new Throwable().getStackTrace());
			Log.e(className, createLog(message));
		}
	}
}