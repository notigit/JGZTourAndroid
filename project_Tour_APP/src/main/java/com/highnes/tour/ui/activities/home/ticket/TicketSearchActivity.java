package com.highnes.tour.ui.activities.home.ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.ticket.TicketFilterListAdapter;
import com.highnes.tour.adapter.home.ticket.TicketSearchHisAdapter;
import com.highnes.tour.beans.CityInfo;
import com.highnes.tour.beans.home.ticket.TicketFilterListInfo;
import com.highnes.tour.beans.home.ticket.TicketFilterListInfo.SoldInfo;
import com.highnes.tour.conf.DefaultData;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.base.BaseActivity;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.utils.GsonUtils;
import com.highnes.tour.utils.JsonUtils;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;
import com.highnes.tour.utils.StringUtils;
import com.highnes.tour.view.gridview.MyGridView;
import com.highnes.tour.view.pull.PullToRefreshLayout;
import com.highnes.tour.view.pull.PullToRefreshLayout.OnRefreshListener;

/**
 * <PRE>
 * 作用:
 *    首页..门票..搜索。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-29   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketSearchActivity extends BaseActivity implements OnRefreshListener {
	// 标题
	private LinearLayout llTitle;
	private View vTitle;
	private TextView tvSearch;
	// 轮播图
	private ListView lvRoot;
	private TicketFilterListAdapter ticketAdapter;
	private int maxH;
	private PullToRefreshLayout pull;

	// 搜索的文本框
	private EditText etSearch;
	private View viewHis;
	private LinearLayout llHis;
	private MyGridView gvHis;
	private TicketSearchHisAdapter hisAdapter;

	// 没有数据的时候显示
	private LinearLayout llShowData;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 20;
	// 总页数
	private int pageTotle = 0;

	// 当前的文字内容
	private String textSearch = "";

	@Override
	@LayoutRes
	protected int getLayoutId() {
		return R.layout.activity_home_ticket_search;
	}

	@Override
	protected void findViewById() {
		llTitle = getViewById(R.id.ll_title);
		pull = getViewById(R.id.pull);
		lvRoot = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		// 滑动透明标题的高度
		maxH = DensityUtils.dip2px(mContext, 150);
		initTitle();

		viewHis = View.inflate(mContext, R.layout.include_city_his, null);
		llHis = (LinearLayout) viewHis.findViewById(R.id.root);
		gvHis = (MyGridView) viewHis.findViewById(R.id.gv_his);
		lvRoot.addHeaderView(viewHis);

		ticketAdapter = new TicketFilterListAdapter(mContext);
		lvRoot.setAdapter(ticketAdapter);

		hisAdapter = new TicketSearchHisAdapter(mContext);
		gvHis.setAdapter(hisAdapter);
		List<String> listData = SPUtils.getList(mContext, DefaultData.LIST_TICKET_SEARCH_HIS_INFO);
		if (listData == null) {
			listData = new ArrayList<String>();
		}
		llHis.setVisibility(listData.size() > 0 ? View.VISIBLE : View.GONE);
		hisAdapter.setList(listData);

	}

	@Override
	protected void setListener() {
		setTicketListener();
		pull.setOnRefreshListener(this);
		/**
		 * 点击历史的城市
		 */
		gvHis.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				textSearch = (String) gvHis.getAdapter().getItem(position);
				putCity2His(textSearch);
				requestByTicket(textSearch);
			}
		});
	}

	@Override
	protected void processLogic(Bundle savedInstanceState) {
		textSearch = SPUtils.get(mContext, PreSettings.USER_CITY.getId(), PreSettings.USER_CITY.getDefaultValue()).toString();
		requestByTicket(textSearch);
	}

	/**
	 * 设置推荐列表的点击事件
	 */
	private void setTicketListener() {
		lvRoot.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TicketFilterListInfo.SoldInfo info = (SoldInfo) lvRoot.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putString("mAreaID", info.AreaID);
				openActivity(TicketDetailActivity.class, bundle);
			}
		});
	}

	// ---------------- 历史 -------------------

	/**
	 * 把当前选择的城市追加到历史的列表中
	 */
	private void putCity2His(String info) {
		try {
			List<String> listData = SPUtils.getList(mContext, DefaultData.LIST_TICKET_SEARCH_HIS_INFO);
			if (listData == null) {
				listData = new ArrayList<String>();
			}
			int infoIndex = -1;
			for (int i = 0; i < listData.size(); i++) {
				if (listData.get(i).equals(info)) {
					infoIndex = i;
					break;
				}
			}
			if (-1 != infoIndex) {
				listData.remove(infoIndex);
			}
			listData.add(0, info);
			if (listData.size() > 6) {
				// 只保留6个,移除最后一个
				listData.remove(listData.size() - 1);
			}
			SPUtils.putList(mContext, DefaultData.LIST_TICKET_SEARCH_HIS_INFO, listData);
			// 保存完后立即刷新列表
			performHandlePostDelayed(0, 500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void performHandlePostDelayedCallBack(int type) {
		super.performHandlePostDelayedCallBack(type);
		loadHisCity();
	}

	/**
	 * 加载最近使用
	 */
	private void loadHisCity() {
		List<String> listData = SPUtils.getList(mContext, DefaultData.LIST_TICKET_SEARCH_HIS_INFO);
		if (listData == null) {
			listData = new ArrayList<String>();
		}
		// 保存完后立即刷新列表
		llHis.setVisibility(listData.size() > 0 ? View.VISIBLE : View.GONE);
		hisAdapter.setList(listData);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 返回
		case R.id.button_backward_img19:
		case R.id.button_backward_img:
			finishActivity();
			break;
		// 搜索
		case R.id.button_forward_txt19:
		case R.id.button_forward_txt:
			String txt = etSearch.getText().toString().trim();
			if (StringUtils.isEmpty(txt)) {
				showToast("请输入查询的关键字");
				return;
			}
			textSearch = txt;
			putCity2His(textSearch);
			requestByTicket(textSearch);
			break;

		default:
			break;
		}
	}

	/**
	 * 标题初始化
	 */
	private void initTitle() {
		boolean isShow = setFullStatusBar();
		llTitle.removeAllViews();
		RelativeLayout root;
		ImageView ivBack;
		LinearLayout llSearch;
		if (isShow) {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_v19_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root_v19);
			llTitle.addView(vTitle);
			tvSearch = (TextView) vTitle.findViewById(R.id.button_forward_txt19);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search3_19);
			etSearch = (EditText) vTitle.findViewById(R.id.et_search19);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img19);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 68F);
			llTitle.setLayoutParams(p);
		} else {
			vTitle = View.inflate(mContext, R.layout.layout_titlebar_plus, null);
			root = (RelativeLayout) vTitle.findViewById(R.id.root);
			llTitle.addView(vTitle);
			tvSearch = (TextView) vTitle.findViewById(R.id.button_forward_txt);
			llSearch = (LinearLayout) vTitle.findViewById(R.id.ll_search3);
			etSearch = (EditText) vTitle.findViewById(R.id.et_search);
			ivBack = (ImageView) vTitle.findViewById(R.id.button_backward_img);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			p.height = DensityUtils.dip2px(mContext, 50F);
			llTitle.setLayoutParams(p);
		}
		tvSearch.setText("搜索");
		tvSearch.setTextColor(ResHelper.getColorById(mContext, R.color.white));
		tvSearch.setVisibility(View.VISIBLE);
		tvSearch.setOnClickListener(this);
		ivBack.setVisibility(View.VISIBLE);
		llSearch.setVisibility(View.VISIBLE);
		ivBack.setOnClickListener(this);
		// 设置背景透明度
		root.setBackgroundColor(Color.argb(255, 72, 211, 194));
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		requestByTicket(textSearch);

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		if (page + 1 <= pageTotle) {
			++page;
			requestByTicket(textSearch);
		} else {
			pull.loadmoreFinish(PullToRefreshLayout.NO_DATA);
		}

	}

	/**
	 * Ticket 网络请求
	 * 
	 * @param isCurrCity
	 *            true表示当前城市
	 * 
	 */
	private void requestByTicket(String text) {
		Map<String, Object> paramDatas = new HashMap<String, Object>();
		paramDatas.put("Text", text);
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TICKET_SEARCH, new NETConnection.SuccessCallback() {

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
				// 显示如果没有数据的时候的处理
				llShowData.setVisibility(View.VISIBLE);
			}
		}, paramDatas);
	}

	/**
	 * Ticket JSON信息
	 * 
	 * @param result
	 *            JSON
	 */
	private void parseJsonTicket(String result) {
		if (StringUtils.isEquals("1", JsonUtils.getString(result, "status", "0"))) {
			final TicketFilterListInfo info = GsonUtils.json2T(result, TicketFilterListInfo.class);
			int pages = Integer.valueOf(info.Count) / pagesize;
			pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
			if (info.sold != null && info.sold.size() > 0) {
				pull.refreshFinish(PullToRefreshLayout.SUCCEED);
				pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				if (1 == page) {
					ticketAdapter.removeAll();
					ticketAdapter.setList(info.sold);
				} else {
					ticketAdapter.addAll(info.sold);
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

	}
}
