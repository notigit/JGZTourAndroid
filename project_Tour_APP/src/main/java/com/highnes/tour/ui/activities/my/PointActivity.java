package com.highnes.tour.ui.activities.my;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.my.PointAdapter;
import com.highnes.tour.beans.my.PointInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.WebViewTitleActivity;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    积分。
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
public class PointActivity extends BaseTitleActivity implements OnRefreshListener {
	private ListView lvList;
	private PullToRefreshLayout pull;
	// 没有数据的时候显示
	private LinearLayout llShowData;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;

	private PointAdapter adapter;
	private TextView tvPoint;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_point_list;
	}

	@Override
	protected void findViewById2T() {
		pull = getViewById(R.id.pull);
		lvList = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("积分");
		showBackwardView("", SHOW_ICON_DEFAULT);
		showForwardView("积分说明", SHOW_ICON_GONE);
		View view = View.inflate(mContext, R.layout.include_my_point_head, null);
		tvPoint = (TextView) view.findViewById(R.id.tv_point);
		lvList.addHeaderView(view);
		adapter = new PointAdapter(mContext);
		lvList.setAdapter(adapter);
	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
		performHandlePostDelayed(0, 100);
	}

	@Override
	protected void onForward(View forwardView) {
		super.onForward(forwardView);
		Bundle bundle = new Bundle();
		bundle.putInt("mType", 5002);
		bundle.putString("mTitle", "积分说明");
		bundle.putString("mUrl", UrlSettings.URL_H5_USER_POIN_INFO);
		openActivity(WebViewTitleActivity.class, bundle);
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		page = 1;
		requestByPoint();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		requestByPoint();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		if (page + 1 <= pageTotle) {
			++page;
			requestByPoint();
		} else {
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
		}

	}

	/**
	 * 积分 网络请求
	 * 
	 */
	private void requestByPoint() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()).toString());
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_USER_POINT, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonPoint(result);
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
	private void parseJsonPoint(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final PointInfo info = GsonUtils.json2T(result, PointInfo.class);
				int pages = Integer.valueOf(info.PCount) / pagesize;
				pageTotle = Integer.valueOf(info.PCount) % pagesize == 0 ? pages : pages + 1;
				tvPoint.setText(info.SumCount);
				if (info.UserPoint != null && info.UserPoint.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						adapter.removeAll();
						adapter.setList(info.UserPoint);
					} else {
						adapter.addAll(info.UserPoint);
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

}
