package com.highnes.tour;

import android.app.Application;
import android.graphics.Bitmap;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.highnes.tour.conf.Constants;
import com.highnes.tour.utils.FileUtils;
import com.highnes.tour.utils.LogUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.socialize.PlatformConfig;

/**
 * <PRE>
 * 作用:
 *    全局Application。
 * 注意事项:
 *    打包的时候把极光推送的日志关闭。分享的日志关闭。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-06-15   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initJPush();
		SDKInitializer.initialize(this);//初始化百度地图
		initShare();
		initOptions();
	}

	public static BaseApplication instance;
	/**
	 * 初始化极光
	 */
	private void initJPush() {
		// 初始化极光推送
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
	}
	/**
	 * 单例，返回一个实例
	 * 
	 * @return
	 */
	public static BaseApplication getInstance() {
		if (instance == null) {
			LogUtils.w("[BaseApplication] instance is null.");
		}
		return instance;
	}

	/**
	 * 初始化友盟
	 */
	private void initShare() {
		// 各个平台的配置，建议放在全局Application或者程序入口
		// 微信
		PlatformConfig.setWeixin(Constants.WIN_ID, Constants.WIN_KEY);
		// 新浪微博
		// PlatformConfig.setSinaWeibo("3921700954",
		// "04b48b094faeb16683c32669824ebdad");
		// QQ
		PlatformConfig.setQQZone(Constants.QQ_ID, Constants.QQ_KEY);

	}
	/**
	 * 初始化ImageLoader
	 */
	@SuppressWarnings("deprecation")
	private void initOptions() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		// .memoryCacheExtraOptions(480, 800)
				.threadPoolSize(5)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024))
				// .memoryCacheSize(2 * 1024 * 1024)
				// .discCacheSize(50 * 1024 * 1024)
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())//
				// 将保存的时候的URI名称用MD5 加密
				// .tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCacheFileCount(100)// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(FileUtils.getCacheImageFiles(instance))) // 自定义缓存路径
				.defaultDisplayImageOptions(getDisplayOptions())
				// .imageDownloader(new BaseImageDownloader(this, 30 * 1000, 30
				// * 1000)) // 超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建

		ImageLoader.getInstance().init(config);

	}

	private DisplayImageOptions getDisplayOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				// .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				// .displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(200))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
		return options;
	}

}
