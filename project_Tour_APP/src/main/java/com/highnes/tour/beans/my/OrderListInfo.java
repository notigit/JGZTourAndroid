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
public class OrderListInfo {

	public String status;
	public String Count;
	public List<OrderInfo> Order; // 订单信息

	public static class OrderInfo {
		public String AddTime;
		public String Body;
		public Integer GoodsCount;
		public String ID;
		public String ImgUrl;
		public Double Money;
		public Double OutMoney;
		public String Status;
		public String Title;
		public String Type;

		/**
		 * 
		 * @param addTime
		 *            购买时间
		 * @param body
		 *            信息展示
		 * @param goodsCount
		 *            商品数量
		 * @param iD
		 *            商品ID
		 * @param imgUrl
		 *            图片地址
		 * @param money
		 *            应付金额
		 * @param outMoney
		 *            实付金额
		 * @param status
		 *            待付款等这些状态
		 * @param title
		 *            标题
		 * @param type
		 *            类型..周边表示PHP
		 */
		public OrderInfo(String addTime, String body, Integer goodsCount, String iD, String imgUrl, Double money, Double outMoney, String status, String title,
				String type) {
			super();
			AddTime = addTime;
			Body = body;
			GoodsCount = goodsCount;
			ID = iD;
			ImgUrl = imgUrl;
			Money = money;
			OutMoney = outMoney;
			Status = status;
			Title = title;
			Type = type;
		}

	}
}
