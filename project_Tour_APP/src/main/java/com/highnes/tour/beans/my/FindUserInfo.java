package com.highnes.tour.beans.my;

import java.util.List;

import com.highnes.tour.utils.StringUtils;

/**
 * <PRE>
 * 作用:
 *    个人信息。
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
public class FindUserInfo {
	public String status;
	public List<DataInfo> data;

	public static class DataInfo {
		public String Birth;
		public String Email;
		public String GradeName;
		public String HeadImg;
		public String UserName;
		public Integer Ponit;
		public String UserID;
		public String Name;
		public String Sex;

		public Integer getSex() {
			return !StringUtils.isEmpty(Sex) && Sex.equals("女") ? 0 : 1;
		}

		/**
		 * 
		 * @param iD
		 *            用户ID
		 * @param areaName
		 *            社区名称
		 * @param email
		 *            邮箱
		 * @param hnumber
		 *            门牌号
		 * @param name
		 *            姓名
		 * @param phone
		 *            手机号
		 * @param qQ
		 *            qq
		 * @param ImageUrl
		 *            头像
		 * @param sex
		 *            性别
		 */
		public DataInfo(String birth, String email, String gradeName, String userName, Integer ponit, String userID, String name, String headImg, String sex) {
			super();
			Birth = birth;
			Email = email;
			GradeName = gradeName;
			UserName = userName;
			Ponit = ponit;
			UserID = userID;
			Name = name;
			HeadImg = headImg;
			Sex = sex;
		}

	}

}
