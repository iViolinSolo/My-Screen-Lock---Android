package com.eva.me.myscreenlock.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class BitmapUtil {

	/**
	 * 缩放图片
	 * 
	 * @param bitmap
	 * @param f
	 * @return
	 */
	public static Bitmap zoom(Bitmap bitmap, float zf) {
		Matrix matrix = new Matrix();
		matrix.postScale(zf, zf);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	/**
	 * 缩放图片
	 * 
	 * @param bitmap
	 * @param f
	 * @return
	 */
	public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
		Matrix matrix = new Matrix();
		matrix.postScale(wf, hf);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	/**
	 * 图片圆角处理
	 * 
	 * @param bitmap
	 * @param roundPX
	 * @return
	 */
	public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
		// RCB means
		// Rounded
		// Corner Bitmap
		Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(dstbmp);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return dstbmp;
	}
}
