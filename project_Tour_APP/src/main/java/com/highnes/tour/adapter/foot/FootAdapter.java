package com.highnes.tour.adapter.foot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.media.tv.TvTrackInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.foot.FootGroupInfo;
import com.highnes.tour.beans.foot.FootGroupInfo.TribuneInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.DateUtils;
import com.highnes.tour.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		足迹..足迹圈。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-08-05   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FootAdapter extends BaseListAdapter<FootGroupInfo.TribuneInfo> {

	public FootAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_foot_group, null);
			holder.gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_avatar);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_remarks);
			holder.tvTime = (TextView) view.findViewById(R.id.tv_item_time);
			holder.tvLoc = (TextView) view.findViewById(R.id.tv_item_loc);
			holder.tvCommentCount = (TextView) view.findViewById(R.id.tv_item_comment);
			holder.tvOkCount = (TextView) view.findViewById(R.id.tv_item_agree);
			holder.llComOk = (LinearLayout) view.findViewById(R.id.ll_comok);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FootGroupInfo.TribuneInfo info = (FootGroupInfo.TribuneInfo) getItem(position);
		holder.tvTitle.setText(info.Contents);
		if (StringUtils.isEmpty(info.AddTime)) {
			holder.tvTime.setText("");
		} else {
			holder.tvTime.setText(DateUtils.getStandardDate(info.AddTime));
		}
		if (StringUtils.isEmpty(info.AddName)) {
			holder.tvLoc.setVisibility(View.INVISIBLE);
		} else {
			holder.tvLoc.setText(info.AddName);
			holder.tvLoc.setVisibility(View.VISIBLE);
		}
		holder.tvCommentCount.setText(info.Count);
		holder.tvOkCount.setText(info.ThingCount);

		if (StringUtils.isEmpty(info.ImageUrls)) {
			holder.gvPhoto.setVisibility(View.GONE);
		} else {
			holder.gvPhoto.setVisibility(View.VISIBLE);
			FootImageAdapter adapter = new FootImageAdapter(mContext);
			holder.gvPhoto.setAdapter(adapter);
			String temp[] = info.ImageUrls.split(",");
			String arr[];
			if (temp.length > 3) {
				arr = new String[3];
				for (int i = 0; i < 3; i++) {
					arr[i] = temp[i];
				}
			} else {
				arr = temp;
			}
			adapter.setList(Arrays.asList(arr));
		}

		if (StringUtils.isEmpty(info.IsDelivered)) {
			holder.llComOk.setVisibility(View.VISIBLE);
		} else {
			if ("False".equals(info.IsDelivered)) {
				holder.llComOk.setVisibility(View.GONE);
			} else {
				holder.llComOk.setVisibility(View.VISIBLE);
			}
		}
		ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.UserHeadImg, holder.ivImg, options, null);
		holder.root.setOnClickListener(new MyOnClickListener(info));
		holder.gvPhoto.setOnItemClickListener(new MyOnItemClickListener(info));
		return view;
	}

	private class ViewHolder {
		private TextView tvTitle, tvTime, tvLoc, tvCommentCount, tvOkCount;
		private ImageView ivImg;
		// 图片
		private GridView gvPhoto;
		private LinearLayout root, llComOk;
	}

	private class MyOnClickListener implements OnClickListener {

		FootGroupInfo.TribuneInfo info;

		public MyOnClickListener(FootGroupInfo.TribuneInfo info) {
			this.info = info;
		}

		@Override
		public void onClick(View v) {
			if (onItemCall != null) {
				onItemCall.onCall(info);
			}
		}

	}
	
	private class MyOnItemClickListener implements OnItemClickListener{
		FootGroupInfo.TribuneInfo info;

		public MyOnItemClickListener(FootGroupInfo.TribuneInfo info) {
			this.info = info;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (onItemCall != null) {
				onItemCall.onCall(info);
			}
		}
		
	}

	private OnItemCall onItemCall;

	public void setOnItemCall(OnItemCall onItemCall) {
		this.onItemCall = onItemCall;
	}

	public interface OnItemCall {
		void onCall(FootGroupInfo.TribuneInfo info);
	}
	
	
}