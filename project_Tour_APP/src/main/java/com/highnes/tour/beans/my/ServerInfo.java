package com.highnes.tour.beans.my;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    服务信息。
 * 注意事项:
 *    。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-08-25   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ServerInfo {

	public String status;
	public String Count;
	public List<DataInfo> data;

	public static class DataInfo {
		public String ID;
		public String IsThing;
		public String Tcount;
		public String Title;
	}
}
