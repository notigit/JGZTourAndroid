package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
 *    个人中心页面..设置..修改密码。
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
public class SettingsPwdActivity extends BaseTitleActivity {

	private EditText etPwdOld, etPwdNew, etPwdNew2;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_settings_pwd;
	}

	@Override
	protected void findViewById2T() {
		etPwdOld = getViewById(R.id.et_pwd_old);
		etPwdNew = getViewById(R.id.et_pwd_new);
		etPwdNew2 = getViewById(R.id.et_pwd_new2);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("修改密码");
		showBackwardView("", SHOW_ICON_DEFAULT);
		showForwardView("保存", SHOW_ICON_INVISIBLE);
	}

	@Override
	protected void setListener2T() {
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
		default:
			break;
		}
	}

	/**
	 * 处理Pwd
	 */
	private void processPwd() {
		String pwdOld = etPwdOld.getText().toString().trim();
		String pwdNew = etPwdNew.getText().toString().trim();
		String pwdNew2 = etPwdNew2.getText().toString().trim();
		String oldPwd = SPUtils.get(mContext, PreSettings.USER_PWD.getId(), PreSettings.USER_PWD.getDefaultValue()).toString();
		if (StringUtils.isEmpty(pwdOld)) {
			showToast("请输入旧密码");
			return;
		}
		if (!oldPwd.equals(MD5Utils.string2MD5(pwdOld))) {
			showToast("旧密码不正确");
			return;
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
		if (!pwdNew.equals(pwdNew2)) {
			showToast("两次密码不一致");
			return;
		}
		requestByPwd(MD5Utils.string2MD5(pwdNew));
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
	private void requestByPwd(final String Password) {
		showLoading();
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("Password", Password);
		new NETConnection(mContext, UrlSettings.URL_USER_SETTINGS_PWD, new NETConnection.SuccessCallback() {

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
			showToast("密码修改成功!");
			finishActivity();
		} else {
			showToast("密码修改失败!");
		}
	}
}
