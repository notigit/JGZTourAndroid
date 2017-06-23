package com.highnes.tour.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.highnes.tour.view.dialog.ECAlertDialog;

/**
 * <PRE>
 * 作用:
 *    地图调用
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-02-26   QINX        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MyAMapUtils {
	private static Context context;
	/** 存放导航的起点终点位置 */
	public static NaviPara para = new NaviPara();

	/**
	 * 调用高德地图导航
	 * 
	 * @param context
	 * @param latLng
	 *            com.amap.api.maps.model.LatLng 终点
	 */
	// public static void startAMapNavigation(Context context,
	// com.amap.api.maps.model.LatLng latLng) {
	// // 构造导航参数
	// com.amap.api.maps.model.NaviPara naviPara = new
	// com.amap.api.maps.model.NaviPara();
	// // 设置终点位置
	// naviPara.setTargetPoint(latLng);
	// // 设置导航策略，这里是避免拥堵
	// naviPara.setNaviStyle(com.amap.api.maps.AMapUtils.DRIVING_AVOID_CONGESTION);
	// try {
	// // 调起高德地图导航
	// com.amap.api.maps.AMapUtils.openAMapNavi(naviPara, context);
	// } catch (Exception e) {
	// // 如果没安装会进入异常，调起下载页面
	// com.amap.api.maps.AMapUtils.getLatestAMapApp(context);
	// }
	// }

	/**
	 * 调用百度地图导航
	 * 
	 * @param context
	 * @param latLngStart
	 *            起点
	 * @param latLngEnd
	 *            终点
	 * @param listener
	 *            点击事件(当没有百度地图App的时候提示是否利用web打开的导航)
	 */
	public static void startBMapNavigation(final Context context,
			LatLng latLngStart, LatLng latLngEnd, OnClickListener listener) {
		MyAMapUtils.context = context;
		para.startPoint = latLngStart;
		para.startName = "起点";
		para.endPoint = latLngEnd;
		para.endName = "终点";
		try {
			BaiduMapNavigation.openBaiduMapNavi(para, context);
		} catch (Exception e) {
			e.printStackTrace();
			// AlertDialog.Builder builder = new AlertDialog.Builder(context);
			// builder.setMessage("您尚未安装百度地图app，是否打开web端导航？");
			// builder.setTitle("提示");
			// builder.setPositiveButton("确认", new OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// // BaiduMapNavigation.getLatestBaiduMapApp(context);
			// BaiduMapNavigation.openWebBaiduMapNavi(para, context);
			// }
			// });
			// builder.setNegativeButton("取消", new OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// }
			// });
			// builder.create().show();
			ECAlertDialog buildAlert = ECAlertDialog.buildAlert(context,
					"您尚未安装百度地图app，是否打开web端导航？", listener);
			buildAlert.setTitle("提示");
			buildAlert.show();
		}
	}

	/**
	 * 是否用web打开导航监听事件
	 */
	public static OnClickListener webOnClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg) {
			// arg等于2的时候是确定
			if (arg == 2) {
				BaiduMapNavigation.openWebBaiduMapNavi(MyAMapUtils.para,
						context);
			}
		}
	};
}
