package com.highnes.tour.ui.activities;

import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.view.imageview.TouchImageView;
import com.highnes.tour.view.viewpager.ExtendedViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * <PRE>
 * 作用:
 *    图片查看详情（多张图片）。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ForumImageActivity extends BaseTitleActivity {
	// 放大查看的图片
	public List<String> forumImages;
	private ExtendedViewPager mViewPager;
	private ProgressBar pbCommon;
	private TextView tvTitle;
	private String title;
	// 当前查看的位置
	private int currIndex = 0;

	@Override
	protected void onPause() {
		super.onPause();
		forumImages.clear();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_image_pager;
	}

	@Override
	protected void findViewById2T() {
		mViewPager = (ExtendedViewPager) findViewById(R.id.pager_common_image);
		pbCommon = (ProgressBar) findViewById(R.id.pb_pager);
		tvTitle = (TextView) findViewById(R.id.tv_pager_title);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("详情");
		showBackwardView("", SHOW_ICON_DEFAULT);
		initOptions();
		forumImages = getIntent().getExtras().getStringArrayList("forumImages");
		mViewPager.setAdapter(new TouchImageAdapter(forumImages, new ForumImageActivity.OnCell() {

			@Override
			public void onCell() {
				finish();
			}
		}));

		title = getIntent().getExtras().getString("title");
		tvTitle.setText(title);
		// tvTitle.setVisibility(View.GONE);
		currIndex = getIntent().getExtras().getInt("index");
		mViewPager.setCurrentItem(currIndex);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void setListener2T() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int mPosition) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	class TouchImageAdapter extends PagerAdapter {

		private List<String> list;
		private OnCell onCell;

		public TouchImageAdapter(List<String> list, OnCell onCell) {
			this.list = list;
			this.onCell = onCell;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			TouchImageView img = new TouchImageView(container.getContext());

			ImageLoader.getInstance().displayImage(list.get(position), img, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					pbCommon.setProgress(0);
					pbCommon.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					pbCommon.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					pbCommon.setVisibility(View.GONE);
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
					pbCommon.setProgress(Math.round(100.0f * current / total));
				}
			});
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onCell.onCell();
				}
			});

			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			return img;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	private interface OnCell {
		void onCell();
	}
	

}
