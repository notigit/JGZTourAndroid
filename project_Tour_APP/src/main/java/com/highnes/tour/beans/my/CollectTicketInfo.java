package com.highnes.tour.beans.my;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    收藏..门票。
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
public class CollectTicketInfo {

	public String status;
	public String Count;
	public List<SoldkeepInfo> Soldkeep;

	public static class SoldkeepInfo {
		public String ImageUrl;
		public String City;
		public String AddTime;
		public String AreaID;
		public String AreaName;
		public String ID;
		public String Star;
	}

}
