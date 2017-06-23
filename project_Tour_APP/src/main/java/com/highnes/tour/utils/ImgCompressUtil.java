package com.highnes.tour.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import junit.framework.Assert;
import Decoder.BASE64Encoder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.util.Base64;

/**
 * 图片压缩工具类
 * 用于压缩图片上传到服务器，图片压缩到200KB以内，并保证不失真和清晰度
 * @author FUNY
 */
public class ImgCompressUtil {
	/**
	 * 最大的图片尺寸
	 */
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static boolean inNativeAllocAccessError = false;
	
	/**
	 *	获取图片压缩后的base流
	 * @param	filePath 	图片本地路径
	 * @return
	 */
	public static String getImgEncodeString(String filePath){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap bitmap = getImgCompress(filePath);
		if(bitmap == null)
			return null;
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
		byte[] bytes = stream.toByteArray();
		//回收bitmap，避免内存浪费
		bitmap.recycle();
		return new String(Base64.encodeToString(bytes, Base64.DEFAULT));
	}
	
	/**
	 * 通过bitmap获取base64字符串
	 * @return
	 */
	public static String getBitmapEncodingStr(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
		byte[] b = stream.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();
		String yy = encoder.encode(b);
		return yy;
	}
	
	/**
	 * 获取压缩后的图片位图信息
	 * 在获取该位图信息并确保该信息无用后确定回收，避免内存浪费
	 * @param filePath	图片本地路径
	 * @return	压缩后的图片位图
	 */
	public static Bitmap getImgCompress(String filePath){
		Options bitmapOptions = getBitmapOptions(filePath);
		//如果图片内存大于200KB或者图片尺寸过大，则压缩
		if((getFileLen(filePath) > 204800)
				|| (bitmapOptions != null 
					&& (((bitmapOptions.outHeight > 960) 
							|| (bitmapOptions.outWidth > 960))))) {
			Bitmap bitmapThumbNail = extractThumbNail(filePath, 960, 960, false);
			return bitmapThumbNail;
		//如果图片本身就很小避免压缩过度
		}else{
			Bitmap bm = getSmallBitmap(filePath);
			return bm;
		}
	}
	
	/**
	 * 根据指定图片位置获取图片位图信息
	 * @param filePath 图片本地路径
	 * @return
	 */
	public final static BitmapFactory.Options getBitmapOptions(String filePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options;
	}
	
	/**
	 * 获取文件长度
	 * @param filePath	本地图片路径
	 * @return
	 */
	public static int getFileLen(String filePath){
		if(filePath == null || "".equals(filePath)) {
            return 0;
        }
        File file = new File(filePath);
        if(!file.exists()) {
            return 0;
        }
        return (int)file.length();
	}
	
	/**
	 * 开始压缩图片，为保证清晰度所以需要多次create
	 * @param path
	 * @param width
	 * @param height
	 * @param crop
	 * @return
	 */
    public static Bitmap extractThumbNail(final String path, final int width, final int height,  final boolean crop) {
        Assert.assertTrue(path != null 
        		&& !path.equals("") 
        		&& height > 0 
        		&& width > 0);
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }
            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                options.inMutable = true;
            }
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            setInNativeAlloc(options);
            if (bm == null) {
                return null;
            }

            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            options = null;
        }
        return null;
    }
    
    public static void setInNativeAlloc(BitmapFactory.Options options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && !inNativeAllocAccessError) {
            try {
                BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
                return ;
            } catch (Exception e) {
                inNativeAllocAccessError = true;
            }
        }
    }
    
	/**
	 * 获取小图的位图信息
	 * @param filePath	图片本地路径
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = getZoomSize(options, 720, 1280);// 以1280*720为主流分辨率
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * 计算缩放比例
	 * @return
	 */
	public static int getZoomSize(BitmapFactory.Options options, int reqWidth,
			int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int zoomSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			zoomSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return zoomSize;
	}
}
