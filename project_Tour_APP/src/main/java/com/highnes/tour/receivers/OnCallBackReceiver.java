package com.highnes.tour.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.ui.fragment.main.FootFragment;
import com.highnes.tour.utils.LogUtils;

/**
 * <PRE>
 * 作用:
 *    回调监听广播。
 * 注意事项:
 *    足迹详情发布说说后列表刷新-->DefaultData.ACTION_CALLBACK_REFRESH。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-22   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class OnCallBackReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// 刷新足迹列表的页面
		if (action.equals(DefaultData.ACTION_CALLBACK_REFRESH)) {
			// Intent msgIntent = new Intent();
			// msgIntent.setAction(FootFragment.ACTION_CALLBACK_REFRESH_FOOTLIST);
			// context.sendBroadcast(msgIntent);
		}
	}

}
