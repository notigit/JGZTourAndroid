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
public class Tour7DetailInfo {

	public String status;
	public String Count;
	public List<CommentInfo> Comment;
	public List<CampingInfo> Camping;
	public String Camkeep;

	public static class CommentInfo {
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

	public static class CampingInfo {
		public String Grades;
		public String ID;
		public String City;
		public String OldPrice;
		public String NewPrice;
		public String GoodsCount;
		public String Landmark;
		public String Latitude;
		public String Name;
		public String Pcount;
		public String Title;
		public String ServicePhone; // 服务电话
		public List<PhotoInfo> Photo;

		public static class PhotoInfo {
			public String PID;
			public String ImageUrl;

			/**
			 * 封面图
			 * 
			 * @param iD
			 * @param imageUrl
			 */
			public PhotoInfo(String iD, String imageUrl) {
				super();
				PID = iD;
				ImageUrl = imageUrl;
			}

		}
	}
}
