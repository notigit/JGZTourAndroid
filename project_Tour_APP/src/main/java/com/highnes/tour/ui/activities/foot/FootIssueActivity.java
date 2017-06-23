package com.highnes.tour.ui.activities.foot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.highnes.tour.R;
import com.highnes.tour.adapter.foot.FootLocAdapter;
import com.highnes.tour.common.image.photo.UpLoadGridAdapter;
import com.highnes.tour.common.image.photo.utils.BitmapCompress;
import com.highnes.tour.common.image.select.MultiImageSelectorActivity;
import com.highnes.tour.common.image.select.PreViewActivity;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.fragment.main.FootFragment;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.ImgCompressUtil;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    足迹..发布。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-06   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FootIssueActivity extends BaseTitleActivity implements OnGetGeoCoderResultListener {
	private GridView imageData;
	private UpLoadGridAdapter gridadapter;
	private static final int REQUEST_IMAGE = 2;
	public static final String EXTRA_RESULT = "select_result";
	private static final int MAX_IMG = 6;// 最大图片数量
	// 选择的图片路径
	private ArrayList<String> mSelectPath;
	private Spinner spLoc;
	private FootLocAdapter adapter;
	// 标题/正文
	private EditText etCenter;
	private List<PoiInfo> mList;
	private GeoCoder mSearch;
	private PoiInfo mPoiInfo;

	// checkbox
	private CheckBox cbShare, cbShowLoc;
	private boolean isShare = true, isShowLoc = true;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_foot_issue;
	}

	@Override
	protected void findViewById2T() {
		spLoc = getViewById(R.id.sp_theme);
		etCenter = getViewById(R.id.et_content);
		cbShare = getViewById(R.id.cb_0);
		cbShowLoc = getViewById(R.id.cb_1);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("发布足迹");
		showBackwardView("", SHOW_ICON_DEFAULT);
		showForwardView("提交", SHOW_ICON_INVISIBLE);
		initViewInfo();
		BitmapCompress.bmp.clear();

		adapter = new FootLocAdapter(mContext);
		spLoc.setAdapter(adapter);

		mList = new ArrayList<PoiInfo>();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		initGeoCode();
	}

	@Override
	protected void setListener2T() {
		setSpeOnItemSelectedListener();
		cbShare.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isShare = isChecked;
			}
		});
		cbShowLoc.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isShowLoc = isChecked;
				spLoc.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});
		spLoc.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mPoiInfo = (PoiInfo) spLoc.getAdapter().getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
	}

	/**
	 * 初始化地理编码
	 */
	private void initGeoCode() {
		double lat = Double.valueOf(SPUtils.get(mContext, PreSettings.USER_LAT.getId(), PreSettings.USER_LAT.getDefaultValue()).toString());
		double lng = Double.valueOf(SPUtils.get(mContext, PreSettings.USER_LNG.getId(), PreSettings.USER_LNG.getDefaultValue()).toString());
		LatLng ptCenter = new LatLng(lat, lng);
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			return;
		}
		List<PoiInfo> list = result.getPoiList();
		mList.clear();
		if (list != null && list.size() > 0) {
			mPoiInfo = list.get(0);
			spLoc.setSelection(0);
			for (int i = 0; i < list.size(); i++) {
				mList.add(list.get(i));
			}
		}
		adapter.setList(mList);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 发布
		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			if (AppUtils.isLogin(mContext)) {
				processIssue();
			} else {
				openActivity(LoginActivity.class);
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 处理发布
	 */
	private void processIssue() {
		final String mCenter = etCenter.getText().toString().trim();
		// 判断内容
		if (StringUtils.isEmpty(mCenter)) {
			showToast("请输入内容");
			return;
		} else {
			if (mCenter.length() > 200) {
				showToast("内容只能200字以内");
				return;
			}
		}
		String uploadStr = "";
		StringBuffer upload = new StringBuffer();
		if (mSelectPath != null && mSelectPath.size() > 0) {
			for (int i = 0; i < mSelectPath.size(); i++) {
				upload.append(ImgCompressUtil.getImgEncodeString(mSelectPath.get(i)));
				upload.append(",");
			}
			String str = upload.toString();
			uploadStr = str.substring(0, str.length() - 1);
		} else {
			// 没有图片发送no_image
			uploadStr = "no_image";
		}
		if (isShowLoc && mPoiInfo != null) {
			requestByUpload(mPoiInfo.name, mPoiInfo.location.longitude + "", mPoiInfo.location.latitude + "", isShare, mCenter, uploadStr);
		} else {
			requestByUpload("0", "0", "0", isShare, mCenter, uploadStr);
		}

	}

	/**
	 * 初始化信息
	 */
	private void initViewInfo() {
		imageData = (GridView) findViewById(R.id.imagedata);
		gridadapter = new UpLoadGridAdapter(mContext, MAX_IMG);
		imageData.setAdapter(gridadapter);
		imageData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				if (position == BitmapCompress.bmp.size()) {
					if (position == MAX_IMG) {
						GoPreView(position);
						return;
					}
					Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
					// 是否显示拍摄图片
					intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
					// 最大可选择图片数量
					intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, MAX_IMG);
					// 选择模式
					intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
					// 默认选择
					if (mSelectPath != null && mSelectPath.size() > 0) {
						intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
					}
					startActivityForResult(intent, REQUEST_IMAGE);
				} else {
					GoPreView(position);
				}
			}
		});
	}

	public void GoPreView(int position) {
		Intent intent = new Intent(mContext, PreViewActivity.class);
		Bundle b = new Bundle();
		b.putStringArrayList("imglist", mSelectPath);
		b.putInt("position", position);
		intent.putExtra("b", b);
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				BitmapCompress.bmp.clear();
				for (int i = 0; i < mSelectPath.size(); i++) {
					try {
						BitmapCompress.bmp.add(BitmapCompress.revitionImageSize(mSelectPath.get(i)));
						gridadapter.notifyDataSetChanged();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			LogUtils.d("--现在图片的多少--" + BitmapCompress.bmp.size() + "");
		}
	}

	/**
	 * 选择专家
	 */
	private void setSpeOnItemSelectedListener() {
		spLoc.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	// -------------------------网络请求---------------------------

	private void requestByUpload(String mAddName, String mLandmark, String mLatitude, boolean mIsDelivered, String Contents, String upload) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		params.put("AddName", mAddName);
		params.put("Landmark", mLandmark);
		params.put("Latitude", mLatitude);
		params.put("IsDelivered", mIsDelivered);
		params.put("Contents", Contents);
		params.put("fileup", upload);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_FOOT_ISSUE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonUpload(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, params, true);
	}

	private void parseJsonUpload(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				showToast("发布成功");
				FootFragment.isLoad = true;
				Intent mIntent = new Intent();
				mIntent.setAction(FootFragment.ACTION_CALLBACK_FOOT);
				sendBroadcast(mIntent);
				finishActivity();
			} else {
				showToast("发布失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
