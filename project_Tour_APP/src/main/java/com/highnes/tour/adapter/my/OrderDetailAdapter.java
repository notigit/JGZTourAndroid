package com.highnes.tour.adapter.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.my.OrderDetailInfo;

/**
 * <PRE>
 * 作用:
 *    在线报警 --今日报警列表。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-05-22   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class OrderDetailAdapter extends BaseListAdapter<OrderDetailInfo> {

	public OrderDetailAdapter(Context paramContext) {
		super(paramContext);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			listCell = new ListCell();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_device_detail, null);
			listCell.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
			listCell.tvValue = (TextView) convertView.findViewById(R.id.tv_item_value);
			convertView.setTag(listCell);
		} else {
			listCell = (ListCell) convertView.getTag();
		}
		// 绑定消息到列表中。
		OrderDetailInfo items = (OrderDetailInfo) getItem(position);
		listCell.tvTitle.setText(items.title);
		listCell.tvValue.setText(items.value);
		return convertView;
	}

	/**
	 * 封装消息项。
	 * 
	 * @author FUNY
	 * 
	 */
	private static class ListCell {
		private TextView tvTitle;
		private TextView tvValue;
	}

	ListCell listCell;
}
