package com.highnes.tour.ui.activities.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;

/**
 * <PRE>
 * 作用:
 *    所有带标题的页面的基类。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-08-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public abstract class BaseTitleActivity extends BaseActivity {

	/** -1表示默认的返回图标 */
	protected final static int SHOW_ICON_DEFAULT = -1;
	/** -2不显示图标不保留位置 */
	protected final static int SHOW_ICON_GONE = -2;
	/** -3表示不显示但保留位置 */
	protected final static int SHOW_ICON_INVISIBLE = -3;

	/** 标题 */
	private TextView mTitleTextView, mTitleTextView19;
	/** 后退 文字 */
	private TextView mBackwardButtonTxt, mBackwardButtonTxt19;
	/** 后退 图片 */
	private ImageView mBackwardButtonImg, mBackwardButtonImg19;
	/** 前进 */
	private TextView mForwardButtonTxt, mForwardButtonTxt19;
	/** 后退 图片 */
	private ImageView mForwardButtonImg, mForwardButtonImg19;
	/** 页面内容 */
	private FrameLayout mContentLayout;
	// 点击的事件
	private LinearLayout llBackward, llBackward19, llForward, llForward19;

	// 根布局
	private RelativeLayout root, rootV19;
	// true表示是19以下的api
	protected boolean is19Below = false;

	@Override
	@LayoutRes
	protected int getLayoutId() {
		return R.layout.activity_title;
	}

	@Override
	protected void findViewById() {
		mTitleTextView = (TextView) findViewById(R.id.text_title);
		mBackwardButtonTxt = (TextView) findViewById(R.id.button_backward_txt);
		mBackwardButtonImg = (ImageView) findViewById(R.id.button_backward_img);
		mForwardButtonTxt = (TextView) findViewById(R.id.button_forward_txt);
		mForwardButtonImg = (ImageView) findViewById(R.id.button_forward_img);
		mContentLayout = (FrameLayout) findViewById(R.id.layout_content);
		llBackward = (LinearLayout) findViewById(R.id.ll_title_back);
		llForward = (LinearLayout) findViewById(R.id.ll_title_forward);
		mTitleTextView19 = (TextView) findViewById(R.id.text_title19);
		mBackwardButtonTxt19 = (TextView) findViewById(R.id.button_backward_txt19);
		mBackwardButtonImg19 = (ImageView) findViewById(R.id.button_backward_img19);
		mForwardButtonTxt19 = (TextView) findViewById(R.id.button_forward_txt19);
		mForwardButtonImg19 = (ImageView) findViewById(R.id.button_forward_img19);
		llBackward19 = (LinearLayout) findViewById(R.id.ll_title_back19);
		llForward19 = (LinearLayout) findViewById(R.id.ll_title_forward19);
		root = (RelativeLayout) findViewById(R.id.root);
		rootV19 = (RelativeLayout) findViewById(R.id.root_v19);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
	}

	@Override
	protected void setListener() {
		llBackward.setOnClickListener(this);
		llForward.setOnClickListener(this);
		llBackward19.setOnClickListener(this);
		llForward19.setOnClickListener(this);
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		// 设置布局内容
		setContentView2T(getLayoutId2T());
		is19Below = setFullStatusBar();
		showRootOrV19(is19Below);
		is19Below= !is19Below;
		findViewById2T();
		initView2T(savedInstanceState);
		setListener2T();
		processLogic2T(savedInstanceState);
	}

	public void setContentView2T(int layoutResID) {
		mContentLayout.removeAllViews();
		View.inflate(this, layoutResID, mContentLayout);
		onContentChanged();
	}

	/**
	 * 布局绑定
	 * 
	 * @return
	 */
	@LayoutRes
	protected abstract int getLayoutId2T();

	/**
	 * 绑定控件id
	 */
	protected abstract void findViewById2T();

	/**
	 * 初始化控件
	 */
	protected abstract void initView2T(Bundle savedInstanceState);

	/**
	 * 事件注册
	 */
	protected abstract void setListener2T();

	/**
	 * 逻辑处理
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void processLogic2T(Bundle savedInstanceState);

	/**
	 * 是显示19版本以上还是以下的主题
	 */
	protected void showRootOrV19(boolean isShowV19) {
		if (isShowV19) {
			root.setVisibility(View.GONE);
			rootV19.setVisibility(View.VISIBLE);
		} else {
			root.setVisibility(View.VISIBLE);
			rootV19.setVisibility(View.GONE);
		}
	}

	/**
	 * 是否显示后退按钮
	 * 
	 * @param backwardText
	 *            按钮显示的文本信息
	 * @param icon
	 *            图片资源的ID,-1表示默认的返回图标，-2表示不显示也不保留位置，-3表示不显示但保留位置
	 * @param show
	 *            显示为true
	 */
	@SuppressLint("NewApi")
	protected void showBackwardView(String backwardText, int icon) {
		try {
			if (is19Below) {
				// 显示
				if (TextUtils.isEmpty(backwardText)) {
					mBackwardButtonTxt.setVisibility(View.GONE);
				} else {
					mBackwardButtonTxt.setVisibility(View.VISIBLE);
				}
				mBackwardButtonTxt.setText(backwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mBackwardButtonImg.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mBackwardButtonImg.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mBackwardButtonImg.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mBackwardButtonImg.setImageResource(icon);
					// 显示默认的图标与文字
					mBackwardButtonImg.setVisibility(View.VISIBLE);
				}

			} else {
				// 显示
				if (TextUtils.isEmpty(backwardText)) {
					mBackwardButtonTxt19.setVisibility(View.GONE);
				} else {
					mBackwardButtonTxt19.setVisibility(View.VISIBLE);
				}
				mBackwardButtonTxt19.setText(backwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mBackwardButtonImg19.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mBackwardButtonImg19.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mBackwardButtonImg19.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mBackwardButtonImg19.setImageResource(icon);
					// 显示默认的图标与文字
					mBackwardButtonImg19.setVisibility(View.VISIBLE);
				}

			}
		} catch (Exception e) {
		}
	}

	/**
	 * 是否显示后退按钮
	 * 
	 * @param backwardText
	 *            按钮显示的文本信息 -1不显示
	 * @param icon
	 *            图片资源的ID,-1表示默认的返回图标，-2表示不显示也不保留位置，-3表示不显示但保留位置
	 * @param show
	 *            显示为true
	 */
	@SuppressLint("NewApi")
	protected void showBackwardView(@StringRes int backwardText, int icon) {
		try {
			if (is19Below) {
				// 显示
				if (-1 == backwardText) {
					mBackwardButtonTxt.setVisibility(View.GONE);
				} else {
					mBackwardButtonTxt.setVisibility(View.VISIBLE);
				}
				mBackwardButtonTxt.setText(backwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mBackwardButtonImg.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mBackwardButtonImg.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mBackwardButtonImg.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mBackwardButtonImg.setImageResource(icon);
					// 显示默认的图标与文字
					mBackwardButtonImg.setVisibility(View.VISIBLE);
				}
			} else {
				// 显示
				if (-1 == backwardText) {
					mBackwardButtonTxt19.setVisibility(View.GONE);
				} else {
					mBackwardButtonTxt19.setVisibility(View.VISIBLE);
				}
				mBackwardButtonTxt19.setText(backwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mBackwardButtonImg19.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mBackwardButtonImg19.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mBackwardButtonImg19.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mBackwardButtonImg19.setImageResource(icon);
					// 显示默认的图标与文字
					mBackwardButtonImg19.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 是否显示前进按钮
	 * 
	 * @param forwardText
	 *            按钮显示的文本信息
	 * @param icon
	 *            图片资源的ID,-1表示默认的返回图标，-2表示不显示也不保留位置，-3表示不显示但保留位置
	 * @param show
	 *            显示为true
	 */
	@SuppressLint("NewApi")
	public void showForwardView(String forwardText, int icon) {
		try {
			if (is19Below) {
				// 显示
				if (TextUtils.isEmpty(forwardText)) {
					mForwardButtonTxt.setVisibility(View.GONE);
				} else {
					mForwardButtonTxt.setVisibility(View.VISIBLE);
				}
				mForwardButtonTxt.setText(forwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mForwardButtonImg.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mForwardButtonImg.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mForwardButtonImg.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mForwardButtonImg.setImageResource(icon);
					// 显示默认的图标与文字
					mForwardButtonImg.setVisibility(View.VISIBLE);
				}
			} else {
				// 显示
				if (TextUtils.isEmpty(forwardText)) {
					mForwardButtonTxt19.setVisibility(View.GONE);
				} else {
					mForwardButtonTxt19.setVisibility(View.VISIBLE);
				}
				mForwardButtonTxt19.setText(forwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mForwardButtonImg19.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mForwardButtonImg19.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mForwardButtonImg19.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mForwardButtonImg19.setImageResource(icon);
					// 显示默认的图标与文字
					mForwardButtonImg19.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 是否显示前进按钮
	 * 
	 * @param forwardText
	 *            按钮显示的文本信息 -1不显示
	 * @param icon
	 *            图片资源的ID,-1表示默认的返回图标，-2表示不显示也不保留位置，-3表示不显示但保留位置
	 * @param show
	 *            显示为true
	 */
	@SuppressLint("NewApi")
	public void showForwardView(@StringRes int forwardText, int icon) {
		try {
			if (is19Below) {
				// 显示

				if (-1 == forwardText) {
					mForwardButtonTxt.setVisibility(View.GONE);
				} else {
					mForwardButtonTxt.setVisibility(View.VISIBLE);
				}
				mForwardButtonTxt.setText(forwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mForwardButtonImg.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mForwardButtonImg.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mForwardButtonImg.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mForwardButtonImg.setImageResource(icon);
					// 显示默认的图标与文字
					mForwardButtonImg.setVisibility(View.VISIBLE);
				}
			} else {
				// 显示

				if (-1 == forwardText) {
					mForwardButtonTxt19.setVisibility(View.GONE);
				} else {
					mForwardButtonTxt19.setVisibility(View.VISIBLE);
				}
				mForwardButtonTxt19.setText(forwardText);
				if (icon == SHOW_ICON_GONE) {
					// 不显示图标不保留位置，只显示文字
					mForwardButtonImg19.setVisibility(View.GONE);
				} else if (icon == SHOW_ICON_INVISIBLE) {
					// 不显示图标要保留位置，只显示文字
					mForwardButtonImg19.setVisibility(View.INVISIBLE);
				} else if (icon == SHOW_ICON_DEFAULT) {
					// 显示默认的图标与文字
					mForwardButtonImg19.setVisibility(View.VISIBLE);
				} else {
					// 指定图标
					mForwardButtonImg19.setImageResource(icon);
					// 显示默认的图标与文字
					mForwardButtonImg19.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 后退事件处理
	 * 
	 * @param backwardView
	 */
	protected void onBackward(View backwardView) {
		finishActivity();
	}

	/**
	 * 前进事件处理
	 * 
	 * @param forwardView
	 */
	protected void onForward(View forwardView) {

	}

	public void setTitle(int titleId) {
		if (is19Below) {
			mTitleTextView.setText(titleId);
		} else {
			mTitleTextView19.setText(titleId);

		}
	}

	public void setTitle(CharSequence title) {
		if (is19Below) {
			mTitleTextView.setText(title);
		} else {
			mTitleTextView19.setText(title);

		}
	}

	public void setTitleColor(int textColor) {
		if (is19Below) {
			mTitleTextView.setTextColor(textColor);
		} else {
			mTitleTextView19.setTextColor(textColor);

		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_title_back19:
		case R.id.ll_title_back:
			onBackward(v);
			break;

		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			onForward(v);
			break;

		default:
			break;
		}
	}

}
