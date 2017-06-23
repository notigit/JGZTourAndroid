package com.highnes.tour.beans.home.tour;

import java.io.Serializable;
import java.util.List;

/**
 * <PRE>
 * 作用:
 *    旅游订单填写。
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
public class Tour2OrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String status;
	public String BabyPrice;
	public String GoDay;
	public String GoTime;
	public String HotelPrice;
	public String PID;
	public String PName;
	public String Price;
	public String SetAdd;
	public String TouID;
	public String TouTitle;
	public List<SafestInfo> Safest;
	public List<PeopleInfo> People;

	/**
	 * 
	 * @param status
	 * @param babyPrice
	 *            儿童价格
	 * @param goDay
	 *            套餐时长
	 * @param goTime
	 *            出发日期
	 * @param hotelPrice
	 *            单房差
	 * @param pID
	 *            套餐id
	 * @param pName
	 *            套餐名称
	 * @param price
	 *            成人价格
	 * @param setAdd
	 *            出发地
	 * @param touID
	 *            旅游产品ID
	 * @param touTitle
	 *            旅游标题
	 * @param safest
	 *            保险
	 * @param people
	 *            出行人
	 */
	public Tour2OrderInfo(String status, String babyPrice, String goDay, String goTime, String hotelPrice, String pID, String pName, String price,
			String setAdd, String touID, String touTitle, List<SafestInfo> safest, List<PeopleInfo> people) {
		super();
		this.status = status;
		BabyPrice = babyPrice;
		GoDay = goDay;
		GoTime = goTime;
		HotelPrice = hotelPrice;
		PID = pID;
		PName = pName;
		Price = price;
		SetAdd = setAdd;
		TouID = touID;
		TouTitle = touTitle;
		Safest = safest;
		this.People = people;
	}

	public static class SafestInfo {
		public String ID;
		public String Money;
		public String Name;
		public String Title;

		/**
		 * 保险
		 * 
		 * @param iD
		 *            id
		 * @param money
		 *            价格
		 * @param name
		 *            名称
		 * @param title
		 *            标题
		 */
		public SafestInfo(String iD, String money, String name, String title) {
			super();
			ID = iD;
			Money = money;
			Name = name;
			Title = title;
		}

	}

	public static class PeopleInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String PeoID;
		public String IDcard;
		public String PeoName;
		public String Phone;

		/**
		 * 
		 * @param iD
		 *            id
		 * @param iDcard
		 *            身份证
		 * @param name
		 *            姓名
		 * @param phone
		 *            手机
		 */
		public PeopleInfo(String iD, String iDcard, String name, String phone) {
			super();
			PeoID = iD;
			IDcard = iDcard;
			PeoName = name;
			Phone = phone;
		}

	}
}
