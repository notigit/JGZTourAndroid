package com.highnes.tour.adapter.home;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.adapter.home.ticket.TicketBuyAdapter.Callback;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.beans.home.HomeTimeDetailInfo;
import com.highnes.tour.beans.home.ticket.TicketDetailBuyInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.DateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		首页..限时秒杀。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-08-03   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class HomeTimesDetailAdapter extends BaseListAdapter<HomeTimeDetailInfo.SoldInfo> {

	private String SoldCount;

	public HomeTimesDetailAdapter(Context paramContext, String SoldCount) {
		super(paramContext);
		this.SoldCount = SoldCount;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_time_detail_item, null);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_price_now);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_info);
			holder.tvCount = (TextView) view.findViewById(R.id.tv_item_count);
			holder.tvOrder = (TextView) view.findViewById(R.id.tv_order);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HomeTimeDetailInfo.SoldInfo info = (HomeTimeDetailInfo.SoldInfo) getItem(position);
		// 设置页面显示值
		holder.tvTitle.setText(info.Title);
		holder.tvCount.setText("剩余" + SoldCount + "张");
		holder.tvMoneyNow.setText(ArithUtil.formatPrice(Double.valueOf(info.NewPrice)) + "");
		holder.tvInfo.setOnClickListener(new MyOnClickListener(info, 0));
		holder.tvOrder.setOnClickListener(new MyOnClickListener(info, 1));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvMoneyNow;
		private TextView tvInfo, tvCount, tvOrder;
	}

	// 点击展开按钮的回调方法
	private Callback callback;

	public void setCallback(Callback callback) {
		this.callback = callback;
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
		 *            0表示自营的票型说明，1表示自营的购票
		 */
		void onSuccess(HomeTimeDetailInfo.SoldInfo info, int type);
	}

	private class MyOnClickListener implements OnClickListener {
		HomeTimeDetailInfo.SoldInfo info;
		int type;

		public MyOnClickListener(HomeTimeDetailInfo.SoldInfo info, int type) {
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
