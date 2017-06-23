package com.highnes.tour.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.highnes.tour.R;

/**
 * <PRE>
 * 作用:
 *    版本更新下载（状态栏显示进度）。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-25   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class NotificationHelper {
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/** Notification管理 */
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder mBuilder;
	/** true:为不确定样式的 false:确定样式 */
	private Boolean indeterminate = false;
	/** Notification的进度条数值 */
	private int now_progress = 0;
	// 上次下载的位置
	private int last_progress = -1;
	/** Notification的ID */
	private int notifyId = 102;
	private DownloadThread downloadThread;
	/** 下载线程是否暂停 */
	public boolean isPause = false;
	// 上下文
	private Context context;
	// app下载的地址
	private String appUrl;
	// APP下载后保存的地址
	private String mSavePath;
	// app保存的名称
	private String appName;

	/**
	 * 
	 * @param context
	 * @param appUrl
	 *            app的地址
	 */
	public NotificationHelper(Context context, String appUrl) {
		this.context = context;
		this.appUrl = appUrl;
		appName = "lph" + System.currentTimeMillis();
		initService(context);
		initNotify(context);
		showProgressNotify();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				if (!isPause) {
					mBuilder.setContentTitle("下载中");
					if (!indeterminate) {
						setNotify(now_progress);
					}
				}

				break;
			case DOWNLOAD_FINISH:
				Log.e("TAG", "handleMessage: "+"下载完成" );
				mBuilder.setContentText("下载完成").setProgress(0, 0, false);
				mNotificationManager.notify(notifyId, mBuilder.build());
				// 安装文件
				installApk();
//				mNotificationManager.cancel(notifyId);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 初始化要用到的系统服务
	 */
	private void initService(Context context) {
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/** 初始化通知栏 */
	@SuppressLint("InlinedApi")
	private void initNotify(Context context) {
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setContentIntent(getDefalutIntent(context, 0))
				// .setNumber(number)//显示数量
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				// .setDefaults(Notification.DEFAULT_ALL)//
				// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
				// requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);
	}

	/** 显示带进度条通知栏 */
	private void showProgressNotify() {
		downloadThread = null;
		indeterminate = false;
		mBuilder.setContentTitle("等待下载").setContentText("进度:").setTicker("开始下载");// 通知首次出现在通知栏，带上升动画效果的
		if (indeterminate) {
			// 不确定进度的
			mBuilder.setProgress(0, 0, true);
		} else {
			// 确定进度的
			mBuilder.setProgress(100, now_progress, false); // 这个方法是显示进度条
			// 设置为true就是不确定的那种进度条效果
		}
		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	/** 设置下载进度 */
	private void setNotify(int progress) {
		mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条
		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	/** 开始下载 */
	public void startDownloadNotify() {
		isPause = false;
		if (downloadThread != null && downloadThread.isAlive()) {
			// downloadThread.start();
		} else {
			downloadThread = new DownloadThread();
			downloadThread.start();
		}
	}

	/** 暂停下载 */
	public void pauseDownloadNotify() {
		isPause = true;
		mBuilder.setContentTitle("已暂停");
		setNotify(now_progress);
	}

	/** 取消下载 */
	public void stopDownloadNotify() {
		if (downloadThread != null) {
			downloadThread.interrupt();
		}
		downloadThread = null;
		mBuilder.setContentTitle("下载已取消").setProgress(0, 0, false);
		mNotificationManager.notify(notifyId, mBuilder.build());
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, appName);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(i);
	}

	/**
	 * 下载线程
	 */
	private class DownloadThread extends Thread {

		@Override
		public void run() {
			try {
				mSavePath = FileUtils.getCacheApkFiles(context).getAbsolutePath();
				URL url = new URL(appUrl);
				// 创建连接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				// 获取文件大小
				int length = conn.getContentLength();
				// 创建输入流
				InputStream is = conn.getInputStream();
				File apkFile = new File(mSavePath, appName);
				FileOutputStream fos = new FileOutputStream(apkFile);
				// 当前接受到的文件大小
				int count = 0;
				// 缓存
				byte buf[] = new byte[1024];
				// 写入到文件中
				do {
					int numread = is.read(buf);
					count += numread;
					// 计算进度条位置
					now_progress = (int) (((float) count / length) * 100);
					Log.e("TAG", "handleMessage: "+now_progress );
					Log.e("TAG", "run: "+numread);
					if (now_progress != last_progress) {
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
					}
					last_progress = now_progress;
					// 写入文件
					fos.write(buf, 0, numread);
				} while (now_progress <= 100);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
	 *           Notification.FLAG_AUTO_CANCEL
	 */
	private PendingIntent getDefalutIntent(Context context, int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
		return pendingIntent;
	}
}
