package com.highnes.tour.ui.fragment.main;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.beans.my.FindUserInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.PHPWebViewActivity;
import com.highnes.tour.ui.activities.my.CollectActivity;
import com.highnes.tour.ui.activities.my.CouponActivity;
import com.highnes.tour.ui.activities.my.InfoActivity;
import com.highnes.tour.ui.activities.my.LoginActivity;
import com.highnes.tour.ui.activities.my.OpinionActivity;
import com.highnes.tour.ui.activities.my.OrderListActivity;
import com.highnes.tour.ui.activities.my.PointActivity;
import com.highnes.tour.ui.activities.my.ServerActivity;
import com.highnes.tour.ui.activities.my.SettingsActivity;
import com.highnes.tour.ui.activities.my.common.CommonUserInfoActivity;
import com.highnes.tour.ui.activities.my.order.AllOrderActivity;
import com.highnes.tour.ui.fragment.base.BaseFragment;
import com.highnes.tour.utils.AppUtils;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 *    个人中心。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-04   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MyFragment extends BaseFragment {
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_MY = "com.highnes.tour.action_callback_my";
	public static boolean isLoad = true;
	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	// 信息
	private ImageView ivAvatar;

	// 显示手机号码
	private TextView tvInfo;
	// 气泡
	private TextView tvBbadge[] = new TextView[4];
	private LinearLayout llOrder[] = new LinearLayout[4];

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unregisterReceiver();
	}

	// 注销广播
	private void unregisterReceiver() {
		mContext.unregisterReceiver(updateReceiver);
	}

	/**
	 * 注册一个广播接收器
	 */
	private void registBoradcast() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CALLBACK_MY);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (isLoad) {
				isLoad = false;
				performHandlePostDelayed(0, 200);
				performHandlePostDelayed(1, 1000);
			}
		}

	};

	@Override
	protected @LayoutRes
	int getLayoutId() {
		return R.layout.fragment_tab_my;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		ivAvatar = getViewById(R.id.iv_avatar);
		tvInfo = getViewById(R.id.tv_info);
		tvBbadge[0] = getViewById(R.id.tv_dfk);
		tvBbadge[1] = getViewById(R.id.tv_dxf);
		tvBbadge[2] = getViewById(R.id.tv_dpj);
		tvBbadge[3] = getViewById(R.id.tv_tkz);
		llOrder[0] = getViewById(R.id.ll_dfk);
		llOrder[1] = getViewById(R.id.ll_dxf);
		llOrder[2] = getViewById(R.id.ll_dpj);
		llOrder[3] = getViewById(R.id.ll_tkz);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		initTitle("个人中心");
		initOptions(R.drawable.ic_avator_default);
		registBoradcast();
		initUserInfo();
	}

	@Override
	protected void setListener() {
		getViewById(R.id.iv_avatar).setOnClickListener(this);
		getViewById(R.id.ll_back).setOnClickListener(this);
		getViewById(R.id.ll_all_order).setOnClickListener(this);
		getViewById(R.id.ll_collect).setOnClickListener(this);
		getViewById(R.id.ll_point).setOnClickListener(this);
		getViewById(R.id.ll_money).setOnClickListener(this);
		getViewById(R.id.ll_info).setOnClickListener(this);
		getViewById(R.id.ll_cart).setOnClickListener(this);
		getViewById(R.id.ll_server).setOnClickListener(this);
		llOrder[0].setOnClickListener(this);
		llOrder[1].setOnClickListener(this);
		llOrder[2].setOnClickListener(this);
		llOrder[3].setOnClickListener(this);
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		if (AppUtils.isLogin(mContext)) {
			if (0 == type) {
				requestByUserInfo();
			} else {
				requestByOrderCount();
			}
		} else {
			initUserInfo();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 点击头像
		case R.id.iv_avatar:
			if (AppUtils.isLogin(mContext)) {
				openActivity(InfoActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;

		// 意见反馈
		case R.id.ll_back:
			if (AppUtils.isLogin(mContext)) {
				openActivity(OpinionActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 客服
		case R.id.ll_server:
			openActivity(ServerActivity.class);
			break;
		// 购物车
		case R.id.ll_cart:
			if (AppUtils.isLogin(mContext)) {
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 50001);
				bundle.putString("mUrl", UrlSettings.URL_PHP_H5_MY_CART+AppUtils.getTemp());
				openActivity(PHPWebViewActivity.class, bundle);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 全部订单
		case R.id.ll_all_order:
			if (AppUtils.isLogin(mContext)) {
				openActivity(AllOrderActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 收藏
		case R.id.ll_collect:
			if (AppUtils.isLogin(mContext)) {
				openActivity(CollectActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 积分
		case R.id.ll_point:
			if (AppUtils.isLogin(mContext)) {
				openActivity(PointActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 红包优惠券
		case R.id.ll_money:
			if (AppUtils.isLogin(mContext)) {
				openActivity(CouponActivity.class);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 常用信息
		case R.id.ll_info:
			if (AppUtils.isLogin(mContext)) {
				Bundle bundle = new Bundle();
				bundle.putBoolean("isSelect", false);
				bundle.putInt("count", -1);
				openActivity(CommonUserInfoActivity.class, bundle);
			} else {
				openActivity(LoginActivity.class);
			}
			break;
		// 设置
		case R.id.button_forward_img19:
		case R.id.button_forward_img:
			openActivity(SettingsActivity.class);
			break;
		// 待付款
		case R.id.ll_dfk: {
			Bundle bundle = new Bundle();
			bundle.putString("mType", "待付款");
			openActivity(OrderListActivity.class, bundle);
		}
			break;
		// 待消费
		case R.id.ll_dxf: {
			Bundle bundle = new Bundle();
			bundle.putString("mType", "待消费");
			openActivity(OrderListActivity.class, bundle);
		}
			break;
		// 待评论
		case R.id.ll_dpj: {
			Bundle bundle = new Bundle();
			bundle.putString("mType", "待评论");
			openActivity(OrderListActivity.class, bundle);
		}
			break;
		// 退款中
		case R.id.ll_tkz: {
			Bundle bundle = new Bundle();
			bundle.putString("mType", "退款中");
			openActivity(OrderListActivity.class, bundle);
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化用户信息
	 */
	private void initUserInfo() {
		if (AppUtils.isLogin(mContext)) {
			// 信息
			String nickname = SPUtils.get(mContext, PreSettings.USER_NICKNAME.getId(), PreSettings.USER_NICKNAME.getDefaultValue()).toString();
			tvInfo.setText(nickname);
			// 头像
			String avator = SPUtils.get(mContext, PreSettings.USER_AVATOR.getId(), PreSettings.USER_AVATOR.getDefaultValue()).toString();
			ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + avator, ivAvatar, options, null);
		} else {
			String avator = SPUtils.get(mContext, PreSettings.USER_AVATOR.getId(), PreSettings.USER_AVATOR.getDefaultValue()).toString();
			ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + avator, ivAvatar, options, null);
			// 信息
			tvInfo.setText("您未登录，请点击头像登录");
		}
	}

	/**
	 * 标题初始化
	 */
	private void initTitle(String title) {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		TextView tvTitle;
		ImageView ivSettings, ivMsg;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title19);
			ivSettings = (ImageView) vTitle.findViewById(R.id.button_forward_img19);
			ivMsg = (ImageView) vTitle.findViewById(R.id.button_down_img_right19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			tvTitle = (TextView) vTitle.findViewById(R.id.text_title);
			ivSettings = (ImageView) vTitle.findViewById(R.id.button_forward_img);
			ivMsg = (ImageView) vTitle.findViewById(R.id.button_down_img_right);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);
		}
		tvTitle.setText(title);
		tvTitle.setVisibility(View.VISIBLE);
		ivMsg.setVisibility(View.GONE);
		ivSettings.setVisibility(View.VISIBLE);
		ivMsg.setImageResource(R.drawable.ic_msg_white);
		ivSettings.setImageResource(R.drawable.ic_settings);
		ivSettings.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundResource(R.color.titlec);
	}

	// -----------------------网络请求-----------------------
	/**
	 * UserInfo 网络请求
	 */
	private void requestByUserInfo() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		new NETConnection(mContext, UrlSettings.URL_USER_FINDUSER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonUserInfo(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Login JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonUserInfo(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			FindUserInfo info = GsonUtils.json2T(result, FindUserInfo.class);
			// 登录状态
			SPUtils.put(mContext, PreSettings.USER_TYPE.getId(), 1);
			// 昵称
			SPUtils.put(mContext, PreSettings.USER_NICKNAME.getId(), info.data.get(0).UserName);
			// 姓名
			SPUtils.put(mContext, PreSettings.USER_NAME.getId(), info.data.get(0).Name);
			// 用户ID
			SPUtils.put(mContext, PreSettings.USER_ID.getId(), info.data.get(0).UserID);
			// 邮箱
			SPUtils.put(mContext, PreSettings.USER_EMAIL.getId(), info.data.get(0).Email);
			// 性别
			SPUtils.put(mContext, PreSettings.USER_SEX.getId(), info.data.get(0).getSex());
			// 头像
			SPUtils.put(mContext, PreSettings.USER_AVATOR.getId(), info.data.get(0).HeadImg);
			// 积分
			SPUtils.put(mContext, PreSettings.USER_POINT.getId(), info.data.get(0).Ponit);
			initUserInfo();
		}
	}

	/**
	 * UserInfo 网络请求
	 */
	private void requestByOrderCount() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_COUNT, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				parseJsonCount(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				// showToast(info);
			}
		}, paramDatas);
	}

	/**
	 * Login JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonCount(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			// 待付款
			int DfkCount = Integer.valueOf(JsonUtils.getString(result, "DfkCount", "0"));
			// 待消费
			int DxfCount = Integer.valueOf(JsonUtils.getString(result, "DxfCount", "0"));
			// 待评论
			int DplCount = Integer.valueOf(JsonUtils.getString(result, "DplCount", "0"));
			// 退款中
			int TkzCount = Integer.valueOf(JsonUtils.getString(result, "TkzCount", "0"));

			if (DfkCount > 0) {
				tvBbadge[0].setText(DfkCount + "");
				tvBbadge[0].setVisibility(View.VISIBLE);
			} else {
				tvBbadge[0].setVisibility(View.GONE);
			}
			if (DxfCount > 0) {
				tvBbadge[1].setText(DxfCount + "");
				tvBbadge[1].setVisibility(View.VISIBLE);
			} else {
				tvBbadge[1].setVisibility(View.GONE);
			}
			if (DplCount > 0) {
				tvBbadge[2].setText(DplCount + "");
				tvBbadge[2].setVisibility(View.VISIBLE);
			} else {
				tvBbadge[2].setVisibility(View.GONE);
			}
			if (TkzCount > 0) {
				tvBbadge[3].setText(TkzCount + "");
				tvBbadge[3].setVisibility(View.VISIBLE);
			} else {
				tvBbadge[3].setVisibility(View.GONE);
			}
		}
	}
}
