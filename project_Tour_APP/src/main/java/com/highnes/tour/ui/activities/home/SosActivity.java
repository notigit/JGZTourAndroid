package com.highnes.tour.ui.activities.home;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.highnes.tour.R;
import com.highnes.tour.ui.activities.base.BaseTitleActivity;

public class SosActivity extends BaseTitleActivity {

	@Override
	@LayoutRes
	protected int getLayoutId2T() {
		return R.layout.activity_home_sos;
	}

	@Override
	protected void findViewById2T() {

	}

	@Override
	protected void initView2T(Bundle savedInstanceState) {
		setTitle("道路救援");
		showBackwardView("", SHOW_ICON_DEFAULT);
	}

	@Override
	protected void setListener2T() {

	}

	@Override
	protected void processLogic2T(Bundle savedInstanceState) {

	}

}
