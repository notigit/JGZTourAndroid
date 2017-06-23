package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.beans.my.LoginUserInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.fragment.main.MyFragment;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.MD5Utils;
import com.highnes.tour.utils.RegExpUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    个人中心页面..忘记密码。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class GetPwdActivity extends BaseTitleActivity {

	private EditText etPhone, etPwdNew, etCode;
	private TextView tvCode;
	private String mCode = "";

	@Override
	protected void onPause() {
		super.onPause();
		try {
			if (count > 0 && timer != null) {
				timer.cancel();
				timer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_get_pwd;
	}

	@Override
	protected void findViewById2T() {
		etPhone = getViewById(R.id.et_phone2);
		etPwdNew = getViewById(R.id.et_pwd_new);
		etCode = getViewById(R.id.et_code);
		tvCode = getViewById(R.id.tv_getcode);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("找回密码");
		showBackwardView("", SHOW_ICON_DEFAULT);
		showForwardView("确定", SHOW_ICON_INVISIBLE);
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.tv_getcode).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 修改密码
		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			processPwd();
			break;
		// 获取验证码
		case R.id.tv_getcode:
			judgeCode();
			break;
		default:
			break;
		}
	}

	/**
	 * 处理Pwd
	 */
	private void processPwd() {
		String phone = etPhone.getText().toString().trim();
		String pwdNew = etPwdNew.getText().toString().trim();
		String code = etCode.getText().toString().trim();
		// 手机号码验证
		if (StringUtils.isEmpty(phone)) {
			showToast("您还未输入手机号码");
			return;
		} else {
			if (!RegExpUtils.isMobileNO(phone)) {
				showToast("请输入正确的手机号码");
				return;
			}
		}
		if (StringUtils.isEmpty(code)) {
			showToast("请输入验证码");
			return;
		} else {
			if (!mCode.equals(code)) {
				showToast("验证码不正确");
				return;
			}
		}

		// 密码验证
		if (StringUtils.isEmpty(pwdNew)) {
			showToast("您还未输入密码");
			return;
		} else {
			if (!RegExpUtils.isPassword(pwdNew)) {
				showToast("请输入6-12位密码，字母数字和特殊符号");
				return;
			}
		}
		requestByPwd(phone, MD5Utils.string2MD5(pwdNew));
	}

	// -----------------------网络请求-----------------------
	/**
	 * Pwd 网络请求
	 * 
	 * @param Phone
	 *            手机号
	 * @param Password
	 *            密码(MD5加密后)
	 */
	private void requestByPwd(String phone, final String Password) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Phone", phone);
		paramDatas.put("Password", Password);
		new NETConnection(mContext, UrlSettings.URL_USER_GET_PWD, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonPwd(result, Password);
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
	 * Pwd JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonPwd(String result, String Password) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			SPUtils.put(mContext, PreSettings.USER_PWD.getId(), Password);
			showToast("密码设置成功!");
			finishActivity();
		} else {
			showToast("密码设置失败!");
		}
	}

	// -----------------------网络请求-----------------------
	/**
	 * RegCode 网络请求
	 * 
	 */
	private void requestByRegCode(final String Phone) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("to", Phone);
		new NETConnection(mContext, UrlSettings.URL_USER_REG_CODE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonRegCode(result);
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
	 * RegCode JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonRegCode(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			mCode = JsonUtils.getString(result, "number", "");
			showToast("验证码发送成功!");
		} else {
			showToast("验证码发送失败!");
		}
	}

	// ----------定时器------------------------------------------------------
	// 是否能点击获取验证码
	private boolean isGetCode = true;
	private int count = 60;
	@SuppressLint("HandlerLeak")
	Handler handlerTime = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (count > 0) {
					tvCode.setText("(" + Integer.toString(count--) + ")秒");
					isGetCode = false;
				} else {
					tvCode.setText("获取验证码");
					timer.cancel();
					isGetCode = true;
					if (task != null) {
						task.cancel(); // 将原任务从队列中移除
					}
				}
			}
			super.handleMessage(msg);
		};
	};
	// 定时器
	Timer timer;
	TimerTask task;

	/**
	 * 判断验证码
	 */
	private void judgeCode() {
		if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
			showToast("请输入手机号码");
			return;
		} else {
			if (RegExpUtils.isMobileNO(etPhone.getText().toString().trim())) {
				if (isGetCode) {
					requestByRegCode(etPhone.getText().toString().trim());
					count = 60;
					timer = new Timer();
					task = new TimerTask() {
						@Override
						public void run() {
							// 需要做的事:发送消息
							Message message = new Message();
							message.what = 1;
							handlerTime.sendMessage(message);
						}
					};
					timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
				}
			} else {
				showToast("请输入正确的手机号码");
			}
		}
	}
}
