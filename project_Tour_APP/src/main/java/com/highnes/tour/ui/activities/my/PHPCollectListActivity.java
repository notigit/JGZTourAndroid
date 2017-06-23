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
import com.highnes.tour.adapter.my.PHPCollectAdapter;
import com.highnes.tour.beans.my.CollectTicketInfo;
import com.highnes.tour.beans.my.CollectTourInfo;
import com.highnes.tour.beans.my.CollectYeyingInfo;
import com.highnes.tour.beans.my.PHPCollectInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.PHPWebViewActivity;
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
public class PHPCollectListActivity extends BaseTitleActivity implements OnRefreshListener {
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

	// 当前的类型 1土特产、2酒店、3美食、4休闲娱乐
	private int mType;
	// 标题
	private String mTitle;

	private PHPCollectAdapter adapter;

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
		mType = getIntent().getExtras().getInt("mType", 1);
		mTitle = getIntent().getExtras().getString("mTitle", "收藏");
		setTitle(mTitle);
		showBackwardView("", SHOW_ICON_DEFAULT);
		adapter = new PHPCollectAdapter(mContext);
		lvList.setAdapter(adapter);

	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PHPCollectInfo.DataInfo info = (PHPCollectInfo.DataInfo) lvList.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putInt("mType", 50002);
				bundle.putString("mUrl", info.url);
				openActivity(PHPWebViewActivity.class, bundle);
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
		paramDatas.put("userid", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		paramDatas.put("page", page);
		paramDatas.put("limit", pagesize);
		paramDatas.put("types", mType);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_PHP_COLLECT, new NETConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				stopLoading();
				parseJsonTicket(result);
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
			if (0== JsonUtils.getInt(result, "errcode", -1)) {
				final PHPCollectInfo info = GsonUtils.json2T(result, PHPCollectInfo.class);
				int pages = Integer.valueOf(info.total) / pagesize;
				pageTotle = Integer.valueOf(info.total) % pagesize == 0 ? pages : pages + 1;
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
