package com.highnes.tour.view.imageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.highnes.tour.R;

/**
 * 自定义圆角的图片
 * 
 * @author FUNY
 * 
 */
public class RoundAngleImageView extends ImageView {

	private Paint paint;
	private int roundWidth = 10;
	private int roundHeight = 10;
	private Paint paint2;
	// 是否显示上面的圆角 0表示上边，1表示下班，2表示所有
	private int isShow = 2;

	public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public RoundAngleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public RoundAngleImageView(Context context) {
		super(context);
		init(context, null);
	}

	@SuppressLint("Recycle")
	private void init(Context context, AttributeSet attrs) {

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
			isShow = a.getInt(R.styleable.RoundAngleImageView_raiv_showTop, 2);
			roundWidth = 10;
			roundHeight = 10;
		} else {
			float density = context.getResources().getDisplayMetrics().density;
			roundWidth = (int) (roundWidth * density);
			roundHeight = (int) (roundHeight * density);
		}

		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

		paint2 = new Paint();
		paint2.setXfermode(null);
	}

	@Override
	public void draw(Canvas canvas) {
		try {
			// InputStream is =
			// this.getResources().openRawResource(R.drawable.pic1);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = false;
			// options.inSampleSize = 10; // width，hight设为原来的十分一
			Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.RGB_565);
			Canvas canvas2 = new Canvas(bitmap);
			super.draw(canvas2);
			if (0 == isShow) {
				drawLiftUp(canvas2);
				drawRightUp(canvas2);
			} else if (1 == isShow) {
				drawLiftDown(canvas2);
				drawRightDown(canvas2);
			} else {
				drawLiftUp(canvas2);
				drawRightUp(canvas2);
				drawLiftDown(canvas2);
				drawRightDown(canvas2);
			}
			canvas.drawBitmap(bitmap, 0, 0, paint2);
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				System.gc(); // 提醒系统及时回收
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawLiftUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, roundHeight);
		path.lineTo(0, 0);
		path.lineTo(roundWidth, 0);
		path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawLiftDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, getHeight() - roundHeight);
		path.lineTo(0, getHeight());
		path.lineTo(roundWidth, getHeight());
		path.arcTo(new RectF(0, getHeight() - roundHeight * 2, 0 + roundWidth * 2, getHeight()), 90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawRightDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth() - roundWidth, getHeight());
		path.lineTo(getWidth(), getHeight());
		path.lineTo(getWidth(), getHeight() - roundHeight);
		path.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight() - roundHeight * 2, getWidth(), getHeight()), 0, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawRightUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth(), roundHeight);
		path.lineTo(getWidth(), 0);
		path.lineTo(getWidth() - roundWidth, 0);
		path.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(), 0 + roundHeight * 2), -90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

}