package com.highnes.tour.beans.home.ticket;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    支付信息。
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
public class TicketOrderPayInfo {

	public String status;
	public String Multiply; // 积分抵扣比例
	public String UserPonit; // 用户当前的积分
	public List<OrderInfo> Order; // 订单信息
	public List<SoldOrderInfo> SoldOrder;// 门票、保险信息
	public List<UserRedInfo> UserRed;// 红包
	public List<SoldInfo> sold;// 门票信息

	public static class OrderInfo {
		public String OrderID;
		public String PayableAmount;
		public String ProID;
	}

	public static class SoldOrderInfo {
		public String RemoveTime;
		public String SafCount;
		public String SafestPrice;
		public String SoldCount;
		public String SoldPriceSum;

	}

	public static class UserRedInfo {
		// 红包
	}

	public static class SoldInfo {
		public String ImageUrl;
		public String SoldID;
		public String SoldName;
	}
}
