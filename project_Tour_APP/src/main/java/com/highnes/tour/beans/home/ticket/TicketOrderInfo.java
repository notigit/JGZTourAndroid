package com.highnes.tour.beans.home.ticket;

import java.io.Serializable;
import java.util.List;

/**
 * <PRE>
 * 作用:
 *    门票订单填写。
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
public class TicketOrderInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String status;
	public List<RedPacketsInfo> RedPackets;
	public List<SafestInfo> Safest;
	public List<PeopleInfo> people;
	public List<SoldInfo> sold;

	public static class RedPacketsInfo {
		// TODO 红包
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

	public static class PeopleInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String ID;
		public String IDcard;
		public String Name;
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
			ID = iD;
			IDcard = iDcard;
			Name = name;
			Phone = phone;
		}

	}

	public static class SoldInfo {
		public String SID;
		public Double NewPrice;
		public Double OldPrice;
		public String SName;
		public String SUserID;
		public String SUserName;
		public String TID;
		public String TName;

		/**
		 * 
		 * @param sID
		 * @param newPrice
		 * @param oldPrice
		 * @param sName
		 * @param sUserID
		 * @param sUserName
		 * @param tID
		 * @param tName
		 */
		public SoldInfo(String sID, Double newPrice, Double oldPrice, String sName, String sUserID, String sUserName, String tID, String tName) {
			super();
			SID = sID;
			NewPrice = newPrice;
			OldPrice = oldPrice;
			SName = sName;
			SUserID = sUserID;
			SUserName = sUserName;
			TID = tID;
			TName = tName;
		}

	}
}
