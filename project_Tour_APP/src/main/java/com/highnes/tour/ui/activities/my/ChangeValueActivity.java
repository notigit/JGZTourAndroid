package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.fragment.main.MyFragment;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.RegExpUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.edittext.ContainsEmojiEditText;

/**
 * <PRE>
 * 作用:
 *    修改昵称页面。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-07   QINX        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ChangeValueActivity extends BaseTitleActivity {
	/** 昵称 */
	private ContainsEmojiEditText etValue;
	/** 提交按钮 */
	private Button btSubmit;
	private TextView tvLab;
	/** 昵称 */
	private String valueData = "";
	// 当前的类型 0表示修改昵称，1表示修改号码,2表示修改姓名，3表示修改邮箱
	private int mType = 0;
	private String value = "";

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_info_change_value;
	}

	@Override
	protected void findViewById2T() {
		etValue = getViewById(R.id.et_value);
		btSubmit = getViewById(R.id.bt_submit);
		tvLab = getViewById(R.id.tv_lab);

	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("修改资料");
		showBackwardView("", SHOW_ICON_DEFAULT);
		mType = getIntent().getExtras().getInt("mType", 0);
		tvLab.setText(getIntent().getExtras().getString("lab", ""));
		valueData = getIntent().getExtras().getString("value", "");
		etValue.setText(valueData);
		etValue.setSelection(etValue.getText().toString().length());
		if (valueData.length() < 1) {
			btSubmit.setBackgroundResource(R.drawable.shape_radius_gray5_line);
			btSubmit.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		} else {
			btSubmit.setBackgroundResource(R.drawable.shape_radius_green5);
			btSubmit.setTextColor(ResHelper.getColorById(mContext, R.color.white));
		}

	}

	@Override
	protected void setListener2T() {
		etValue.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		etValue.addTextChangedListener(etTextWatcher);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 确定
		case R.id.bt_submit:
			// 0表示修改昵称，1表示修改号码,2表示修改姓名，3表示修改邮箱
			if (StringUtils.isEmpty(value)) {
				showToast("不能为空");
				return;
			}
			if (valueData.equals(value)) {
				finishActivity();
				return;
			}
			if (0 == mType) {
				if (value.length() > 1 && value.length() < 11) {
					requestByUpdateData(value);
				} else {
					showToast("昵称只能2-10位");
					return;
				}
			} else if (1 == mType) {
				if (!RegExpUtils.isMobileNO(value)) {
					showToast("手机号码格式不正确");
					return;
				} else {
					requestByUpdateData(value);
				}
			} else if (2 == mType) {
				if (value.length() > 1 && value.length() < 11) {
					requestByUpdateData(value);
				} else {
					showToast("姓名只能2-8位");
					return;
				}
			} else if (3 == mType) {
				if (!RegExpUtils.isEmail(value)) {
					showToast("邮箱格式不正确");
					return;
				} else {
					requestByUpdateData(value);
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 昵称输入框值改变监听
	 */
	TextWatcher etTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			value = etValue.getText().toString().trim();
			if (value.length() < 1) {
				btSubmit.setBackgroundResource(R.drawable.shape_radius_gray5_line);
				btSubmit.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
			} else {
				btSubmit.setBackgroundResource(R.drawable.shape_radius_green5);
				btSubmit.setTextColor(ResHelper.getColorById(mContext, R.color.white));
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};

	// -----------------------网络请求-----------------------

	/**
	 * UpdateData 网络请求
	 * 
	 */
	private void requestByUpdateData(final String value) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		String sex = SPUtils.get(mContext, PreSettings.USER_SEX.getId(), PreSettings.USER_SEX.getDefaultValue()).toString();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		paramDatas.put("UserName", SPUtils.get(mContext, PreSettings.USER_NICKNAME.getId(), PreSettings.USER_NICKNAME.getDefaultValue()).toString());
		paramDatas.put("Sex", "1".equals(sex) ? "男" : "女");
		paramDatas.put("Email", SPUtils.get(mContext, PreSettings.USER_EMAIL.getId(), PreSettings.USER_EMAIL.getDefaultValue()).toString());
		paramDatas.put("Name", SPUtils.get(mContext, PreSettings.USER_NAME.getId(), PreSettings.USER_NAME.getDefaultValue()).toString());
		paramDatas.put("Birth", SPUtils.get(mContext, PreSettings.USER_BIRTHDAY.getId(), PreSettings.USER_BIRTHDAY.getDefaultValue()).toString());
		// 0表示修改昵称，1表示修改号码,2表示修改姓名，3表示修改邮箱
		if (0 == mType) {
			paramDatas.put("UserName", value);
		} else if (1 == mType) {
		} else if (2 == mType) {
			paramDatas.put("Name", value);
		} else if (3 == mType) {
			paramDatas.put("Email", value);
		}
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_UPDATEUSER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonUpdateData(result, value);
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
	 * Specialty JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonUpdateData(String result, String value) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				showToast("修改成功");
				if (0 == mType) {
					SPUtils.put(mContext, PreSettings.USER_NICKNAME.getId(), value);
					Intent mIntent = new Intent();
					MyFragment.isLoad = true;
					mIntent.setAction(MyFragment.ACTION_CALLBACK_MY);
					sendBroadcast(mIntent);
				} else if (1 == mType) {
				} else if (2 == mType) {
					SPUtils.put(mContext, PreSettings.USER_NAME.getId(), value);
				} else if (3 == mType) {
					SPUtils.put(mContext, PreSettings.USER_EMAIL.getId(), value);
				}
				finishActivity();
			} else {
				showToast("修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
