package com.highnes.tour.ui.activities.my.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.OrderDetailAdapter;
import com.highnes.tour.beans.my.OrderDetailInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.JsonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    订单详情。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-10-18   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class OrderDetailActivity extends BaseTitleActivity {

	// 订单id
	private String mOrderID;
	private ListView lvList;
	private OrderDetailAdapter adapter;

	private ImageView ivImg, ivState;
	private TextView tvTitle, tvPrice, tvCode;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_order_detail;
	}

	@Override
	protected void findViewById2T() {
		lvList = getViewById(R.id.lv_list);
		ivImg = getViewById(R.id.iv_item_img);
		tvTitle = getViewById(R.id.tv_title);
		tvPrice = getViewById(R.id.tv_price);
		ivState = getViewById(R.id.iv_state);
		tvCode = getViewById(R.id.tv_code);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mOrderID = getIntent().getExtras().getString("mOrderID", "");
		setTitle("订单详情");
		showBackwardView("", SHOW_ICON_DEFAULT);

		adapter = new OrderDetailAdapter(mContext);
		lvList.setAdapter(adapter);
		initOptions();

	}

	@Override
	protected void setListener2T() {
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByOrder();
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrder() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("OrderID", mOrderID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_DETAIL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonGroup(result);
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
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonGroup(String result) {
		try {
			int status = Integer.valueOf(JsonUtils.getString(result, "status", "-1"));
			switch (status) {
			// 门票
			case 0:
				proTicket(result);
				break;
			// 旅游
			case 1:
				proTour(result);
				break;
			// 野营
			case 2:
				proYeying(result);
				break;
			// 抢购
			case 3:
				proQianggou(result);
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 门票处理
	 * 
	 * @param result
	 */
	private void proTicket(String result) {
		String ActualAmount = JsonUtils.getString(result, "ActualAmount", "");
		String GoodsName = JsonUtils.getString(result, "GoodsName", "");
		String ImageUrl = JsonUtils.getString(result, "ImageUrl", "");
		String PayableAmount = JsonUtils.getString(result, "PayableAmount", "");
		String Phone = JsonUtils.getString(result, "Phone", "");
		String RemoveTime = JsonUtils.getString(result, "RemoveTime", "");
		String SafestName = JsonUtils.getString(result, "SafestName", "");
		String SfCount = JsonUtils.getString(result, "SfCount", "");
		String SfPrice = JsonUtils.getString(result, "SfPrice", "");
		String SoldPriceSum = JsonUtils.getString(result, "SoldPriceSum", "");
		String SoldeCount = JsonUtils.getString(result, "SoldeCount", "");
		String Time = JsonUtils.getString(result, "Time", "");
		String UserName = JsonUtils.getString(result, "UserName", "");
		String VoucherAmount = JsonUtils.getString(result, "VoucherAmount", "");
		String anarchy = JsonUtils.getString(result, "anarchy", "");
		String status = JsonUtils.getString(result, "status", "");
		String SoldNum = JsonUtils.getString(result, "SoldNum", "");


		tvTitle.setText(GoodsName+" "+SoldeCount+"份");
		tvPrice.setText("￥" + ActualAmount);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + ImageUrl, ivImg, options, null);

		if ("待消费".equals(anarchy)) {
			ivState.setImageResource(R.drawable.ic_order_use_no);
			tvCode.setText(SoldNum);
		} else {
			ivState.setImageResource(R.drawable.ic_order_use_ed);
			tvCode.setText("**** ****");
		}

		// 列表
		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
		list.add(new OrderDetailInfo("保险", SafestName + " " + SfCount + "份"));
		list.add(new OrderDetailInfo("购买日期", Time));
		list.add(new OrderDetailInfo("出行日期", RemoveTime));
		list.add(new OrderDetailInfo("取票人", UserName));
		list.add(new OrderDetailInfo("取票人手机号码", Phone.trim()));
		adapter.setList(list);
	}

	/**
	 * 旅游处理
	 * 
	 * @param result
	 */
	private void proTour(String result) {
		String ActualAmount = JsonUtils.getString(result, "ActualAmount", "");
		String Count = JsonUtils.getString(result, "Count", "");
		String GoodsName = JsonUtils.getString(result, "GoodsName", "");
		String ImageUrl = JsonUtils.getString(result, "ImageUrl", "");
		String OrderID = JsonUtils.getString(result, "OrderID", "");
		String PayableAmount = JsonUtils.getString(result, "PayableAmount", "");
		String Phone = JsonUtils.getString(result, "Phone", "");
		String RemoveTime = JsonUtils.getString(result, "RemoveTime", "");
		String SafestName = JsonUtils.getString(result, "SafestName", "");
		String SfCount = JsonUtils.getString(result, "SfCount", "");
		String SfPrice = JsonUtils.getString(result, "SfPrice", "");
		String SoldPriceSum = JsonUtils.getString(result, "SoldPriceSum", "");
		String Time = JsonUtils.getString(result, "Time", "");
		String TouID = JsonUtils.getString(result, "TouID", "");
		String UserName = JsonUtils.getString(result, "UserName", "");
		String VoucherAmount = JsonUtils.getString(result, "VoucherAmount", "");
		String anarchy = JsonUtils.getString(result, "anarchy", "");
		String status = JsonUtils.getString(result, "status", "");
		String TouNum = JsonUtils.getString(result, "TouNum", "");


		tvTitle.setText(GoodsName+" "+Count+"份");
		tvPrice.setText("￥" + ActualAmount);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + ImageUrl, ivImg, options, null);

		if ("待消费".equals(anarchy)) {
			ivState.setImageResource(R.drawable.ic_order_use_no);
			tvCode.setText(TouNum);
		} else {
			ivState.setImageResource(R.drawable.ic_order_use_ed);
			tvCode.setText("**** ****");
		}

		// 列表
		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
		list.add(new OrderDetailInfo("保险", SafestName + " " + SfCount + "份"));
		list.add(new OrderDetailInfo("购买日期", Time));
		list.add(new OrderDetailInfo("出行日期", RemoveTime));
		list.add(new OrderDetailInfo("取票人", UserName));
		list.add(new OrderDetailInfo("取票人手机号码", Phone.trim()));
		adapter.setList(list);
	}

	/**
	 * 野营处理
	 * 
	 * @param result
	 */
	private void proYeying(String result) {
		String ActualAmount = JsonUtils.getString(result, "ActualAmount", "");
		String CamID = JsonUtils.getString(result, "CamID", "");
		String Count = JsonUtils.getString(result, "Count", "");
		String GoodsName = JsonUtils.getString(result, "GoodsName", "");
		String ImageUrl = JsonUtils.getString(result, "ImageUrl", "");
		String OrderID = JsonUtils.getString(result, "OrderID", "");
		String PayableAmount = JsonUtils.getString(result, "PayableAmount", "");
		String Phone = JsonUtils.getString(result, "Phone", "");
		String RemoveTime = JsonUtils.getString(result, "RemoveTime", "");
		String SoldPriceSum = JsonUtils.getString(result, "SoldPriceSum", "");
		String Time = JsonUtils.getString(result, "Time", "");
		String UserName = JsonUtils.getString(result, "UserName", "");
		String VoucherAmount = JsonUtils.getString(result, "VoucherAmount", "");
		String anarchy = JsonUtils.getString(result, "anarchy", "");
		String status = JsonUtils.getString(result, "status", "");
		String CamNum = JsonUtils.getString(result, "CamNum", "");

		tvTitle.setText(GoodsName+" "+Count+"份");
		tvPrice.setText("￥" + ActualAmount);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + ImageUrl, ivImg, options, null);

		if ("待消费".equals(anarchy)) {
			ivState.setImageResource(R.drawable.ic_order_use_no);
			tvCode.setText(CamNum);
		} else {
			ivState.setImageResource(R.drawable.ic_order_use_ed);
			tvCode.setText("**** ****");
		}

		// 列表
		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
		list.add(new OrderDetailInfo("购买日期", Time));
		list.add(new OrderDetailInfo("出行日期", RemoveTime));
		list.add(new OrderDetailInfo("取票人", UserName));
		list.add(new OrderDetailInfo("取票人手机号码", Phone.trim()));
		adapter.setList(list);
	}

	/**
	 * 抢购处理
	 * 
	 * @param result
	 */
	private void proQianggou(String result) {
		String ActualAmount = JsonUtils.getString(result, "ActualAmount", "");
		String GoodsName = JsonUtils.getString(result, "GoodsName", "");
		String ImageUrl = JsonUtils.getString(result, "ImageUrl", "");
		String PayableAmount = JsonUtils.getString(result, "PayableAmount", "");
		String Phone = JsonUtils.getString(result, "Phone", "");
		String RemoveTime = JsonUtils.getString(result, "RemoveTime", "");
		String SafestName = JsonUtils.getString(result, "SafestName", "");
		String SfCount = JsonUtils.getString(result, "SfCount", "");
		String SfPrice = JsonUtils.getString(result, "SfPrice", "");
		String SoldPriceSum = JsonUtils.getString(result, "SoldPriceSum", "");
		String SoldeCount = JsonUtils.getString(result, "SoldeCount", "");
		String Time = JsonUtils.getString(result, "Time", "");
		String UserName = JsonUtils.getString(result, "UserName", "");
		String VoucherAmount = JsonUtils.getString(result, "VoucherAmount", "");
		String anarchy = JsonUtils.getString(result, "anarchy", "");
		String status = JsonUtils.getString(result, "status", "");
		String SoldNum = JsonUtils.getString(result, "SoldNum", "");

		tvTitle.setText(GoodsName+" "+SoldeCount+"份");
		tvPrice.setText("￥" + ActualAmount);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + ImageUrl, ivImg, options, null);

		if ("待消费".equals(anarchy)) {
			ivState.setImageResource(R.drawable.ic_order_use_no);
			tvCode.setText(SoldNum);
		} else {
			ivState.setImageResource(R.drawable.ic_order_use_ed);
			tvCode.setText("**** ****");
		}

		// 列表
		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
		list.add(new OrderDetailInfo("保险", SafestName + " " + SfCount + "份"));
		list.add(new OrderDetailInfo("购买日期", Time));
		list.add(new OrderDetailInfo("出行日期", RemoveTime));
		list.add(new OrderDetailInfo("取票人", UserName));
		list.add(new OrderDetailInfo("取票人手机号码", Phone.trim()));
		adapter.setList(list);
	}

}
