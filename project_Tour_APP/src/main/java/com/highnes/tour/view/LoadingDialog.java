package com.highnes.tour.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.highnes.tour.R;
import com.highnes.tour.utils.AppManager;

/**
 * <PRE>
 * 作用:
 *    加载等待框。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-08-30   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
@SuppressWarnings("ResourceType")
public class LoadingDialog extends Dialog {
	private CircularProgress cp;
	// 是否可以点击外部取消
	private boolean cancelable = false;

	// protected SystemBarTintManager mTintManager;
	public LoadingDialog(Context context) {
		super(context, R.style.Dialog_bocop_loading);
		init();
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// setTranslucentStatus(true);
		// }
		// if(AppManager.getAppManager().currentActivity()!=null){
		// mTintManager = new
		// SystemBarTintManager(AppManager.getAppManager().currentActivity());
		// }
	}

	private void init() {
		View contentView = View.inflate(getContext(), R.layout.custom_loding_dialog, null);
		setContentView(contentView);

		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancelable) {
					dismiss();
				}
			}
		});
		cp = (CircularProgress) findViewById(R.id.pro_is_show);
		getWindow().setWindowAnimations(R.anim.alpha_in);

	}

	// @TargetApi(19)
	// protected void setTranslucentStatus(boolean on) {
	// Window win = getWindow();
	// WindowManager.LayoutParams winParams = win.getAttributes();
	// final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
	// if (on) {
	// winParams.flags |= bits;
	// } else {
	// winParams.flags &= ~bits;
	// }
	// win.setAttributes(winParams);
	// }

	@Override
	public void show() {
		// mTintManager.setStatusBarTintEnabled(true);
		// mTintManager.setStatusBarTintResource(R.color.titlec);
		cp.setVisibility(View.VISIBLE);
		super.show();
	}

	@Override
	public void dismiss() {
		cp.setVisibility(View.INVISIBLE);
		super.dismiss();
	}

	@Override
	public void setCancelable(boolean flag) {
		cancelable = flag;
		super.setCancelable(flag);
	}
}
