package com.highnes.tour.ui.activities.home.ticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.InsureAdapter;
import com.highnes.tour.adapter.home.InsureUserInfoAdapter;
import com.highnes.tour.beans.home.ticket.SoldSafestInfo;
import com.highnes.tour.beans.home.ticket.TicketOrderInfo;
import com.highnes.tour.beans.home.ticket.SoldSafestInfo.SafestInfo;
import com.highnes.tour.beans.home.ticket.TicketOrderInfo.PeopleInfo;
import com.highnes.tour.beans.my.CommonUserInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.activities.my.common.CommonUserInfoActivity;
import com.highnes.tour.ui.activities.my.common.CommonUserInfoManageActivity;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    购买保险。
 * 注意事项:
 * 	  无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-25   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketInsureActivity extends BaseTitleActivity {

	// 当前的门票id
	private String mSoldID;
	// 门票数量
	private int mSoldNum;
	// 默认的保险ID
	private String mSelectId;
	// 当前的保险人
	private TicketOrderInfo.PeopleInfo mPeopleInfo;
	// 选择的保险的金额、名称
	private String mSafestPrice, mSafestName;

	// 保险
	private InsureAdapter insureAdapter;
	private ListView lvInsure;

	// 参保人
	private InsureUserInfoAdapter userAdapter;
	private ListView lvUser;

	// 添加人
	private LinearLayout llAdd;
	private List<CommonUserInfo.PeopleInfo> mSelect;
	// 接受到的用户列表
	private List<CommonUserInfo.PeopleInfo> mSelectUserSafe;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_insure;
	}

	@Override
	protected void findViewById2T() {
		lvInsure = getViewById(R.id.lv_insure);
		lvUser = getViewById(R.id.lv_user);
		llAdd = getViewById(R.id.ll_userinfo_add);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("选择保险类型");
		showBackwardView("", SHOW_ICON_DEFAULT);
		mSoldID = getIntent().getExtras().getString("mSoldID", "");
		mSelectId = getIntent().getExtras().getString("mSelectId", "");
		mSafestPrice = getIntent().getExtras().getString("mSafestPrice", "");
		mSoldNum = getIntent().getExtras().getInt("mSoldNum", 1);
		// 默认的取票人参保人
		mPeopleInfo = (PeopleInfo) getIntent().getExtras().getSerializable("mPeopleInfo");
		// 选择的多人参保人
		mSelectUserSafe = (List<CommonUserInfo.PeopleInfo>) getIntent().getExtras().getSerializable("mSelectUserSafe");

		insureAdapter = new InsureAdapter(mContext);
		lvInsure.setAdapter(insureAdapter);

		userAdapter = new InsureUserInfoAdapter(mContext, true);
		lvUser.setAdapter(userAdapter);
		mSelect = new ArrayList<CommonUserInfo.PeopleInfo>();

		if (mSelectUserSafe != null && mSelectUserSafe.size() > 1) {
			// 多人
			for (int i = 0; i < mSelectUserSafe.size(); i++) {
				CommonUserInfo.PeopleInfo data = new CommonUserInfo.PeopleInfo(mSelectUserSafe.get(i).ID, mSelectUserSafe.get(i).IDcard, true,
						mSelectUserSafe.get(i).Name, mSelectUserSafe.get(i).Phone);
				data.setSelect(true);
				mSelect.add(data);
			}
			userAdapter.setList(mSelect);
		} else {
			// 只有默认的人
			if (mPeopleInfo != null) {
				CommonUserInfo.PeopleInfo data = new CommonUserInfo.PeopleInfo(mPeopleInfo.ID, mPeopleInfo.IDcard, true, mPeopleInfo.Name, mPeopleInfo.Phone);
				data.setSelect(true);
				mSelect.add(data);
				userAdapter.setList(mSelect);
			}
		}
	}

	@Override
	protected void setListener2T() {
		llAdd.setOnClickListener(this);
		getViewById(R.id.btn_submit).setOnClickListener(this);

		insureAdapter.setOnChecked(new InsureAdapter.OnChecked() {

			@Override
			public void onChecked(final int position, SafestInfo info, int type) {
				if (0 == type) {
					mSelectId = info.ID;
					mSafestPrice = info.Money + "";
					mSafestName = info.Name;
					insureAdapter.setIndex(position);

				} else {
					Bundle bundle = new Bundle();
					bundle.putInt("mType", 1102);
					bundle.putString("mTitle", "保险说明");
					bundle.putString("mUrl", UrlSettings.URL_H5_HOME_TICKET_DETAIL_SAFEST_INFO + info.ID);
					openActivity(WebViewTitleActivity.class, bundle);
				}
			}
		});

		userAdapter.setOnChecked(new InsureUserInfoAdapter.OnChecked() {

			@Override
			public void onChecked(CommonUserInfo.PeopleInfo info, int type) {
				if (1 == type) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("info", info);
					bundle.putString("state", "update");
					openActivity(CommonUserInfoManageActivity.class, bundle);
				} else {
					if (mSelect != null) {
						if (info.isSelect()) {
							if (mSelect.size() < mSoldNum) {
								mSelect.add(info);
							} else {
								showToast("只能选择" + mSoldNum + "位旅客信息");
								info.setSelect(false);
								userAdapter.notifyDataSetChanged();
							}
						} else {
							mSelect.remove(info);
						}
					}
				}
			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByInfo();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == DefaultData.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				this.mSelect.clear();
				this.mSelect = (List<CommonUserInfo.PeopleInfo>) data.getSerializableExtra("mSelect");
				userAdapter.setList(mSelect);
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// add user
		case R.id.ll_userinfo_add:
			if (AppUtils.isLogin(mContext)) {
				Intent intent = new Intent(mContext, CommonUserInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("isSelect", true);
				bundle.putInt("count", mSoldNum);
				intent.putExtras(bundle);
				startActivityForResult(intent, DefaultData.REQUEST_CODE);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 完成
		case R.id.btn_submit:
			Intent data = new Intent();
			data.putExtra("mSafestID", mSelectId);
			data.putExtra("mSafestPrice", mSafestPrice);
			data.putExtra("mSafestName", mSafestName);
			data.putExtra("mSelectUser", (Serializable) mSelect);
			setResult(RESULT_OK, data);
			finishActivity();
			break;
		default:
			break;
		}
	}

	// -----------------------网络请求-----------------------
	/**
	 * Info 网络请求
	 * 
	 */
	private void requestByInfo() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("SoldID", mSoldID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TICKET_DETAIL_SAFEST_LIST, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonInfo(result);
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
	private void parseJsonInfo(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final SoldSafestInfo info = GsonUtils.json2T(result, SoldSafestInfo.class);
				if (info.SoldSafest != null && info.SoldSafest.size() > 0) {
					insureAdapter.setList(info.SoldSafest);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							for (int i = 0; i < info.SoldSafest.size(); i++) {
								if (mSelectId.equals(info.SoldSafest.get(i).ID)) {
									insureAdapter.setIndex(i);
									break;
								}
							}
						}
					}, 200);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}