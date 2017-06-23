package com.highnes.tour.conf;

/**
 * <PRE>
 * 作用:
 *    用户信息的设置。
 * 注意事项:
 *    1、注意中间是使用的逗号，最后才使用分号。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-16   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public enum PreSettings {
	// ----------------------------------------用户的信息设置----------------------------------------
	/** 用户登录身份 0表示游客，1表示普通账号登录int */
	USER_TYPE("com.highnes.tour_user_type", 0),
	/** 用户ID String */
	USER_ID("com.highnes.tour_user_id", ""),
	/** 用户PHP_ID int */
	USER_NUNBERID("com.highnes.tour_user_numberid", 0),
	/** 用户手机号码 String */
	USER_PHONE("com.highnes.tour_user_phone", ""),
	/** 用户头像 String */
	USER_AVATOR("com.highnes.tour_user_avator", ""),
	/** 用户姓名 String */
	USER_NAME("com.highnes.tour_user_name", ""),
	/** 用户昵称 String */
	USER_NICKNAME("com.highnes.tour_user_nickname", ""),
	/** 用户性别 0表示女，1表示男 int */
	USER_SEX("com.highnes.tour_user_sex", 0),
	/** email String */
	USER_EMAIL("com.highnes.tour_user_email", ""),
	/** birthday String */
	USER_BIRTHDAY("com.highnes.tour_user_birthday", ""),
	/** 密码 md5 String */
	USER_PWD("com.highnes.tour_user_pwd", ""),
	/** 积分 int */
	USER_POINT("com.highnes.tour_user_point", 0),
	/** 纬度 String */
	USER_LAT("com.highnes.tour_user_lat", "29.621391"),
	/** 经度 String */
	USER_LNG("com.highnes.tour_user_lng", "106.491239"),
	/** 当前定位的城市 String */
	USER_CITY("com.highnes.tour_user_city", "重庆市"),
	/** 当前选择的城市 String */
	USER_CITY_SELECT("com.highnes.tour_user_city_select", "重庆市"),

	// ----------------------------------------APP的信息设置----------------------------------------
	/** 是否是第一次安装APP应用，用于判断是否进入引导页 boolean true表示是 */
	APP_FIRST_IN("com.highnes.tour_app_first_in", Boolean.TRUE),
	/** 上一个版本的版本号，用于判断是否进入引导页 int */
	APP_VER_LAST("com.highnes.tour_app_ver_last", 100),

	// ----------------------------------------网络信息的设置----------------------------------------
	/** 服务器地址 外网 */
	
	URL_SERVER("com.highnes.tour_url_server", "http://app.jgz1618.com"),
//	URL_SERVER("com.highnes.tour_url_server", "http://101.201.121.41:9090"),
//	URL_SERVER("com.highnes.tour_url_server", "http://123.56.101.173:9095"),
//	 URL_SERVER("com.highnes.tour_url_server", "http://192.168.0.123"),
	// 商城地址
	URL_SERVER_SHOP("com.highnes.tour_url_server_shop", "http://hotel.jgz1618.com");
//	URL_SERVER_SHOP("com.highnes.tour_url_server_shop", "http://101.201.121.41:8888");
//	URL_SERVER_SHOP("com.highnes.tour_url_server_shop", "http://ly.micmark.com");
	// URL_SERVER_SHOP("com.highnes.tour_url_server_shop", "http://192.168.0.102");

	private final String id;
	private final Object defaultValue;

	/**
	 * @param id
	 *            key值
	 * @param defaultValue
	 *            默认值
	 */
	private PreSettings(String id, Object defaultValue) {
		this.id = id;
		this.defaultValue = defaultValue;
	}

	/**
	 * 获取key
	 * 
	 * @return key
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 获取默认的值
	 * 
	 * @return 该key对应的值
	 */
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	public static PreSettings fromId(String id) {
		PreSettings[] values = values();
		int cc = values.length;
		for (int i = 0; i < cc; i++) {
			if (values[i].id == id) {
				return values[i];
			}
		}
		return null;
	}
}
