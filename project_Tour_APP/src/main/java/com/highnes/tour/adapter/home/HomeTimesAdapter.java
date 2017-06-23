package com.highnes.tour.adapter.home;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.adapter.home.HomeActivitiesAdapter.OnCellItem;
import com.highnes.tour.beans.home.HomeAllInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.ArithUtil;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.TimeTools;
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
public class HomeTimesAdapter extends BaseListAdapter<HomeAllInfo.BenefitInfo> {
	DecimalFormat df;
	public HomeTimesAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
		 df = new DecimalFormat("00");
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_home_time, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
			holder.tvMoneyNow = (TextView) view.findViewById(R.id.tv_item_money_now);
			holder.tvMoneyOld = (TextView) view.findViewById(R.id.tv_item_money_old);
			holder.tvTimeHour = (TextView) view.findViewById(R.id.tv_item_time_hour);
			holder.tvTimeMin = (TextView) view.findViewById(R.id.tv_item_time_min);
			holder.tvTimeSecond = (TextView) view.findViewById(R.id.tv_item_time_second);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HomeAllInfo.BenefitInfo info = (HomeAllInfo.BenefitInfo) getItem(position);
		// 设置页面显示值
		holder.tvTitle.setText(info.Title);
//		holder.tvMoneyNow.setText(ArithUtil.format2Int(info.NewPrice) + "");
		holder.tvMoneyNow.setText(info.NewPrice + "");
//		holder.tvMoneyOld.setText("￥" + ArithUtil.format2Int(info.OldPrice));
		holder.tvMoneyOld.setText("￥" + info.OldPrice);
		holder.tvMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		// TODO 时间的处理
		// 设置页面显示值
		long time = info.getRestTime();
		String[] date = TimeTools.getDifference(time).split(",");
		if(Integer.valueOf(date[0])>99){
			holder.tvTimeHour.setText("99");
		}else{
			holder.tvTimeHour.setText(df.format(Integer.parseInt(date[0])));
		}
		holder.tvTimeMin.setText(df.format(Integer.parseInt(date[1])));
		holder.tvTimeSecond.setText(df.format(Integer.parseInt(date[2])));
		
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.Photo, holder.ivImg, options, null);
		holder.root.setOnClickListener(new MyOnClickListener(info));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle;
		private TextView tvMoneyNow;
		private TextView tvMoneyOld;
		private TextView tvTimeHour, tvTimeMin, tvTimeSecond;
		private ImageView ivImg;
		private LinearLayout root;
	}

	/**
	 * 局部刷新
	 * 
	 * @param view
	 * @param itemIndex
	 */
	public void updateView(View view, int itemIndex,HomeAllInfo.BenefitInfo info) {
		if (view == null) {
			return;
		}
		// 从view中取得holder
		ViewHolder holder = (ViewHolder) view.getTag();
		// 设置页面显示值
		long time = info.getRestTime();
		String[] date = TimeTools.getDifference(time).split(",");
		if(Integer.valueOf(date[0])>99){
			holder.tvTimeHour.setText("99");
		}else{
			holder.tvTimeHour.setText(df.format(Integer.parseInt(date[0])));
		}
		holder.tvTimeMin.setText(df.format(Integer.parseInt(date[1])));
		holder.tvTimeSecond.setText(df.format(Integer.parseInt(date[2])));
	}
	
	private class MyOnClickListener implements OnClickListener {
		private HomeAllInfo.BenefitInfo mInfo;

		public MyOnClickListener(HomeAllInfo.BenefitInfo mInfo) {
			this.mInfo = mInfo;
		}

		@Override
		public void onClick(View v) {
			if (mOnCellItem != null) {
				mOnCellItem.onItem(mInfo);
			}
		}

	}

	private OnCellItem mOnCellItem;

	public void setmOnCellItem(OnCellItem mOnCellItem) {
		this.mOnCellItem = mOnCellItem;
	}

	public interface OnCellItem {
		void onItem(HomeAllInfo.BenefitInfo info);

	}
}
