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
public class CollectTourInfo {

	public String status;
	public String Count;
	public List<TourismkeepInfo> Tourismkeep;

	public static class TourismkeepInfo {
		public String ImageUrl;
		public String Title;
		public String AddTime;
		public String TourismID;
		public String ID;
	}

}
