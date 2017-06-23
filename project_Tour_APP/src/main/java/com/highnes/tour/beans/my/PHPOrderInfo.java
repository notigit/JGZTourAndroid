package com.highnes.tour.beans.my;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    订单信息。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-25   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PHPOrderInfo {

	public int errcode;
	public String count;
	public List<DataInfo> data; // 订单信息

	public static class DataInfo {
		public String id;
		public String imgsrc;
		public String title;
		public String totalmoney;
		public String state;
		public String number;
		public String url; //订单详情
	}
}
