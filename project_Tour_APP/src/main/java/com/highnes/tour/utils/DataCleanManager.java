package com.highnes.tour.utils;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

/**
 * <PRE>
 *  作用:
 *     数据清除管理器。
 *  注意事项:
 *     主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录。
 *     
 *     缓存清理方案：
 *     
 *     1、应用缓存清理:（在获得root权限的前提下）
 *         A./data/data/ packageName /cache（应用缓存）
 *         B./mnt/sdcard/Android/ packageName /cache（外部应用缓存，FROYO以后支持）
 *         C./data/data/packageName/database/webview.db*（WebView缓存）
 *         D./data/data/packageName/database/webviewCache.db*（WebView缓存）
 *         E.其他一些/data/data/packageName/*cache目录（应用缓存）
 *         F./data/data/packageName/files（比较严格的清理策略时也可以选择清理）
 *     
 *     2、应用卸载残留清理
 *         方案规划中...
 *         
 *     3、无用安装包清理
 *         3.1无用安装包的清理比较简单。判断无用安装包的标准是：
 *             A.存储目录中存在APK文件，但是该APK已被安装
 *             B.APK文件已损坏
 *         3.2扫描安装包有两种处理方式：
 *             A.深度扫描：扫描SD卡上的所有目录
 *             B.快速扫描，只扫描手机管理软件（豌豆荚、360手机助手、应用宝等）和浏览器（UCWeb、QQ浏览器）和Download目录。
 *         3.3要清理其他下载文件也可以按照这个思路来实现。
 *         
 *     4、系统垃圾清理
 *         系统垃圾清理包括临时文件、缩略图、系统日志、失效文件、空白文件等的清理，下面做一下介绍。
 *         
 *         4.1清理系统日志
 *             日志文件分为系统日志和应用日志两部分，其各自的存放位置分别为：
 *             4.1.1系统日志的存放位置如下（不同手机可能会有所差异）：
 *                 A./data/local/tmp/*
 *                 B./data/tmp/*
 *                 C./data/system/usagestats/*
 *                 D./data/system/appusagestates/*
 *                 E./data/system/dropbox/*
 *                 F./data/tombstones/*
 *                 G./data/anr/*
 *                 H./dev/log/main
 *             4.1.2应用日志存放位置可以判断的有：SD卡上后缀名为“.log”或者“*log.txt”等结尾的文件。
 *             
 *         4.2清理图片缩略图
 *             在SD卡上的DICM目录下有一个隐藏的目录，名字叫“.thumbnails”，这个目录存放的是系统图片的缓存。
 *             清理缓存主要就是清理这个目录。应用目录也可能有缩略图文件，但不容易识别，所以不建议清理，可以放在清理残留数据时一起清理。
 *             
 *         4.3清理失效文件与空白文件
 *             判断标准：
 *                 A.文件的长度为0则认为是空白文件，可以删除
 *                 B.文件夹中不包含任何文件或文件夹，则认为是空白文件夹，可以删除。
 *                 C.除了上面两种情况外，还可以扫描文件的创建时间，很长时间未使用的文件认为是无效文件。
 *                 
 *         4.4大文件清理
 *             对于大文件的识别比较简单，只判断文件大小是否超过一定的阀值（例如：豌豆荚认为大小超过10M即为大文件）即可。
 *             但这里有两个需要注意的点：
 *                 A.大文件一般是视频文件或者应用数据（例如百度map的数据），对于这些文件在清理是建议默为“不选中”状态。
 *                 B.可以充分利用2.2.1中建立的映射关系，对大文件是否建议删除提供更加准确的建议。
 *                 
 *         4.5广告文件等的识别和处理
 *         
 *  修改历史:
 *  -----------------------------------------------------------
 *      VERSION    DATE         AUTHOR      CHANGE/COMMENT
 *  -----------------------------------------------------------
 *      1.0        2015-01-21   FUNY        create
 *  -----------------------------------------------------------
 * </PRE>
 */
public class DataCleanManager {
	/**
	 * 清除本应用内部缓存(/data/data/packageName/cache)
	 * 
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * 清除本应用所有数据库(/data/data/packageName/databases)
	 * 
	 * @param context
	 */
	@SuppressLint("SdCardPath")
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * 清除本应用SharedPreference(/data/data/packageName/shared_prefs)
	 * 
	 * @param context
	 */
	@SuppressLint("SdCardPath")
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * 按名字清除本应用数据库
	 * 
	 * @param context
	 * @param dbName
	 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/**
	 * 清除/data/data/packageName/files下的内容
	 * 
	 * @param context
	 */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/packageName/cache)
	 * 
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * 
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/**
	 * 清除本应用所有的数据
	 * 
	 * @param context
	 * @param filepath
	 */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/**
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
	 * 
	 * @param directory
	 */
	public static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
}