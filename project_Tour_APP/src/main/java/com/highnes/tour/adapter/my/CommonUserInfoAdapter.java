package com.highnes.tour.adapter.my;

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
public class CommonUserInfoAdapter extends BaseListAdapter<CommonUserInfo.PeopleInfo> {

	// 是否是选择的模式
	private boolean isSelect = false;

	public CommonUserInfoAdapter(Context paramContext, boolean isSelect) {
		super(paramContext);
		this.isSelect = isSelect;
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_my_common_userinfo, null);
			holder.tvNamePhone = (TextView) view.findViewById(R.id.tv_item_name_phone);
			holder.tvIDCard = (TextView) view.findViewById(R.id.tv_item_idcard);
			holder.cbSelect = (CheckBox) view.findViewById(R.id.cb_select);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CommonUserInfo.PeopleInfo info = (CommonUserInfo.PeopleInfo) getItem(position);
		// 设置页面显示值
		if (info.Phone == null) {
			holder.tvNamePhone.setText(info.Name);
		} else {
			holder.tvNamePhone.setText(info.Name + "　" + info.Phone);
		}
		holder.tvIDCard.setText(info.IDcard);
		if (isSelect) {
			holder.cbSelect.setVisibility(View.VISIBLE);
		} else {
			holder.cbSelect.setVisibility(View.GONE);
		}
		holder.cbSelect.setChecked(info.isSelect()); 
		holder.cbSelect.setOnCheckedChangeListener(new MyOnCheckedChangeListener(info, 0));
		holder.root.setOnClickListener(new MyOnClickListener(info,1));
		return view;
	}

	private class ViewHolder {
		private TextView tvNamePhone;
		private TextView tvIDCard;
		private CheckBox cbSelect;
		private LinearLayout root;
	}
	
	private class MyOnClickListener implements OnClickListener {
		CommonUserInfo.PeopleInfo info;
		int type;
		
		public MyOnClickListener(CommonUserInfo.PeopleInfo info,int type) {
			this.info = info;
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			if (onChecked != null) {
				onChecked.onChecked(info,type);
			}
		}

	}

	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		CommonUserInfo.PeopleInfo info;
		int type;

		public MyOnCheckedChangeListener(CommonUserInfo.PeopleInfo info, int type) {
			this.info = info;
			this.type = type;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			info.setSelect(isChecked);
			if (onChecked != null) {
				onChecked.onChecked(info, type);
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
		void onChecked(CommonUserInfo.PeopleInfo info, int type);
	}

	public void setOnChecked(OnChecked onChecked) {
		this.onChecked = onChecked;
	}
}
