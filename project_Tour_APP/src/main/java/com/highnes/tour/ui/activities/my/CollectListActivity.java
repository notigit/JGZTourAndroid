package com.highnes.tour.ui.activities.my;

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
import com.highnes.tour.adapter.my.CollectTicketAdapter;
import com.highnes.tour.adapter.my.CollectTourAdapter;
import com.highnes.tour.adapter.my.CollectYeyingAdapter;
import com.highnes.tour.beans.my.CollectTicketInfo;
import com.highnes.tour.beans.my.CollectTourInfo;
import com.highnes.tour.beans.my.CollectYeyingInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;
import com.highnes.tour.ui.activities.home.ticket.TicketDetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour1DetailActivity;
import com.highnes.tour.ui.activities.home.tour.Tour7DetailActivity;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    发现..列表。
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
public class CollectListActivity extends BaseTitleActivity implements OnRefreshListener {
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

	// 当前的类型 0门票、1旅游、2野营
	private int mType;
	// 标题
	private String mTitle;

	private CollectTicketAdapter ticketAdapter;
	private CollectTourAdapter tourAdapter;
	private CollectYeyingAdapter yeyingAdapter;

	@Override
	protected void onResume() {
		super.onResume();
		performHandlePostDelayed(0, 200);
	}

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_my_collect_list;
	}

	@Override
	protected void findViewById2T() {
		pull = getViewById(R.id.pull);
		lvList = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mType = getIntent().getExtras().getInt("mType", 0);
		mTitle = getIntent().getExtras().getString("mTitle", "收藏");
		setTitle(mTitle);
		showBackwardView("", SHOW_ICON_DEFAULT);
		if (0 == mType) {
			ticketAdapter = new CollectTicketAdapter(mContext);
			lvList.setAdapter(ticketAdapter);
		} else if (1 == mType) {
			tourAdapter = new CollectTourAdapter(mContext);
			lvList.setAdapter(tourAdapter);
		} else if (2 == mType) {
			yeyingAdapter = new CollectYeyingAdapter(mContext);
			lvList.setAdapter(yeyingAdapter);
		}

	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (0 == mType) {
					CollectTicketInfo.SoldkeepInfo info = (CollectTicketInfo.SoldkeepInfo) lvList.getAdapter().getItem(position);
					Bundle bundle = new Bundle();
					bundle.putString("mAreaID", info.AreaID);
					openActivity(TicketDetailActivity.class, bundle);
				} else if (1 == mType) {
					CollectTourInfo.TourismkeepInfo info = (CollectTourInfo.TourismkeepInfo) lvList.getAdapter().getItem(position);
					Bundle bundle = new Bundle();
					bundle.putString("mTourID", info.TourismID);
					openActivity(Tour1DetailActivity.class, bundle);
				} else if (2 == mType) {
					CollectYeyingInfo.CamkeepInfo info = (CollectYeyingInfo.CamkeepInfo) lvList.getAdapter().getItem(position);
					Bundle bundle = new Bundle();
					bundle.putString("mTourID", info.CampingID);
					openActivity(Tour7DetailActivity.class, bundle);
				}
			}
		});

	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		page = 1;
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
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		String url = "";
		if (0 == mType) {
			url = UrlSettings.URL_USER_COLLECT_LIST_TICKET;
		} else if (1 == mType) {
			url = UrlSettings.URL_USER_COLLECT_LIST_TOUR;
		} else if (2 == mType) {
			url = UrlSettings.URL_USER_COLLECT_LIST_YEYING;
		}
		new NETConnection(mContext, url, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				if (0 == mType) {
					parseJsonTicket(result);
				} else if (1 == mType) {
					parseJsonTour(result);
				} else if (2 == mType) {
					parseJsonYeying(result);
				}
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
	private void parseJsonTicket(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final CollectTicketInfo info = GsonUtils.json2T(result, CollectTicketInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Soldkeep != null && info.Soldkeep.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						ticketAdapter.removeAll();
						ticketAdapter.setList(info.Soldkeep);
					} else {
						ticketAdapter.addAll(info.Soldkeep);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					ticketAdapter.removeAll();
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
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonTour(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final CollectTourInfo info = GsonUtils.json2T(result, CollectTourInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Tourismkeep != null && info.Tourismkeep.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						tourAdapter.removeAll();
						tourAdapter.setList(info.Tourismkeep);
					} else {
						tourAdapter.addAll(info.Tourismkeep);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					tourAdapter.removeAll();
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
	 * Data JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonYeying(String result) {
		try {
			if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
				final CollectYeyingInfo info = GsonUtils.json2T(result, CollectYeyingInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Camkeep != null && info.Camkeep.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						yeyingAdapter.removeAll();
						yeyingAdapter.setList(info.Camkeep);
					} else {
						yeyingAdapter.addAll(info.Camkeep);
					}
					llShowData.setVisibility(View.GONE);
				} else {
					pull.refreshFinish(PullToRefreshLayout.NO_DATA);
					pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
					yeyingAdapter.removeAll();
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
