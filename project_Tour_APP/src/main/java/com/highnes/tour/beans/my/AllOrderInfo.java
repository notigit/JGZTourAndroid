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
public class AllOrderInfo {

	public String status;
	public String Count;
	public List<OrderInfo> Order; // 订单信息

	public static class OrderInfo {
		public String ActualPrice;
		public String GoodsCount;
		public String AreaID;// 景区ID
		public String TouID;// 旅游ID
		public String CamID;// 野营ID
		public String ID;
		public String OrderID;
		public String Price;
		public String Status;
		public String Title;
		public String Photo;
		public String ProID;
		// 抢购需要的属性
		public String SoldID;
		public String mID;
		public String NewPrice;
		public String RemoveTime;
		public String OTime;

		/**
		 * 
		 * @param actualPrice
		 *            实际付款金额
		 * @param goodsCount
		 *            数量
		 * @param iD
		 *            明细订单号
		 * @param orderID
		 *            订单id
		 * @param price
		 *            应付金额
		 * @param status
		 *            状态
		 * @param title
		 *            商品名称
		 */
		public OrderInfo(String actualPrice, String goodsCount, String iD, String orderID, String price, String status, String title) {
			super();
			ActualPrice = actualPrice;
			GoodsCount = goodsCount;
			ID = iD;
			OrderID = orderID;
			Price = price;
			Status = status;
			Title = title;
		}

	}
}
