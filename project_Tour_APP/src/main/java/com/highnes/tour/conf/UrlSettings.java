package com.highnes.tour.conf;

/**
 * <PRE>
 * 作用:
 *    常量参数。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-16   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class UrlSettings {

	// ----------------------------数据通信接口----------------------------------------------------
	/** 服务器地址 */
	public static final String URL_ = PreSettings.URL_SERVER.getDefaultValue().toString() + "/LYInterface";
	/** 服务器地址 */
	public static final String URL_SHOP = PreSettings.URL_SERVER_SHOP.getDefaultValue().toString() + "/mall";
	/** 图片地址 */
	public static final String URL_IMAGE = PreSettings.URL_SERVER.getDefaultValue().toString();
	/** 支付地址 */
	public static final String URL_PAY = PreSettings.URL_SERVER.getDefaultValue().toString();
	/** APK地址 */
	public static final String URL_APK = PreSettings.URL_SERVER.getDefaultValue().toString();
	/** H5地址 */
	public static final String URL_HTML = PreSettings.URL_SERVER.getDefaultValue().toString();

	/** 门票分享 */
	public static final String URL_SHARE_TICKET = URL_ + "/SoldPartook?ID=%s&UserID=%s";
	/** 旅游分享 */
	public static final String URL_SHARE_TOUR = URL_ + "/TouPartook?ID=%s&UserID=%s";
	/** 发现分享 */
	public static final String URL_SHARE_FIND = URL_ + "/NotesPartook?ID=%s&UserID=%s";

	/** 首页汇总接口-通用（轮播图、通知、活动、抢购信息、门票） */
	public static final String URL_HOME_ALL = URL_ + "/AllHomepage";
	/** 首页汇总接口-城市（轮播图、通知、活动、抢购信息、门票） */
	public static final String URL_HOME_ALL_CITY = URL_ + "/AllHomepageByAdd";
	/** 首页..活动详情H5 */
	public static final String URL_H5_HOMEE_ACTIVITIES_DETAIL = URL_ + "/BindContents?id=";
	/** 首页..门票 */
	public static final String URL_HOME_TICKET = URL_ + "/FindSoldOrType";
	/** 首页..门票..搜索 */
	public static final String URL_HOME_TICKET_SEARCH = URL_ + "/FindSoldByText";
	/** 首页..门票..详情 */
	public static final String URL_HOME_TICKET_DETAIL = URL_ + "/FindSoldByID";
	/** 首页..门票..筛选..菜单 */
	public static final String URL_HOME_TICKET_FILTER_MENU = URL_ + "/FindSiftings";
	/** 首页..门票..筛选..列表 */
	public static final String URL_HOME_TICKET_FILTER_LIST = URL_ + "/FindSoldBySif";
	/** 首页..门票..筛选..本地景点 */
	public static final String URL_HOME_TICKET_FILTER_LOC = URL_ + "/FindSolde";
	/** 首页..门票..筛选..周边景点 */
	public static final String URL_HOME_TICKET_FILTER_NEARBY = URL_ + "/NearSolde";
	/** 首页..门票..详情..图文H5 */
	public static final String URL_H5_HOME_TICKET_DETAIL = URL_ + "/FindSoldDetails?AreaID=";
	/** 首页..门票..详情..收藏与取消收藏 */
	public static final String URL_HOME_TICKET_DETAIL_COLLECT = URL_ + "/AddSoldkeep";
	/** 首页..门票..详情..票型说明H5 */
	public static final String URL_H5_HOME_TICKET_INFO = URL_ + "/FindSoldType?";
	/** 首页..门票..详情..门票信息选择 */
	public static final String URL_HOME_TICKET_DETAIL_INFO = URL_ + "/AddSoldOrder1";
	/** 首页..门票..详情..当前门票的保险类型 */
	public static final String URL_HOME_TICKET_DETAIL_SAFEST_LIST = URL_ + "/FindSoldSafest";
	/** 首页..门票..详情..当前门票的说明 */
	public static final String URL_H5_HOME_TICKET_DETAIL_SAFEST_INFO = URL_ + "/FindSafestByID?id=";
	/** 首页..门票..详情..提交订单 */
	public static final String URL_HOME_TICKET_DETAIL_ORDER = URL_ + "/AddOrderDeatil";
	/** 首页..门票..详情..剩余的门票 */
	public static final String URL_HOME_TICKET_COUNT = URL_ + "/FindSurplusSold";
	/** 首页..抢购..详情..剩余的门票 */
	public static final String URL_HOME_TIME_COUNT = URL_ + "/FindSurplusGrabSold";

	/** 首页..旅游度假..Banner */
	public static final String URL_HOME_TOUR_BANNER = URL_ + "/FindTourismType";
	/** 首页..旅游度假..景区门票..景区门票和人气排行 */
	public static final String URL_HOME_TOUR_0 = URL_ + "/FindHotSold";
	/** 首页..旅游度假..周边游..推荐、本地景点及周边景点 */
	public static final String URL_HOME_TOUR_1 = URL_ + "/FindTourismByType";
	/** 首页..旅游度假..周边游..筛选..菜单 */
	public static final String URL_HOME_TOUR_1_FILTER_MENU = URL_ + "/FindTouSiftings";
	/** 首页..旅游度假..周边游..筛选..列表 */
	public static final String URL_HOME_TOUR_1_FILTER_LIST = URL_ + "/FindTouBySif";
	/** 首页..旅游度假..周边游..筛选..列表..推荐 */
	public static final String URL_HOME_TOUR_1_FILTER_LIST_AREA = URL_ + "/FindTourismsByTouArea";
	/** 首页..旅游度假..周边游..筛选..列表..本地热门 */
	public static final String URL_HOME_TOUR_1_FILTER_LIST_LOC = URL_ + "/FindTourismByCity";
	/** 首页..旅游度假..周边游..筛选..列表..周边热门 */
	public static final String URL_HOME_TOUR_1_FILTER_LIST_NEARBY = URL_ + "/FindTourismByLandmark";
	/** 首页..旅游度假..国内游..人气排行榜、热门城市和热门国内游 */
	public static final String URL_HOME_TOUR_2 = URL_ + "/FingTouAreaByType";
	/** 首页..旅游度假..国内游..筛选..菜单 */
	public static final String URL_HOME_TOUR_2_FILTER_MENU = URL_ + "/FindTouSifting";
	/** 首页..旅游度假..国内游..筛选..列表 */
	public static final String URL_HOME_TOUR_2_FILTER_LIST = URL_ + "/FindTouBySifs";
	/** 首页..旅游度假..国内游..筛选..列表..人气 */
	public static final String URL_HOME_TOUR_2_FILTER_LIST_AREA = URL_ + "/FindTourismsByTouArea";
	/** 首页..旅游度假..国内游..筛选..列表..城市 */
	public static final String URL_HOME_TOUR_2_FILTER_LIST_CITY = URL_ + "/FindTourismsByArea";
	/** 首页..旅游度假..国外游..人气排行榜和热门国内游 */
	public static final String URL_HOME_TOUR_3 = URL_ + "/FindForeignTou";
	/** 首页..旅游度假..国外游..筛选..列表..人气 */
	public static final String URL_HOME_TOUR_3_FILTER_LIST_AREA = URL_ + "/FindTourismsByAreaName";
	/** 首页..旅游度假..自驾游..自驾游 */
	public static final String URL_HOME_TOUR_4 = URL_ + "/FindSelfdriveTou";
	/** 首页..旅游度假..自由行..热门自由行 */
	public static final String URL_HOME_TOUR_5 = URL_ + "/FindFreeTou";
	/** 首页..旅游度假..跟团游..推荐和热门 */
	public static final String URL_HOME_TOUR_6 = URL_ + "/FindTeamTou";
	/** 首页..旅游度假..跟团游..筛选..列表..人气 */
	public static final String URL_HOME_TOUR_6_FILTER_LIST_AREA = URL_ + "/FindTouFoCity";
	/** 首页..旅游度假..野营 */
	public static final String URL_HOME_TOUR_7 = URL_ + "/FindCamping";
	/** 首页..旅游度假..野营 */
	public static final String URL_HOME_TOUR_7_MORE = URL_ + "/FindCampings";
	/** 首页..旅游度假..野营..详情 */
	public static final String URL_HOME_TOUR_7_DETAIL = URL_ + "/FindCampingDis";
	/** 首页..旅游度假..旅游产品详情..行程H5 */
	public static final String URL_HOME_TOUR_7_DETAIL_INFO = URL_ + "/FindCampingDiscount?id=";
	/** 首页..旅游度假..旅游产品详情..费用H5 */
	public static final String URL_HOME_TOUR_7_DETAIL_INFO2 = URL_ + "/FindCampingExplain?id=";
	/** 首页..旅游度假..旅游产品详情..收藏与取消收藏 */
	public static final String URL_HOME_TOUR_7_DETAIL_COLLECT = URL_ + "/AddCamkeep";
	/** 首页..旅游度假..旅游产品详情..立即预订 */
	public static final String URL_HOME_TOUR_7_DETAIL_ADDORDER = URL_ + "/AddCampingOrder1";
	/** 首页..旅游度假..旅游产品详情..提交订单 */
	public static final String URL_HOME_TOUR_7_DETAIL_ORDERSURE = URL_ + "/AddCampingOrder2";
	/** 首页..旅游度假..旅游产品详情 */
	public static final String URL_HOME_TOUR_DETAIL = URL_ + "/FindTourismDetails";
	/** 首页..旅游度假..旅游产品详情..行程H5 */
	public static final String URL_HOME_TOUR_DETAIL_INFO = URL_ + "/FindTouDetails?id=";
	/** 首页..旅游度假..旅游产品详情..费用H5 */
	public static final String URL_HOME_TOUR_DETAIL_INFO2 = URL_ + "/FindPackageDetails?id=";
	/** 首页..旅游度假..旅游产品详情..收藏与取消收藏 */
	public static final String URL_HOME_TOUR_DETAIL_COLLECT = URL_ + "/AddToukeep";
	/** 首页..旅游度假..旅游产品详情..立即预订 */
	public static final String URL_HOME_TOUR_DETAIL_ADDORDER = URL_ + "/AddTouOrder";
	/** 首页..旅游度假..旅游产品详情..立即预订的保险类型 */
	public static final String URL_HOME_TOUR_DETAIL_SAFEST_LIST = URL_ + "/FindTouSafest";
	/** 首页..旅游度假..旅游产品详情..立即预订的说明 */
	public static final String URL_HOME_TOUR_DETAIL_SAFEST_INFO = URL_ + "/FindTouSafestByID?id=";
	/** 首页..旅游度假..旅游产品详情..提交订单 */
	public static final String URL_HOME_TOUR_DETAIL_ORDER = URL_ + "/AddTouOrder2";
	/** 首页..旅游度假..旅游产品详情..单房差 */
	public static final String URL_HOME_TOUR_DETAIL_ORDER_HOUSE = URL_ + "/FindSingle";
	/** 首页..旅游度假..剩余的票 */
	public static final String URL_HOME_TOUR_DETAIL_COUNT = URL_ + "/FindSurplusTou";

	/** 首页..定位..热门城市 */
	public static final String URL_HOME_CITY = URL_ + "/FindHitCity";
	/**
	 * 美食
	 */
	public static final String URL_HOME_MEISHI = "https://m.nuomi.com/cq/326/0-0/0-0-0-0-0";

	/** 首页..快捷菜单..违章查询H5 */
	// public static final String URL_H5_HOME_WZCX =
	// "http://traffic.lashare.cn/vehicleviolation";
