package com.highnes.tour.beans.home.tour;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    旅游度假..详情。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-07   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TourDetailInfo {

	public String status;
	public List<CommInfo> goodCom;
	public List<PackageInfo> Package;
	public List<PhotoInfo> Photo;
	public List<TourismsInfo> Tourisms;
	public String Count;
	public String MinPrice;
	public String Toukeep;

	public static class CommInfo {
		/**
		 * 评论
		 */
		public String AddTime;
		public String CID;
		public String Contents;
		public String Grades;
		public String HeadImg;
		public String ImageUrl;
		public String UserName;
	}

	public static class PhotoInfo {
		public String ID;
		public String ImageUrl;

		/**
		 * 封面图
		 * 
		 * @param iD
		 * @param imageUrl
		 */
		public PhotoInfo(String iD, String imageUrl) {
			super();
			ID = iD;
			ImageUrl = imageUrl;
		}

	}

	public static class PackageInfo {
		public String ATime;
		public String OTime;
		public String PID;
		public String PName;
		public String Price;
		public String SetAdd;

		/**
		 * 旅游套餐
		 * 
		 * @param aTime
		 *            开始时间
		 * @param oTime
		 *            结束时间
		 * @param pID
		 * @param pName
		 * @param price
		 *            价格
		 * @param setAdd
		 *            出发点
		 */
		public PackageInfo(String aTime, String oTime, String pID, String pName, String price, String setAdd) {
			super();
			ATime = aTime;
			OTime = oTime;
			PID = pID;
			PName = pName;
			Price = price;
			SetAdd = setAdd;
		}

	}

	public static class TourismsInfo {
		public String Grades;
		public String ID;
		public String ImageUrl;
		public String Price;
		public String Rostered;
		public String Title;
		public String TourismType;
		public String TypeName;
		public Double OldPrice;
		public Boolean IsUpdate;
		public Boolean IsReserved;
		public Boolean IsRefunds;
		public Integer TimeLength; // 旅游时长
		public String ServicePhone; // 服务电话

		/**
		 * 本地景点
		 * 
		 * @param grades
		 *            满意度
		 * @param iD
		 * @param imageUrl
		 *            封面图
		 * @param price
		 *            起价
		 * @param rostered
		 *            班期
		 * @param title
		 *            标题
		 * @param tourismType
		 *            旅游类型
		 * @param typeName
		 *            景点类型
		 */
		public TourismsInfo(String grades, String iD, String imageUrl, String price, String rostered, String title, String tourismType, String typeName) {
			super();
			Grades = grades;
			ID = iD;
			ImageUrl = imageUrl;
			Price = price;
			Rostered = rostered;
			Title = title;
			TourismType = tourismType;
			TypeName = typeName;
		}

	}
}
