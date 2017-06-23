package com.highnes.tour.ui.activities.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.highnes.tour.R;
import com.highnes.tour.receivers.NetReceivers;
import com.highnes.tour.utils.AppManager;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.ToastHelper;
import com.highnes.tour.view.LoadingDialog;
import com.highnes.tour.view.SystemBarTintManager;
import com.highnes.tour.view.dialog.ECAlertDialog;
import com.highnes.tour.view.dialog.ECListDialog;
import com.highnes.tour.view.dialog.ECListDialog.OnDialogItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * <PRE>
 * 作用:
 *    所有Activity的基类。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-13   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener, Handler.Callback {
	protected Handler mHandler;
	protected boolean isDestroy;
	/** 加载框 */
	protected LoadingDialog dialog;
	// 用于加载网络图片
	protected DisplayImageOptions options;
	/** 网络变化的广播 */
	private NetReceivers mReceiver = new NetReceivers();
	private IntentFilter mFilter = new IntentFilter();
	/** 上下文 */
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		mContext = this;
		AppManager.getAppManager().addActivity(this);
		mHandler = new Handler(this);
		findViewById();
		initView(savedInstanceState);
		setListener();
		processLogic(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 添加网络变化的广播
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	@Override
	protected void onDestroy() {
		isDestroy = true;
		super.onDestroy();
	}

	/**
	 * 验证是否为隐藏状态
	 * 
	 * @param v
	 * @return
	 */
	protected Boolean isVisib(View v) {
		if (v.getVisibility() == View.INVISIBLE) {
			return false;
		}
		return true;
	}

	// 获取布局文件ID
	protected abstract @LayoutRes
	int getLayoutId();

	/**
	 * 查找View
	 * 
	 * @param id
	 *            控件的id
	 * @param <VT>
	 *            View类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <VT extends View> VT getViewById(@IdRes int id) {
		return (VT) findViewById(id);
	}

	/**
	 * 绑定id
	 */
	protected abstract void findViewById();

	/**
	 * 初始化布局以及View控件
	 */
	protected abstract void initView(Bundle savedInstanceState);

	/**
	 * 给View控件添加事件监听器
	 */
	protected abstract void setListener();

	/**
	 * 处理业务逻辑，状态恢复等操作
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void processLogic(Bundle savedInstanceState);

	@Override
	public boolean handleMessage(Message msg) {
		if (!isDestroy) {
			performHandleMessage(msg);
		}
		return true;
	}

	/**
	 * 接收处理mHandler的消息
	 */
	protected void performHandleMessage(Message msg) {

	}

	/**
	 * 线程延迟操作
	 */
	protected void performHandlePostDelayed(final int type, int delayMillis) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				performHandlePostDelayedCallBack(type);
			}
		}, delayMillis);
	}

	/**
	 * 线程延迟操作回调复写方法
	 */
	protected void performHandlePostDelayedCallBack(int type) {

	}

	/**
	 * Toast 显示通知[短格式]
	 * 
	 * @param text
	 *            通知内容
	 */
	protected void showToast(CharSequence text) {
		// Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		ToastHelper.showToast(mContext, text.toString());
	}

	/**
	 * Toast 显示通知[短格式]
	 * 
	 * @param stringRes
	 *            通知内容
	 */
	protected void showToast(@StringRes int stringRes) {
		// Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show();
		ToastHelper.showToast(mContext, stringRes);
	}

	/**
	 * 通过类名启动Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		final Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(intent);

			}
		}, 100);
	}

	/**
	 * 通过Action启动Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 通过Action启动Activity，并且含有Bundle数据
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 关闭当前的Activity页面
	 */
	protected void finishActivity() {
		AppManager.getAppManager().finishActivity();
	}

	@Override
	public void onClick(View v) {
	}

	/**
	 * 自定义状态栏
	 * 
	 * @param on
	 */
	@SuppressLint("InlinedApi")
	protected void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 显示状态栏的颜色
	 */
	protected void showSystemBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.titlec);// 通知栏颜色
		}
	}

	/**
	 * 显示状态栏的颜色
	 */
	protected void hideSystemBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(false);
			tintManager.setStatusBarTintResource(R.color.titlec);// 通知栏颜色
		}
	}

	/**
	 * 监听Back键按下事件,方法2: 注意: 返回值表示:是否能完全处理该事件 在此处返回false,所以会继续传播该事件.
	 * 在具体项目中此处的返回值视情况而定.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finishActivity();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	/**
	 * 显示加载框
	 */
	protected void showLoading() {
		dialog = new LoadingDialog(this);
		dialog.show();
	}

	/**
	 * 显示加载框
	 */
	protected LoadingDialog getLoading() {
		return new LoadingDialog(this);
	}

	/**
	 * 取消加载框
	 */
	protected void stopLoading() {
		try {
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化Options
	 */
	protected void initOptions() {
		/**
		 * 开始加载图片
		 */
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_img).showImageForEmptyUri(R.drawable.ic_default_img)
				.showImageOnFail(R.drawable.ic_default_img).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(100)).build();
	}

	/**
	 * 初始化options
	 * 
	 * @param resId
	 *            设置图片的资源ID
	 */
	protected void initOptions(@DrawableRes int resId) {
		options = new DisplayImageOptions.Builder().showImageOnLoading(resId).showImageForEmptyUri(resId).showImageOnFail(resId).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(100)).build();
	}

	/**
	 * 列表对话框 没有按钮
	 * 
	 * @param l
	 *            事件
	 * @param title
	 *            标题
	 * @param resourceIdArray
	 *            item
	 */
	protected void showDialogList(OnDialogItemClickListener l, String title, @ArrayRes int resourceIdArray) {
		ECListDialog dialog = new ECListDialog(mContext, resourceIdArray);
		dialog.setOnDialogItemClickListener(l);
		dialog.setTitle(title);
		dialog.show();
	}

	/**
	 * 两个按钮
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param listener
	 *            点击事件
	 */
	protected void showDialog(CharSequence title, CharSequence message, DialogInterface.OnClickListener listener) {
		ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mContext, message, listener);
		buildAlert.setTitle(title);
		buildAlert.show();
	}

	/**
	 * 两个按钮
	 * 
	 * @param title
	 *            无
	 * @param message
	 *            内容
	 * @param listener
	 *            点击事件
	 */
	protected void showDialog(CharSequence message, DialogInterface.OnClickListener listener) {
		ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mContext, "\n\n" + message, listener);
		buildAlert.setTitleVisibility(View.GONE);
		buildAlert.show();
	}

	/**
	 * 一个按钮的对话框
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param text
	 *            按钮文字
	 * @param listener
	 *            点击事件
	 * @param flag
	 *            true表示可以取消
	 */
	protected void showDialog(CharSequence title, CharSequence message, CharSequence text, DialogInterface.OnClickListener listener, boolean flag) {
		ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mContext, message, text, listener);
		buildAlert.setTitle(title);
		buildAlert.setCancelable(flag);
		buildAlert.show();
	}

	protected void showNoData() {
		showToast("该功能正在建设中,敬请期待！");
	}

	// ------------------------
	@SuppressLint({ "InlinedApi" })
	protected boolean setFullStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = this.getWindow();
			// 设置透明状态栏,这样才能让 ContentView 向上
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// 设置状态栏颜色
//			window.setStatusBarColor(ResHelper.getColorById(mContext, R.color.titlec));
			ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
			View mChildView = mContentView.getChildAt(0);
			if (mChildView != null) {
				// 注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView
				// 的第一个子
				// View . 使其不为系统 View 预留空间.
				ViewCompat.setFitsSystemWindows(mChildView, false);
			}
			return true;
		}
		return false;
	}
}