package com.highnes.tour.ui.activities.home.tour;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.SelectUserInfoAdapter;
import com.highnes.tour.adapter.home.SelectUserInfoAdapter.OnChecked;
import com.highnes.tour.beans.home.tour.Tour1OrderPayInfo;
import com.highnes.tour.beans.home.tour.Tour2OrderInfo;
import com.highnes.tour.beans.my.CommonUserInfo;
import com.highnes.tour.beans.my.CommonUserInfo.PeopleInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.PayActivity;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.activities.my.common.CommonUserInfoActivity;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.LogUtils;
import com.highnes.tour.utils.RegExpUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.toggle.ToggleButton;
import com.highnes.tour.view.toggle.ToggleButton.OnToggleChanged;

/**
 * <PRE>
 * 作用:
 *    订单填写。
 * 注意事项:
 * 	  无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-22   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class Tour1DetailOrderActivity extends BaseTitleActivity {

	// 门票信息/价格
	private TextView tvTitle, tvPriceNow, tvPriceNow2, tvTimeInfo, tvLoc;
	// 取票人姓名/手机号码
	private EditText etName, etPhone;
	// 当前旅游产品id/日期/套餐ID
	private String mTourID, mDate, mPackageID;
	// 当前的门票信息
	private Tour2OrderInfo mTourInfo;

	// ---------------------------------------

	// 当前的保险人
	private Tour2OrderInfo.PeopleInfo mPeopleInfo;
	// 保险
	private TextView tvInsureName, tvInsurePrice, tvInsureState;
	// 当前的保险ID
	private String mSafestId = "";
	private String mSafestPrice = "0";
	private String mSafestName = "";
	// 选择的保险人
	List<CommonUserInfo.PeopleInfo> mSelectUserSafe;
	private String mPeopleID = "";
	private double SafestMoney;
	// 是否有保险套餐
	private boolean isHasSafe = false;

	// ---------------------------------------

	// 总金额、
	private TextView tvMoneyTotle;
	// 旅游产品的价格-成人
	private double mTourPriceCR;
	// 旅游产品的价格--儿童
	private double mTourPriceET;
	// 成人和儿童的人数
	private int crCount = 0;
	private int etCount = 0;

	// ---------------------------------------

	// 出行人
	private ListView lvList;
	private SelectUserInfoAdapter adapter;
	List<CommonUserInfo.PeopleInfo> mSelect;
	private String mSelectID = "";
	// 房差
	private ToggleButton tbHouse;
	private boolean isHouse = false;
	private double housePrice = 0;
	private TextView tvHouseMoney;

	// ---------------------------------------

	// 留言
	private EditText etMsg;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_tour_1_detail_order;
	}

	@Override
	protected void findViewById2T() {
		etName = getViewById(R.id.et_name);
		etPhone = getViewById(R.id.et_phone);
		tvTitle = getViewById(R.id.tv_title);
		tvPriceNow = getViewById(R.id.tv_price_now);
		tvPriceNow2 = getViewById(R.id.tv_price_now2);
		tvInsureName = getViewById(R.id.tv_insure_name);
		tvInsurePrice = getViewById(R.id.tv_insure_price);
		tvInsureState = getViewById(R.id.tv_insure_state);
		tvMoneyTotle = getViewById(R.id.tv_money);
		tvTimeInfo = getViewById(R.id.tv_time_info);
		tvLoc = getViewById(R.id.tv_loc);
		lvList = getViewById(R.id.lv_list);
		tbHouse = getViewById(R.id.tb_house);
		tvHouseMoney = getViewById(R.id.tv_house_money);
		etMsg = getViewById(R.id.et_message);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("订单填写");
		showBackwardView("", SHOW_ICON_DEFAULT);
		mTourID = getIntent().getExtras().getString("mTourID", "");
		mPackageID = getIntent().getExtras().getString("mPackageID", "");
		mDate = getIntent().getExtras().getString("mDate", "");
		mSelectUserSafe = new ArrayList<CommonUserInfo.PeopleInfo>();
		mSelect = new ArrayList<CommonUserInfo.PeopleInfo>();
		adapter = new SelectUserInfoAdapter(mContext);
		lvList.setAdapter(adapter);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_userinfo).setOnClickListener(this);
		getViewById(R.id.ll_userinfo2).setOnClickListener(this);
		getViewById(R.id.ll_insure).setOnClickListener(this);
		getViewById(R.id.tv_submit).setOnClickListener(this);
		getViewById(R.id.ll_house).setOnClickListener(this);

		adapter.setOnChecked(new OnChecked() {

			@Override
			public void onChecked(PeopleInfo info) {
				adapter.remove(info);
				proLogicPrice();
			}
		});

		// 是否打开通知
		tbHouse.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				isHouse = on;
				proLogicPrice();
			}
		});
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByInfo();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 添加取票人
		case R.id.ll_userinfo:
			if (AppUtils.isLogin(mContext)) {
				Intent intent = new Intent(mContext, CommonUserInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("isSelect", true);
				bundle.putInt("count", 1);
				intent.putExtras(bundle);
				startActivityForResult(intent, DefaultData.REQUEST_CODE);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 添加出行人
		case R.id.ll_userinfo2:
			if (AppUtils.isLogin(mContext)) {
				Intent intent = new Intent(mContext, CommonUserInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("isSelect", true);
				bundle.putInt("count", -1);
				intent.putExtras(bundle);
				startActivityForResult(intent, DefaultData.REQUEST_CODESS);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 房差说明
		case R.id.ll_house: {
			Bundle bundle = new Bundle();
			bundle.putString("mTitle", "房差说明");
			bundle.putInt("mType", 1201);
			bundle.putString("mUrl", UrlSettings.URL_HOME_TOUR_DETAIL_ORDER_HOUSE);
			openActivity(WebViewTitleActivity.class, bundle);
		}
			break;
		// 保险
		case R.id.ll_insure: {
			if (AppUtils.isLogin(mContext)) {
				Intent intent = new Intent(mContext, TourInsureActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("mTourID", mTourID);
				bundle.putString("mSelectId", mSafestId);
				bundle.putString("mSafestPrice", mSafestPrice);
				bundle.putInt("mTourNum", mSelect.size());
				bundle.putSerializable("mPeopleInfo", mPeopleInfo);
				bundle.putSerializable("mSelectUserSafe", (Serializable) mSelectUserSafe);
				intent.putExtras(bundle);
				startActivityForResult(intent, DefaultData.REQUEST_CODES);
			} else {
				openActivity(LoginActivity.class);
			}

		}
			break;
		// 提交订单
		case R.id.tv_submit:
			proLogicOrder();
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == DefaultData.REQUEST_CODE) {// 选择联系人
			if (resultCode == RESULT_OK) {
				List<CommonUserInfo.PeopleInfo> mSelect = (List<CommonUserInfo.PeopleInfo>) data.getSerializableExtra("mSelect");
				proLogicSelectUserInfo(mSelect);
			}

		} else if (requestCode == DefaultData.REQUEST_CODESS) {// 出行人
			if (resultCode == RESULT_OK) {
				List<CommonUserInfo.PeopleInfo> mSelect = (List<CommonUserInfo.PeopleInfo>) data.getSerializableExtra("mSelect");
				proLogicSelectUserInfo2(mSelect);
			}
		} else if (requestCode == DefaultData.REQUEST_CODES) {// 选择保险人
			if (resultCode == RESULT_OK) {
				mSafestId = data.getStringExtra("mSafestID");
				mSafestPrice = data.getStringExtra("mSafestPrice");
				mSafestName = data.getStringExtra("mSafestName");
				mSelectUserSafe.clear();
				mSelectUserSafe = (List<CommonUserInfo.PeopleInfo>) data.getSerializableExtra("mSelectUser");
				System.out.println("--mSelectUserSafe" + mSelectUserSafe.size());
				if (mSelectUserSafe != null && mSelectUserSafe.size() > 0) {
					tvInsureState.setText("已选择");
				} else {
					tvInsureState.setText("未选择");
				}
				if (mSafestName != null) {
					tvInsureName.setText(mSafestName);
				}
				if (mSafestPrice != null) {
					tvInsurePrice.setText("￥" + mSafestPrice);
				}
				proLogicPrice();
			}
		}
	}

	/**
	 * 处理选择的旅客
	 */
	private void proLogicSelectUserInfo(List<CommonUserInfo.PeopleInfo> mSelect) {
		try {
			if (mSelect != null && mSelect.size() > 0) {
				etName.setText(mSelect.get(0).Name);
				etName.setSelection(etName.getText().toString().trim().length());
				etPhone.setText(mSelect.get(0).Phone);
				etPhone.setSelection(etPhone.getText().toString().trim().length());
			} else {
				etName.setText("");
				etName.setText("");
				etPhone.setText("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理选择的旅客
	 */
	private void proLogicSelectUserInfo2(List<CommonUserInfo.PeopleInfo> mSelect) {
		adapter.setList(mSelect);
		this.mSelect = mSelect;
		proLogicPrice();
	}

	/**
	 * 计算总价
	 */
	private double proLogicPrice() {
		double priceTotle = 0;
		mTourPriceCR = 0;
		mTourPriceET = 0;
		etCount = 0;
		crCount = 0;
		try {
			// 1.旅游产品价格
			double crPrice = Double.valueOf(mTourInfo.Price);
			double etPrice = Double.valueOf(mTourInfo.BabyPrice);
			for (int i = 0; i < mSelect.size(); i++) {
				LogUtils.d("--选择" + mSelect.get(i).IsBaby);
				if (mSelect.get(i).IsBaby) {
					mTourPriceET += etPrice;
					etCount++;
				} else {
					mTourPriceCR += crPrice;
					crCount++;
				}
			}
			LogUtils.d("--选择价格--" + mTourPriceCR + "--" + mTourPriceET);
			// 2.保险价格
			SafestMoney = 0;
			// 3.参保人
			mPeopleID = "";
			if (isHasSafe) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < mSelectUserSafe.size(); i++) {
					SafestMoney += Double.valueOf(mSafestPrice);
					sb.append(mSelectUserSafe.get(i).ID + ",");
				}
				if (mSelectUserSafe.size() > 0) {
					mPeopleID = sb.toString().substring(0, sb.toString().length() - 1);
				}
			}
			// 3.单房差价格
			housePrice = isHouse ? Double.valueOf(mTourInfo.HotelPrice) : 0;
			priceTotle = mTourPriceCR + mTourPriceET + SafestMoney + housePrice;
			tvMoneyTotle.setText("￥" + ArithUtil.formatPrice(priceTotle));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return priceTotle;
	}

	/**
	 * 处理提交订单
	 */
	private void proLogicOrder() {
		if (AppUtils.isLogin(mContext)) {
			String name = etName.getText().toString().trim();
			String phone = etPhone.getText().toString().trim();
			// 联系人
			if (StringUtils.isEmpty(name)) {
				showToast("请填写或者选择联系人");
				return;
			}

			// 联系人手机号码
			if (StringUtils.isEmpty(phone)) {
				showToast("请填写或者选择联系人手机号码");
				return;
			} else {
				if (!RegExpUtils.isMobileNO(phone)) {
					showToast("联系人手机号码格式不正确");
					return;
				}
			}
			// 参保人数
			int safeCount = 0;
			// 保险金额
			double SafestMoney = 0;
			// 参保人
			if (isHasSafe) {
				for (int i = 0; i < mSelectUserSafe.size(); i++) {
					SafestMoney += Double.valueOf(mSafestPrice);
					safeCount++;
				}
			}
			// 出行人
			if (mSelect != null && mSelect.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < mSelect.size(); i++) {
					sb.append(mSelect.get(i).ID + ",");
				}
				if (mSelect.size() > 0) {
					mSelectID = sb.toString().substring(0, sb.toString().length() - 1);
				}
				requestByOrder(proLogicPrice(), housePrice, crCount, etCount, mTourPriceCR + mTourPriceET, safeCount, SafestMoney, mPeopleID, name, phone);
			} else {
				showToast("请选择出行人");
				return;
			}
		} else {
			openActivity(LoginActivity.class);
		}

	}

	// -----------------------网络请求-----------------------
	/**
	 * Info 网络请求
	 * 
	 */
	private void requestByInfo() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Time", mDate);
		paramDatas.put("PID", mPackageID);
		paramDatas.put("TouID", mTourID);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_DETAIL_ADDORDER, new NETConnection.SuccessCallback() {

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
				Tour2OrderInfo info = GsonUtils.json2T(result, Tour2OrderInfo.class);
				mTourInfo = info;
				if (info.People != null && info.People.size() > 0) {
					mPeopleInfo = info.People.get(0);
				}
				initTourInfo(info);
				initUserInfo(info.People);
				initInsure(info.Safest);
				proLogicPrice();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理选择的旅客
	 */
	private void initUserInfo(List<Tour2OrderInfo.PeopleInfo> mSelect) {
		try {
			if (mSelect != null && mSelect.size() > 0) {
				etName.setText(mSelect.get(0).PeoName);
				etName.setSelection(etName.getText().toString().trim().length());
				etPhone.setText(mSelect.get(0).Phone);
				etPhone.setSelection(etPhone.getText().toString().trim().length());
				mSelectUserSafe.add(new CommonUserInfo.PeopleInfo(mSelect.get(0).PeoID, mSelect.get(0).IDcard, true, mSelect.get(0).PeoName,
						mSelect.get(0).Phone));
			} else {
				mSelect.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 套餐信息
	 */
	private void initTourInfo(Tour2OrderInfo info) {
		try {
			if (info != null) {
				tvTitle.setText(info.TouTitle);
				tvPriceNow.setText(info.Price + "");
				tvPriceNow2.setText(info.BabyPrice + "");
				tvHouseMoney.setText("+￥" + info.HotelPrice);
				tvTimeInfo.setText(DateUtils.format2Date(info.GoTime, "yyyy年MM月dd日") + "出发　" + info.GoDay);
				if (!StringUtils.isEmpty(info.SetAdd)) {
					tvLoc.setText("出发地：" + info.SetAdd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保险信息
	 */
	private void initInsure(List<Tour2OrderInfo.SafestInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				tvInsureName.setText(info.get(0).Name);
				tvInsurePrice.setText("￥" + info.get(0).Money + "");
				mSafestId = info.get(0).ID;
				mSafestPrice = info.get(0).Money;
				if (mSelectUserSafe.size() > 0) {
					tvInsureState.setText("已选择");
					isHasSafe = true;
				} else {
					tvInsureState.setText("未选择");
					isHasSafe = false;
				}
			} else {
				isHasSafe = false;
				if (mSafestName != null) {
					tvInsureName.setText("没有相关的保险信息");
				}
				if (mSafestPrice != null) {
					tvInsurePrice.setText("￥0");
				}
				tvInsureState.setText("未选择");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Order 网络请求
	 * 
	 * @param UserID
	 *            用户ID
	 * @param TourID
	 *            旅游产品ID
	 * @param PriceSum
	 *            总价格
	 * @param HotelPrice
	 *            单房差
	 * @param TourCount
	 *            成人旅游产品数量
	 * @param TourBabyCount
	 *            儿童旅游产品数量
	 * @param TourPrice
	 *            旅游产品总价
	 * @param PeopleID
	 *            出行人ID集合，英文逗号分隔
	 * @param PID
	 *            旅游套餐ID
	 * @param RemoveTime
	 *            出行日期 使用日期 yyyy-MM-dd
	 * @param SafestID
	 *            保险ID
	 * @param SafCount
	 *            保险数量
	 * @param SafPrice
	 *            保险总价
	 * @param SafUserID
	 *            参保人ID集合，英文逗号分隔
	 * @param Name
	 *            联系人姓名
	 * @param Phone
	 *            联系人电话
	 * @param UserMessage
	 *            用户留言信息
	 */
	private void requestByOrder(double mPriceSum, double mHotelPrice, int mTourCount, int mTourBabyCount, double mTourPrice, int mSafCount, double mSafPrice,
			String mSafUserID, String mName, String mPhone) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("TouID", mTourID);
		paramDatas.put("PriceSum", mPriceSum);
		paramDatas.put("HotelPrice", mHotelPrice);
		paramDatas.put("TouCount", mTourCount);
		paramDatas.put("TouBabyCount", mTourBabyCount);
		paramDatas.put("TouPrice", mTourPrice);
		paramDatas.put("PeopleID", mSelectID);
		paramDatas.put("RemoveTime", mDate);
		paramDatas.put("PID", mPackageID);
		paramDatas.put("SafestID", mSafestId);
		paramDatas.put("SafCount", mSafCount);
		paramDatas.put("SafPrice", mSafPrice);
		paramDatas.put("SafUserID", mSafUserID);
		paramDatas.put("Name", mName);
		paramDatas.put("Phone", mPhone);
		paramDatas.put("UserMessage", etMsg.getText().toString().trim());
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_DETAIL_ORDER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonOrder(result);
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
	 * Order JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonOrder(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				Tour1OrderPayInfo info = GsonUtils.json2T(result, Tour1OrderPayInfo.class);
				// 付款总额
				String price = info.Order.get(0).PayableAmount;
				// 门票数量
				String soldCount = info.TouUser.size() + "";
				// 保险数量
				String safCount = info.TouSaf.size() + "";
				// 门票名称
				String soldName = info.TouOrder.get(0).TouTitle;
				// 订单ID
				String orderID = info.Order.get(0).OrderID;
				String ProID = info.Order.get(0).ProID;

				Bundle bundle = new Bundle();
				bundle.putString("price", price);
				bundle.putString("orderID", orderID);
				bundle.putString("mInfo", soldName + " " + soldCount + "个出行人 " + ((Integer.valueOf(safCount) > 0 ? (safCount + "张保险") : "")));
				bundle.putString("ProID", ProID);
				bundle.putInt("payType", 1); // 支付旅游
				openActivity(PayActivity.class, bundle);
			} else {
				showToast("提交订单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