//	public static final String URL_H5_HOME_WZCX = "http://m.cheshouye.com/api/weizhang/?t=ff9600";
	public static final String URL_H5_HOME_WZCX = "http://m.weizhang8.cn";
	/** 首页..快捷菜单..ETC查询H5 */
	// public static final String URL_H5_HOME_ETC =
	// "http://cqetcweixin.u-road.com/CQETCWechatAPIServer/mainserver/main?from=singlemessage&isappinstalled=0";
	public static final String URL_H5_HOME_ETC = "https://www.gs12122.com/index.html";
	/** 首页..快捷菜单..资费查询H5 */
	public static final String URL_H5_HOME_ZCCX = "http://cegc.com.cn:8080/";
	/** 首页..快捷菜单..航班动态H5 */
//	public static final String URL_H5_HOME_HBDT = "http://traffic.lashare.cn/flightdynamics";
	public static final String URL_H5_HOME_HBDT = "http://flights.ctrip.com/actualtime";
	/** 首页..快捷菜单..天气查询H5 */
//	public static final String URL_H5_HOME_TQCX = "http://wx.weather.com.cn/mweather/101040700.shtml#1";
	public static final String URL_H5_HOME_TQCX = "http://wx.weather.com.cn";
//	public static final String URL_H5_HOME_TQCX = "https://tianqi.moji.com/";

	/** 首页..特惠头条列表 */
	public static final String URL_HOME_NEWS = URL_ + "/NewsAll";
	/** 首页..特惠头条列表..详情 */
	public static final String URL_H5_HOME_NEWS_DETAIL = URL_ + "/FindNewsContents?NewsID=";
	/** 首页..消息列表 */
	public static final String URL_HOME_MSG = URL_ + "/FindNotice";
	/** 首页..消息..未读数量 */
	public static final String URL_HOME_MSG_COUNT = URL_ + "/FindNotCount";
	/** 首页..消息列表 */
	public static final String URL_H5_HOME_MSG_DETAIL = URL_ + "/FindNoticeRemark?UserID=%s&NoticeID=%s";
	/** 首页..消息列表..删除 */
	public static final String URL_HOME_MSG_DEL = URL_ + "/DeleteNotice";

	/** 首页..限时抢购..列表 */
	public static final String URL_HOME_TIME_LIST = URL_ + "/FindBenefits";
	/** 首页..限时抢购..列表..详情 */
	public static final String URL_HOME_TIME_LIST_DETAIL = URL_ + "/FindGrabSoldByID";
	/** 首页..限时抢购..列表..详情..提交订单 */
	public static final String URL_HOME_TIME_LIST_DETAIL_ORDER = URL_ + "/AddGrabSoldOrder1";
	/** 首页..限时抢购..列表..详情..提交订单..立即提交 */
	public static final String URL_HOME_TIME_LIST_DETAIL_ORDER_SUBMIT = URL_ + "/AddOrderGrab";

	/** 发现 */
	public static final String URL_FIND = URL_ + "/FindNotes";
	/** 发现..列表 */
	public static final String URL_FIND_LIST = URL_ + "/FindNotesByType";
	/** 发现..攻略..简单介绍 */
	public static final String URL_H5_FIND_TACK0 = URL_ + "/FindTasteDetails?City=";
	/** 发现..攻略..景点 */
	public static final String URL_H5_FIND_TACK1 = URL_ + "/FindTasteArea?City=";
	/** 发现..攻略..美食 */
	public static final String URL_H5_FIND_TACK2 = URL_ + "/FindTasteDelicacy?City=";
	/** 发现..攻略..住宿 */
	public static final String URL_H5_FIND_TACK3 = URL_ + "/FindTasteHotel?City=";
	/** 发现..攻略..娱乐 */
	public static final String URL_H5_FIND_TACK4 = URL_ + "/FindTasteAmuse?City=";
	/** 发现..详情..图文H5 */
	public static final String URL_H5_FIND_DETAIL = URL_ + "/FindNotesDetails?ID=";
	/** 发现..详情..推荐门票 */
	public static final String URL_FIND_DETAIL_TICKET = URL_ + "/FindSoldByCity";
	/** 发现..详情..添加评论 */
	public static final String URL_FIND_DETAIL_COMMENT_ADD = URL_ + "/AddNotesComment";
	/** 发现..详情..查询评论 */
	public static final String URL_FIND_DETAIL_COMMENT_QUERY = URL_ + "/FindNotesByID";
	/** 发现..详情..点赞 */
	public static final String URL_FIND_DETAIL_OK = URL_ + "/AddNotesThing";

	/** 足迹..足迹圈 */
	public static final String URL_FOOT_GROUP = URL_ + "/FindTribuneAll";
	/** 足迹..足迹圈..详情 */
	public static final String URL_FOOT_GROUP_DETAIL = URL_ + "/FindTribuneByID";
	/** 足迹..足迹圈..详情..添加评论 */
	public static final String URL_FOOT_GROUP_DETAIL_COMMENT_ADD = URL_ + "/AddComment";
	/** 足迹..足迹圈..详情..点赞 */
	public static final String URL_FOOT_GROUP_DETAIL_OK = URL_ + "/AddTribuneThing";
	/** 足迹..足迹圈..详情..举报 */
	public static final String URL_FOOT_GROUP_DETAIL_REP = URL_ + "/AddDcount";
	/** 足迹..足迹圈..详情..删除 */
	public static final String URL_FOOT_GROUP_DETAIL_DEL = URL_ + "/DeleteTribuneByID";
	/** 足迹..我的足迹 */
	public static final String URL_FOOT_MY = URL_ + "/FindTribuneByUser";
	/** 足迹..发布 */
	public static final String URL_FOOT_ISSUE = URL_ + "/AddTribune";

	/** 个人中心..注册 */
	public static final String URL_USER_REG = URL_ + "/AddUser";
	/** 个人中心..注册..协议 */
	public static final String URL_H5_USER_REG_TALK = URL_ + "/Agreement";
	/** 个人中心..注册..验证码 */
	public static final String URL_USER_REG_CODE = URL_ + "/FindNumber";
	/** 个人中心..登录 */
	public static final String URL_USER_LOGIN = URL_ + "/Login";
	/** 个人中心..登录2 */
	public static final String URL_USER_LOGIN_PHONE = URL_ + "/LoginByNumber";
	/** 个人中心..查看个人资料 */
	public static final String URL_USER_FINDUSER = URL_ + "/FindUser";
	/** 个人中心..修改个人资料 */
	public static final String URL_USER_UPDATEUSER = URL_ + "/UpdateUser";
	/** 个人中心..修改头像 */
	public static final String URL_USER_UPDATEAVATOR = URL_ + "/updateUserImage";
	/** 个人中心..客服列表 */
	public static final String URL_USER_SERVICE = URL_ + "/FindAdviceService";
	/** 个人中心..客服列表..详情 */
	public static final String URL_H5_USER_SERVICE_Detail = URL_ + "/FindAdviceServiceByID?AdviceID=%s";
	/** 个人中心..客服列表..点赞 */
	public static final String URL_USER_SERVICE_ZAN = URL_ + "/AddServiceUser";
	/** 个人中心..常用信息..旅客列表 */
	public static final String URL_USER_COMMON_USERINFO_LIST = URL_ + "/FindUserPeople";
	/** 个人中心..常用信息..旅客列表..新增旅客 */
	public static final String URL_USER_COMMON_USERINFO_ADD = URL_ + "/AddUserPeople";
	/** 个人中心..常用信息..旅客列表..修改旅客 */
	public static final String URL_USER_COMMON_USERINFO_UPDATE = URL_ + "/UpdatePeople";
	/** 个人中心..常用信息..旅客列表..删除旅客 */
	public static final String URL_USER_COMMON_USERINFO_DEL = URL_ + "/RemovePeople";
	/** 个人中心..意见反馈 */
	public static final String URL_USER_FEEDBACK = URL_ + "/AddFedback";
	/** 个人中心..积分优惠券 */
	public static final String URL_USER_COUPON = URL_ + "/UserCoupon";
	/** 个人中心..积分优惠券..说明 */
	public static final String URL_H5_USER_COUPON_INFO = URL_ + "/FindDescript?ID=";
	/** 个人中心..积分优惠券..兑换 */
	public static final String URL_USER_COUPON_CODE = URL_ + "/Exchange";
	/** 个人中心..积分 */
	public static final String URL_USER_POINT = URL_ + "/FindUserPointDetails";
	/** 个人中心..积分..说明 */
	public static final String URL_H5_USER_POIN_INFO = URL_ + "/FindPoint";
	/** 个人中心..收藏..门票 */
	public static final String URL_USER_COLLECT_LIST_TICKET = URL_ + "/FindSoldkeep";
	/** 个人中心..收藏..旅游 */
	public static final String URL_USER_COLLECT_LIST_TOUR = URL_ + "/FindTourismkeep";
	/** 个人中心..收藏..野营 */
	public static final String URL_USER_COLLECT_LIST_YEYING = URL_ + "/FindCamkeep";
	/** 个人中心..设置..版本更新 */
	public static final String URL_USER_SETTINGS_UPDATE = URL_ + "/FindAppVersions";
	/** 个人中心..设置..修改密码 */
	public static final String URL_USER_SETTINGS_PWD = URL_ + "/UpdateUserPassword";
	/** 个人中心..设置..忘记密码 */
	public static final String URL_USER_GET_PWD = URL_ + "/UpdatePsByNum";
	/** 个人中心..根据类别查询所有订单（门票/旅游...） */
	public static final String URL_USER_ORDER_ALL = URL_ + "/FindOrderByPro";
	/** 个人中心..根据状态查询所有订单（待付款/待消费...） */
	public static final String URL_USER_ORDER_QUERY = URL_ + "/FindAllOrder";
	/** 个人中心..订单条数 */
	public static final String URL_USER_ORDER_COUNT = URL_ + "/FindOrderCount";
	/** 个人中心..取消订单 */
	public static final String URL_USER_ORDER_CANCEL = URL_ + "/DeleteOrder";
	/** 个人中心..订单..添加评论 */
	public static final String URL_USER_ORDER_COMMENT = URL_ + "/AddOrderComment";
	/** 个人中心..订单..详情 */
	public static final String URL_USER_ORDER_DETAIL = URL_ + "/FindOrderDeatil";
	/** 个人中心..订单..退款 */
	public static final String URL_USER_ORDER_REFUNDS = URL_ + "/RefundsOrder";

	/** 周边..首页 */
	public static final String URL_PHP_H5_NEARBY_INDEX = URL_SHOP + "/index/index.html";
	/** 首页..酒店..更多 */
	public static final String URL_PHP_H5_HOME_HOTEL_MORE = URL_SHOP + "/index/hotal_index.html";
	/** 首页..美食 */
	public static final String URL_PHP_H5_HOME_MEISHI = URL_SHOP + "/index/food.html";
	/** 首页..特产 */
	public static final String URL_PHP_H5_HOME_TECHAN = URL_SHOP + "/index/native.html";
	/** 首页..娱乐 */
	public static final String URL_PHP_H5_HOME_YULE = URL_SHOP + "/index/entertainment.html";
	/** 首页..购物车 */
	public static final String URL_PHP_H5_MY_CART = URL_SHOP + "/index/cart.html";
	/** 首页..酒店接口 */
	public static final String URL_PHP_HOME_HOTEL_LIST = URL_SHOP + "/Merchants/appgetlist.html";

	/** 支付..支付宝..门票 */
	public static final String URL_PAY_TICKET_ZFB = URL_PAY + "/AopPay/AppPaySold";
	/** 支付..支付宝..旅游 */
	public static final String URL_PAY_TOUR_ZFB = URL_PAY + "/AopPay/AppPayTou";
	/** 支付..支付宝..野营 */
	public static final String URL_PAY_YEYING_ZFB = URL_PAY + "/AopPay/AppPayCamping";
	/** 支付..支付宝..抢购 */
	public static final String URL_PAY_QIANGGOU_ZFB = URL_PAY + "/AopPay/AppPayGrabSold";

	/** 支付..微信..门票 */
	public static final String URL_PAY_TICKET_WX = URL_PAY + "/WxPay/AppPaySold";
	/** 支付..微信..旅游 */
	public static final String URL_PAY_TOUR_WX = URL_PAY + "/WxPay/AppPayTou";
	/** 支付..微信..野营 */
	public static final String URL_PAY_YEYING_WX = URL_PAY + "/WxPay/AppPayCamping";
	/** 支付..微信..抢购 */
	public static final String URL_PAY_QIANGGOU_WX = URL_PAY + "/WxPay/AppPayGrabSold";

	/** 支付..查询积分和红包 */
	public static final String URL_PAY_QUERY = URL_ + "/PayOrderAgain";

	/** 我的订单..电商订单 */
	public static final String URL_PHP_ORDER = URL_SHOP + "/book/getlist";
	/** 我的订单..取消订单 */
	public static final String URL_PHP_ORDER_CANCEL = URL_SHOP + "/book/orderCancel";
	/** 我的..PHP收藏 */
	public static final String URL_PHP_COLLECT = URL_SHOP + "/collect/lists.html";
	/** 我的..添加评论 */
	public static final String URL_PHP_ORDER_COMMENT = URL_SHOP + "/comment/add.html";
}
