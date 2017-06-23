package com.highnes.tour.ui.activities.home;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.HomeTimesListAdapter;
import com.highnes.tour.beans.find.FindListInfo;
import com.highnes.tour.beans.home.HomeTimeListInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.find.FindDetailActivity;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    抢购..列表。
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
public class TimeListActivity extends BaseTitleActivity implements OnRefreshListener {
	private ListView lvList;
	private PullToRefreshLayout pull;
	private HomeTimesListAdapter adapter;
	// 没有数据的时候显示
	private LinearLayout llShowData;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;

	// 当前的类型
	private String mTypeID;
	// 标题
	private String mTitle;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_time_list;
	}

	@Override
	protected void findViewById2T() {
		pull = getViewById(R.id.pull);
		lvList = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("限时抢购");
		showBackwardView("", SHOW_ICON_DEFAULT);
		adapter = new HomeTimesListAdapter(mContext);
		lvList.setAdapter(adapter);
	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HomeTimeListInfo.BenefitInfo info = (HomeTimeListInfo.BenefitInfo) lvList.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mSoldID", info.SoldID);
				bundle.putString("mID", info.ID);
				bundle.putString("mNewPrice", info.NewPrice + "");
				bundle.putString("mOverTime", info.Odate);
				openActivity(TimeDetailActivity.class, bundle);
			}
		});

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
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByData() {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("City", SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()));
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TIME_LIST, new NETConnection.SuccessCallback() {

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
				llShowData.setVisibility(View.VISIBLE);
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
	private void parseJsonData(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final HomeTimeListInfo info = GsonUtils.json2T(result, HomeTimeListInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Benefit != null && info.Benefit.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						adapter.removeAll();
						adapter.setList(info.Benefit);
					} else {
						adapter.addAll(info.Benefit);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					adapter.removeAll();
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
}
