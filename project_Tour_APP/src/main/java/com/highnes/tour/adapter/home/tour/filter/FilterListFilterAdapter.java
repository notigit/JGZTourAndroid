package com.highnes.tour.adapter.home.tour.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.tour.Tour1FilterInfo;

/**
 * <PRE>
 * 作用:
 * 		筛选列表。
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
public class FilterListFilterAdapter extends BaseListAdapter<Tour1FilterInfo.SiftingsInfo.SiftingInfo> {

	public FilterListFilterAdapter(Context paramContext) {
		super(paramContext);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {

		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_text_center_checkbox, null);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_text);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.cbSelect = (CheckBox) view.findViewById(R.id.cb_select);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Tour1FilterInfo.SiftingsInfo.SiftingInfo info = (Tour1FilterInfo.SiftingsInfo.SiftingInfo) getItem(position);
		holder.tvName.setText(info.Name);
		holder.cbSelect.setOnCheckedChangeListener(new MyOnCheckedChangeListener(info, holder.root,position));
		holder.root.setOnClickListener(new MyOnClickListener(info, holder.root,holder.cbSelect,position));
		return view;
	}

	private class ViewHolder {
		private TextView tvName;
		private LinearLayout root;
		private CheckBox cbSelect;
	}

	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		Tour1FilterInfo.SiftingsInfo.SiftingInfo info;
		LinearLayout root;
		int position;

		public MyOnCheckedChangeListener(Tour1FilterInfo.SiftingsInfo.SiftingInfo info, LinearLayout root,int position) {
			this.info = info;
			this.root = root;
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			info.setSelect(isChecked);
			if (info.isSelect()) {
				// 选中的item
				root.setBackgroundResource(R.color.xml_bg_normal);
			} else {
				root.setBackgroundResource(R.color.white);
			}
			if (onChecked != null) {
				onChecked.onChecked(info,position);
			}
		}
	}

	private class MyOnClickListener implements OnClickListener {
		Tour1FilterInfo.SiftingsInfo.SiftingInfo info;
		LinearLayout root;
		CheckBox cbSelect;
		int position;
		
		public MyOnClickListener(Tour1FilterInfo.SiftingsInfo.SiftingInfo info, LinearLayout root,CheckBox cbSelect,int position) {
			this.info = info;
			this.root = root;
			this.cbSelect = cbSelect;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			info.setSelect(!info.isSelect());
			if (info.isSelect()) {
				// 选中的item
				root.setBackgroundResource(R.color.xml_bg_normal);
			} else {
				root.setBackgroundResource(R.color.white);
			}
			cbSelect.setChecked(info.isSelect());
			if (onChecked != null) {
				onChecked.onChecked(info,position);
			}
		}

	}

	private OnChecked onChecked;

	public interface OnChecked {
		/**
		 * 
		 * @param info 当前的item
		 * @param position 所在的位置
		 */
		void onChecked(Tour1FilterInfo.SiftingsInfo.SiftingInfo info,int position);
	}

	public void setOnChecked(OnChecked onChecked) {
		this.onChecked = onChecked;
	}
}
