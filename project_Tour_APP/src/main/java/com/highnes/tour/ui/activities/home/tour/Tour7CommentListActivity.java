package com.highnes.tour.ui.activities.home.tour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.home.tour.Tour1CommentAdapter;
import com.highnes.tour.adapter.home.tour.Tour1CommentAdapter.OnItemCall;
import com.highnes.tour.adapter.home.tour.Tour7CommentAdapter;
import com.highnes.tour.beans.home.tour.Tour7DetailInfo;
import com.highnes.tour.beans.home.tour.Tour7DetailInfo.CommentInfo;
import com.highnes.tour.beans.home.tour.TourDetailInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.net.NETConnection;
import com.highnes.tour.ui.activities.ForumImageActivity;
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
 *    门票..评论..列表。
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
public class Tour7CommentListActivity extends BaseTitleActivity implements OnRefreshListener {
	private ListView lvList;
	private PullToRefreshLayout pull;
	private Tour7CommentAdapter adapter;
	// 没有数据的时候显示
	private LinearLayout llShowData;
	// 当前页数，最小为1
	private int page = 1;
	// 每页显示几条
	private int pagesize = 10;
	// 总页数
	private int pageTotle = 0;

	// 当前的类型
	private String mTourID;

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_find_list;
	}

	@Override
	protected void findViewById2T() {
		pull = getViewById(R.id.pull);
		lvList = getViewById(R.id.lv_list);
		llShowData = getViewById(R.id.ll_class_hint);
	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		mTourID = getIntent().getExtras().getString("mTourID", "");
		setTitle("评论列表");
		showBackwardView("", SHOW_ICON_DEFAULT);
		adapter = new Tour7CommentAdapter(mContext);
		lvList.setAdapter(adapter);

	}

	@Override
	protected void setListener2T() {
		pull.setOnRefreshListener(this);
		adapter.setOnItemCall(new Tour7CommentAdapter.OnItemCall() {

			@Override
			public void onCall(CommentInfo info, int position) {
				Bundle bundle = new Bundle();
				bundle.putInt("index", position);
				bundle.putString("title", info != null ? info.Contents : "");
				final List<String> list = new ArrayList<String>();
				String temp[] = info.ImageUrl.split(",");
				for (int i = 0; i < temp.length; i++) {
					list.add(UrlSettings.URL_IMAGE + temp[i]);
				}
				bundle.putStringArrayList("forumImages", (ArrayList<String>)list);
				openActivity(ForumImageActivity.class, bundle);
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
		paramDatas.put("id", mTourID);
		paramDatas.put("page", page);
		paramDatas.put("rows", pagesize);
		paramDatas.put("UserID", SPUtils.get(mContext, PreSettings.USER_ID.getId(), PreSettings.USER_ID.getDefaultValue()));
		showLoading();
		new NETConnection(mContext, UrlSettings.URL_HOME_TOUR_7_DETAIL, new NETConnection.SuccessCallback() {
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
				final Tour7DetailInfo info = GsonUtils.json2T(result, Tour7DetailInfo.class);
				int pages = Integer.valueOf(info.Count) / pagesize;
				pageTotle = Integer.valueOf(info.Count) % pagesize == 0 ? pages : pages + 1;
				if (info.Comment != null && info.Comment.size() > 0) {
					pull.refreshFinish(PullToRefreshLayout.SUCCEED);
					pull.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					if (1 == page) {
						adapter.removeAll();
						adapter.setList(info.Comment);
					} else {
						adapter.addAll(info.Comment);
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
