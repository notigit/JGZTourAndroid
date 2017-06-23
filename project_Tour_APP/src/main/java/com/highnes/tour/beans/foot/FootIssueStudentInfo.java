package com.highnes.tour.beans.foot;

import java.util.List;

public class FootIssueStudentInfo {

	public String status;
	public List<DataInfo> data;

	public static class DataInfo {

		public String StuID;
		public String StuName;

		/**
		 * 
		 * @param stuID
		 *            学生编号
		 * @param stuName
		 *            学生姓名
		 */
		public DataInfo(String stuID, String stuName) {
			super();
			StuID = stuID;
			StuName = stuName;
		}

	}

}
