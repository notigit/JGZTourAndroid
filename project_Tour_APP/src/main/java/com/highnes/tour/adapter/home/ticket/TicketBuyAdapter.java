package com.highnes.tour.adapter.home.ticket;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.beans.home.ticket.TicketDetailBuyInfo;
import com.highnes.tour.utils.ArithUtil;

/**
 * <PRE>
 * 作用:
 *    首页..门票..详情..购票的适配器。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2014-02-12   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketBuyAdapter extends BaseExpandableListAdapter {

	private int currSelectGroup = 0;

	// ExpandableList中的数据
	public List<TicketDetailBuyInfo> listGroup;
	// ListView的数据
	public List<List<TicketDetailBuyInfo>> listChild;
	LayoutInflater mInflater;
	Context context;
	// 点击展开按钮的回调方法
	private Callback callback;

	public TicketBuyAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	public TicketBuyAdapter(Context context, List<TicketDetailBuyInfo> listGroup, List<List<TicketDetailBuyInfo>> listChild, Callback callback) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.listGroup = listGroup;
		this.listChild = listChild;
		this.callback = callback;
	}

	// 获取组在给定的位置编号，即listGroup中元素的ID
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 获取在给定的组的子元素的ID，就是listChild中元素的ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 获取的群体数量，得到listGroup里元素的个数
	@Override
	public int getGroupCount() {
		return listGroup.size();
	}

	// 取得指定组中的子元素个数，就是listChild中每一个的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return listChild.get(groupPosition).size();
	}

	// 获取与给定的组相关的数据，得到集合listGroup中元素的数据
	@Override
	public Object getGroup(int groupPosition) {
		return listGroup.get(groupPosition);
	}

	// 获取一个视图显示给定组，存放listGroup
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewParent = new GroupView();
			convertView = mInflater.inflate(R.layout.item_home_ticket_buy_group, null);
			mViewParent.tvType = (TextView) convertView.findViewById(R.id.tv_item_type);
			mViewParent.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
			mViewParent.tvPriceNow = (TextView) convertView.findViewById(R.id.tv_price_now);
			mViewParent.tvPriceOld = (TextView) convertView.findViewById(R.id.tv_price_old);
			mViewParent.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
			mViewParent.tvPriceStart = (TextView) convertView.findViewById(R.id.tv_price_start);
			mViewParent.tvOrder = (TextView) convertView.findViewById(R.id.tv_order);
			mViewParent.ivTag0 = (ImageView) convertView.findViewById(R.id.iv_item_tag0);
			mViewParent.ivTag1 = (ImageView) convertView.findViewById(R.id.iv_item_tag1);
			mViewParent.ivTag2 = (ImageView) convertView.findViewById(R.id.iv_item_tag2);
			mViewParent.ivExp = (ImageView) convertView.findViewById(R.id.iv_exp);
			mViewParent.llTop = (LinearLayout) convertView.findViewById(R.id.ll_top);
			mViewParent.llBottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
			mViewParent.llGroup = (LinearLayout) convertView.findViewById(R.id.ll_group);
			convertView.setTag(mViewParent);
		} else {
			mViewParent = (GroupView) convertView.getTag();
		}

		TicketDetailBuyInfo info = (TicketDetailBuyInfo) getGroup(groupPosition);
		mViewParent.tvType.setText(info.Type);
		mViewParent.tvTitle.setText(info.SoldName);
		mViewParent.tvPriceNow.setText(ArithUtil.formatPrice(Double.valueOf(info.NewPrice)) + "");
		mViewParent.tvPriceOld.setText("￥" + ArithUtil.formatPrice(Double.valueOf(info.OldPrice)));
		mViewParent.tvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		mViewParent.tvName.setText(info.SoldName);
		if (getChildrenCount(groupPosition) > 0) {
			TicketDetailBuyInfo child = (TicketDetailBuyInfo) getChild(groupPosition, 0);
			mViewParent.tvPriceStart.setText(child.NewPrice + "");
			mViewParent.llBottom.setVisibility(View.VISIBLE);
		} else {
			mViewParent.llBottom.setVisibility(View.GONE);
		}

		if ("True".equals(info.IsReserved)) {
			mViewParent.ivTag0.setVisibility(View.VISIBLE);
		} else {
			mViewParent.ivTag0.setVisibility(View.GONE);
		}
		if ("True".equals(info.IsUpdate)) {
			mViewParent.ivTag1.setVisibility(View.VISIBLE);
		} else {
			mViewParent.ivTag1.setVisibility(View.GONE);
		}
		if ("True".equals(info.IsRefunds)) {
			mViewParent.ivTag2.setVisibility(View.VISIBLE);
		} else {
			mViewParent.ivTag2.setVisibility(View.GONE);
		}

		// if (currSelectGroup == groupPosition) {
		// mViewParent.llGroup.setVisibility(View.VISIBLE);
		// } else {
		// mViewParent.llGroup.setVisibility(View.GONE);
		// }
		mViewParent.llGroup.setOnClickListener(new MyOnClickListener(info, 0));
		mViewParent.tvOrder.setOnClickListener(new MyOnClickListener(info, 1));
		mViewParent.llTop.setOnClickListener(null);
		// 判断isExpanded就可以控制是按下还是关闭，同时更换图片
		if (isExpanded) {
			mViewParent.ivExp.setImageResource(R.drawable.ic_down);
		} else {
			mViewParent.ivExp.setImageResource(R.drawable.ic_up);
		}
		return convertView;
	}

	// 获取与子元素在给定的组相关的数据,得到数组listChild中元素的数据
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return listChild.get(groupPosition).get(childPosition);
	}

	// 获取一个视图显示在给定的组的子元素的数据，就是存放listChild
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewChild = new ChildView();
			convertView = mInflater.inflate(R.layout.item_home_ticket_buy_child, null);
			mViewChild.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
			mViewChild.tvPriceNow = (TextView) convertView.findViewById(R.id.tv_price_now);
			mViewChild.ivTag0 = (ImageView) convertView.findViewById(R.id.iv_item_tag0);
			mViewChild.ivTag1 = (ImageView) convertView.findViewById(R.id.iv_item_tag1);
			mViewChild.ivTag2 = (ImageView) convertView.findViewById(R.id.iv_item_tag2);
			mViewChild.llChild = (LinearLayout) convertView.findViewById(R.id.ll_child);
			mViewChild.tvOrder = (TextView) convertView.findViewById(R.id.tv_order);
			convertView.setTag(mViewChild);
		} else {
			mViewChild = (ChildView) convertView.getTag();
		}
		// -- 设置listview的数据
		TicketDetailBuyInfo info = (TicketDetailBuyInfo) getChild(groupPosition, childPosition);
		mViewChild.tvTitle.setText(info.SoldName);
		mViewChild.tvPriceNow.setText(info.NewPrice + "");

		if ("True".equals(info.IsReserved)) {
			mViewChild.ivTag0.setVisibility(View.VISIBLE);
		} else {
			mViewChild.ivTag0.setVisibility(View.GONE);
		}
		if ("True".equals(info.IsUpdate)) {
			mViewChild.ivTag1.setVisibility(View.VISIBLE);
		} else {
			mViewChild.ivTag1.setVisibility(View.GONE);
		}
		if ("True".equals(info.IsRefunds)) {
			mViewChild.ivTag2.setVisibility(View.VISIBLE);
		} else {
			mViewChild.ivTag2.setVisibility(View.GONE);
		}
		mViewChild.llChild.setOnClickListener(new MyOnClickListener(info, 2));
		mViewChild.tvOrder.setOnClickListener(new MyOnClickListener(info, 3));
		return convertView;
	}

	/** 添加消息 */
	public void addParentAll(List<TicketDetailBuyInfo> list) {
		listGroup.addAll(list);
		notifyDataSetChanged();
	}

	/** 清空列表 */
	public void cleanParentAll() {
		listGroup.clear();
		notifyDataSetChanged();
	}

	/** 添加消息 */
	public void addChildAll(List<List<TicketDetailBuyInfo>> list) {
		listChild.addAll(list);
		notifyDataSetChanged();
	}

	/** 清空列表 */
	public void cleanChildAll() {
		listChild.clear();
		notifyDataSetChanged();
	}

	/**
	 * 修改指定位置的子元素
	 */
	public void updateChildItem(int groupPosition, List<TicketDetailBuyInfo> list) {
		listChild.set(groupPosition, list);
		notifyDataSetChanged();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private GroupView mViewParent;

	private static class GroupView {
		private TextView tvType;
		private TextView tvTitle;
		private TextView tvPriceNow, tvPriceOld;
		private TextView tvName;
		private TextView tvPriceStart;
		private TextView tvOrder;
		private ImageView ivTag0, ivTag1, ivTag2, ivExp;
		private LinearLayout llTop, llBottom, llGroup;
	}

	private ChildView mViewChild;

	private static class ChildView {
		private TextView tvTitle;
		private TextView tvPriceNow;
		private TextView tvOrder;
		private ImageView ivTag0, ivTag1, ivTag2;
		private LinearLayout llChild;
	}

	/**
	 * 回调方法
	 */
	public static interface Callback {
		/**
		 * 回调方法
		 * 
		 * @param info
		 *            当前的票
		 * @param type
		 *            0表示自营的票型说明，1表示自营的购票，2表示代理的票型说明，3表示代理的购票，
		 */
		void onSuccess(TicketDetailBuyInfo info, int type);
	}

	private class MyOnClickListener implements OnClickListener {
		TicketDetailBuyInfo info;
		int type;

		public MyOnClickListener(TicketDetailBuyInfo info, int type) {
			this.info = info;
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			if (callback != null) {
				callback.onSuccess(info, type);
			}
		}

	}
}