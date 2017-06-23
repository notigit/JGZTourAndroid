package com.highnes.tour.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.highnes.tour.utils.NetUtils;

/**
 * <PRE>
 * 作用:
 *    监听网络变化的广播。
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
public class NetReceivers extends BroadcastReceiver {
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = NetUtils.isNetworkConnected(context);
//			LogUtils.i("网络状态：" + isConnected);
//			LogUtils.i("wifi状态：" + NetUtils.isWifiConnected(context));
//			LogUtils.i("移动网络状态：" + NetUtils.isMobileConnected(context));
//			LogUtils.i("网络连接类型：" + NetUtils.getConnectedType(context));
			if (isConnected) {
			} else {
				// TODO 没有网络的时候
//				ToastHelper.showToast(context, "检测到无网络连接");
				// new
				// AlertDialog(context).builder().setTitle("提示").setMsg("检测到无网络连接")
				// .setPositiveButton("设置", new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// Intent intent = null;
				// // 判断手机系统的版本 即API大于10 就是3.0或以上版本
				// intent = new
				// Intent(android.provider.Settings.ACTION_SETTINGS);
				// context.startActivity(intent);
				// }
				// }).setNegativeButton("取消", new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				//
				// }
				// }).show();
			}
		}
	}

}
