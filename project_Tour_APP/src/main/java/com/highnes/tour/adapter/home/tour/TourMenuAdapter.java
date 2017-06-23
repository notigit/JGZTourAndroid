package com.highnes.tour.adapter.home.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.ticket.TicketFilterInfo;
import com.highnes.tour.utils.ResHelper;

/**
 * <PRE>
 * 作用:
 * 		旅游度假..菜单。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-02-20   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TourMenuAdapter extends BaseListAdapter<String> {
	// 当前选择的Item
	private int index = 0;

	public void setIndex(int index) {
		this.index = index;
		notifyDataSetChanged();
	}

	public TourMenuAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {

		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_tour_menu, null);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String info = (String) getItem(position);
		holder.tvName.setText(info);
		if (index == position) {
			// 选中的item
			holder.tvName.setBackgroundResource(R.color.xml_bg_normal);
			holder.tvName.setTextColor(ResHelper.getColorById(mContext, R.color.titlec));
		} else {
			holder.tvName.setBackgroundResource(R.color.white);
			holder.tvName.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		}
		return view;
	}

	private class ViewHolder {
		private TextView tvName;
	}
}
