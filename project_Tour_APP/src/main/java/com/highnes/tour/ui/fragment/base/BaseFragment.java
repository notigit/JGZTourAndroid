package com.highnes.tour.ui.fragment.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.highnes.tour.R;
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
 * 所有Fragment的基类。
 * 注意事项:
 * 无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-06-13   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public abstract class BaseFragment extends Fragment implements OnClickListener, Handler.Callback {
    protected Context mContext;
    protected View mView;
    protected Handler mHandler;
    protected boolean isDestroy;
    // 用于加载网络图片
    protected DisplayImageOptions options;
    /**
     * 加载框
     */
    protected LoadingDialog dialog;

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mView.findViewById(id);
    }

    /**
     * 获取布局文件ID
     *
     * @return
     */
    protected abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 绑定id
     */
    protected abstract void findViewById();

    /**
     * 初始化
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        findViewById();
        mHandler = new Handler(this);
        initView(mView, savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
        return mView;
    }

    @Override
    public void onDestroyView() {
        isDestroy = true;
        super.onDestroyView();
    }

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
     * @param text 通知内容
     */
    protected void showToast(CharSequence text) {
        // Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        ToastHelper.showToast(mContext, text.toString());
    }

    /**
     * Toast 显示通知[短格式]
     *
     * @param stringRes 通知内容
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
        final Intent intent = new Intent(getActivity(), pClass);
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

    @Override
    public void onClick(View v) {
    }

    /**
     * 自定义状态栏
     *
     * @param on
     */
    @SuppressLint("InlinedApi")
    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
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
            SystemBarTintManager tintManager = new SystemBarTintManager(
                    getActivity());
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
            SystemBarTintManager tintManager = new SystemBarTintManager(
                    getActivity());
            tintManager.setStatusBarTintEnabled(false);
            tintManager.setStatusBarTintResource(R.color.titlec);// 通知栏颜色
        }
    }

    /**
     * 显示加载框
     */
    protected void showLoading() {
        dialog = new LoadingDialog(mContext);
        dialog.show();
    }

    /**
     * 显示加载框
     */
    protected LoadingDialog getLoading() {
        return new LoadingDialog(mContext);
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
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_img)
                .showImageForEmptyUri(R.drawable.ic_default_img)
                .showImageOnFail(R.drawable.ic_default_img).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100)).build();
    }

    /**
     * 初始化options
     *
     * @param resId 设置图片的资源ID
     */
    protected void initOptions(@DrawableRes int resId) {
        options = new DisplayImageOptions.Builder().showImageOnLoading(resId)
                .showImageForEmptyUri(resId).showImageOnFail(resId)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100)).build();
    }

    protected void showNoData() {
        showToast("该功能正在建设中,敬请期待！");
    }

    /**
     * 版本是否大于19
     *
     * @return
     */
    protected boolean setFullStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        }
        return false;
    }

    /**
     * 列表对话框 没有按钮
     *
     * @param l               事件
     * @param title           标题
     * @param resourceIdArray item
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
     * @param title    标题
     * @param message  内容
     * @param listener 点击事件
     */
    protected void showDialog(CharSequence title, CharSequence message, DialogInterface.OnClickListener listener) {
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mContext, message, listener);
        buildAlert.setTitle(title);
        buildAlert.show();
    }

    /**
     * 两个按钮
     *
     * @param title    无
     * @param message  内容
     * @param listener 点击事件
     */
    protected void showDialog(CharSequence message, DialogInterface.OnClickListener listener) {
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mContext, "\n\n" + message, listener);
        buildAlert.setTitleVisibility(View.GONE);
        buildAlert.show();
    }

    /**
     * 一个按钮的对话框
     *
     * @param title    标题
     * @param message  内容
     * @param text     按钮文字
     * @param listener 点击事件
     * @param flag     true表示可以取消
     */
    protected void showDialog(CharSequence title, CharSequence message, CharSequence text, DialogInterface.OnClickListener listener, boolean flag) {
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(mContext, message, text, listener);
        buildAlert.setTitle(title);
        buildAlert.setCancelable(flag);
        buildAlert.show();
    }
}