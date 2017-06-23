package com.highnes.tour.ui.activities.my.order;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.PHPOrderAdapter;
import com.highnes.tour.adapter.my.PHPOrderAdapter.OnItemCall;
import com.highnes.tour.adapter.my.PHPOrderSortAdapter;
import com.highnes.tour.beans.my.PHPOrderInfo;
import com.highnes.tour.beans.my.PHPOrderInfo.DataInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.PHPWebViewActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    PHP订单。
 * 注意事项:
 *    .。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-13   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PHPOrderListActivity extends BaseTitleActivity implements OnRefreshListener {
	// 是否加载
	public static boolean isLoad = true;
	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK = "com.highnes.tour.action_callback";
	// 当前的订单名称
	private String mName;

	private PullToRefreshLayout pull;
	private PHPOrderAdapter adapter;
	private ListView lvOrder;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;
	// 没有数据的时候显示
	private LinearLayout llShowData;

	// -------------------智能排序---------------
	private TextView tvMenu;
	private PopupWindow pwSort;
	private ListView lvSort;
	private PHPOrderSortAdapter sortAdapter;
	// 类型（1、土特产；2：酒店；3：美食；4：休闲娱乐）
	private String currTypes = "1";
	// 订单状态(-1.已删除。0.已关闭,。1.待支付。 2.待确认。3.待发货。4.已发货。5.待评论。
	// 6.待使用。7.待收货。8.已完成。9.申请退货。10.待退款。11.拒绝退款。12.退款完成）
	private String currState = "1";

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		intentFilter.addAction(ACTION_CALLBACK);
		mContext.registerReceiver(updateReceiver, intentFilter);
	}

	/**
	 * 自定义广播接收器，用于显示未读数
	 */
	private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		}

	};

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_php_order_list;
	}

	@Override
	protected void findViewById2T() {
		tvMenu = getViewById(R.id.tv_menu);
		pull = getViewById(R.id.pull);
		lvOrder = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mName = getIntent().getExtras().getString("name", "订单");
		currTypes = getIntent().getExtras().getString("types", "1");
		setTitle(mName + "订单");
		showBackwardView("", SHOW_ICON_DEFAULT);
		registBoradcast();

		adapter = new PHPOrderAdapter(mContext);
		lvOrder.setAdapter(adapter);

	}

	@Override
	protected void setListener2T() {
		getViewById(R.id.ll_menu).setOnClickListener(this);
		pull.setOnRefreshListener(this);

		adapter.setmOnItemCall(new OnItemCall() {

			@Override
			public void onItem(int type, DataInfo info) {
				switch (type) {
				// item
				case 0:
					// 跳转各自的详情页面,判断类型即可
					proItem(info);
					break;
				// 第一个按钮
				case 1:
					// 业务处理等操作，判断类型和状态。
					proOrder(info);
					break;
				// 第二个按钮
				case 2:
					// 查看订单等操作 ,判断类型即可
					proHandler(info);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 处理跳转的页面详情
	 * 
	 * @param info
	 */
	private void proItem(DataInfo info) {
		Bundle bundle = new Bundle();
		bundle.putInt("mType", 60001);
		bundle.putString("mUrl", info.url);
		openActivity(PHPWebViewActivity.class, bundle);
	}

	/**
	 * 订单处理1的个按钮
	 * 
	 * @param info
	 */
	private void proOrder(final DataInfo info) {
		int state = Integer.valueOf(info.state);
		switch (state) {
		// 删除订单
		case 1:
			requestByOrderCancel(info);
			break;
		// 取消订单
		case 2:
			requestByOrderCancel(info);
			break;
		// 去评论
		case 5: {
			Bundle bundle = new Bundle();
			bundle.putString("mId", info.id);
			bundle.putString("mImage", info.imgsrc);
			bundle.putString("mPrice", info.totalmoney);
			bundle.putString("mTitle", info.title);
			openActivity(PHPAddCommentActivity.class, bundle);
		}
			break;
		// 申请退款
		case 8: {
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 60001);
			bundle.putString("mUrl", info.url);
			openActivity(PHPWebViewActivity.class, bundle);
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 订单处理2的个按钮
	 * 
	 * @param info
	 */
	private void proHandler(final DataInfo info) {
		int state = Integer.valueOf(info.state);
		if (1 == state) {
			// 去付款
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 60001);
			bundle.putString("mUrl", info.url);
			openActivity(PHPWebViewActivity.class, bundle);
		} else {
			// 查看订单
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 60001);
			bundle.putString("mUrl", info.url);
			openActivity(PHPWebViewActivity.class, bundle);
		}
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		requestByOrder(currTypes, currState);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 菜单
		case R.id.ll_menu:
			dismissPwAll();
			displaySort(v);
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		requestByOrder(currTypes, currState);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		if (page + 1 <= pageTotle) {
			++page;
			requestByOrder(currTypes, currState);
		} else {
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
		}

	}

	/**
	 * 关闭所有的pw
	 */
	private void dismissPwAll() {
		// 修改字体颜色和箭头的方向
		Drawable navDown = getResources().getDrawable(R.drawable.ic_menu_down);
		navDown.setBounds(0, 0, navDown.getMinimumWidth(), navDown.getMinimumHeight());
		tvMenu.setCompoundDrawables(null, null, navDown, null);
		tvMenu.setTextColor(ResHelper.getColorById(mContext, R.color.titlec));
		Drawable navUp = getResources().getDrawable(R.drawable.ic_menu_up);
		navUp.setBounds(0, 0, navUp.getMinimumWidth(), navUp.getMinimumHeight());
		tvMenu.setCompoundDrawables(null, null, navUp, null);
		if (pwSort != null && pwSort.isShowing()) {
			pwSort.dismiss();
		}
	}

	/**
	 * 判断显示排序
	 */
	private void displaySort(View view) {
		try {
			if (pwSort == null) {
				initpwSort();
				pwSort.showAsDropDown(view, 0, 0);
				sortAdapter = new PHPOrderSortAdapter(mContext);
				lvSort.setAdapter(sortAdapter);
				String[] stringLeft = getResources().getStringArray(R.array.php_order_state);
				sortAdapter.setList(Arrays.asList(stringLeft));
				if (sortAdapter.getCount() > 0) {
					sortAdapter.setIndex(0);
				}
			} else {
				if (pwSort.isShowing()) {
					pwSort.dismiss();
				} else {
					pwSort.showAsDropDown(view, 0, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 初始化显示智能排序菜单
	 */
	private void initpwSort() {
		try {
			// 获取自定义布局文件pop.xml的视图
			View customView = getLayoutInflater().inflate(R.layout.pw_ticket_menu_list, null, false);
			/** 在这里可以实现自定义视图的功能 */
			lvSort = (ListView) customView.findViewById(R.id.lv_type);
			customView.findViewById(R.id.ll_single).setVisibility(View.VISIBLE);
			customView.findViewById(R.id.ll_double).setVisibility(View.GONE);
			// 创建PopupWindow实例
			pwSort = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			pwSort.setAnimationStyle(R.style.AnimationFade);
			// 自定义view添加触摸事件
			customView.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (pwSort != null && pwSort.isShowing()) {
						pwSort.dismiss();
					}
					return false;
				}
			});
			lvSort.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					sortAdapter.setIndex(position);
					currState = ((String) lvSort.getAdapter().getItem(position)).split(",")[1];
					tvMenu.setText(((String) lvSort.getAdapter().getItem(position)).split(",")[0]);
					requestByOrder(currTypes, currState);
					pwSort.dismiss();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求订单
	 * 
	 * @param types
	 *            （1、土特产；2：酒店；3：美食；4：休闲娱乐）
	 * @param state
	 *            订单状态(-1.已删除。0.已关闭,。1.待支付。 2.待确认。3.待发货。4.已发货。5.待评论。
	 *            6.待使用。7.待收货。8.已完成。9.申请退货。10.待退款。11.拒绝退款。12.退款完成）
	 */
	private void requestByOrder(String types, String state) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("userid", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("types", types);
		paramDatas.put("state", state);
		paramDatas.put("page", page);
		paramDatas.put("limit", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_PHP_ORDER, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonGroup(result);
			}
		}, new NETConnection.FailCallback() {

			@Override
			public void onFail(String info) {
				stopLoading();
				pull.refreshFinish(PullToRefreshLayout.FAIL);
				pull.loadmoreFinish(PullToRefreshLayout.FAIL);
				showToast(info);
				llShowData.setVisibility(View.VISIBLE);
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
			if (0 == JsonUtils.getInt(result, "errcode", -1)) {
				final PHPOrderInfo info = GsonUtils.json2T(result, PHPOrderInfo.class);
				int pages = Integer.valueOf(info.count) / pagesize;
				pageTotle = Integer.valueOf(info.count) % pagesize == 0 ? pages : pages + 1;
				if (info.data != null && info.data.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						adapter.removeAll();
						adapter.setList(info.data);
					} else {
						adapter.addAll(info.data);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					adapter.removeAll();
					// 显示如果没有数据的时候的处理
					llShowData.setVisibility(View.VISIBLE);
				}
			} else {
				pull.refreshFinish(PullToRefreshLayout.NO_DATA);
				pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
				llShowData.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求订单
	 * 
	 * @param types
	 * @param state
	 */
	private void requestByOrderCancel(final DataInfo info) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("id", info.id);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_PHP_ORDER_CANCEL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonOrderCancel(result, info);
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
	private void parseJsonOrderCancel(String result, DataInfo info) {
		try {
			if (0 == JsonUtils.getInt(result, "errcode", -1)) {
				// 处理取消订单状态
				adapter.remove(info);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
