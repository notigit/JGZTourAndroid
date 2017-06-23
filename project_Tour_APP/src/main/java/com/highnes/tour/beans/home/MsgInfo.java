package com.highnes.tour.beans.home;

import java.io.Serializable;
import java.util.List;

/**
 * <PRE>
 * 作用:
 *    消息。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-20   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class MsgInfo implements Serializable {
	public String status;
	public String Count;
	public List<NoticeInfo> Notice;

	public static class NoticeInfo implements Serializable {
		public String ID;
		public String NoticeID;
		public String Time;
		public String Title;
		public Boolean IsRead;

	}

}
