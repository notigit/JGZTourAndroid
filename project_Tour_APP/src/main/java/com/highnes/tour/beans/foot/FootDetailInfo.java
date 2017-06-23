package com.highnes.tour.beans.foot;

import java.util.List;

public class FootDetailInfo {

	public String status;
	public List<DataInfo> data;
	public List<CommentInfo> comment;
	public String ThingCount;
	public String Count;
	public String IsThing;


	public static class DataInfo {

		public String AddName;
		public String AddTime;
		public String Contents;
		public String ID;
		public String ImageUrls;
		public String UserID;
		public String UserHeadImg;
		public String UserName;

		/**
		 * 
		 * @param AddTime
		 *            时间
		 * @param commentCount
		 *            评论数量
		 * @param contents
		 *            内容
		 * @param iD
		 *            消息id
		 * @param imageUrls
		 *            消息图片逗号分隔
		 * @param thingCount
		 *            点赞数量
		 * @param userID
		 *            发布者id
		 * @param userImageUrl
		 *            发布者头像
		 * @param userName
		 *            发布者昵称
		 */
		public DataInfo(String addTime,String contents, String iD, String imageUrls, String userID,
				String userImageUrl, String userName) {
			super();
			AddTime = addTime;
			Contents = contents;
			ID = iD;
			ImageUrls = imageUrls;
			UserID = userID;
			UserHeadImg = userImageUrl;
			UserName = userName;
		}

	}

	public static class CommentInfo {
		public String UAddTime;
		public String UContents;
		public String CommentID;
		public String UID;
		public String UHeadImg;
		public String UName;
	}
}
