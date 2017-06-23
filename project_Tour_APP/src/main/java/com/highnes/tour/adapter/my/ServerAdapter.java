package com.highnes.tour.adapter.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.my.ServerInfo;

/**
 * <PRE>
 * 作用:
 * 		客服。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-06-29   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ServerAdapter extends BaseListAdapter<ServerInfo.DataInfo> {

	public ServerAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_my_server, null);
			holder.tvCentent = (TextView) view.findViewById(R.id.tv_item_centent);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_item_time);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			holder.tvZan = (TextView) view.findViewById(R.id.tv_agree);
			holder.tvLook = (TextView) view.findViewById(R.id.tv_look);
			holder.ivZan = (ImageView) view.findViewById(R.id.iv_item_zan);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		ServerInfo.DataInfo info = (ServerInfo.DataInfo) getItem(position);
		// 设置页面显示值
		holder.tvName.setText(info.Title);
		if ("没登陆".equals(info.IsThing)) {
			holder.ivZan.setImageResource(R.drawable.ic_zan_g);
		} else if ("True".equals(info.IsThing)) {
			holder.ivZan.setImageResource(R.drawable.ic_zan_red);
		} else {
			holder.ivZan.setImageResource(R.drawable.ic_zan_g);
		}
		holder.tvZan.setText(info.Tcount);
		if (position == getCount() - 1 && null != mOnLastItem) {
			mOnLastItem.onLastItem();
		}
		return view;
	}

	private class ViewHolder {
		private TextView tvCentent;
		private TextView tvTime;
		private TextView tvName;
		private TextView tvZan;
		private TextView tvLook;

		private ImageView ivZan;
	}

}
