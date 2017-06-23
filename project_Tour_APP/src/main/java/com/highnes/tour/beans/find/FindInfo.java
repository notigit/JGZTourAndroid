package com.highnes.tour.beans.find;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    发现的接口汇总。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-10   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class FindInfo {

	public String status;
	public List<AdvertisementInfo> Advertisement;
	public List<NotesTypeInfo> NotesType;

	public static class AdvertisementInfo {
		public String ID;
		public String Href;
		public String JumpUrl;
		public String PhotoUrl;
		public Integer Sort;
		public Boolean Type;

		/**
		 * 轮播图
		 * 
		 * @param iD
		 *            资源id
		 * @param jumpUrl
		 *            跳转地址
		 * @param photoUrl
		 *            图片地址
		 * @param sort
		 *            排序
		 */
		public AdvertisementInfo(String iD, String jumpUrl, String photoUrl, Integer sort) {
			super();
			ID = iD;
			JumpUrl = jumpUrl;
			PhotoUrl = photoUrl;
			Sort = sort;
		}

	}

	public static class NotesTypeInfo {
		public String TypeID;
		public String TypeName;
		public List<NotesInfo> Notes;

		public static class NotesInfo {
			public String ID;
			public String ImageUrl;
			public String Name;
		}
	}
}
