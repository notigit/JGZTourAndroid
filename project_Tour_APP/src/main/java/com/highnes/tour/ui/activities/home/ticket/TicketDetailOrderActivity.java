package com.highnes.tour.ui.activities.home.ticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.beans.home.ticket.TicketOrderInfo;
import com.highnes.tour.beans.home.ticket.TicketOrderPayInfo;
import com.highnes.tour.beans.my.CommonUserInfo;
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
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.datedialog.CalendarPickerView;
import com.highnes.tour.view.datedialog.CalendarPickerView.OnDateSelectedListener;

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
public class TicketDetailOrderActivity extends BaseTitleActivity {

	// 门票信息/价格
	private TextView tvTitle, tvPriceNow;
	// 取票人姓名/手机号码/身份证
	private TextView tvName, tvPhone, tvIDCard;
	// 当前的票数/当前选择的日期
	private TextView tvTicketNum, tvDate;
	// 当前的票数
	private int mTicketNum = 1;
	// 当前的门票id
	private String mSoldID;
	// 那个页面来的
	private String from;
	// 抢购的价格
	private String mNewPrice;
	// 结束时间
	private String mOverTime;
	// 当前的门票信息
	private TicketOrderInfo.SoldInfo mSoldInfo;
	// 当前的保险人
	private TicketOrderInfo.PeopleInfo mPeopleInfo;

	// 保险
	private TextView tvInsureName, tvInsurePrice, tvInsureState;
	// 当前的保险ID
	private String mSelectId = "";
	private String mSafestPrice = "";
	private String mSafestName = "";
	// 选择的保险人
	List<CommonUserInfo.PeopleInfo> mSelectUserSafe;

	// 日历
	private CalendarPickerView calendar;
	private AlertDialog theDialog;
	private CalendarPickerView dialogView;
	// 当前选择的日期
	private Date mDate;

	// 总金额、
	private TextView tvMoneyTotle;

	private double PriceSum;
	private String People = "";
	private double SafestMoney;

