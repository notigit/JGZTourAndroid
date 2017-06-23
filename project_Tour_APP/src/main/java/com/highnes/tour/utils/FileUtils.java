package com.highnes.tour.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;

/**
 * <PRE>
 * 作用:
 *    文件目录管理。
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
public class FileUtils {

	/**
	 * 获得图片的缓存路径
	 * 
	 * @param context
	 *            上下文
	 * */
	public static File getCacheImageFiles(Context context) {
		File file;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			file = new File(context.getExternalCacheDir().getAbsolutePath() + "/images/");
			if (!file.exists()) {
				file.mkdir();
				LogUtils.i("（有SDK卡）创建SDK图片缓存路径" + file.getAbsolutePath());
			}
		} else {
			// 当前不可用
			file = new File(context.getCacheDir().getAbsolutePath() + "/images/");
			if (!file.exists()) {
				file.mkdir();
				LogUtils.i("(没有SDK卡)创建图片缓存路径" + file.getAbsolutePath());
			}
		}
		return file;
	}

	/**
	 * 获得图片的缓存路径
	 * 
	 * @param context
	 *            上下文
	 * */
	public static File getCacheApkFiles(Context context) {
		File file;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			file = new File(context.getExternalCacheDir().getAbsolutePath() + "/apk/");
			if (!file.exists()) {
				file.mkdir();
				LogUtils.i("（有SDK卡）创建SDK apk缓存路径" + file.getAbsolutePath());
			}
		} else {
			// 当前不可用
			file = new File(context.getCacheDir().getAbsolutePath() + "/apk/");
			if (!file.exists()) {
				file.mkdir();
				LogUtils.i("(没有SDK卡)创建apk缓存路径" + file.getAbsolutePath());
			}
		}
		return file;
	}

	/**
	 * 获得HTML的缓存路径
	 * 
	 * @param 上下文
	 */
	public static File getCacheHtmlFiles(Context context) {
		File file;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			file = new File(context.getExternalCacheDir().getAbsolutePath() + "/html/");
			if (!file.exists()) {
				file.mkdir();
				LogUtils.i("（有SDK卡）创建SDK HTML缓存路径" + file.getAbsolutePath());
			}
		} else {
			// 当前不可用
			file = new File(context.getCacheDir().getAbsolutePath() + "/html/");
			if (!file.exists()) {
				file.mkdir();
				LogUtils.i("(没有SDK卡)创建HTML缓存路径" + file.getAbsolutePath());
			}
		}
		return file;
	}

	/**
	 * 删除一个目录包含里面的文件
	 * 
	 * @param path
	 *            路径
	 */
	public static void deleteDirAndFile(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete(); // 删除一个文件
			}
		}
		dir.delete();// 删除目录本身
	}

	public static String copyImgToSd(Context context, int resId) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {

			File dbFile = new File(getCacheImageFiles(context), "orderImg");  ;
			if (!dbFile.exists()) {
				is = context.getResources().openRawResource(resId);
				fos = new FileOutputStream(dbFile);
				byte[] buffer = new byte[8 * 1024];// 8K
				while (is.read(buffer) > 0) {
					fos.write(buffer);
				}
			}
			return dbFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	   public static File createTmpFile(Context context){

	        String state = Environment.getExternalStorageState();
	        if(state.equals(Environment.MEDIA_MOUNTED)){
	            // 已挂载
	            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
	            String fileName = "multi_image_"+timeStamp+"";
	            File tmpFile = new File(pic, fileName+".jpg");
	            return tmpFile;
	        }else{
	            File cacheDir = context.getCacheDir();
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
	            String fileName = "multi_image_"+timeStamp+"";
	            File tmpFile = new File(cacheDir, fileName+".jpg");
	            return tmpFile;
	        }

	    }

}
