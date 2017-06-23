package com.highnes.tour.adapter.my;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.ticket.TicketInfo;
import com.highnes.tour.beans.my.AllOrderInfo;
import com.highnes.tour.beans.my.CollectTicketInfo;
import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.AMapUtils;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.SPUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		首页..门票推荐。
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
public class AllOrderAdapter extends BaseListAdapter<AllOrderInfo.OrderInfo> {

	public AllOrderAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_my_all_order, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvPrice = (TextView) view.findViewById(R.id.tv_item_price);
			holder.tvCount = (TextView) view.findViewById(R.id.tv_item_count);
			holder.tvOrder = (TextView) view.findViewById(R.id.tv_item_order);
			holder.tvHandle = (TextView) view.findViewById(R.id.tv_item_handle);
			holder.llRoot = (LinearLayout) view.findViewById(R.id.ll_root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		AllOrderInfo.OrderInfo info = (AllOrderInfo.OrderInfo) getItem(position);
		if ("待付款".equals(info.Status)) {
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("删除订单");
			holder.tvHandle.setVisibility(View.VISIBLE);
			holder.tvHandle.setText("去付款");
			holder.tvPrice.setText("￥"+info.Price);
		} else if ("待消费".equals(info.Status)) {
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("查看订单");
			holder.tvHandle.setVisibility(View.VISIBLE);
			holder.tvHandle.setText("申请退款");
			holder.tvPrice.setText("￥"+info.ActualPrice);
		} else if ("待评论".equals(info.Status)) {
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("查看订单");
			holder.tvHandle.setVisibility(View.VISIBLE);
			holder.tvHandle.setText("去评价");
			holder.tvPrice.setText("￥"+info.ActualPrice);
		} else if ("已完成".equals(info.Status)) {
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("查看订单");
			holder.tvHandle.setVisibility(View.GONE);
			holder.tvPrice.setText("￥"+info.ActualPrice);
		} else {// 退款中
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("查看订单");
			holder.tvHandle.setVisibility(View.GONE);
			holder.tvPrice.setText("￥"+info.ActualPrice);
		}
		holder.tvTitle.setText(info.Title);
		holder.tvCount.setText("x"+info.GoodsCount);
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.Photo, holder.ivImg, options, null);
		holder.llRoot.setOnClickListener(new MyOnClickListener(0, info));
		holder.tvOrder.setOnClickListener(new MyOnClickListener(1, info));
		holder.tvHandle.setOnClickListener(new MyOnClickListener(2, info));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvPrice, tvCount;
		private TextView tvOrder, tvHandle;
		private ImageView ivImg;
		private LinearLayout llRoot;
	}
	
	private class MyOnClickListener implements OnClickListener{

		private AllOrderInfo.OrderInfo info;
		private int type;
		public MyOnClickListener(int type,AllOrderInfo.OrderInfo info) {
			this.type = type;
			this.info = info;
		}
		@Override
		public void onClick(View v) {
			if(mOnItemCall!=null){
				mOnItemCall.onItem(type,info);
			}
			
		}
		
	}
	
	private OnItemCall mOnItemCall;
	
	public void setmOnItemCall(OnItemCall mOnItemCall) {
		this.mOnItemCall = mOnItemCall;
	}

	public interface OnItemCall{
		void onItem(int type,AllOrderInfo.OrderInfo info);
	}
}
