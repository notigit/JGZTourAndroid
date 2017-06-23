package com.highnes.tour.common.image.select;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;

/**
 * <PRE>
 * 作用:
 *    拍照或者选择图片。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-07   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MultiImageSelectorActivity extends BaseTitleActivity implements MultiImageSelectorFragment.Callback {

	/** 最大图片选择次数，int类型，默认9 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/** 图片选择模式，默认多选 */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** 是否显示相机，默认显示 */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
	public static final String EXTRA_RESULT = "select_result";
	/** 默认选择集 */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

	/** 单选 */
	public static final int MODE_SINGLE = 0;
	/** 多选 */
	public static final int MODE_MULTI = 1;

	private ArrayList<String> resultList = new ArrayList<String>();
	private int mDefaultCount;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.image_activity_default;
	}

	@Override
	protected void findViewById2T() {

	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("选择照片");
		showForwardView("", SHOW_ICON_GONE);
		showBackwardView("", SHOW_ICON_DEFAULT);
		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
		int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
		boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
		if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
			resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
		}

		Bundle bundle = new Bundle();
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
		bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
		bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);
		getSupportFragmentManager().beginTransaction().add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
				.commit();
		// 完成按钮
		if (resultList == null || resultList.size() <= 0) {
			showForwardView("", SHOW_ICON_GONE);
		} else {
			showForwardView("完成(" + resultList.size() + "/" + mDefaultCount + ")", SHOW_ICON_GONE);
		}
	}

	@Override
	protected void setListener2T() {

	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		// super.onClick(v);
		switch (v.getId()) {
		// 返回
		case R.id.ll_title_back19:
		case R.id.ll_title_back:
			setResult(RESULT_CANCELED);
			finishActivity();
			break;
		// 完成
		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			if (resultList != null && resultList.size() > 0) {
				// 返回已选择的图片数据
				Intent data = new Intent();
				data.putStringArrayListExtra(EXTRA_RESULT, resultList);
				setResult(RESULT_OK, data);
				finishActivity();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onSingleImageSelected(String path) {
		Intent data = new Intent();
		resultList.add(path);
		data.putStringArrayListExtra(EXTRA_RESULT, resultList);
		setResult(RESULT_OK, data);
		finishActivity();
	}

	@Override
	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// 有图片之后，改变按钮状态
		if (resultList.size() > 0) {
			showForwardView("完成(" + resultList.size() + "/" + mDefaultCount + ")", SHOW_ICON_GONE);
		}
	}

	@Override
	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
		}
		showForwardView("完成(" + resultList.size() + "/" + mDefaultCount + ")", SHOW_ICON_GONE);
		// 当为选择图片时候的状态
		if (resultList.size() == 0) {
			showForwardView("", SHOW_ICON_GONE);
		}
	}

	@Override
	public void onCameraShot(File imageFile) {
		if (imageFile != null) {
			Intent data = new Intent();
			resultList.add(imageFile.getAbsolutePath());
			data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			setResult(RESULT_OK, data);
			finishActivity();
		}
	}

}
