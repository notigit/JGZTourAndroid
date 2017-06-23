package com.highnes.tour.beans.find;

import java.util.List;

/**
 * <PRE>
 * 作用:
 *    发现..列表。
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
public class FindListInfo {

	public String status;
	public String Count;
	public List<NotesInfo> Notes;

	public static class NotesInfo {
		public String ID;
		public String ImageUrl;
		public String Name;
		public String AddTime;
	}
}
