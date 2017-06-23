package com.highnes.tour.beans.home.tour;

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
public class Tour1OrderPayInfo {

	public String status;
	public String Multiply; // 积分抵扣比例
	public String UserPonit; // 用户当前的积分
	public List<OrderInfo> Order; // 订单信息
	public List<TouOrderInfo> TouOrder;// 门票、保险信息
	public List<UserRedInfo> UserRed;// 红包
	public List<TouSafInfo> TouSaf;// 门票信息
	public List<TouUserInfo> TouUser;// 门票信息

	public static class OrderInfo {
		public String OrderID;
		public String PayableAmount;
		public String ATime;
		public String SafCount;
		public String SafMoney;
		public String ProID;
	}

	public static class TouOrderInfo {
		public String HotelPrice;
		public String ServiceName;
		public String ServicePhone;
		public String TouBabyPrice;
		public String TouOrderID;
		public String TouPrice;
		public String TouTitle;
		public String TouTypeName;
		public String UserName;
		public String UserPhone;

	}

	public static class UserRedInfo {
		// 红包
	}

	public static class TouSafInfo {
		public String ID;
		public String IDcard;
		public String Name;
		public String Phone;
		public Double Price;
		public String SafName;
	}
	public static class TouUserInfo {
		public String ID;
		public String IDcard;
		public String Name;
		public String Phone;
	}
}
