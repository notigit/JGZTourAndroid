package com.highnes.tour.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.CityInfo;

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
public class CityAdapter extends BaseListAdapter<CityInfo> implements SectionIndexer {

	public CityAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			listCell = new ListCell();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city, null);
			listCell.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
			listCell.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			convertView.setTag(listCell);
		} else {
			listCell = (ListCell) convertView.getTag();
		}
		// 绑定消息到列表中。
		CityInfo items = (CityInfo) getItem(position);
		// 根据position获取分类的首字母的Char ascii值
		// int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		// if (position == getPositionForSection(section)) {
		// listCell.tvLetter.setVisibility(View.VISIBLE);
		// listCell.tvLetter.setText(items.getSortLetters());
		// } else {
		// listCell.tvLetter.setVisibility(View.GONE);
		// }
		if (items.name.charAt(0) == '#') {
			listCell.tvLetter.setVisibility(View.VISIBLE);
			listCell.tvLetter.setText(items.name.charAt(1) + "");
			listCell.tvName.setText(items.name.substring(2));
		} else {
			listCell.tvLetter.setVisibility(View.GONE);
			listCell.tvName.setText(items.name);
		}

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
		private TextView tvLetter;
	}

	ListCell listCell;

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		return mList.get(position).getSortLetters().charAt(0);
	}
}
