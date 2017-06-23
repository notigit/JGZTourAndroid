package com.highnes.tour.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.MsgInfo;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.ResHelper;

/**
 * <PRE>
 * 作用:
 * 		消息列表。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-07-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MsgAdapter extends BaseListAdapter<MsgInfo.NoticeInfo> {

	public MsgAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_msg, null);
			holder.tvCentent = (TextView) view.findViewById(R.id.tv_item_centent);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_item_time);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			holder.view =  view.findViewById(R.id.view_icon);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		MsgInfo.NoticeInfo info = (MsgInfo.NoticeInfo) getItem(position);
		// 设置页面显示值
		holder.tvCentent.setText(info.Title);
		holder.tvTime.setText(DateUtils.formatDates(info.Time, "yyyy/MM/dd HH:mm:ss"));
		if (info.IsRead) {
			holder.tvName.setText("已读消息");
			holder.view.setBackgroundResource(R.color.xml_bg_green);
		} else {
			holder.tvName.setText("未读消息");
			holder.view.setBackgroundResource(R.color.xml_bg_red);
		}

		if (position == getCount() - 1 && null != mOnLastItem) {
			mOnLastItem.onLastItem();
		}
		return view;
	}

	private class ViewHolder {
		private TextView tvCentent;
		private TextView tvTime;
		private TextView tvName;
		private View view;
	}

}
