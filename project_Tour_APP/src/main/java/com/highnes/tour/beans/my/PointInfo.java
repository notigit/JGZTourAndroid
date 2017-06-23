package com.highnes.tour.beans.my;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    积分。
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
public class PointInfo {

	public String status;
	public String PCount;
	public String SumCount;
	public List<UserPointInfo> UserPoint;

	public static class UserPointInfo {
		public String AddTime;
		public Integer Count;
		public String ID;
		public String Name;
		public String Source;
		public Integer UpDown;
	}

}
