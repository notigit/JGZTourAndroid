package com.highnes.tour.ui.activities.home.ticket;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;

/**
 * 路况查询
 * 
 * @author FUNY
 * 
 */
public class MapRoadActivity extends BaseTitleActivity {
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true; // 是否首次定位
	private UiSettings mUiSettings;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_road;
	}

	@Override
	protected void findViewById2T() {
		mMapView = getViewById(R.id.bmapView);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("路况查询");
		showBackwardView("", SHOW_ICON_DEFAULT);
		mCurrentMode = LocationMode.NORMAL;
		// 地图初始化
		mBaiduMap = mMapView.getMap();
		mUiSettings = mBaiduMap.getUiSettings();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 交通图
		mBaiduMap.setTrafficEnabled(true);
		// 指南针
		mUiSettings.setCompassEnabled(true);

		// 注意：先允许定位图层后设置定位图层配置信息才会生效
		// mCurrentMarker =
		// BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		// mBaiduMap.setMyLocationConfigeration(new
		// MyLocationConfiguration(LocationMode.NORMAL, true, mCurrentMarker));

		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	protected void setListener2T() {

	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		if (mMapView != null){
			mMapView.onDestroy();
			mMapView = null;
		}
		super.onDestroy();
	}
}