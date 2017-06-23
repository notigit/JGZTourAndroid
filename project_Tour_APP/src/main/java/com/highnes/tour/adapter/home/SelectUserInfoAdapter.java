package com.highnes.tour.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.adapter.my.CommonUserInfoAdapter.OnChecked;
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
public class SelectUserInfoAdapter extends BaseListAdapter<CommonUserInfo.PeopleInfo> {

	public SelectUserInfoAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_select_userinfo, null);
			holder.tvName = (TextView) view.findViewById(R.id.tv_item_name);
			holder.tvIDcard = (TextView) view.findViewById(R.id.tv_item_crad);
			holder.ivDel = (ImageView) view.findViewById(R.id.iv_item_del);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CommonUserInfo.PeopleInfo info = (CommonUserInfo.PeopleInfo) getItem(position);
		// 设置页面显示值
		if (info.Phone == null) {
			holder.tvName.setText(info.Name);
		} else {
			holder.tvName.setText(info.Name + "　　" + info.Phone);
		}
		holder.tvIDcard.setText("身份证："+info.IDcard);
		holder.ivDel.setOnClickListener(new MyOnClickListener(info));
		return view;
	}

	private class ViewHolder {
		private TextView tvName;
		private TextView tvIDcard;
		private ImageView ivDel;
	}

	private class MyOnClickListener implements OnClickListener {
		CommonUserInfo.PeopleInfo info;

		public MyOnClickListener(CommonUserInfo.PeopleInfo info) {
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			if (onChecked != null) {
				onChecked.onChecked(info);
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
		void onChecked(CommonUserInfo.PeopleInfo info);
	}

	public void setOnChecked(OnChecked onChecked) {
		this.onChecked = onChecked;
	}
}
