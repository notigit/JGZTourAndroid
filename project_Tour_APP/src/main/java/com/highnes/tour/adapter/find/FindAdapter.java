package com.highnes.tour.adapter.find;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.find.FindInfo;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.DensityUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * <PRE>
 * 作用:
 * 		发现..景色。
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
public class FindAdapter extends BaseListAdapter<FindInfo.NotesTypeInfo> {

	public FindAdapter(Context paramContext) {
		super(paramContext);
		initOptions();
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_find, null);
			holder.ivImg = (ImageView) view.findViewById(R.id.iv_item_img);
			holder.tvInfo = (TextView) view.findViewById(R.id.tv_item_centent);
			holder.ivImg2 = (ImageView) view.findViewById(R.id.iv_item_img2);
			holder.tvInfo2 = (TextView) view.findViewById(R.id.tv_item_centent2);
			holder.root1 = (RelativeLayout) view.findViewById(R.id.root1);
			holder.root2 = (RelativeLayout) view.findViewById(R.id.root2);
			holder.tvMore = (TextView) view.findViewById(R.id.tv_item_more);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_type);
			holder.llRoot = (LinearLayout) view.findViewById(R.id.ll_root);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FindInfo.NotesTypeInfo info = (FindInfo.NotesTypeInfo) getItem(position);
		if (info != null && info.Notes.size() > 0) {
			holder.tvTitle.setText(info.TypeName);
			holder.llRoot.setVisibility(View.VISIBLE);
			if (info.Notes.size() >= 2) {
				holder.root1.setVisibility(View.VISIBLE);
				holder.root2.setVisibility(View.VISIBLE);
				holder.tvMore.setVisibility(View.VISIBLE);
				holder.tvInfo.setText(info.Notes.get(0).Name);
				ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.Notes.get(0).ImageUrl, holder.ivImg, options, null);
				holder.tvInfo2.setText(info.Notes.get(1).Name);
				ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.Notes.get(1).ImageUrl, holder.ivImg2, options, null);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.height = DensityUtils.dip2px(mContext, 350);
				holder.llRoot.setLayoutParams(ll);
				holder.root1.setOnClickListener(new MyOnClickListener(info.TypeID, info.Notes.get(0).ID, 1, info.TypeName));
				holder.root2.setOnClickListener(new MyOnClickListener(info.TypeID, info.Notes.get(1).ID, 1, info.TypeName));
				holder.tvMore.setOnClickListener(new MyOnClickListener(info.TypeID, info.Notes.get(0).ID, 0, info.TypeName));
			} else {
				holder.root1.setVisibility(View.VISIBLE);
				holder.root2.setVisibility(View.GONE);
				holder.tvMore.setVisibility(View.VISIBLE);
				holder.tvInfo.setText(info.Notes.get(0).Name);
				ImageLoader.getInstance().displayImage(UrlSettings.URL_IMAGE + info.Notes.get(0).ImageUrl, holder.ivImg, options, null);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.height = DensityUtils.dip2px(mContext, 200);
				holder.llRoot.setLayoutParams(ll);
				holder.root1.setOnClickListener(new MyOnClickListener(info.TypeID, info.Notes.get(0).ID, 1, info.TypeName));
				holder.tvMore.setOnClickListener(new MyOnClickListener(info.TypeID, info.Notes.get(0).ID, 0, info.TypeName));
			}
		} else {
			holder.llRoot.setVisibility(View.GONE);
			holder.root1.setVisibility(View.GONE);
			holder.root2.setVisibility(View.GONE);
			holder.tvMore.setVisibility(View.GONE);
		}
		return view;
	}

	private class ViewHolder {
		private TextView tvInfo, tvInfo2;
		private ImageView ivImg, ivImg2;
		private RelativeLayout root1, root2;
		private TextView tvMore;
		private TextView tvTitle;
		private LinearLayout llRoot;
	}

	private class MyOnClickListener implements OnClickListener {

		private String mTypeID;
		private String mNoteID;
		private int type;
		private String mTitle;

		public MyOnClickListener(String mTypeID, String mNoteID, int type, String mTitle) {
			this.mNoteID = mNoteID;
			this.mTypeID = mTypeID;
			this.type = type;
			this.mTitle = mTitle;
		}

		@Override
		public void onClick(View v) {
			if (mOnItemCall != null) {
				mOnItemCall.onCall(mTypeID, mNoteID, type, mTitle);
			}
		}

	}

	public void setmOnItemCall(OnItemCall mOnItemCall) {
		this.mOnItemCall = mOnItemCall;
	}

	private OnItemCall mOnItemCall;

	public interface OnItemCall {
		/**
		 * 
		 * @param mTypeID
		 *            类型ID
		 * @param mNoteID
		 *            ItemID
		 * @param type
		 *            0表示更多，1表示item点击
		 * @param mTitle
		 *            标题
		 */
		void onCall(String mTypeID, String mNoteID, int type, String mTitle);
	}
}
