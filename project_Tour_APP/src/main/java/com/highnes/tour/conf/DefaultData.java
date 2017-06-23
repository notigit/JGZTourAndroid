package com.highnes.tour.conf;

import com.highnes.tour.R;

/**
 * <PRE>
 * 作用:
 *    默认值。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-23   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class DefaultData {

	/** 广播回调监听_刷新 */
	public static final String ACTION_CALLBACK_REFRESH = "com.highnes.tour.action_callback_refresh";
	/** 选择 */
	public static final String EXTRA_RESULT = "select_result";
	public static final int REQUEST_CODE = 0x1001;
	public static final int REQUEST_CODES = 0x1002;
	public static final int REQUEST_CODESS = 0x1003;

	/** SP保存城市历史数据 */
	public static final String LIST_CITY_HIS_INFO = "com.highnes.tour.list_city_his_info";
	/** SP保存门票搜索历史记录 */
	public static final String LIST_TICKET_SEARCH_HIS_INFO = "com.highnes.tour.list_ticket_search_his_info";
	/** SP保存上一次首页的JSON */
	public static final String JSON_HOME = "com.highnes.tour.json_home";

	/**
	 * 轮播图 默认图片
	 */
	public static final String[] IMAGE_FROM_LOCAL = { "drawable://" + R.drawable.ic_default_img, "drawable://" + R.drawable.ic_default_img, };

	/**
	 * 轮播图 默认图片 高度比较小的
	 */
	public static final String[] IMAGE_FROM_LOCAL_SHORT = { "drawable://" + R.drawable.ic_default_img_short, "drawable://" + R.drawable.ic_default_img_short, };
}
