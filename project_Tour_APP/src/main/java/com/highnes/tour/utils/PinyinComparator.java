package com.highnes.tour.utils;

import java.util.Comparator;

import com.highnes.tour.beans.CityInfo;
/**
 * <PRE>
 * 作用:
 *    好友列表选择。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2015-05-28   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class PinyinComparator implements Comparator<CityInfo> {

	public int compare(CityInfo o1, CityInfo o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
