package com.highnes.tour.ui.activities.home.ticket;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.AMapHelper;
import com.highnes.tour.utils.AMapHelper.MyOnReceiveLocation;
import com.highnes.tour.utils.MyAMapUtils;
import com.highnes.tour.view.dialog.ECAlertDialog;

/**
 * <PRE>
 * 作用:
 *    	商家地图展示页面。
 * 注意事项: 
 * 		@author QinX
 * 		@param boolean 
 * 				boolean 中文定位true/经纬度定位false(必传)
 * 		@param String
 *            	mapCity 定位城市(boolean = true时必传)
 * 		@param String
 *            	mapAddress 定位地址(boolean = true时必传)
 * 		@param Double
 *            	la 纬度(boolean = false时必传)
 * 		@param Double
 *            	lo 经度(boolean = false时必传)
 * 		@param String
 *            	title 标题(必传)
 * 		@param String
 *            	distance 距离(必传)
 * 		@param String
 *            	address 展示地址(必传)
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-02-27   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MapDisplayActivity extends BaseTitleActivity implements OnGetGeoCoderResultListener {
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private BaiduMap mBaiduMap = null;
	// ************************ 页面控件 ************************
	/** 地图 */
	private MapView mMapView = null;
	/** 标题 */
	private TextView tvTitle;
	/** 距离 */
	private TextView tvDistance;
	/** 地址 */
	private TextView tvAddress;
	/** 导航 */
	private LinearLayout llNavigation;
	// 经纬度
	private LatLng shopLatLng = null;
	// 我的经纬度位置
	private LatLng myLatLng = null;
	/** 中文定位true/经纬度定位false */
	private boolean isAddress = true;
	/** 城市 */
	private String mapCity = "";
	/** 地址 */
	private String mapAddress = "";
	/** 切换显示 */
	private Button btSwitch;
	/** 显示的是什么位置(true:用户定位位置/false:商家位置) */
	private boolean isMyPosition = false;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_map_display;
	}

	@Override
	protected void findViewById2T() {
		mMapView = (MapView) findViewById(R.id.bmv_display);
		mBaiduMap = mMapView.getMap();
		mSearch = GeoCoder.newInstance();
		tvTitle = (TextView) findViewById(R.id.tv_mapdisplay_title);
		tvDistance = (TextView) findViewById(R.id.tv_mapdisplay_distance);
		tvAddress = (TextView) findViewById(R.id.tv_mapdisplay_address);
		llNavigation = (LinearLayout) findViewById(R.id.ll_mapdisplay_navigation);
		btSwitch = (Button) findViewById(R.id.bt_mapdisplay_switch);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		showBackwardView("", SHOW_ICON_DEFAULT);
		setTitle("景区地址");
	}

	@Override
	protected void setListener2T() {
		mSearch.setOnGetGeoCodeResultListener(this);
		llNavigation.setOnClickListener(this);
		btSwitch.setOnClickListener(this);
	};

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		showAddress();
		locationMode(getIntent().getExtras());
		searchEngine();
		setMapView();
	}

	// ━┉┉┉┉┉┉┉┉┉┉┉┉∞∞ 事件处理 ∞∞┉┉┉┉┉┉┉┉┉┉┉┉━
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_title_back:// 返回
			finishActivity();
			break;
		case R.id.ll_mapdisplay_navigation:// 开起百度导航
			if (myLatLng != null && shopLatLng != null) {
				ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mContext, "是否启动百度地图app导航？", webOnClickListener);
				buildAlert.setTitle("提示");
				buildAlert.show();
			}
			break;
		case R.id.bt_mapdisplay_switch:// 切换位置显示
			isMyPosition = !isMyPosition;
			if (isMyPosition) {
				if (myLatLng != null) {
					setTitle("我的位置");
					mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(myLatLng));
				}
			} else {
				setTitle("景区位置");
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(shopLatLng));
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 是否用web打开导航监听事件
	 */
	OnClickListener webOnClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg) {
			// arg等于2的时候是确定
			if (arg == 2) {
				MyAMapUtils.startBMapNavigation(mContext, myLatLng, shopLatLng, MyAMapUtils.webOnClickListener);
			}
		}
	};

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			showToast("抱歉，未能找到结果");
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapdisplay_fix)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			showToast("抱歉，未能找到结果");
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapdisplay_fix)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
	}

	/**
	 * 显示定位信息
	 */
	private void showAddress() {
		AMapHelper helper = new AMapHelper();
		helper.startLocation(mContext, new MyOnReceiveLocation() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location != null) {
					myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
					btSwitch.setVisibility(View.VISIBLE);
				} else {
					btSwitch.setVisibility(View.GONE);
				}
			}
		});
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
		mMapView.onDestroy();
		// mSearch.destroy();
		super.onDestroy();
	}

	// ━┉┉┉┉┉┉┉┉┉┉┉┉∞∞ 自定义信息处理 ∞∞┉┉┉┉┉┉┉┉┉┉┉┉━
	/**
	 * 发起搜索
	 * 
	 */
	public void searchEngine() {
		// 判断是经纬度定位还是地址定位
		if (isAddress) {
			if ("".equals(mapAddress)) {
				showToast("地址为空");
			} else {
				// Geo搜索
				mSearch.geocode(new GeoCodeOption().city(mapCity).address(mapAddress));
			}
		} else {
			if (shopLatLng == null) {
				showToast("经纬度为空");
			} else {
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(shopLatLng));
			}
		}
	}

	/**
	 * 判断定点方式
	 * 
	 * @param bd
	 *            上一个页面传入的值
	 */
	private void locationMode(Bundle bd) {
		isAddress = bd.getBoolean("boolean", true);
		// 判断是经纬度定位还是地址定位
		if (isAddress) {
			mapCity = bd.getString("mapCity");
			mapAddress = bd.getString("mapAddress");
		} else {
			shopLatLng = new LatLng(bd.getDouble("la"), bd.getDouble("lo"));
		}
		tvTitle.setText(bd.getString("title"));
		tvDistance.setText(bd.getString("distance"));
		tvAddress.setText(bd.getString("address"));
		// TODO 判断是否获得定位 2016年3月2日20:17:10
		// if (mlocation == null) {
		// startLocation(false);
		// }
	}

	/**
	 * 设置地图显示比例尺
	 */
	private void setMapView() {
		// 设置地图初始显示大小为18
		MapStatus mMapStatus = new MapStatus.Builder().target(shopLatLng).zoom(18).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

}
