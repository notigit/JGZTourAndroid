package com.highnes.tour.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * <PRE>
 * 作用:
 * 		地图相关工具类。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-03-03   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class AMapUtils {
	/**
	 * 返回两个点之间的距离
	 * 
	 * @param p1LL
	 *            起点的百度经纬度坐标
	 * @param p2LL
	 *            终点的百度经纬度坐标
	 * @return 两点距离 单位为：米,转换错误时返回-1.
	 */
	public static double getDistance(LatLng p1LL, LatLng p2LL) {
		return DistanceUtil.getDistance(p1LL, p2LL);
	}

	/**
	 * 米换算成其他单位
	 * 
	 * @param dist
	 * @return
	 */
	public static String m2XX(double dist) {
		if (dist < 1000) {
			return ArithUtil.round(dist, 0) + "m";
		}
		return ArithUtil.round(Math.round(dist / 100d) / 10d, 2) + "km";
	}

	/**
	 * 计算两点距离并换成其他单位
	 * 
	 * @param p1LL
	 *            起点的百度经纬度坐标
	 * @param p2LL
	 *            终点的百度经纬度坐标
	 * @return
	 */
	public static String getDistance2XX(LatLng p1LL, LatLng p2LL) {
		double dist = getDistance(p1LL, p2LL);
		if (-1 == dist) {
			return "";
		}
		return m2XX(dist);
	}
}