	// 剩余的门票
	private int ticketCount = 0;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_ticket_detail_order;
	}

	@Override
	protected void findViewById2T() {
		tvName = getViewById(R.id.tv_name);
		tvPhone = getViewById(R.id.tv_phone);
		tvIDCard = getViewById(R.id.tv_idcard);
		tvTicketNum = getViewById(R.id.tv_num);
		tvDate = getViewById(R.id.tv_date);
		tvTitle = getViewById(R.id.tv_item_title);
		tvPriceNow = getViewById(R.id.tv_price_now);
		tvInsureName = getViewById(R.id.tv_insure_name);
		tvInsurePrice = getViewById(R.id.tv_insure_price);
		tvInsureState = getViewById(R.id.tv_insure_state);
		tvMoneyTotle = getViewById(R.id.tv_money);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("订单填写");
		showBackwardView("", SHOW_ICON_DEFAULT);
		mSoldID = getIntent().getExtras().getString("mSoldID", "");
		from = getIntent().getExtras().getString("from", "");
		mNewPrice = getIntent().getExtras().getString("mNewPrice", "");
		mOverTime = getIntent().getExtras().getString("mOverTime", "");
		System.out.println("--mOverTime2-"+mOverTime);
		mSelectUserSafe = new ArrayList<CommonUserInfo.PeopleInfo>();
		initDate();
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_userinfo).setOnClickListener(this);
		getViewById(R.id.tv_cut).setOnClickListener(this);
		getViewById(R.id.tv_plus).setOnClickListener(this);
		getViewById(R.id.ll_date).setOnClickListener(this);
		getViewById(R.id.tv_item_info).setOnClickListener(this);
		getViewById(R.id.ll_insure).setOnClickListener(this);
		getViewById(R.id.tv_submit).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByInfo();
	}

	/**
	 * 初始化时间
	 */
	private void initDate() {
		String ymd;
		if (DateUtils.isGTRCurrentTime(1600)) {
			mDate = Calendar.getInstance().getTime();
			ymd = DateUtils.convertToDate(DateUtils.convert2longT(mDate));
			tvDate.setText(ymd + "　" + DateUtils.getWeek(ymd));
		} else {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
			mDate = c.getTime();
			ymd = DateUtils.convertToDate(DateUtils.convert2longT(mDate));
			tvDate.setText(ymd + "　" + DateUtils.getWeek(ymd));
		}
		requestByTicketCount(ymd);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 添加出行人
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
		// 减少票数
		case R.id.tv_cut:
			proLogicTicketNum(0);
			break;
		// 增加票数
		case R.id.tv_plus:
			proLogicTicketNum(1);
			break;
		// 日期
		case R.id.ll_date:
			proLogicTime();
			break;
		// 票型说明
		case R.id.tv_item_info: {
			// 票型说明
			Bundle bundle = new Bundle();
			bundle.putString("mTitle", "票型说明");
			bundle.putInt("mType", 1101);
			bundle.putString("mUrl", UrlSettings.URL_H5_HOME_TICKET_INFO + "STID=" + mSoldInfo.TID + "&SoldID=" + mSoldInfo.SID);
			openActivity(WebViewTitleActivity.class, bundle);
		}
			break;
		// 保险
		case R.id.ll_insure: {
			if (AppUtils.isLogin(mContext)) {
				Intent intent = new Intent(mContext, TicketInsureActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("mSoldID", mSoldID);
				bundle.putString("mSelectId", mSelectId);
				bundle.putInt("mSoldNum", mTicketNum);
				bundle.putString("mSafestPrice", mSafestPrice);
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
		if (requestCode == DefaultData.REQUEST_CODE) {
			// 选择出行人
			if (resultCode == RESULT_OK) {
				List<CommonUserInfo.PeopleInfo> mSelect = (List<CommonUserInfo.PeopleInfo>) data.getSerializableExtra("mSelect");
				proLogicSelectUserInfo(mSelect);
			}
		} else if (requestCode == DefaultData.REQUEST_CODES) {
			// 选择保险及参保人
			if (resultCode == RESULT_OK) {
				mSelectId = data.getStringExtra("mSafestID");
				mSafestPrice = data.getStringExtra("mSafestPrice");
				mSafestName = data.getStringExtra("mSafestName");
				// 判断显示
				mSelectUserSafe = (List<CommonUserInfo.PeopleInfo>) data.getSerializableExtra("mSelectUser");
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
				tvName.setText(mSelect.get(0).Name);
				tvPhone.setText(mSelect.get(0).Phone);
				tvIDCard.setText(mSelect.get(0).IDcard);
			} else {
				tvName.setText("");
				tvPhone.setText("");
				tvIDCard.setText("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 票数改变监听
	 * 
	 * @param type
	 *            0表示减少，1表示增加
	 */
	private void proLogicTicketNum(int type) {
		if (0 == type) {
			if (mTicketNum == 1) {
				showToast("最少选择一张票");
			} else {
				mTicketNum--;
			}
		} else {
			if (mTicketNum >= ticketCount) {
				showToast("没有更多的门票了");
				return;
			}
			mTicketNum++;
		}

		tvTicketNum.setText(mTicketNum + "");
		proLogicPrice();
	}

	/**
	 * 计算总价
	 */
	private void proLogicPrice() {
		try {
			// 门票价格
			PriceSum = mSoldInfo.NewPrice * mTicketNum;
			SafestMoney = 0;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mSelectUserSafe.size(); i++) {
				SafestMoney += Double.valueOf(mSafestPrice);
				sb.append(mSelectUserSafe.get(i).ID + ",");
			}
			// 参保人
			if (mSelectUserSafe.size() > 0) {
				People = sb.toString().substring(0, sb.toString().length() - 1);
			}
			tvMoneyTotle.setText("￥" + ArithUtil.formatPrice(PriceSum + SafestMoney));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 到店时间
	 */
	private void proLogicTime() {
		// 开始时间
		final Calendar lastYear = Calendar.getInstance();
		if (!DateUtils.isGTRCurrentTime(1600)) {
			// 第二天
			lastYear.add(Calendar.DAY_OF_MONTH, 1);
		}
		// 结束时间
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.MONTH, 1);

		dialogView = (CalendarPickerView) getLayoutInflater().inflate(R.layout.custom_date_dialog, null, false);
		dialogView.init(lastYear.getTime(), nextYear.getTime()).withSelectedDate(mDate == null ? new Date() : mDate);
		theDialog = new AlertDialog.Builder(mContext).setTitle("请选择日期").setView(dialogView).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				dialogInterface.dismiss();
			}
		}).setNeutralButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
				if (mDate != null) {
					String ymd = DateUtils.convertToDate(DateUtils.convert2longT(mDate));
					tvDate.setText(ymd + "　" + DateUtils.getWeek(ymd));
					requestByTicketCount(ymd);
				}

			}
		}).create();
		theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				dialogView.fixDialogDimens();
			}
		});
		// 选择了日期的监听
		dialogView.setOnDateSelectedListener(new OnDateSelectedListener() {

			@Override
			public void onDateUnselected(Date date) {
			}

			@Override
			public void onDateSelected(Date date) {
				mDate = date;
			}
		});
		theDialog.show();
	}

	/**
	 * 处理提交订单
	 */
	private void proLogicOrder() {
		if (AppUtils.isLogin(mContext)) {
			String name = tvName.getText().toString().trim();
			if (mTicketNum > ticketCount) {
				showToast("没有更多的门票了");
				return;
			}
			if (StringUtils.isEmpty(name)) {
				showToast("请选择取票人");
				return;
			}
			if (from.equals("time")) {
				requestByOrderTime(mOverTime);
			} else {
				requestByOrder();
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
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("SoldID", mSoldID);
		showLoading();
		String url = "";
		if (from.equals("time")) {
			paramDatas.put("NewPrice", mNewPrice);
			url = UrlSettings.URL_HOME_TIME_LIST_DETAIL_ORDER;
		} else {
			url = UrlSettings.URL_HOME_TICKET_DETAIL_INFO;
		}
		new NETConnection(mContext, url, new NETConnection.SuccessCallback() {

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
				TicketOrderInfo info = GsonUtils.json2T(result, TicketOrderInfo.class);
				mSoldInfo = info.sold.get(0);
				if (info.people != null && info.people.size() > 0) {
					mPeopleInfo = info.people.get(0);
				}
				initTicket(info.sold);
				initUserInfo(info.people);
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
	private void initUserInfo(List<TicketOrderInfo.PeopleInfo> mSelect) {
		try {
			if (mSelect != null && mSelect.size() > 0) {
				tvName.setText(mSelect.get(0).Name);
				tvPhone.setText(mSelect.get(0).Phone);
				tvIDCard.setText(mSelect.get(0).IDcard);
				mSelectUserSafe.add(new CommonUserInfo.PeopleInfo(mSelect.get(0).ID, mSelect.get(0).IDcard, true, mSelect.get(0).Name, mSelect.get(0).Phone));
			} else {
				mSelectUserSafe.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 门票信息
	 */
	private void initTicket(List<TicketOrderInfo.SoldInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				tvTitle.setText(info.get(0).SName);
				tvPriceNow.setText(info.get(0).NewPrice + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保险信息
	 */
	private void initInsure(List<TicketOrderInfo.SafestInfo> info) {
		try {
			if (info != null && info.size() > 0) {
				tvInsureName.setText(info.get(0).Name);
				tvInsurePrice.setText("￥" + info.get(0).Money + "");
				mSelectId = info.get(0).ID;
				mSafestPrice = info.get(0).Money;
				if (mSelectUserSafe.size() > 0) {
					tvInsureState.setText("已选择");
				} else {
					tvInsureState.setText("未选择");
				}
			} else {
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
	 */
	private void requestByOrder() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("SoldID", mSoldID);
		paramDatas.put("Count", mTicketNum);
		paramDatas.put("SCount", mSelectUserSafe.size());
		paramDatas.put("PriceSum", PriceSum);
		paramDatas.put("RemoveTime", DateUtils.convertToDate(DateUtils.convert2longT(mDate)));
		paramDatas.put("Name", tvName.getText().toString().trim());
		paramDatas.put("Phone", tvPhone.getText().toString().trim());
		paramDatas.put("IDcard", tvIDCard.getText().toString().trim());
		paramDatas.put("People", People);
		paramDatas.put("SafestID", mSelectId);
		paramDatas.put("SafestMoney", SafestMoney);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TICKET_DETAIL_ORDER, new NETConnection.SuccessCallback() {

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
			Log.e("TAG", "parseJsonOrder: "+JsonUtils.getString(result, "status", "0"));
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				TicketOrderPayInfo info = GsonUtils.json2T(result, TicketOrderPayInfo.class);
				// 付款总额
				String price = info.Order.get(0).PayableAmount;
				// 门票数量
				String soldCount = info.SoldOrder.get(0).SoldCount;
				// 门票数量
				String safCount = info.SoldOrder.get(0).SafCount;
				// 门票名称
				String soldName = info.sold.get(0).SoldName;
				// 订单ID
				String orderID = info.Order.get(0).OrderID;
				// 订单ID
				String ProID = info.Order.get(0).ProID;

				Bundle bundle = new Bundle();
				bundle.putString("price", price);
				bundle.putString("orderID", orderID);
				bundle.putString("ProID", ProID);
				bundle.putString("mInfo", soldName + " " + soldCount + "张门票 " + safCount + "张保险");
				if (from.equals("time")) {
					bundle.putInt("payType", 3); // 支付抢购
				} else {
					bundle.putInt("payType", 0); // 支付门票
				}
				openActivity(PayActivity.class, bundle);
			} else {
				showToast("提交订单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Order 网络请求
	 *
	 */
	private void requestByOrderTime(String OverTime) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("SoldID", mSoldID);
		paramDatas.put("Count", mTicketNum);
		paramDatas.put("SCount", mSelectUserSafe.size());
		paramDatas.put("PriceSum", PriceSum);
		paramDatas.put("RemoveTime", DateUtils.convertToDate(DateUtils.convert2longT(mDate)));
		paramDatas.put("Name", tvName.getText().toString().trim());
		paramDatas.put("Phone", tvPhone.getText().toString().trim());
		paramDatas.put("IDcard", tvIDCard.getText().toString().trim());
		paramDatas.put("People", People);
		paramDatas.put("SafestID", mSelectId);
		paramDatas.put("SafestMoney", SafestMoney);
		paramDatas.put("OverTime", DateUtils.formatDates(OverTime, "yyyy-MM-dd HH:mm:ss"));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TIME_LIST_DETAIL_ORDER_SUBMIT, new NETConnection.SuccessCallback() {

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
	 * Order 网络请求
	 * 
	 */
	private void requestByTicketCount(String time) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("SoldID", mSoldID);
		String url;
		if (from.equals("time")) {
			url = UrlSettings.URL_HOME_TIME_COUNT;
		} else {
			paramDatas.put("Time", time);
			url = UrlSettings.URL_HOME_TICKET_COUNT;
		}
		new NETConnection(mContext, url, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonTicketCount(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
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
	private void parseJsonTicketCount(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				ticketCount = Integer.valueOf(JsonUtils.getString(result, "points", "0"));
				if (ticketCount > 0) {
					String ymd = DateUtils.convertToDate(DateUtils.convert2longT(mDate));
					tvDate.setText(ymd + "　" + DateUtils.getWeek(ymd) + "　有票");
				} else {
					showToast("选择的日期无票");
					tvDate.setText(tvDate.getText().toString().trim() + "　无票");
					String ymd = DateUtils.convertToDate(DateUtils.convert2longT(mDate));
					tvDate.setText(ymd + "　" + DateUtils.getWeek(ymd) + "　无票");
				}
			} else {
				ticketCount = 0;
				showToast("选择的日期无票");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
