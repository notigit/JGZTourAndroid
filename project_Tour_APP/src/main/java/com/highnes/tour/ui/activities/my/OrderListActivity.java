package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.OrderListAdapter;
import com.highnes.tour.beans.my.OrderListInfo;
import com.highnes.tour.beans.my.OrderListInfo.OrderInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.PHPWebViewActivity;
import com.highnes.tour.ui.activities.PayActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.home.TimeDetailActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailActivity;
import com.highnes.tour.ui.activities.my.order.AddCommentActivity;
import com.highnes.tour.ui.activities.my.order.OrderDetailActivity;
import com.highnes.tour.ui.activities.my.order.PHPAddCommentActivity;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    订单..列表。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class OrderListActivity extends BaseTitleActivity implements OnRefreshListener {
	private ListView lvList;
	private PullToRefreshLayout pull;
	private OrderListAdapter adapter;
	// 没有数据的时候显示
	private LinearLayout llShowData;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;

	// 当前的类型
	private String mType;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_order_list;
	}

	@Override
	protected void findViewById2T() {
		pull = getViewById(R.id.pull);
		lvList = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mType = getIntent().getExtras().getString("mType", "");
		setTitle(mType);
		showBackwardView("", SHOW_ICON_DEFAULT);
		adapter = new OrderListAdapter(mContext, mType);
		lvList.setAdapter(adapter);
	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		adapter.setmOnItemCall(new OrderListAdapter.OnItemCall() {

			@Override
			public void onItem(int type, OrderListInfo.OrderInfo info) {
				switch (type) {
				// item
				case 0:
					// 跳转各自的详情页面,判断类型即可
					proItem(info);
					break;
				// 第一个按钮
				case 1:
					// 查看订单等操作 ,判断类型即可
					proOrder(info);
					break;
				// 第二个按钮
				case 2:
					// 业务处理等操作，判断类型和状态。
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
	private void proItem(OrderListInfo.OrderInfo info) {
		if ("门票".equals(info.Type)) {
			Bundle bundle = new Bundle();
			bundle.putString("mAreaID", JsonUtils.getString(info.Body, "AreaID", ""));
			openActivity(TicketDetailActivity.class, bundle);
		} else if ("旅游".equals(info.Type)) {
			Bundle bundle = new Bundle();
			bundle.putString("mTourID", JsonUtils.getString(info.Body, "TouID", ""));
			openActivity(Tour1DetailActivity.class, bundle);
		} else if ("野营".equals(info.Type)) {
			Bundle bundle = new Bundle();
			bundle.putString("mTourID", JsonUtils.getString(info.Body, "CamID", ""));
			openActivity(Tour7DetailActivity.class, bundle);
		} else if ("抢购".equals(info.Type)) {
			Bundle bundle = new Bundle();
			bundle.putString("mSoldID", JsonUtils.getString(info.Body, "SoldID", ""));
			bundle.putString("mID", JsonUtils.getString(info.Body, "mID", ""));
			bundle.putString("mNewPrice", JsonUtils.getString(info.Body, "NewPrice", ""));
			// 此处要注意时间的格式，时间中间有T字符
			bundle.putString("mOverTime", JsonUtils.getString(info.Body, "OTime", ""));
			openActivity(TimeDetailActivity.class, bundle);
		} else {
			// PHP 跳转
			Bundle bundle = new Bundle();
			bundle.putInt("mType", 60001);
			bundle.putString("mUrl", info.Body);
			openActivity(PHPWebViewActivity.class, bundle);
		}
	}

	/**
	 * 订单处理1的个按钮
	 * 
	 * @param info
	 */
	private void proOrder(final OrderListInfo.OrderInfo info) {
		if ("待付款".equals(info.Status)) {
			// 取消订单
			showDialog("是否删除订单？", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (2 == which) {
						if ("周边".equals(info.Type)) {
							// PHP
							requestByOrderCancelPHP(info);
						} else {
							// NET
							requestByOrderCancel(info);
						}
					}
				}
			});

		} else {// 查看订单
			if ("周边".equals(info.Type)) {
				// PHP
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 60001);
				bundle.putString("mUrl", info.Body);
				openActivity(PHPWebViewActivity.class, bundle);
			} else {
				// NET
				Bundle bundle = new Bundle();
				bundle.putString("mOrderID", info.ID);
				openActivity(OrderDetailActivity.class, bundle);
			}

		}
	}

	/**
	 * 订单处理2的个按钮
	 * 
	 * @param info
	 */
	private void proHandler(final OrderListInfo.OrderInfo info) {
		if ("待评论".equals(info.Status)) {
			if ("周边".equals(info.Type)) {
				// PHP
				Bundle bundle = new Bundle();
				bundle.putString("mId", info.ID);
				bundle.putString("mImage", info.ImgUrl);
				bundle.putString("mPrice", info.Money + "");
				bundle.putString("mTitle", info.Title);
				openActivity(PHPAddCommentActivity.class, bundle);
			} else {
				// NET
				Bundle bundle = new Bundle();
				bundle.putString("mOrderID", info.ID);
				openActivity(AddCommentActivity.class, bundle);
			}
		} else if ("待消费".equals(info.Status)) {
			if ("周边".equals(info.Type)) {
				// PHP
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 60001);
				bundle.putString("mUrl", info.Body);
				openActivity(PHPWebViewActivity.class, bundle);
			} else
				// 时间等因素判断
				showDialog("是否立即申请退款？", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (2 == which) {

							// NET
							requestByOrderRefundsOrder(info);
						}
					}
				});
		} else if ("待付款".equals(info.Status)) {// 去付款
			if ("周边".equals(info.Type)) {
				// PHP
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 60001);
				bundle.putString("mUrl", info.Body);
				openActivity(PHPWebViewActivity.class, bundle);
			} else {
				// NET
				Bundle bundle = new Bundle();
				bundle.putString("price", info.Money + "");
				bundle.putString("orderID", info.ID);
				bundle.putString("mInfo", info.Title);
				bundle.putString("ProID", JsonUtils.getString(info.Body, "ProID", ""));
				/** 支付类别 0-门票，1-旅游，2-野营，3-抢购 */
				if ("门票".equals(info.Type)) {
					bundle.putInt("payType", 0);
				} else if ("旅游".equals(info.Type)) {
					bundle.putInt("payType", 1);
				} else if ("野营".equals(info.Type)) {
					bundle.putInt("payType", 2);
				} else if ("抢购".equals(info.Type)) {
					bundle.putInt("payType", 3);
				}
				openActivity(PayActivity.class, bundle);
			}
		}
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 200);

	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		requestByData();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		requestByData();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		if (page + 1 <= pageTotle) {
			++page;
			requestByData();
		} else {
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
		}

	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByData() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("status", mType);
		paramDatas.put("rows", pagesize);
		paramDatas.put("page", page);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_QUERY, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonData(result);
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
	private void parseJsonData(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final OrderListInfo info = GsonUtils.json2T(result, OrderListInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Order != null && info.Order.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						adapter.removeAll();
						adapter.setList(info.Order);
					} else {
						adapter.addAll(info.Order);
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrderCancel(final OrderInfo info) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("OrderID", info.ID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_CANCEL, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonCancel(result, info);
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
	private void parseJsonCancel(String result, OrderInfo info) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				// 删除成功
				showToast("删除成功");
				adapter.remove(info);
			} else {
				showToast("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Data 网络请求
	 * 
	 */
	private void requestByOrderRefundsOrder(final OrderInfo info) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("OrderID", info.ID);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_ORDER_REFUNDS, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonRefunds(result, info);
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
	private void parseJsonRefunds(String result, OrderInfo info) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				showToast("申请退款成功");
				adapter.remove(info);
			} else {
				showToast(JsonUtils.getString(result, "points", "申请退款失败"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求订单 PHP
	 * 
	 * @param types
	 * @param state
	 */
	private void requestByOrderCancelPHP(final OrderInfo info) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("id", info.ID);
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
	private void parseJsonOrderCancel(String result, OrderInfo info) {
		try {
			if (0 == JsonUtils.getInt(result, "errcode", -1)) {
				// 处理取消订单状态
				adapter.remove(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
