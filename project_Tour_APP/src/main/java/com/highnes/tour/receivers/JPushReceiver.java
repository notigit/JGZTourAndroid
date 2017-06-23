package com.highnes.tour.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;

/**
 * <PRE>
 * 作用:
 *    极光推送的广播处理。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-10   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class JPushReceiver extends BroadcastReceiver {
	private static String ID = "";

	@Override
	public void onReceive(Context context, Intent in) {
		Bundle bundle = in.getExtras();
		LogUtils.i(" onReceive - " + in.getAction());
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(in.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			LogUtils.i(" 接收Registration Id : " + regId);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(in.getAction())) {
			LogUtils.i(" 接收到推送下来的自定义消息: ");
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
			LogUtils.i("--extra--" + extra);
			LogUtils.i("--alert--" + alert);
			ID = JsonUtils.getString(extra, "ID", "");
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(in.getAction())) {
			LogUtils.i(" 接收到推送下来的通知");
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
			LogUtils.i("--extra--" + extra);
			LogUtils.i("--alert--" + alert);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(in.getAction())) {
			LogUtils.i(" 用户点击打开了通知");
			startActivity(context, bundle);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(in.getAction())) {
			LogUtils.i(" 用户收到到RICH PUSH CALLBACK: ");
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(in.getAction())) {
			boolean connected = in.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			LogUtils.i("" + in.getAction() + " connected state change to " + connected);
		} else {
			LogUtils.i(" Unhandled intent - " + in.getAction());
		}
	}

	/**
	 * 跳转APP到详情页面
	 * 
	 * @param context
	 * @param bundle
	 */
	private void startActivity(final Context context, Bundle bundle) {
		try {
//			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
//			LogUtils.i("--extra--" + extra);
//			LogUtils.i("--alert--" + alert);
			// 打开自定义的Activity
			String userId = SPUtils.get(context, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString();
			bundle.putInt("mType", 1902);
			bundle.putString("mTitle", "消息详情");
			bundle.putString("mUrl", String.format(UrlSettings.URL_H5_HOME_MSG_DETAIL, userId, ID));
			
			Intent i = new Intent(context, WebViewTitleActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}