package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.highnes.tour.MainActivity;
import com.highnes.tour.R;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DataCleanManager;
import com.highnes.tour.utils.FileSizeUtil;
import com.highnes.tour.utils.FileUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.NotificationHelper;
import com.highnes.tour.view.ToastDialog;

/**
 * <PRE>
 * 作用:
 *    个人中心页面..设置。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class SettingsActivity extends BaseTitleActivity {

	private TextView tvClean, tvUpdate;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_settings;
	}

	@Override
	protected void findViewById2T() {
		tvClean = getViewById(R.id.tv_clean);
		tvUpdate = getViewById(R.id.tv_update);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("设置");
		showBackwardView("", SHOW_ICON_DEFAULT);
		// 缓存大小
		tvClean.setText(FileSizeUtil.getAutoFileOrFilesSize(FileUtils.getCacheImageFiles(mContext).getAbsolutePath()));

	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_pwd).setOnClickListener(this);
		getViewById(R.id.ll_clean).setOnClickListener(this);
		getViewById(R.id.ll_about).setOnClickListener(this);
		getViewById(R.id.ll_update).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByData(false);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 修改密码
		case R.id.ll_pwd:
			if (AppUtils.isLogin(mContext)) {
				openActivity(SettingsPwdActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 清除缓存
		case R.id.ll_clean:
			showDialog("立即清除缓存？", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (2 == which) {
						DataCleanManager.deleteFilesByDirectory(FileUtils.getCacheImageFiles(mContext));
						tvClean.setText("0B");
					}
				}
			});
			break;
		// 关于我们
		case R.id.ll_about:
			openActivity(AboutActivity.class);
			break;
		// 版本更新
		case R.id.ll_update:
			requestByData(true);
			break;
		default:
			break;
		}
	}

	// ------------------版本更新-------------------
	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByData(final boolean isShow) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Type", "Android");
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_SETTINGS_UPDATE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonData(result, isShow);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonData(String result, boolean isShow) {
		try {
			JSONObject objs = new JSONObject(result);
			JSONObject obj = JsonUtils.getJSONObject(objs, "AppVersions", null);
			final String DownAddrs = JsonUtils.getString(obj, "DownAddrs", "");
			final Integer LowestVersions = JsonUtils.getInt(obj, "LowestVersions", 0);
			final Integer VersionsNumber = JsonUtils.getInt(obj, "VersionsNumber", 0);
			final String UpdateTime = JsonUtils.getString(obj, "UpdateTime", "");
			final String RenewalDetails = JsonUtils.getString(obj, "RenewalDetails", "");

			if (VersionsNumber > AppUtils.getVersionCode(mContext)) {
				tvUpdate.setText("您有新的版本点击更新");
				if (!isShow)
					return;
				// ismwt 是否强行更新判断
				ToastDialog toast = new ToastDialog(mContext, "发现新版本", RenewalDetails, new ToastDialog.OnClickCallback() {

					@Override
					public void onClickYes() {
					}

					@Override
					public void onClickNo() {
						// 立即更新
						NotificationHelper helper = new NotificationHelper(mContext, UrlSettings.URL_APK + DownAddrs);
						helper.startDownloadNotify();
					}
				});
				toast.setOnKeyListener(new DialogOnKeyListener());
				// 必须强制更新
				if (LowestVersions > AppUtils.getVersionCode(mContext)) {
					toast.showOneBtn();
					toast.show();
				} else {
					toast.show();
				}
			} else {
				tvUpdate.setText("您已是最新版本");
				LogUtils.i("没有发现新版本");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tvUpdate.setText("您已是最新版本");
			LogUtils.i("没有发现新版本");
		}
	}

	/**
	 * Dialog 监听返回事件
	 */
	public class DialogOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			}
			return false;
		}

	}
}
