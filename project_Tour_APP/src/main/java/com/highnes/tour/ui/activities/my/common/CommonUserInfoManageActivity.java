package com.highnes.tour.ui.activities.my.common;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.EditText;

import com.highnes.tour.R;
import com.highnes.tour.beans.my.CommonUserInfo;
import com.highnes.tour.beans.my.CommonUserInfo.PeopleInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.RegExpUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.toggle.ToggleButton;
import com.highnes.tour.view.toggle.ToggleButton.OnToggleChanged;

/**
 * <PRE>
 * 作用:
 *    我的..常用信息 ..常用旅客信息..添加/修改/删除。
 * 注意事项:
 *   无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-23   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class CommonUserInfoManageActivity extends BaseTitleActivity {

	// 获取到的值
	private CommonUserInfo.PeopleInfo mInfo;
	private String mState;

	private EditText etName, etPhone, etIDCard;
	private String mName, mPhone, mIDCard;
	/** 默认领取人 */
	private ToggleButton tbDef, tbErtong;
	private boolean isDef = false;
	private boolean isErtong = false;
	
	private String mName2, mPhone2, mIDCard2;
	private boolean isDef2 = false;
	private boolean isErtong2 = false;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_common_userinfo_add;
	}

	@Override
	protected void findViewById2T() {
		etName = getViewById(R.id.et_name);
		etPhone = getViewById(R.id.et_phone);
		etIDCard = getViewById(R.id.et_idcard);
		tbDef = getViewById(R.id.tb_def);
		tbErtong = getViewById(R.id.tb_ertong);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("添加常用旅客");
		showBackwardView("", SHOW_ICON_DEFAULT);
		try {
			mState = getIntent().getExtras().getString("state", "add");
			mInfo = (PeopleInfo) getIntent().getExtras().getSerializable("info");
		} catch (Exception e) {
			mState = "add";
			mInfo = null;
		}
		initInfo();

	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.btn_save).setOnClickListener(this);
		// 是否打开通知
		tbDef.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				isDef = on;
			}
		});
		// 是否打开通知
		tbErtong.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				isErtong = on;
			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 保存
		case R.id.btn_save:
			saveUserInfo();
			break;
		// 删除信息
		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			delUserInfo();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化信息
	 */
	private void initInfo() {
		try {
			if (mInfo != null) {
				mName = mInfo.Name;
				mName2 = mName;
				mPhone = mInfo.Phone;
				mPhone2 = mPhone;
				mIDCard = mInfo.IDcard;
				mIDCard2 = mIDCard;
				isDef = mInfo.IsDefault;
				isDef2 = isDef;
				isErtong = mInfo.IsBaby;
				isErtong2 = isErtong;
				etName.setText(mName);
				etName.setSelection(mName.length());
				if (mPhone == null) {
					etPhone.setText("");
				} else {
					etPhone.setText(mPhone);
					etPhone.setSelection(mPhone.length());
				}
				etIDCard.setText(mIDCard);
				etIDCard.setSelection(mIDCard.length());
				if (isDef) {
					tbDef.toggleOn();
				} else {
					tbDef.toggleOff();
				}
				if (isErtong) {
					tbErtong.toggleOn();
				} else {
					tbErtong.toggleOff();
				}
			}
			if ("update".equals(mState)) {
				// 修改
				showForwardView("删除信息", SHOW_ICON_INVISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 保存用户信息
	 */
	private void saveUserInfo() {
		mName = etName.getText().toString().trim();
		mPhone = etPhone.getText().toString().trim();
		mIDCard = etIDCard.getText().toString().trim();

		if (StringUtils.isEmpty(mName) || mName.length() < 2) {
			showToast("请输入旅客的真实姓名");
			return;
		}
		if (!StringUtils.isEmpty(mPhone)) {
			if (!RegExpUtils.isMobileNO(mPhone)) {
				showToast("手机号码格式不正确");
				return;
			}
		}
		if (StringUtils.isEmpty(mIDCard)) {
			showToast("请输入旅客的身份证号码");
			return;
		} else {
			if (!RegExpUtils.isIDCard(mIDCard)) {
				showToast("身份证格式不正确");
				return;
			}
		}
		
		
		if (isDef) {
			if (StringUtils.isEmpty(mPhone)) {
				showToast("请输入默认领取人手机号码");
			} else {
				if (!RegExpUtils.isMobileNO(mPhone)) {
					showToast("手机号码格式不正确");
				} else {
					if(mName.equals(mName2) && mPhone.equals(mPhone2) && mIDCard.equals(mIDCard2) && isDef==isDef2 && isErtong == isErtong2){
						finishActivity();
						return;
					}
					if ("add".equals(mState)) {
						requestByAddUserInfo(mName, mPhone, mIDCard); // 新增
					} else {
						requestByUpdateUserInfo(mName, mPhone, mIDCard);// 修改
					}
				}
			}
		} else {
			if(mName.equals(mName2) && mPhone.equals(mPhone2) && mIDCard.equals(mIDCard2) && isDef==isDef2 && isErtong == isErtong2){
				finishActivity();
				return;
			}
			
			if ("add".equals(mState)) {
				requestByAddUserInfo(mName, mPhone, mIDCard); // 新增
			} else {
				requestByUpdateUserInfo(mName, mPhone, mIDCard);// 修改
			}
		}
	}

	/**
	 * 删除信息
	 */
	private void delUserInfo() {
		showDialog("确定删除 " + mInfo.Name + " 的信息吗？", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (2 == which) {
					requestByDelUserInfo();
				}
			}
		});
	}

	// -----------------------网络请求-----------------------
	/**
	 * AddUserInfo 网络请求
	 */
	private void requestByAddUserInfo(String Name, String Phone, String IDcard) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("Name", Name);
		paramDatas.put("Phone", Phone);
		paramDatas.put("IDcard", IDcard);
		paramDatas.put("IsDefault", isDef);
		paramDatas.put("IsBaby", isErtong);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_COMMON_USERINFO_ADD, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonAddUserInfo(result);
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
	 * AddUserInfo JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonAddUserInfo(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			showToast("添加信息成功!");
			finishActivity();
		} else { 
			showToast(JsonUtils.getString(result, "points", "添加信息失败!"));
		}
	}

	/**
	 * UpdateUserInfo 网络请求
	 */
	private void requestByUpdateUserInfo(String Name, String Phone, String IDcard) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("id", mInfo.ID);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("Name", Name);
		paramDatas.put("Phone", Phone);
		paramDatas.put("IDcard", IDcard);
		paramDatas.put("IsDefault", isDef);
		paramDatas.put("IsBaby", isErtong);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_COMMON_USERINFO_UPDATE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonUpdateUserInfo(result);
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
	 * UpdateUserInfo JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonUpdateUserInfo(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			showToast("修改信息成功!");
			finishActivity();
		} else {
			showToast("修改信息失败!");
		}
	}

	/**
	 * DelUserInfo 网络请求
	 */
	private void requestByDelUserInfo() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("id", mInfo.ID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_COMMON_USERINFO_DEL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonDelUserInfo(result);
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
	 * UpdateUserInfo JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonDelUserInfo(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			showToast("删除信息成功!");
			finishActivity();
		} else {
			showToast("删除信息失败!");
		}
	}
}
