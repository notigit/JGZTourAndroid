package com.highnes.tour.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.HomeNewsInfo;
import com.highnes.tour.utils.DateUtils;

/**
 * <PRE>
 * 作用:
 * 		首页..新闻。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-06-17   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class HomeNewsAdapter extends BaseListAdapter<HomeNewsInfo.DataInfo> {

	public HomeNewsAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_news_more, null);
			holder.tvCentent = (TextView) view.findViewById(R.id.tv_item_centent);
			holder.tv_look = (TextView) view.findViewById(R.id.tv_look);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HomeNewsInfo.DataInfo info = (HomeNewsInfo.DataInfo) getItem(position);
		// 设置页面显示值
		holder.tvCentent.setText(info.Title);
		holder.tv_look.setText(info.LookCount + "");
		if (position == getCount() - 1 && null != mOnLastItem) {
			mOnLastItem.onLastItem();
		}
		return view;
	}

	private class ViewHolder {
		private TextView tvCentent;
		private TextView tv_look;
	}

}
