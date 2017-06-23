package com.highnes.tour.common.image.select;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.common.image.select.photoview.PhotoView;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.PreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    对图片的预览。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-07-07   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PreViewActivity extends BaseTitleActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ViewPager pager;
	private TextView delimg;
	private CheckBox cb;
	private ArrayList<String> resultList = null;
	private ArrayList<String> resultListDel = null;
	private ArrayList<Boolean> resultBooleanList = null;
	private int position;
	public static final String EXTRA_RESULT = "select_result";

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.image_activity_image_pager;
	}

	@Override
	protected void findViewById2T() {

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("预览");
		showBackwardView("", SHOW_ICON_DEFAULT);
		showForwardView("完成", SHOW_ICON_GONE);

		delimg = getViewById(R.id.delimg);
		cb = getViewById(R.id.cb);
		Bundle b = getIntent().getBundleExtra("b");
		resultList = b.getStringArrayList("imglist");
		position = b.getInt("position");
		resultListDel = new ArrayList<String>();

		if (resultList != null && resultList.size() > 0) {
			resultBooleanList = new ArrayList<Boolean>();
			for (int i = 0; i < resultList.size(); i++) {
				resultBooleanList.add(true);
			}
			showForwardView("完成(" + resultList.size() + "/" + resultList.size() + ")", SHOW_ICON_GONE);
			pager = getViewById(R.id.pager);
			pager.setAdapter(new ImagePagerAdapter(resultList));
			pager.setCurrentItem(position);
			String posi = (position + 1) + "/" + resultList.size();
			showBackwardView(posi, SHOW_ICON_DEFAULT);
			pager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					position = arg0;
					String posi = (arg0 + 1) + "/" + resultList.size();
					showBackwardView(posi, SHOW_ICON_DEFAULT);
					cb.setChecked(resultBooleanList.get(position));
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});

			cb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (resultBooleanList.get(position)) {
						// 不选中了
						resultBooleanList.remove(position);
						resultBooleanList.add(position, false);
						resultListDel.add(resultList.get(position));
					} else {
						// 选中
						resultBooleanList.remove(position);
						resultBooleanList.add(position, true);
						resultListDel.remove(resultList.get(position));
					}
					cb.setChecked(resultBooleanList.get(position));
					showForwardView("完成(" + (resultList.size() - resultListDel.size()) + "/" + resultList.size() + ")", SHOW_ICON_GONE);
				}
			});
		}
	}

	@Override
	protected void setListener2T() {
		delimg.setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private ArrayList<String> images;
		private LayoutInflater inflater;

		ImagePagerAdapter(ArrayList<String> images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View imageLayout = inflater.inflate(R.layout.image_item_pager_image, view, false);
			assert imageLayout != null;
			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
			imageLoader.displayImage("file://" + images.get(position), imageView);
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (resultListDel != null && resultListDel.size() > 0) {
				String imgpaths = "";
				for (String imgpath : resultListDel) {
					imgpaths += imgpath + ",";
				}
				imgpaths.subSequence(0, imgpaths.length() - 1);// 去掉最后一个逗号
				PreferencesUtils.putSharePre(this, "imgsdel", imgpaths);
			}
			finishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			if (resultListDel != null && resultListDel.size() > 0) {
				String imgpaths = "";
				for (String imgpath : resultListDel) {
					imgpaths += imgpath + ",";
				}
				imgpaths.subSequence(0, imgpaths.length() - 1);// 去掉最后一个逗号
				PreferencesUtils.putSharePre(this, "imgsdel", imgpaths);
			}
			for (int i = 0; i < resultBooleanList.size(); i++) {
				if (!resultBooleanList.get(i)) {
					resultList.remove(i);
				}
			}
			// 返回已选择的图片数据
			Intent data = new Intent();
			data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			setResult(RESULT_OK, data);
			finishActivity();
			break;
		default:
			break;
		}
	}

}
