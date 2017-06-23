package com.highnes.tour.ui.activities.my.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.highnes.tour.R;
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
import com.highnes.tour.ui.fragment.tour.Tour0Fragment;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.ImgCompressUtil;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.StarBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    添加评论。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-18   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PHPAddCommentActivity extends BaseTitleActivity {
	private GridView imageData;
	private UpLoadGridAdapter gridadapter;
	private static final int REQUEST_IMAGE = 2;
	public static final String EXTRA_RESULT = "select_result";
	private static final int MAX_IMG = 6;// 最大图片数量
	// 标题/正文
	private EditText etCenter;
	// 选择的图片路径
	private ArrayList<String> mSelectPath;
	// 星星
	private StarBar mStarBar;
	// 星星
	private int point = 5;
	private String mId = "";
	private String mImage = "";
	private String mPrice = "";
	private String mTitle = "";

	private ImageView ivImg;
	private TextView tvTitle, tvPrice;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_order_add_comment;
	}

	@Override
	protected void findViewById2T() {
		mStarBar = (StarBar) findViewById(R.id.starBar);
		etCenter = getViewById(R.id.et_content);
		ivImg = getViewById(R.id.iv_item_img);
		tvTitle = getViewById(R.id.tv_title);
		tvPrice = getViewById(R.id.tv_price);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("添加评论");
		showBackwardView("", SHOW_ICON_DEFAULT);
		showForwardView("发表", SHOW_ICON_INVISIBLE);
		mId = getIntent().getExtras().getString("mId", "");
		mImage = getIntent().getExtras().getString("mImage", "");
		mPrice = getIntent().getExtras().getString("mPrice", "");
		mTitle = getIntent().getExtras().getString("mTitle", "");

		mStarBar.setStarMark(point);
		mStarBar.setIntegerMark(true);
		// initViewInfo();
		// BitmapCompress.bmp.clear();
		initOptions();

		tvTitle.setText(mTitle);
		tvPrice.setText("￥" + mPrice);
		ImageLoader.getInstance().displayImage(mImage, ivImg, options, null);
	}

	@Override
	protected void setListener2T() {
		mStarBar.setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 选择评分
		case R.id.starBar:
			point = (int) mStarBar.getStarMark();
			break;
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
		requestByUpload(mId, point + "", mCenter);
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

	// -------------------------网络请求---------------------------

	private void requestByUpload(String OrderID, String Grades, String Contents) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		params.put("order", OrderID);
		params.put("contents", Contents);
		params.put("score", Grades);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_PHP_ORDER_COMMENT, new NETConnection.SuccessCallback() {

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
		}, params);
	}

	private void parseJsonUpload(String result) {
		try {
			if (0 == JsonUtils.getInt(result, "errcode", -1)) {
				showToast("发布成功");
				Intent mIntent = new Intent();
				mIntent.setAction(PHPOrderListActivity.ACTION_CALLBACK);
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
