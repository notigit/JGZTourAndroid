package com.highnes.tour.ui.activities.my.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ListView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.CommonUserInfoAdapter;
import com.highnes.tour.adapter.my.CommonUserInfoAdapter.OnChecked;
import com.highnes.tour.beans.my.CommonUserInfo;
import com.highnes.tour.beans.my.CommonUserInfo.PeopleInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    我的..常用信息 ..常用旅客信息。
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
public class CommonUserInfoActivity extends BaseTitleActivity {

	private ListView lvInfo;
	private CommonUserInfoAdapter adapter;
	// 是否是选择模式
	boolean isSelect = false;
	// 选择的数量 <0表示不限制选择人数
	int count = 0;

	private List<CommonUserInfo.PeopleInfo> mSelect;

	@Override
	protected void onResume() {
		super.onResume();
		requestByUserInfo();
	}

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_common_userinfo;
	}

	@Override
	protected void findViewById2T() {
		lvInfo = getViewById(R.id.lv_info);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("常用旅客信息");
		showBackwardView("", SHOW_ICON_DEFAULT);

		isSelect = getIntent().getExtras().getBoolean("isSelect", false);
		count = getIntent().getExtras().getInt("count", 0);
		adapter = new CommonUserInfoAdapter(mContext, isSelect);
		lvInfo.setAdapter(adapter);
		if (isSelect) {
			showForwardView("确定", SHOW_ICON_INVISIBLE);
		}
		mSelect = new ArrayList<CommonUserInfo.PeopleInfo>();
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_userinfo_add).setOnClickListener(this);
		// 选择联系人
		adapter.setOnChecked(new OnChecked() {

			@Override
			public void onChecked(PeopleInfo info, int type) {
				try {
					if (0 == type) {// 选择联系人
						if (isSelect && mSelect != null) {
							if (info.isSelect()) {
								if (count <= 0) {
									mSelect.add(info);
								} else {
									if (mSelect.size() < count) {
										mSelect.add(info);
									} else {
										showToast("只能选择" + count + "位旅客信息");
										info.setSelect(false);
										adapter.notifyDataSetChanged();
									}
								}
							} else {
								mSelect.remove(info);
							}
						}
					} else { // 查看联系人
						Bundle bundle = new Bundle();
						bundle.putSerializable("info", info);
						bundle.putString("state", "update");
						openActivity(CommonUserInfoManageActivity.class, bundle);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		// 新增信息
		case R.id.ll_userinfo_add:
			if (AppUtils.isLogin(mContext)) {
				Bundle bundle = new Bundle();
//				bundle.putSerializable("info", null);
				bundle.putString("state", "add");
				openActivity(CommonUserInfoManageActivity.class, bundle);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 确定
		case R.id.ll_title_forward19:
		case R.id.ll_title_forward:
			resultUserInfo();
			break;
		default:
			break;
		}
	}

	/**
	 * 返回选择的旅客
	 * 
	 */
	private void resultUserInfo() {
		try {
			if (mSelect.size() > 0) {
				boolean isError = false;
				// 先判断是否填写了手机号码
				for (int i = 0; i < mSelect.size(); i++) {
					final PeopleInfo info = mSelect.get(i);
					if (mSelect.get(i).Phone == null) {
						isError = true;
						showDialog("您选择的旅客未填写手机号码\n请完善信息", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (2 == which) {
									Bundle bundle = new Bundle();
									bundle.putSerializable("info", info);
									bundle.putString("state", "update");
									openActivity(CommonUserInfoManageActivity.class, bundle);
								}
							}
						});
					}
				}
				if (!isError) {
					Intent data = new Intent();
					data.putExtra("mSelect", (Serializable) mSelect);
					setResult(RESULT_OK, data);
					finishActivity();
				}
			} else {
				showToast("您还没有选择旅客");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -----------------------网络请求-----------------------
	/**
	 * UserInfo 网络请求
	 * 
	 */
	private void requestByUserInfo() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_COMMON_USERINFO_LIST, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonUserInfo(result);
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
	 * UserInfo JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonUserInfo(String result) {
		try {

			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				CommonUserInfo info = GsonUtils.json2T(result, CommonUserInfo.class);
				if (info.people != null && info.people.size() > 0) {
					adapter.setList(info.people);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
