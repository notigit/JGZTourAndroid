package com.highnes.tour.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highnes.tour.R;
import com.highnes.tour.adapter.base.BaseListAdapter;
import com.highnes.tour.beans.home.HomeFastInfo;
import com.highnes.tour.utils.DensityUtils;
import com.highnes.tour.view.layout.RippleView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2017/3/3.
 */

public class UtilsAdapter extends BaseListAdapter<HomeFastInfo> {
    public UtilsAdapter(Context paramContext) {
        super(paramContext);
        initOptions();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UtilsViewHolder holder;
        if (convertView == null) {
            holder = new UtilsViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.utils_item, parent, false);
            holder.rippleView = (RippleView) convertView.findViewById(R.id.utils_ripple);
            holder.imageView = (ImageView) convertView.findViewById(R.id.utils_iv);
            holder.textView = (TextView) convertView.findViewById(R.id.utils_tv);
            convertView.setTag(holder);
        } else {
            holder = (UtilsViewHolder) convertView.getTag();
        }
        HomeFastInfo info = (HomeFastInfo) getItem(position);
        ImageLoader.getInstance().displayImage(info.imgUrl, holder.imageView, options, null);
        holder.textView.setText(info.name);
        holder.rippleView.setOnClickListener(new UtilsClickListener(info,position));
        return convertView;
    }
    public class UtilsClickListener implements View.OnClickListener{
        HomeFastInfo mInfo;
        int position;
        public UtilsClickListener(HomeFastInfo info, int position) {
            mInfo = info;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onUtilsClickListener != null){
                onUtilsClickListener.utilsClick(mInfo,position);
            }
        }
    }

    public class UtilsViewHolder {
        ImageView imageView;
        TextView textView;
        RippleView rippleView;
    }

    public OnUtilsClickListener onUtilsClickListener;

    public interface OnUtilsClickListener {
        void utilsClick(HomeFastInfo info, int position);
    }
    public void setOnUtilsClickListener(OnUtilsClickListener onUtilsClickListener){
        this.onUtilsClickListener = onUtilsClickListener;
    }
}
