package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.MD5Utils;
import com.highnes.tour.utils.RegExpUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    注册页面。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-05   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class RegisterActivity extends BaseTitleActivity {
	// 手机号，门牌号，姓名及密码
	private EditText etPhone, etCode, etPwd;
	private CheckBox cbTalk;
	private boolean isCheckedTalk = true;
	private TextView tvCode;
	private String mCode = "";

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		return R.layout.activity_my_register;
	}

	@Override
	protected void findViewById2T() {
		etPhone = getViewById(R.id.et_phone);
		etCode = getViewById(R.id.et_code);
		etPwd = getViewById(R.id.et_pwd);
		cbTalk = getViewById(R.id.cb_talk);
		tvCode = getViewById(R.id.tv_getcode);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("注册");
		showBackwardView("", SHOW_ICON_DEFAULT);

	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.btn_register).setOnClickListener(this);
		getViewById(R.id.tv_login).setOnClickListener(this);
		getViewById(R.id.tv_getcode).setOnClickListener(this);
		getViewById(R.id.tv_talk).setOnClickListener(this);
		setOnCheckedChangeListener();
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	/**
	 * 复选框
	 */
	private void setOnCheckedChangeListener() {
		cbTalk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isCheckedTalk = isChecked;
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 注册
		case R.id.btn_register:
			processReg();
			break;
		// 获取验证码
		case R.id.tv_getcode:
			judgeCode();
			break;
		// 登录
		case R.id.tv_login:
			finishActivity();
			break;
		// 注册协议
		case R.id.tv_talk: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 5001);
			bundle.putString("mTitle", "注册协议");
			bundle.putString("mUrl", UrlSettings.URL_H5_USER_REG_TALK);
			openActivity(WebViewTitleActivity.class, bundle);
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 处理注册
	 */
	private void processReg() {
		String phone = etPhone.getText().toString().trim();
		String code = etCode.getText().toString().trim();
		String password = etPwd.getText().toString().trim();
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
		if (StringUtils.isEmpty(password)) {
			showToast("您还未输入密码");
			return;
		} else {
			if (!RegExpUtils.isPassword(password)) {
				showToast("请输入6-12位密码，字母数字和特殊符号");
				return;
			}
		}
		if (!isCheckedTalk) {
			showToast("请同意条款后进行注册");
			return;
		}
		// TODO 验证码的判断
		requestByReg(phone, MD5Utils.string2MD5(password));
	}

	// -----------------------网络请求-----------------------
	/**
	 * Reg 网络请求
	 * 
	 * @param Phone
	 *            手机号
	 * @param Sex
	 *            性别（传递男or女）
	 * @param UserName
	 *            业主姓名
	 * @param Password
	 *            密码（MD5加密后）
	 */
	private void requestByReg(final String Phone, String Password) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Phone", Phone);
		paramDatas.put("Sex", "男");
		paramDatas.put("UserName", "金龟子");
		paramDatas.put("Password", Password);
		new NETConnection(mContext, UrlSettings.URL_USER_REG, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonReg(result, Phone);
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
	 * Reg JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonReg(String result, String Phone) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			showToast("注册成功!");
			SPUtils.put(mContext, PreSettings.USER_PHONE.getId(), Phone);
			finishActivity();
		} else {
			showToast("注册失败!");
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
