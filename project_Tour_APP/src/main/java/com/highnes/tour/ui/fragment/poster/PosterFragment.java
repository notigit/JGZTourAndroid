package com.highnes.tour.ui.fragment.poster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.highnes.tour.R;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.view.poster.AutoScrollPoster;
import com.highnes.tour.view.poster.ZoomOutPageTransformer;
import com.highnes.tour.view.poster.ViewPagerCompat.OnCurrItem;

/**
 * <PRE>
 * 作用:
 *    轮播图 Fragment。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-23   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PosterFragment extends Fragment {

	private AutoScrollPoster mPosterView;
	// 放圆点的View的list
	private List<View> dotViewsList;
	// 圆点
	private LinearLayout dotLayout;
	// 是否是小的图片
	private boolean isShort = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_poster, container, false);
		initView(view);
		return view;
	}

	/**
	 * 初始化
	 * 
	 * @param view
	 */
	public void initView(View view) {
		dotViewsList = new ArrayList<View>();
		dotLayout = (LinearLayout) view.findViewById(R.id.dotLayout);
		mPosterView = (AutoScrollPoster) view.findViewById(R.id.viewPager);
		// mPosterView.setDisplayImageOptions(displayImageOptions);
		mPosterView.setScaleType(ScaleType.CENTER_CROP);
		mPosterView.needLoadAnimation(false);
		// 设置动画
		mPosterView.setPageTransformer(true, new ZoomOutPageTransformer());
		// mPosterView.setPageTransformer(true, null);
		if (isShort) {
			initPoster(Arrays.asList(DefaultData.IMAGE_FROM_LOCAL_SHORT));
		} else {
			initPoster(Arrays.asList(DefaultData.IMAGE_FROM_LOCAL));
		}
	}

	/**
	 * 设置是否是短的图片
	 * 
	 * @param isShort
	 */
	public void setIsShort(boolean isShort) {
		this.isShort = isShort;
	}

	/**
	 * 启动轮播
	 * 
	 * @param imageLists
	 *            图片地址
	 * @param isLocalData
	 *            是否加载默认图片 默认true显示
	 */
	public void initPoster(final List<String> imageList) {
		try {
			if (mPosterView == null) {
				return;
			}
			mPosterView.cleanAll();
			dotLayout.removeAllViews();
			dotViewsList.clear();
			mPosterView.addItems(imageList);
			mPosterView.startAutoScroll(5 * 1000);

			mPosterView.setOnItemViewClickListener(new AutoScrollPoster.OnItemViewClickListener() {

				@Override
				public void onItemViewClick(View view, Object item, int position) {
					if (onItemClick != null) {
						int pos = (position - imageList.size() * 10000) % imageList.size();
						if (pos >= 0) {
						} else {
							pos = imageList.size() + pos;
						}
						onItemClick.onItemClick(pos);
					}
				}
			});
			// 热点个数与图片特殊相等
			for (int i = 0; i < imageList.size(); i++) {
				ImageView dotView = new ImageView(getActivity());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.leftMargin = 5;
				params.rightMargin = 5;
				dotView.setBackgroundResource(R.drawable.ic_dot_pressred);
				dotLayout.addView(dotView, params);
				dotViewsList.add(dotView);
			}
			// 初始化
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == 0) {
					((View) dotViewsList.get(0)).setBackgroundResource(R.drawable.ic_dot_pressred);
				} else {
					((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.ic_dot_normal);
				}
			}
			/**
			 * FUNY 添加观察者 监听当前的位置
			 */
			mPosterView.setOnCurrItem(new OnCurrItem() {

				@Override
				public void onCurrItem(int position) {
					int pos = (position - imageList.size() * 10000) % imageList.size();
					if (pos >= 0) {
					} else {
						pos = imageList.size() + pos;
					}

					for (int i = 0; i < dotViewsList.size(); i++) {
						if (i == pos) {
							((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.ic_dot_pressred);
						} else {
							((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.ic_dot_normal);
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// XXX mPosterView.resumeScroll();
//		if (mPosterView == null) {
//			return;
//		}
//		LogUtils.i("Resume scroll...");
//		mPosterView.resumeScroll();
	}

	public void resumeScroll() {
//		if (mPosterView == null) {
//			return;
//		}
//		mPosterView.resumeScroll();
	}

	@Override
	public void onPause() {
		super.onPause();

		// LogUtils.i("Stop scroll...");
		// mPosterView.stopScroll();
		// XXX mPosterView.stopScroll();
	}

	private OnItemClick onItemClick;

	public interface OnItemClick {
		void onItemClick(int position);
	}

	public void setOnItemClick(OnItemClick onItemClick) {
		this.onItemClick = onItemClick;
	}

}
