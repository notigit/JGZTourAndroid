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
import com.highnes.tour.beans.my.PHPOrderInfo;
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
public class PHPOrderAdapter extends BaseListAdapter<PHPOrderInfo.DataInfo> {

	public PHPOrderAdapter(Context paramContext) {
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
		PHPOrderInfo.DataInfo info = (PHPOrderInfo.DataInfo) getItem(position);
		int state = Integer.valueOf(info.state);
		// 订单状态(-1.已删除。0.已关闭,。1.待支付。 2.待确认。3.待发货。4.已发货。5.待评论。 6.待使用。7.待收货。8.已完成。9.申请退货。10.待退款。11.拒绝退款。12.退款完成）
		switch (state) {
		case 1:
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("删除订单");
			holder.tvHandle.setVisibility(View.VISIBLE);
			holder.tvHandle.setText("去付款");
			break;
		case 2:
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("取消订单");
			holder.tvHandle.setVisibility(View.VISIBLE);
			holder.tvHandle.setText("查看订单");
			break;
		case 3:
		case 4:
		case 6:
		case 7:
		case 9:
		case 10:
		case 11:
		case 12:
			holder.tvOrder.setVisibility(View.GONE);
			holder.tvHandle.setText("查看订单");
			holder.tvHandle.setVisibility(View.VISIBLE);
			break;
		case 5:
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("去评论");
			holder.tvHandle.setVisibility(View.VISIBLE);
			holder.tvHandle.setText("查看订单");
			break;
		case 8:
			holder.tvOrder.setVisibility(View.VISIBLE);
			holder.tvOrder.setText("申请退货");
			holder.tvHandle.setVisibility(View.VISIBLE);
			holder.tvHandle.setText("查看订单");
			break;
		default:
			holder.tvOrder.setVisibility(View.GONE);
			holder.tvHandle.setVisibility(View.GONE);
			break;
		}
		holder.tvTitle.setText(info.title);
		holder.tvCount.setText("x" + info.number);
		holder.tvPrice.setText("￥" + info.totalmoney);
		ImageLoader.getInstance().displayImage(info.imgsrc, holder.ivImg, options, null);
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

	private class MyOnClickListener implements OnClickListener {

		private PHPOrderInfo.DataInfo info;
		private int type;

		public MyOnClickListener(int type, PHPOrderInfo.DataInfo info) {
			this.type = type;
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			if (mOnItemCall != null) {
				mOnItemCall.onItem(type, info);
			}

		}

	}

	private OnItemCall mOnItemCall;

	public void setmOnItemCall(OnItemCall mOnItemCall) {
		this.mOnItemCall = mOnItemCall;
	}

	public interface OnItemCall {
		void onItem(int type, PHPOrderInfo.DataInfo info);
	}
}
