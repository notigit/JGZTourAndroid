package com.highnes.tour.beans.home.tour;

import java.util.List;

/**
 * <PRE>
 * 作用:
 * 		旅游度假..Banner。
 * 注意事项:
 * 		无。
 * 修改历史:
 * -----------------------------------------------------------
 * VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 * 1.0.0      2016-02-20   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class TourBannerInfo {
	public String status;
	public List<AdvertisementInfo> Advertisement;

	public static class AdvertisementInfo {
		public String Href;
		public String ID;
		public String JumpUrl;
		public String PhotoUrl;
		public Integer Sort;
		public Boolean Type;

		/**
		 * 广告轮播图规则 Type 参数： 原生 or H5 H5——JumpUrl跟地址 原生——栏目 和ID
		 * （如：门票，95e866aed3f546bd（AreaID））
		 * 
		 * <pre>
		 * href规则
		 * 1、门票：href[0]=门票，href[1]=景区ID
		 * 2、旅游度假：href[0]=旅游，href[1]=
		 * </pre>
		 * 
		 * @param href
		 * @param iD
		 *            广告ID
		 * @param jumpUrl
		 *            如果是原生 表示链接地址
		 * @param photoUrl
		 *            广告图地址
		 * @param sort
		 *            广告图排序
		 * @param type
		 *            true表示原生，false表示H5
		 */
		public AdvertisementInfo(String href, String iD, String jumpUrl, String photoUrl, Integer sort, Boolean type) {
			super();
			Href = href;
			ID = iD;
			JumpUrl = jumpUrl;
			PhotoUrl = photoUrl;
			Sort = sort;
			Type = type;
		}

	}
}
