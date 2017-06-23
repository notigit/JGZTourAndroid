package com.highnes.tour.beans.my;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    收藏..旅游。
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
public class CollectYeyingInfo {

	public String status;
	public String Count;
	public List<CamkeepInfo> Camkeep;

	public static class CamkeepInfo {
		public String AddTime;
		public String ImageUrl;
		public String Name;
		public String City;
		public String ID;
		public String CampingID;
	}

}
