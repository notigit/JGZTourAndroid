package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    意见反馈页面。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-08   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class OpinionActivity extends BaseTitleActivity {
	/** 内容 */
	private EditText etContent;
	private String strContent = "";
	/** 提交 */
	private Button btSubmit;
	/** 输入字数 */
	private TextView tvHint;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_opinion;
	}

	@Override
	protected void findViewById2T() {
		etContent = getViewById(R.id.et_content);
		btSubmit = getViewById(R.id.bt_submit);
		tvHint = getViewById(R.id.tv_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("意见反馈");
		showBackwardView("", SHOW_ICON_DEFAULT);

	}

	@Override
	protected void setListener2T() {
		btSubmit.setOnClickListener(this);
		etContent.addTextChangedListener(etTextWatcher);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 提交
		case R.id.bt_submit:
			requestByFeedback(strContent);
			break;

		default:
			break;
		}
	}

	TextWatcher etTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			strContent = etContent.getText().toString();
			tvHint.setText(strContent.length() + "/500");
			if ("".equals(strContent)) {
				btSubmit.setBackgroundResource(R.drawable.shape_radius_gray5);
				btSubmit.setClickable(false);
			} else {
				btSubmit.setBackgroundResource(R.drawable.shape_radius_green5);
				btSubmit.setClickable(true);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};

	/**
	 * Feedback 网络请求
	 * 
	 */
	private void requestByFeedback(final String value) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		paramDatas.put("Contents", value);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_FEEDBACK, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonFeedback(result, value);
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
	 * Feedback JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonFeedback(String result, String value) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				showToast("反馈成功");
				finishActivity();
			} else {
				showToast("反馈失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
