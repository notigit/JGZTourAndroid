package com.highnes.tour.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;

/**
 * 版本更新的提示框
 * 
 */
@SuppressWarnings("ResourceType")
public class ToastDialog extends Dialog {

	private TextView tvTitle, tvYes, tvNo;
	private TextView titleV;// 标题栏
	private View titleLineV;// 标题分割线
	private View centerLine;
	// 默认可以取消
	private boolean cancelable = true;
	private Context context;
	private OnClickCallback mOnClickCallback;
	private LinearLayout llButton;

	/**
	 * FUNY用的这个 TODO
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param mOnClickCallback
	 */
	public ToastDialog(Context context, String title, String content, OnClickCallback mOnClickCallback) {
		super(context, R.style.Dialog_bocop);
		this.context = context;
		init();
		titleV.setText(title);
		tvTitle.setText(content);
		showTitle();
		this.mOnClickCallback = mOnClickCallback;
	}

	public ToastDialog(Context context, int title, OnClickCallback mOnClickCallback) {
		super(context, R.style.Dialog_bocop);
		this.context = context;
		init();
		tvTitle.setText(title);
		this.mOnClickCallback = mOnClickCallback;
	}

	public ToastDialog(Context context, String title, boolean cancelable, OnClickCallback mOnClickCallback) {
		super(context, R.style.Dialog_bocop);
		this.context = context;
		init();
		tvTitle.setText(title);
		setCancelable(cancelable);
		this.mOnClickCallback = mOnClickCallback;
	}

	public ToastDialog(Context context, String title, String yes, String no, OnClickCallback mOnClickCallback) {
		super(context, R.style.Dialog_bocop);
		this.context = context;
		init();
		tvTitle.setText(title);
		tvYes.setText(yes);
		tvNo.setText(no);
		this.mOnClickCallback = mOnClickCallback;
	}

	/**
	 * 全部参数构造
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            标题
	 * @param yes
	 *            左边的按钮
	 * @param no
	 *            右边的按钮
	 * @param cancelable
	 *            是否可以取消 false表示不可取消
	 */
	public ToastDialog(Context context, String title, String yes, String no, boolean cancelable, OnClickCallback mOnClickCallback) {
		super(context, R.style.Dialog_bocop);
		this.context = context;
		init();
		tvTitle.setText(title);
		tvYes.setText(yes);
		tvNo.setText(no);
		setCancelable(cancelable);
		this.mOnClickCallback = mOnClickCallback;
	}

	/**
	 * 全部参数构造
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            标题
	 * @param noButton
	 *            不显示按钮
	 */
	public ToastDialog(Context context, String title, boolean noButton) {
		super(context, R.style.Dialog_bocop);
		this.context = context;
		init();
		if (noButton) {
			tvTitle.setText(title);
			llButton.setVisibility(View.INVISIBLE);
			setCancelable(false);
		}

	}

	private void init() {
		View contentView = View.inflate(getContext(), R.layout.custom_dialog_ok, null);
		setContentView(contentView);

		tvTitle = (TextView) findViewById(R.id.tv_custom_dialog_title);
		llButton = (LinearLayout) findViewById(R.id.ll_button);
		tvYes = (TextView) findViewById(R.id.tv_custom_dialog_yes);
		tvNo = (TextView) findViewById(R.id.tv_custom_dialog_no);
		centerLine = findViewById(R.id.center_line);
		titleV = (TextView) findViewById(R.id.title);
		titleLineV = findViewById(R.id.title_line);
		titleV.setVisibility(View.GONE);
		titleV.setVisibility(View.GONE);

		tvYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnClickCallback.onClickYes();
				dismiss();
			}
		});
		tvNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnClickCallback.onClickNo();
				dismiss();
			}
		});

		// contentView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View arg0, MotionEvent arg1) {
		// dismiss();
		// return false;
		// }
		// });
		getWindow().setWindowAnimations(R.anim.alpha_in);
	}

	/**
	 * 只显示一个按钮
	 */
	public void showOneBtn() {
		tvYes.setVisibility(View.GONE);
		centerLine.setVisibility(View.GONE);
	}

	/**
	 * 显示标题
	 */
	public void showTitle() {
		titleV.setVisibility(View.VISIBLE);
		titleLineV.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示dol
	 */
	@Override
	public void show() {
		super.show();
	}

	/**
	 * 关闭dolg
	 */
	@Override
	public void dismiss() {
		super.dismiss();
	}

	/**
	 * 设置取消
	 */
	@Override
	public void setCancelable(boolean flag) {
		super.setCancelable(flag);
		super.setCanceledOnTouchOutside(flag);
	}

	/**
	 * 设置标题字体大小
	 * 
	 * @param size
	 */
	public void setTitleSize(float size) {
		tvTitle.setTextSize(size);
	}

	/**
	 * 设置按钮字体大小
	 * 
	 * @param size
	 */
	public void setButtonSize(float size) {
		tvYes.setTextSize(size);
		tvNo.setTextSize(size);
	}

	/**
	 * 设置标题字体的颜色
	 * 
	 * @param colorId
	 *            颜色id
	 */
	public void setTitleColor(int colorId) {
		tvTitle.setTextColor(context.getResources().getColor(colorId));
	}

	/**
	 * 设置左边按钮字体的颜色
	 * 
	 * @param colorId
	 *            颜色id
	 */
	public void setTextView1Color(int colorId) {
		tvYes.setTextColor(context.getResources().getColor(colorId));
	}

	/**
	 * 设置左边按钮字体的颜色
	 * 
	 * @param colorId
	 *            颜色id
	 */
	public void setTextView2Color(int colorId) {
		tvNo.setTextColor(context.getResources().getColor(colorId));
	}

	/**
	 * 回调方法
	 */
	public static interface OnClickCallback {
		/**
		 * 成功方法
		 * 
		 * @param result
		 *            返回结果（解密后的数据）
		 */
		void onClickYes();

		void onClickNo();
	}

}
