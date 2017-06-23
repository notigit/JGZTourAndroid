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
public class MyOrderInfo {

	public String status;
	public String Count;
	public List<OrderInfo> Order; // 订单信息

	public static class OrderInfo {
		public String AddTime; // 订单时间
		public String ID; // id
		public Double Money;// 价格
		public String Title;// 标题
		public String Status;// 状态
		public String Type;
		public String Body;

		public static class BodyInfo {
			public String ActualPrice;
			public String AreaID;
			public String Count;
			public String ID;
			public String MID;
			public String NewPrice;
			public String Odate;
			public String Price;
		}

	}
}
