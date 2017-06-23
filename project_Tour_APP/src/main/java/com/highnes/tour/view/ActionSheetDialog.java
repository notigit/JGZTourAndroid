package com.highnes.tour.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.utils.DensityUtils;

public class ActionSheetDialog implements OnClickListener {
	private Activity activity;
	private Dialog dialog;
	private TextView txt_title;
	private TextView txt_cancel;
	private OnItemClick mOnItemClick;

	public ActionSheetDialog(Activity activity) {
		this.activity = activity;
	}

	public ActionSheetDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(activity).inflate(R.layout.custom_board, null);
		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(DensityUtils.getAreaTwo(activity).getmWidth());
		view.findViewById(R.id.ll_share_weixin_cricle).setOnClickListener(this);
		view.findViewById(R.id.ll_share_weixin).setOnClickListener(this);
		view.findViewById(R.id.ll_share_qq).setOnClickListener(this);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 定义Dialog布局和参数
		dialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}

	public ActionSheetDialog setTitle(String title) {
		txt_title.setVisibility(View.VISIBLE);
		txt_title.setText(title);
		return this;
	}

	public ActionSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}

	public interface OnItemClick {
		void onClick(int rsid);
	}

	public void setmOnItemClick(OnItemClick mOnItemClick) {
		this.mOnItemClick = mOnItemClick;
	}

	@Override
	public void onClick(View v) {
		if (mOnItemClick != null) {
			mOnItemClick.onClick(v.getId());
			dismiss();
		}
	}

}
