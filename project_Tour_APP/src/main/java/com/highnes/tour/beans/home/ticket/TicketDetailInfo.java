package com.highnes.tour.beans.home.ticket;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    首页..门票..详情。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-17   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TicketDetailInfo {

	public String status;
	public String AreaID;
	public String AreaName;
	public String City;
	public String Comcount;
	public String Landmark;
	public String Latitude;
	public String PayPhone;
	public String SoldTime;
	public String soldkeep;
	public List<ImageUrlsInfo> ImageUrls;
	public List<GoodComInfo> goodCom;
	public List<SoldTypeInfo> soldType;

	/**
	 * 
	 * @param status
	 *            状态值
	 * @param areaID
	 *            景区ID
	 * @param areaName
	 *            景区名称
	 * @param city
	 *            景区地址
	 * @param comcount
	 *            评论的数量
	 * @param landmark
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param payPhone
	 *            联系电话
	 * @param soldTime
	 *            景区时间
	 * @param soldkeep
	 *            是否收藏 False表示没有
	 * @param imageUrls
	 *            轮播图
	 * @param goodCom
	 *            评论列表
	 * @param soldType
	 *            购票
	 */
	public TicketDetailInfo(String status, String areaID, String areaName, String city, String comcount, String landmark, String latitude, String payPhone,
			String soldTime, String soldkeep, List<ImageUrlsInfo> imageUrls, List<GoodComInfo> goodCom, List<SoldTypeInfo> soldType) {
		super();
		this.status = status;
		AreaID = areaID;
		AreaName = areaName;
		City = city;
		Comcount = comcount;
		Landmark = landmark;
		Latitude = latitude;
		PayPhone = payPhone;
		SoldTime = soldTime;
		this.soldkeep = soldkeep;
		ImageUrls = imageUrls;
		this.goodCom = goodCom;
		this.soldType = soldType;
	}

	public static class ImageUrlsInfo {
		public String ImageUrl;
		public String PID;
	}

	public static class GoodComInfo {
		public String AddTime;
		public String CID;
		public String Contents;
		public String Grades;
		public String HeadImg;
		public String ImageUrl;
		public String UserName;
	}

	public static class SoldTypeInfo {
		public String Details;
		public String Type;
		public List<Solds> solds;

		/**
		 * 
		 * @param details
		 *            介绍
		 * @param type
		 *            类型
		 * @param solds
		 */
		public SoldTypeInfo(String details, String type, List<Solds> solds) {
			super();
			Details = details;
			Type = type;
			this.solds = solds;
		}

		public static class Solds {
			public String ClienteleUser;
			public String IsRefunds;
			public String IsReserved;
			public String IsUpdate;
			public String NewPrice;
			public String OldPrice;
			public String SID;
			public String SType;
			public String SoldName;
			public String Title;

			/**
			 * 门票
			 * 
			 * @param clienteleUser
			 *            是自营还是代理
			 * @param isRefunds
			 *            退
			 * @param isReserved
			 *            订
			 * @param isUpdate
			 *            改
			 * @param newPrice
			 *            现价
			 * @param oldPrice
			 *            原价
			 * @param sID
			 *            门票id
			 * @param sType
			 *            门票类型
			 * @param soldName
			 *            景区名称
			 * @param title
			 *            标题
			 */
			public Solds(String clienteleUser, String isRefunds, String isReserved, String isUpdate, String newPrice, String oldPrice, String sID,
					String sType, String soldName, String title) {
				super();
				ClienteleUser = clienteleUser;
				IsRefunds = isRefunds;
				IsReserved = isReserved;
				IsUpdate = isUpdate;
				NewPrice = newPrice;
				OldPrice = oldPrice;
				SID = sID;
				SType = sType;
				SoldName = soldName;
				Title = title;
			}

		}
	}

}
