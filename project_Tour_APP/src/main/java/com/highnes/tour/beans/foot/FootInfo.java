package com.highnes.tour.beans.foot;

import java.util.List;

public class FootInfo {

	public String status;
	public List<DataInfo> data;
	public String size;
	public List<StudentInfo> student;

	/**
	 * 
	 * @param status
	 *            状态值 0表示没有数据 1表示正常
	 * @param data
	 *            数据
	 * @param size
	 *            总页数
	 */
	public FootInfo(String status, List<DataInfo> data, String size, List<StudentInfo> student) {
		super();
		this.status = status;
		this.data = data;
		this.size = size;
		this.student = student;
	}

	public static class DataInfo {

		public String AddTime;
		public String CommentCount;
		public String Contents;
		public String ID;
		public String ImageUrls;
		public String ThingCount;
		public String UserID;
		public String UserImageUrl;
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
		public DataInfo(String addTime, String commentCount, String contents, String iD, String imageUrls, String thingCount, String userID,
				String userImageUrl, String userName) {
			super();
			AddTime = addTime;
			CommentCount = commentCount;
			Contents = contents;
			ID = iD;
			ImageUrls = imageUrls;
			ThingCount = thingCount;
			UserID = userID;
			UserImageUrl = userImageUrl;
			UserName = userName;
		}

	}

	public static class StudentInfo {
		public String HeadImage;
	}
}
