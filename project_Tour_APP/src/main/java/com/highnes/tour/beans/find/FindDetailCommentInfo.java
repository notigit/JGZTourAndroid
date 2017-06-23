package com.highnes.tour.beans.find;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    发现..评论列表。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-09-13   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FindDetailCommentInfo {

	public String status;
	public String CommentCount;
	public String IsTing;
	public String ThingCount;
	public List<NotesCommentInfo> NotesComment;

	public static class NotesCommentInfo {
		public String ComAddTime;
		public String ComDetails;
		public String ComID;
		public String HeadImg;
		public String UserID;
		public String UserName;

		public NotesCommentInfo(String comAddTime, String comDetails, String headImg, String userName) {
			super();
			ComAddTime = comAddTime;
			ComDetails = comDetails;
			HeadImg = headImg;
			UserName = userName;
		}

	}
}
