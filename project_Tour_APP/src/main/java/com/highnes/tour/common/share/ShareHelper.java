package com.highnes.tour.common.share;

import android.app.Activity;
import android.app.Dialog;

import com.highnes.tour.conf.PreSettings;
import com.highnes.tour.conf.UrlSettings;
import com.highnes.tour.utils.SPUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * <PRE>
 * 作用:
 *    分享。
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-03-09   FUNY        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class ShareHelper {

	/**
	 * 
	 * @param type
	 *            0朋友圈，1微信好友，2qq好友
	 * @param activity
	 *            上下文
	 * @param dialog
	 *            加载框
	 * @param url
	 *            分享的地址
	 * @param title
	 *            分享的标题
	 * @param text
	 *            分享的文字
	 * @param image
	 *            分享的图片
	 * @param umShareListener
	 *            回调监听
	 */
	public static void share(int type, Activity activity, Dialog dialog, String url, String title, String text, String image, UMShareListener umShareListener) {
		Config.dialog = dialog;
		switch (type) {
		// 朋友圈
		case 0:
			shareWeiXinCircle(activity, text, url, umShareListener, image);
			break;
		// 微信好友
		case 1:
			shareWeiXin(activity, text, title, url, umShareListener, image);
			break;
		// QQ好友
		case 2:
			shareQQ(activity, text, title, url, umShareListener, image);
			break;

		default:
			break;
		}
	}

	/**
	 * 分享到QQ好友
	 * 
	 * @param activity
	 * @param withText
	 *            内容
	 * @param withTitle
	 *            标题
	 * @param withTargetUrl
	 *            网页地址
	 * @param umShareListener
	 *            接口回调
	 */
	public static void shareQQ(Activity activity, String withText, String withTitle, String withTargetUrl, UMShareListener umShareListener, String imgUrl) {
		UMImage image = new UMImage(activity, imgUrl);
		new ShareAction(activity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener).withText(withText).withMedia(image).withTitle(withTitle)
				.withTargetUrl(withTargetUrl).share();
	}

	/**
	 * 分享到微信好友
	 * 
	 * @param activity
	 * @param withText
	 *            内容
	 * @param withTitle
	 *            标题
	 * @param withTargetUrl
	 *            网页地址
	 * @param umShareListener
	 *            接口回调
	 */
	public static void shareWeiXin(Activity activity, String withText, String withTitle, String withTargetUrl, UMShareListener umShareListener, String imgUrl) {
		UMImage image = new UMImage(activity, imgUrl);
		image.setTargetUrl(withTargetUrl);
		new ShareAction(activity).withTargetUrl(withTargetUrl).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).withText(withText).withMedia(image)
				.withTitle(withTitle).share();
	}

	/**
	 * 分享到微信朋友圈
	 * 
	 * @param activity
	 * @param withText
	 *            内容
	 * @param withTargetUrl
	 *            网页地址
	 * @param umShareListener
	 *            接口回调
	 */
	public static void shareWeiXinCircle(Activity activity, String withText, String withTargetUrl, UMShareListener umShareListener, String imgUrl) {
		UMImage image = new UMImage(activity, imgUrl);
		// Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
		// R.drawable.ic_launcher);
		// UMImage image = new UMImage(activity, bitmap);
		image.setTargetUrl(withTargetUrl);
		new ShareAction(activity).withTargetUrl(withTargetUrl).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).withText(withText)
				.withMedia(image).share();
	}

}
