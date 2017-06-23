package com.highnes.tour.adapter.home.ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.CityInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.utils.ResHelper;
import com.highnes.tour.utils.SPUtils;

/**
 * <PRE>
 * 作用:
 *    城市适配器。
 * 注意事项:
 *    无！
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-09-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketSearchHisAdapter extends BaseListAdapter<String> {

	public TicketSearchHisAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			listCell = new ListCell();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_hot, null);
			listCell.tvName = (TextView) convertView.findViewById(R.id.tv_city);
			convertView.setTag(listCell);
		} else {
			listCell = (ListCell) convertView.getTag();
		}
		// 绑定消息到列表中。
		String items = (String) getItem(position);
		listCell.tvName.setText(items);
		listCell.tvName.setTextColor(ResHelper.getColorById(mContext, R.color.font_black_normal));
		listCell.tvName.setBackgroundResource(R.drawable.shape_radius_gray5_line);
		return convertView;
	}

	/**
	 * 封装消息项。
	 * 
	 * @author FUNY
	 * 
	 */
	private static class ListCell {
		private TextView tvName;
	}

	ListCell listCell;
}
