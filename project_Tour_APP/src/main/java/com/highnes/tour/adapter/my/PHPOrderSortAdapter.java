package com.highnes.tour.adapter.my;

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

/**
 * <PRE>
 * 作用:
 * 		美食评论列表。
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
public class PHPOrderSortAdapter extends BaseListAdapter<String> {
	// 当前选择的Item
	private int index = 0;

	public void setIndex(int index) {
		this.index = index;
		notifyDataSetChanged();
	}

	public PHPOrderSortAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {

		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_text_center, null);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_text);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.ivSelect = (ImageView) view.findViewById(R.id.iv_select);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String info = (String) getItem(position);
		holder.tvName.setText(info.split(",")[0]);
		if(index == position){
			//选中的item
			holder.root.setBackgroundResource(R.color.xml_bg_normal);
			holder.ivSelect.setVisibility(View.VISIBLE);
		}else{
			holder.root.setBackgroundResource(R.color.white);
			holder.ivSelect.setVisibility(View.INVISIBLE);
		}
		return view;
	}

	private class ViewHolder {
		private TextView tvName;
		private LinearLayout root;
		private ImageView ivSelect;
	}
}
