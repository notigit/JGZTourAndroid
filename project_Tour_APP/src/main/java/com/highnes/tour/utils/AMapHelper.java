package com.highnes.tour.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class AMapHelper {
	// 定位方式 采用低功耗模式（基站定位）
	// private LocationMode tempMode = LocationMode.Battery_Saving;
	// gcj02(国测局加密经纬度坐标)
	// 定位的时间 ms
	int span = 5000;
	private LocationClient mLocationClient;
	// private GeofenceClient mGeofenceClient;
	private MyLocationListener mMyLocationListener;

	private void InitLocation(Context mContext) {
		try {
			mLocationClient = new LocationClient(mContext);
			mMyLocationListener = new MyLocationListener();
			mLocationClient.registerLocationListener(mMyLocationListener);

			LocationClientOption option = new LocationClientOption();
			option.setCoorType("gcj02");// 可选，默认gcj02，设置返回的定位结果坐标系
			int span = 1000;
			option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
			option.setOpenGps(true);// 可选，默认false,设置是否使用gps
			option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
			mLocationClient.setLocOption(option);

			option.setAddrType("all");
			mLocationClient.setLocOption(option);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始定位
	 * 
	 * @param isGeoLocation
	 *            是否需要反编译地址 true
	 */
	public void startLocation(Context mContext, MyOnReceiveLocation myOnReceiveLocation) {
		try {
			setMyOnReceiveLocation(myOnReceiveLocation);
			InitLocation(mContext);
			mLocationClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止定位
	 */
	public void stopLocation() {
		try {
			mLocationClient.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			try {
				if (myOnReceiveLocation != null) {
					myOnReceiveLocation.onReceiveLocation(location);
				}
				stopLocation();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {
		}
	}

	private MyOnReceiveLocation myOnReceiveLocation;

	public interface MyOnReceiveLocation {
		void onReceiveLocation(BDLocation location);
	}

	private void setMyOnReceiveLocation(MyOnReceiveLocation myOnReceiveLocation) {
		this.myOnReceiveLocation = myOnReceiveLocation;
	}
}
