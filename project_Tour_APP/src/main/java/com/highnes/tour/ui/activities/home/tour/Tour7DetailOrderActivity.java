package com.highnes.tour.ui.activities.home.tour;

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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.beans.home.tour.Tour7OrderPayInfo;
import com.highnes.tour.beans.my.CommonUserInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.PayActivity;
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
public class Tour7DetailOrderActivity extends BaseTitleActivity {

	// 门票信息/价格
	private TextView tvTitle, tvPriceNow;
	// 取票人姓名/手机号码/留言信息
	private EditText etName, etPhone, etMsg;
	// 当前的票数/当前选择的日期
	private TextView tvTourNum, tvDate;
	// 当前的票数
	private int mTourNum = 1;
	// 当前的门票id
	private String mTourID;
	// 日历
	private CalendarPickerView calendar;
	private AlertDialog theDialog;
	private CalendarPickerView dialogView;
	// 当前选择的日期
	private Date mDate;

	// 总金额、
	private TextView tvMoneyTotle;
	// 单价
	private String mNewPrice;
	private double PriceSum;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_tour_7_detail_order;
	}

	@Override
	protected void findViewById2T() {
		etName = getViewById(R.id.et_name);
		etPhone = getViewById(R.id.et_phone);
		etMsg = getViewById(R.id.et_message);
		tvTourNum = getViewById(R.id.tv_num);
		tvDate = getViewById(R.id.tv_date);
		tvTitle = getViewById(R.id.tv_title);
		tvPriceNow = getViewById(R.id.tv_price_now);
		tvMoneyTotle = getViewById(R.id.tv_money);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("订单填写");
		showBackwardView("", SHOW_ICON_DEFAULT);
		mTourID = getIntent().getExtras().getString("mTourID", "");
		initDate();
	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_userinfo).setOnClickListener(this);
		getViewById(R.id.tv_cut).setOnClickListener(this);
		getViewById(R.id.tv_plus).setOnClickListener(this);
		getViewById(R.id.ll_date).setOnClickListener(this);
		getViewById(R.id.ll_insure).setOnClickListener(this);
		getViewById(R.id.tv_submit).setOnClickListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		// requestByInfo();
		requestByOrder1();
	}

	/**
	 * 初始化时间
	 */
	private void initDate() {
		if (DateUtils.isGTRCurrentTime(1600)) {
			mDate = Calendar.getInstance().getTime();
			String ymd = DateUtils.convertToDate(DateUtils.convert2longT(mDate));
			tvDate.setText(ymd + "　" + DateUtils.getWeek(ymd));
		} else {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
			mDate = c.getTime();
			String ymd = DateUtils.convertToDate(DateUtils.convert2longT(mDate));
			tvDate.setText(ymd + "　" + DateUtils.getWeek(ymd));
		}
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
			if (resultCode == RESULT_OK) {
				List<CommonUserInfo.PeopleInfo> mSelect = (List<CommonUserInfo.PeopleInfo>) data.getSerializableExtra("mSelect");
				proLogicSelectUserInfo(mSelect);
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
			if (mTourNum == 1) {
				showToast("最少选择一张票");
			} else {
				mTourNum--;
			}
		} else {
			mTourNum++;
		}
		tvTourNum.setText(mTourNum + "");
		proLogicPrice();
	}

	/**
	 * 计算总价
	 */
	private void proLogicPrice() {
		try {
			// 门票价格
			PriceSum = Double.valueOf(mNewPrice) * mTourNum;
			tvMoneyTotle.setText("￥" + ArithUtil.formatPrice(PriceSum));
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
			String name = etName.getText().toString().trim();
			String phone = etPhone.getText().toString().trim();
			if (StringUtils.isEmpty(name)) {
				showToast("请选择或者输入联系人");
				return;
			}
			if (StringUtils.isEmpty(phone)) {
				showToast("请选择或者输入联系人手机号码");
				return;
			}
			requestByOrder2(name, phone);
		} else {
			openActivity(LoginActivity.class);
		}

	}

	// -----------------------网络请求-----------------------

	/**
	 * Order 网络请求
	 * 
	 * @param UserID
	 *            用户ID
	 * 
	 */
	private void requestByOrder1() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("id", mTourID);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_7_DETAIL_ADDORDER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonOrder1(result);
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
	 * 收藏 JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonOrder1(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				String mID = JsonUtils.getString(result, "ID", "");
				String mName = JsonUtils.getString(result, "Name", "");
				mNewPrice = JsonUtils.getString(result, "NewPrice", "");
				String mPhone = JsonUtils.getString(result, "Phone", "");
				String mTypeName = JsonUtils.getString(result, "TypeName", "");
				String mUserName = JsonUtils.getString(result, "UserName", "");
				tvTitle.setText(mName);
				tvPriceNow.setText(mNewPrice);
				etName.setText(mUserName);
				etName.setSelection(mUserName.length());
				etPhone.setText(mPhone);
				etPhone.setSelection(mPhone.length());
				proLogicPrice();
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Order 网络请求
	 * 
	 */
	private void requestByOrder2(String Name, String Phone) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("PriceSum", PriceSum);
		paramDatas.put("Count", mTourNum);
		paramDatas.put("CamID", mTourID);
		paramDatas.put("UserMessage", etMsg.getText().toString().trim());
		paramDatas.put("RemoveTime", DateUtils.convertToDate(DateUtils.convert2longT(mDate)));
		paramDatas.put("Name", Name);
		paramDatas.put("Phone", Phone);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_7_DETAIL_ORDERSURE, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonOrder2(result);
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
	private void parseJsonOrder2(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				Tour7OrderPayInfo info = GsonUtils.json2T(result, Tour7OrderPayInfo.class);
				// 付款总额
				String price = info.Price;
				// 门票数量
				String soldCount = info.Count;
				// 保险数量
				String safCount = "";
				// 门票名称
				String soldName = info.CamName;
				// 订单ID
				String orderID = info.OrderID;
				String ProID = info.ProID;

				Bundle bundle = new Bundle();
				bundle.putString("price", price);
				bundle.putString("orderID", orderID);
				bundle.putString("ProID", ProID);
				bundle.putString("mInfo", soldName + " " + soldCount + "件 ");
				bundle.putInt("payType", 2); // 支付野营
				openActivity(PayActivity.class, bundle);
			} else {
				showToast("提交订单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
