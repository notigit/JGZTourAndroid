package com.highnes.tour.ui.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.highnes.tour.R;
import com.highnes.tour.adapter.CityAdapter;
import com.highnes.tour.adapter.CityHotAdapter;
import com.highnes.tour.beans.CityHotInfo;
import com.highnes.tour.beans.CityInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.AMapHelper;
import com.highnes.tour.utils.AMapHelper.MyOnReceiveLocation;
import com.highnes.tour.utils.CharacterParser;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.PinyinComparator;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.SortComparator;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.gridview.MyGridView;
import com.highnes.tour.view.sidebar.ClearEditText;
import com.highnes.tour.view.sidebar.SideBar;
import com.highnes.tour.view.sidebar.SideBar.OnTouchingLetterChangedListener;

/**
 * <PRE>
 * 作用:
 *    选择城市。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-09   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class CityActivity extends BaseTitleActivity {
	/** 城市列表 */
	private ListView lvCity;
	/** 字母列表 */
	private SideBar sbCity;
	/** 选择字母列表后的提示 */
	private TextView tvDialog;
	/** 城市列表适配器 */
	private CityAdapter adapter;
	/** 搜索框，可清除内容 */
	private ClearEditText etClean;

	/** 汉字转换成拼音的类 */
	private CharacterParser characterParser;
	private List<CityInfo> SourceDateList;
	/** 根据拼音来排列ListView里面的数据类 */
	private PinyinComparator pinyinComparator;

	// 没有城市
	private TextView tvShowCity;

	private View viewCurr, viewHis, viewHot;
	private LinearLayout llCurr, llHis, llHot;
	private Map<String, Integer> map;

	// 当前定位的城市
	private TextView tvCurrCity;
	// 热门城市/历史
	private CityHotAdapter hotAdapter, hisAdapter;
	private MyGridView gvHot, gvHis;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_city;
	}

	@Override
	protected void findViewById2T() {
		sbCity = (SideBar) findViewById(R.id.sb_city);
		tvDialog = (TextView) findViewById(R.id.tv_city_dialog);
		lvCity = (ListView) findViewById(R.id.lv_city);
		etClean = (ClearEditText) findViewById(R.id.et_city_filter);
		tvShowCity = (TextView) findViewById(R.id.tv_city_show);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("选择城市");
		showBackwardView("", SHOW_ICON_DEFAULT);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sbCity.setTextView(tvDialog);
		viewCurr = View.inflate(mContext, R.layout.include_city_curr, null);
		llCurr = (LinearLayout) viewCurr.findViewById(R.id.root);
		tvCurrCity = (TextView) viewCurr.findViewById(R.id.tv_curr_city);

		viewHis = View.inflate(mContext, R.layout.include_city_his, null);
		llHis = (LinearLayout) viewHis.findViewById(R.id.root);
		gvHis = (MyGridView) viewHis.findViewById(R.id.gv_his);
		viewHot = View.inflate(mContext, R.layout.include_city_hot, null);
		llHot = (LinearLayout) viewHot.findViewById(R.id.root);
		gvHot = (MyGridView) viewHot.findViewById(R.id.gv_city);

		lvCity.addHeaderView(viewCurr);
		lvCity.addHeaderView(viewHis);
		lvCity.addHeaderView(viewHot);
		adapter = new CityAdapter(mContext);
		lvCity.setAdapter(adapter);
		map = new HashMap<String, Integer>();
		// 根据a-z进行排序源数据
		SourceDateList = getData();
		// Collections.sort(SourceDateList, pinyinComparator);
		for (int i = 0; i < SourceDateList.size(); i++) {
			if (SourceDateList.get(i).name.charAt(0) == '#') {
				map.put(SourceDateList.get(i).name.charAt(1) + "", i);
			}
		}
		adapter.setList(SourceDateList);

		// 历史
		hisAdapter = new CityHotAdapter(mContext);
		gvHis.setAdapter(hisAdapter);
		List<CityInfo> listData = SPUtils.getList(mContext, DefaultData.LIST_CITY_HIS_INFO);
		if (listData == null) {
			listData = new ArrayList<CityInfo>();
		}
		hisAdapter.setList(listData);

		// 热门
		hotAdapter = new CityHotAdapter(mContext);
		gvHot.setAdapter(hotAdapter);
	}

	@Override
	protected void setListener2T() {
		tvCurrCity.setOnClickListener(this);
		/**
		 * 设置右侧触摸监听
		 */
		sbCity.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				if (s.equals("当前")) {
					lvCity.setSelection(0);
				} else if (s.equals("历史")) {
					lvCity.setSelection(1);
				} else if (s.equals("热门")) {
					lvCity.setSelection(2);
				} else {
					int position = getPos(s);
					if (position != -1) {
						lvCity.setSelection(position + 3);
					}
				}
			}
		});

		/**
		 * 根据输入框输入值的改变来过滤搜索
		 */
		etClean.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		/**
		 * 点击热门的城市
		 */
		gvHot.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CityInfo info = (CityInfo) gvHot.getAdapter().getItem(position);
				putCity2His(info);
				resultCity(info.name);
			}
		});
		/**
		 * 点击历史的城市
		 */
		gvHis.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CityInfo info = (CityInfo) gvHis.getAdapter().getItem(position);
				putCity2His(info);
				resultCity(info.name);
			}
		});

		/**
		 * 点击城市列表
		 */
		lvCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CityInfo info = (CityInfo) lvCity.getAdapter().getItem(position);
				if (info.name.charAt(0) == '#') {
					info.name = info.name.substring(2);
				}
				putCity2His(info);
				resultCity(info.name);
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 当前的定位
		case R.id.tv_curr_city:
			String txt = tvCurrCity.getText().toString().trim();
			if ("定位中...".equals(txt)) {
				// 正在定位...
			} else if ("重新定位".equals(txt)) {
				// 重新定位
				tvCurrCity.setText("定位中...");
				locMap();
			} else {
				resultCity(txt);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 根据字母获取第一次出现的位置
	 * 
	 * @param key
	 * @return
	 */
	public int getPos(String key) {
		try {
			return map.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByHotCity();
		locMap();
	}

	/**
	 * 获取定位信息
	 */
	private void locMap() {
		AMapHelper helper = new AMapHelper();
		helper.startLocation(mContext, new MyOnReceiveLocation() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// System.out.println("--定位状态--" + location.getLocType());
				// System.out.println("--定位时间--" + location.getTime());
				// System.out.println("--纬度--" + location.getLatitude());
				// System.out.println("--经度--" + location.getLongitude());
				// System.out.println("--城市--" + location.getCity());
				// System.out.println("--详细地址--" + location.getAddrStr());
				// if (location.getLocType() == BDLocation.TypeGpsLocation) {//
				// GPS定位结果
				// } else if (location.getLocType() ==
				// BDLocation.TypeNetWorkLocation) {// 网络定位结果
				// } else if (location.getLocType() ==
				// BDLocation.TypeOffLineLocation) {// 离线定位结果
				// } else if (location.getLocType() ==
				// BDLocation.TypeServerError) {
				// //
				// sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				// } else if (location.getLocType() ==
				// BDLocation.TypeNetWorkException) {
				// // sb.append("网络不同导致定位失败，请检查网络是否通畅");
				// } else if (location.getLocType() ==
				// BDLocation.TypeCriteriaException) {
				// //
				// sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				// }
				if (location != null) {
					tvCurrCity.setText(location.getCity());
				} else {
					tvCurrCity.setText("重新定位");
				}
			}
		});
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<CityInfo> filterDateList = new ArrayList<CityInfo>();
		if (TextUtils.isEmpty(filterStr)) {
			llCurr.setVisibility(View.VISIBLE);
			llHis.setVisibility(View.VISIBLE);
			llHot.setVisibility(View.VISIBLE);
			llCurr.setPadding(0, 0, 0, 0);
			llHis.setPadding(0, 0, 0, 0);
			llHot.setPadding(0, 0, 0, 0);
			filterDateList = SourceDateList;
			adapter.setList(filterDateList);
		} else {
			filterDateList.clear();
			llCurr.setVisibility(View.GONE);
			llCurr.setPadding(0, -llCurr.getHeight(), 0, 0);
			llHis.setVisibility(View.GONE);
			llHis.setPadding(0, -llHis.getHeight(), 0, 0);
			llHot.setVisibility(View.GONE);
			llHot.setPadding(0, -llHot.getHeight(), 0, 0);
			for (CityInfo sortModel : SourceDateList) {
				String name = sortModel.name;
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
			adapter.setList(filterDateList);
		}
	}

	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	private List<CityInfo> getData() {
		List<CityInfo> list = new ArrayList<CityInfo>();
		String[] data = getResources().getStringArray(R.array.area);
		for (int i = 0; i < data.length; i++) {
			list.add(new CityInfo(data[i]));
		}
		return list;
	}

	// ---------------- 历史 -------------------

	/**
	 * 把当前选择的城市追加到历史的列表中
	 */
	private void putCity2His(CityInfo info) {
		try {
			List<CityInfo> listData = SPUtils.getList(mContext, DefaultData.LIST_CITY_HIS_INFO);
			if (listData == null) {
				listData = new ArrayList<CityInfo>();
			}
			int infoIndex = -1;
			for (int i = 0; i < listData.size(); i++) {
				if (listData.get(i).name.equals(info.name)) {
					infoIndex = i;
					break;
				}
			}
			if (-1 != infoIndex) {
				listData.remove(infoIndex);
			}
			listData.add(0, info);
			if (listData.size() > 6) {
				// 只保留6个,移除最后一个
				listData.remove(listData.size() - 1);
			}
			SPUtils.putList(mContext, DefaultData.LIST_CITY_HIS_INFO, listData);
			// 保存完后立即刷新列表
			performHandlePostDelayed(0, 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		loadHisCity();
	}

	/**
	 * 加载最近使用
	 */
	private void loadHisCity() {
		List<CityInfo> listData = SPUtils.getList(mContext, DefaultData.LIST_CITY_HIS_INFO);
		if (listData == null) {
			listData = new ArrayList<CityInfo>();
		}
		// 保存完后立即刷新列表
		hisAdapter.setList(listData);
	}

	// -----------------------网络请求-----------------------
	/**
	 * HotCity 网络请求
	 * 
	 */
	private void requestByHotCity() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_CITY, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonHotCity(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * HotCity JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonHotCity(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				CityHotInfo info = GsonUtils.json2T(result, CityHotInfo.class);
				Collections.sort(info.HitCity, new SortComparator.CityHotComparator());
				List<CityInfo> list = new ArrayList<CityInfo>();
				for (int i = 0; i < info.HitCity.size(); i++) {
					list.add(new CityInfo(info.HitCity.get(i).CityName));
				}
				hotAdapter.setList(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回城市的名称
	 * 
	 * @param cityName
	 */
	private void resultCity(String cityName) {
		Intent data = new Intent();
		data.putExtra(DefaultData.EXTRA_RESULT, cityName);
		setResult(RESULT_OK, data);
		finishActivity();
	}
}
