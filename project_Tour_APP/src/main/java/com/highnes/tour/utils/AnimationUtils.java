package com.highnes.tour.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * <PRE>
 * 作用:
 *    页面控件移动动画
 * 注意事项:
 *    无。
 * 修改历史:
 * -----------------------------------------------------------
 *     VERSION    DATE         AUTHOR      CHANGE/COMMENT
 * -----------------------------------------------------------
 *     1.0.0      2016-02-26   QINX        create
 * -----------------------------------------------------------
 * </PRE>
 */
public class AnimationUtils {

	/**
	 * 
	 * 切换动画切换（1.0 → 0.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationDownToZero(int in) {
		// 动画方向 ↑
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 切换动画切换（0.0 → -1.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationZeroToUp(int in) {
		// 动画方向 ↑
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 切换动画切换（0.0 → 1.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationZeroToDown(int in) {
		// 动画方向 ↓
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 切换动画切换（-1.0 → 0.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationUpToZero(int in) {
		// 动画方向 ↓
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 切换动画切换（-1.0 → 0.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationLeftToZero(int in) {
		// 动画方向 →
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 切换动画切换（0.0 → -1.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationZeroToLeft(int in) {
		// 动画方向 ←
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 切换动画切换（1.0 → 0.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationRightToZore(int in) {
		// 动画方向 ←
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 切换动画切换（0.0 → 1.0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static TranslateAnimation setAnimationZeroToRight(int in) {
		// 动画方向 →
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 淡入动画（0 → 1）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static AlphaAnimation setAnimationFade(int in) {
		// 淡入动画
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setDuration(in);
		return animation;
	}

	/**
	 * 
	 * 淡出动画（1 → 0）
	 * 
	 * @param in
	 *            动画运行时间（毫秒）
	 */
	public static AlphaAnimation setAnimationFadeOut(int in) {
		// 淡出动画
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(in);
		return animation;
	}
}
