package com.highnes.tour.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.highnes.tour.R;
import com.highnes.tour.adapter.GuidePagerAdapter;

/**
 * 引导页
 * 
 * @author YYBJ
 * 
 */
public class GuidePageActivity extends Activity {
	public final String TAG = "GuidePage";
	/** 显示引导页中所有页面的控件 */
	private ViewPager viewPager_guidance = null;
	/** 保存引导页布局 */
	private List<View> views;
	/** 适配viewpager的适配器 */
	private GuidePagerAdapter guidePagerAdapter = null;
	/** 页面中的点 */
	@SuppressWarnings("unused")
	private LinearLayout linear_guidance_dots = null;
	/** 底部图片 */
	private ImageView[] dots;
	/** 记录当前选中位置 */
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initViews();
		initDots();
	}

	/**
	 * 初始化底部小点
	 */
	private void initDots() {

		LinearLayout linear_guidance_dots = (LinearLayout) findViewById(R.id.linear_guidance_dots);
		dots = new ImageView[views.size()];
		for (int i = 0; i < views.size(); i++) {// 循环取得小点图片
			dots[i] = (ImageView) linear_guidance_dots.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	/**
	 * 初始化操作
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void initViews() {
		setContentView(R.layout.activity_guide);
		/* 初始化布局控件及监听 */
		viewPager_guidance = (ViewPager) findViewById(R.id.viewPager_guidance);
		/* 初始化引导图片列表 */
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.activity_guide_one, null));
		views.add(inflater.inflate(R.layout.activity_guide_two, null));
//		views.add(inflater.inflate(R.layout.activity_guide_three, null));
		views.add(inflater.inflate(R.layout.activity_guide_four, null));
		/* 初始化Adapter */
		guidePagerAdapter = new GuidePagerAdapter(views, this);
		viewPager_guidance.setAdapter(guidePagerAdapter);
		viewPager_guidance.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			/*
			 * 新的页面被选中时调用(non-Javadoc)
			 * 
			 * @see android.support.v4.view.ViewPager.OnPageChangeListener
			 * #onPageSelected(int)
			 */
			@Override
			public void onPageSelected(int position) {
				if (position < 0 || position > views.size() - 1 || currentIndex == position) {
					return;
				}

				dots[position].setEnabled(false);
				dots[currentIndex].setEnabled(true);

				currentIndex = position;
			}

			/*
			 * 当前页面被滑动时调用(non-Javadoc)
			 * 
			 * @see android.support.v4.view.ViewPager.OnPageChangeListener
			 * #onPageScrolled(int, float, int)
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			/*
			 * 滑动状态改变时调用(non-Javadoc)
			 * 
			 * @see android.support.v4.view.ViewPager.OnPageChangeListener
			 * #onPageScrollStateChanged(int)
			 */
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);// 上往下推出效果
	}
}
