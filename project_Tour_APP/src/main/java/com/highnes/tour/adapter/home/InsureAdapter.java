package com.highnes.tour.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.ticket.SoldSafestInfo;
import com.highnes.tour.beans.my.CommonUserInfo;

/**
 * <PRE>
 * 作用:
 * 		旅客信息。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-08-23   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class InsureAdapter extends BaseListAdapter<SoldSafestInfo.SafestInfo> {

	private int index = 0;

	public InsureAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	public void setIndex(int index) {
		this.index = index;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_insure, null);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			holder.tvPrice = (TextView) view.findViewById(R.id.tv_price_start);
			holder.cbSelect = (ImageView) view.findViewById(R.id.cb_select);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		SoldSafestInfo.SafestInfo info = (SoldSafestInfo.SafestInfo) getItem(position);
		// 设置页面显示值
		holder.tvName.setText(info.Name);
		holder.tvPrice.setText(info.Money + "");
		if (position == index) {
			holder.cbSelect.setImageResource(R.drawable.cb_pressred);
		} else {
			holder.cbSelect.setImageResource(R.drawable.cb_normal);
		}
		holder.cbSelect.setOnClickListener(new MyOnClickListener(position, info, 0));
		holder.root.setOnClickListener(new MyOnClickListener(position, info, 1));
		return view;
	}

	private class ViewHolder {
		private TextView tvName;
		private TextView tvPrice;
		private ImageView cbSelect;
		private LinearLayout root;
	}

	private class MyOnClickListener implements OnClickListener {
		SoldSafestInfo.SafestInfo info;
		int type;
		int position;

		public MyOnClickListener(int position, SoldSafestInfo.SafestInfo info, int type) {
			this.info = info;
			this.type = type;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onChecked != null) {
				onChecked.onChecked(position, info, type);
			}
		}

	}

	private OnChecked onChecked;

	public interface OnChecked {
		/**
		 * 
		 * @param info
		 *            当前的item
		 * @param type
		 *            类别
		 */
		void onChecked(int position, SoldSafestInfo.SafestInfo info, int type);
	}

	public void setOnChecked(OnChecked onChecked) {
		this.onChecked = onChecked;
	}
}
