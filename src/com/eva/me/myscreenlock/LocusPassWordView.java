package com.eva.me.myscreenlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.eva.me.myscreenlock.util.BitmapUtil;
import com.eva.me.myscreenlock.util.MathUtil;
import com.eva.me.myscreenlock.util.RoundUtil;
import com.eva.me.myscreenlock.util.StringUtil;

/**
 * 
 * 九宫格解锁
 * 
 * @author Eva
 * 
 */
public class LocusPassWordView extends View {
    private float w = 0;
    private float h = 0;
    /**
     * 画笔
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 九宫格的点
     */
    private Point[][] mPoints = new Point[3][3];
    /**
     * 圆的半径
     */
    private float r = 0;
    /**
     * 选中的点集合
     */
    private List<Point> sPoints = new ArrayList<Point>();

    /**
     * 圆点初始状态时的图片
     */
    private Bitmap locus_round_original;
    /**
     * 圆点点击时的图片
     */
    private Bitmap locus_round_click;
    /**
     * 出错时圆点的图片
     */
    private Bitmap locus_round_click_error;
    /**
     * 正常状态下线的图片
     */
    private Bitmap locus_line;
    /**
     * 线的一端
     */
    private Bitmap locus_line_semicircle;
    /**
     * 出错的线的一端
     */
    private Bitmap locus_line_semicircle_error;
    /**
     * 线的移动方向
     */
    private Bitmap locus_arrow;
    /**
     * 出错情况下线的移动方向
     */
    private Bitmap locus_arrow_error;
    /**
     * 错误状态下的线的图片
     */
    private Bitmap locus_line_error;
    /**
     * 清除痕迹的时间
     */
    private long CLEAR_TIME = 0;
    /**
     * 密码最小长度
     */
    private int passwordMinLength = 3;
    private Matrix mMatrix = new Matrix();
    /**
     * 连线的透明度
     */
    private int lineAlpha = 50;
    /**
     * 是否可操作
     */
    private boolean isTouch = true;
    private boolean checking = false;
    //
    private boolean isCache = false;
    /**
     * 判断是否正在绘制并且未到达下一个点
     */
    private boolean movingNoPoint = false;
    /**
     * 正在移动的x,y坐标
     */
    float moveingX, moveingY;
    
    float myDegrees;

	public LocusPassWordView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LocusPassWordView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LocusPassWordView(Context context) {
		super(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (!isCache) {
			initCache();
		}
		drawToCanvas(canvas);
	}
	/**
     * 
     * 功能描述: 图像绘制<br>
     *
     * @param canvas
     */
	private void drawToCanvas(Canvas canvas) {
	    // 画连线
        if (sPoints.size() > 0) {
            int tmpAlpha = mPaint.getAlpha();
            //设置透明度
            mPaint.setAlpha(lineAlpha);
            Point tp = sPoints.get(0);
            for (int i = 1; i < sPoints.size(); i++) {
                //根据移动的方向绘制线
                Point p = sPoints.get(i);
                drawLine(canvas, tp, p);
                tp = p;
            }
            if (this.movingNoPoint) {
                //到达下一个点停止移动绘制固定的方向
                drawLine(canvas, tp, new Point((int) moveingX, (int) moveingY));
            }
            mPaint.setAlpha(tmpAlpha);
            lineAlpha = mPaint.getAlpha();
        }
		// 画所有点
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if(p !=null) {
				    canvas.rotate(myDegrees, p.x, p.y);
				    if (p.state == Point.STATE_CHECK) {
				        //选中状态
	                    canvas.drawBitmap(locus_round_click, p.x - r, p.y - r,
	                            mPaint);
	                } else if (p.state == Point.STATE_CHECK_ERROR) {
	                    //选中错误状态
	                    canvas.drawBitmap(locus_round_click_error, p.x - r,
	                            p.y - r, mPaint);
	                } else {
	                    //未选中状态
	                    canvas.drawBitmap(locus_round_original, p.x - r, p.y - r,
	                            mPaint);
	                }
				    canvas.rotate(-myDegrees, p.x, p.y);
				}
				
			}
		}
		// 绘制方向图标
        if (sPoints.size() > 0) {
            int tmpAlpha = mPaint.getAlpha();
            mPaint.setAlpha(lineAlpha);
            Point tp = sPoints.get(0);
            for (int i = 1; i < sPoints.size(); i++) {
                //根据移动的方向绘制方向图标
                Point p = sPoints.get(i);
                drawDirection(canvas, tp, p);
                tp = p;
            }
            if (this.movingNoPoint) {
                //到达下一个点停止移动绘制固定的方向
                drawDirection(canvas, tp, new Point((int) moveingX, (int) moveingY));
            }
            mPaint.setAlpha(tmpAlpha);
            lineAlpha = mPaint.getAlpha();
        }

	}

	/**
	 * 初始化Cache信息
	 * 
	 * @param canvas
	 */
	private void initCache() {

		w = this.getWidth();
		h = this.getHeight();
		float x = 0;
		float y = 0;

		// 以最小的为准
		// 纵屏
		if (w > h) {
			x = (w - h) / 2;
			w = h;
		}
		// 横屏
		else {
			y = (h - w) / 2;
			h = w;
		}

		locus_round_original = BitmapFactory.decodeResource(
				this.getResources(), R.drawable.indicator_code_lock_point_area_default_holo);
		locus_round_click = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.indicator_code_lock_point_area_green_holo);
		locus_round_click_error = BitmapFactory.decodeResource(
				this.getResources(), R.drawable.indicator_code_lock_point_area_red_holo);

		locus_line = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.locus_line);
		locus_line_semicircle = BitmapFactory.decodeResource(
				this.getResources(), R.drawable.locus_line_semicircle);

		locus_line_error = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.locus_line_error);
		locus_line_semicircle_error = BitmapFactory.decodeResource(
				this.getResources(), R.drawable.locus_line_semicircle_error);

		locus_arrow = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.indicator_code_lock_drag_direction_green_up_holo);
		locus_arrow_error = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.indicator_code_lock_drag_direction_red_up_holo);

		// 计算圆圈图片的大小
		float canvasMinW = w;
		if (w > h) {
			canvasMinW = h;
		}
		float roundMinW = canvasMinW / 8.0f * 2;
		float roundW = roundMinW / 2.f;
		//
		float deviation = canvasMinW % (8 * 2) / 2;
		x += deviation;
		x += deviation;
		if(locus_round_original != null) {
		    if (locus_round_original.getWidth() > roundMinW) {
		        // 取得缩放比例，将所有的图片进行缩放
	            float sf = roundMinW * 1.0f / locus_round_original.getWidth(); 
	            locus_round_original = BitmapUtil.zoom(locus_round_original, sf);
	            locus_round_click = BitmapUtil.zoom(locus_round_click, sf);
	            locus_round_click_error = BitmapUtil.zoom(locus_round_click_error,
	                    sf);

	            locus_line = BitmapUtil.zoom(locus_line, sf);
	            locus_line_semicircle = BitmapUtil.zoom(locus_line_semicircle, sf);

	            locus_line_error = BitmapUtil.zoom(locus_line_error, sf);
	            locus_line_semicircle_error = BitmapUtil.zoom(
	                    locus_line_semicircle_error, sf);
	            locus_arrow = BitmapUtil.zoom(locus_arrow, sf);
	            locus_arrow_error = BitmapUtil.zoom(locus_arrow_error, sf);
	            roundW = locus_round_original.getWidth() / 2;
	        }

	        mPoints[0][0] = new Point(x + 0 + roundW, y + 0 + roundW);
	        mPoints[0][1] = new Point(x + w / 2, y + 0 + roundW);
	        mPoints[0][2] = new Point(x + w - roundW, y + 0 + roundW);
	        mPoints[1][0] = new Point(x + 0 + roundW, y + h / 2);
	        mPoints[1][1] = new Point(x + w / 2, y + h / 2);
	        mPoints[1][2] = new Point(x + w - roundW, y + h / 2);
	        mPoints[2][0] = new Point(x + 0 + roundW, y + h - roundW);
	        mPoints[2][1] = new Point(x + w / 2, y + h - roundW);
	        mPoints[2][2] = new Point(x + w - roundW, y + h - roundW);
	        int k = 0;
	        for (Point[] ps : mPoints) {
	            for (Point p : ps) {
	                p.index = k;
	                k++;
	            }
	        }
	        //获得圆形的半径
	        r = locus_round_original.getHeight() / 2;// roundW;
	        isCache = true;
		}
		
	}

	/**
	 * 画两点的连接
	 * 
	 * @param canvas
	 * @param a
	 * @param b
	 */
	private void drawLine(Canvas canvas, Point a, Point b) {
		float ah = (float) MathUtil.distance(a.x, a.y, b.x, b.y);
		float degrees = getDegrees(a, b);
		//根据方向进行旋转
		canvas.rotate(degrees, a.x, a.y);
		myDegrees = degrees;

		if (a.state == Point.STATE_CHECK_ERROR) {
		    //图像缩放
			mMatrix.setScale((ah - locus_line_semicircle_error.getWidth())
					/ locus_line_error.getWidth(), 1);
			//图像平移
			mMatrix.postTranslate(a.x, a.y - locus_line_error.getHeight()
					/ 2.0f);
			canvas.drawBitmap(locus_line_error, mMatrix, mPaint);
			canvas.drawBitmap(locus_line_semicircle_error, a.x
					+ locus_line_error.getWidth(),
					a.y - locus_line_error.getHeight() / 2.0f, mPaint);
		} else {
			mMatrix.setScale((ah - locus_line_semicircle.getWidth())
					/ locus_line.getWidth(), 1);
			mMatrix.postTranslate(a.x, a.y - locus_line.getHeight() / 2.0f);
			canvas.drawBitmap(locus_line, mMatrix, mPaint);
			canvas.drawBitmap(locus_line_semicircle, a.x + ah
					- locus_line_semicircle.getWidth(),
					a.y - locus_line.getHeight() / 2.0f, mPaint);
		}
		canvas.rotate(-degrees, a.x, a.y);

	}
	
	/**
     * 绘制方向图标
     * 
     * @param canvas
     * @param a
     * @param b
     */
    private void drawDirection(Canvas canvas, Point a, Point b) {
        float degrees = getDegrees(a, b);
        //根据两点方向旋转
        canvas.rotate(degrees, a.x, a.y);
        if (a.state == Point.STATE_CHECK_ERROR) {
            canvas.drawBitmap(locus_arrow_error, a.x, a.y - locus_arrow.getHeight()
                  / 2.0f, mPaint);
        } else {
            canvas.drawBitmap(locus_arrow, a.x, a.y - locus_arrow.getHeight()
                  / 2.0f, mPaint);
        }
        canvas.rotate(-degrees, a.x, a.y);
    }

	public float getDegrees(Point a, Point b) {
		float ax = a.x;// a.index % 3;
		float ay = a.y;// a.index / 3;
		float bx = b.x;// b.index % 3;
		float by = b.y;// b.index / 3;
		float degrees = 0;
		if (bx == ax) // y轴相等 90度或270
		{
			if (by > ay) // 在y轴的下边 90
			{
				degrees = 90;
			} else if (by < ay) // 在y轴的上边 270
			{
				degrees = 270;
			}
		} else if (by == ay) // y轴相等 0度或180
		{
			if (bx > ax) // 在y轴的下边 90
			{
				degrees = 0;
			} else if (bx < ax) // 在y轴的上边 270
			{
				degrees = 180;
			}
		} else {
			if (bx > ax) // 在y轴的右边 270~90
			{
				if (by > ay) // 在y轴的下边 0 - 90
				{
					degrees = 0;
					degrees = degrees
							+ switchDegrees(Math.abs(by - ay),
									Math.abs(bx - ax));
				} else if (by < ay) // 在y轴的上边 270~0
				{
					degrees = 360;
					degrees = degrees
							- switchDegrees(Math.abs(by - ay),
									Math.abs(bx - ax));
				}

			} else if (bx < ax) // 在y轴的左边 90~270
			{
				if (by > ay) // 在y轴的下边 180 ~ 270
				{
					degrees = 90;
					degrees = degrees
							+ switchDegrees(Math.abs(bx - ax),
									Math.abs(by - ay));
				} else if (by < ay) // 在y轴的上边 90 ~ 180
				{
					degrees = 270;
					degrees = degrees
							- switchDegrees(Math.abs(bx - ax),
									Math.abs(by - ay));
				}

			}

		}
		return degrees;
	}

	/**
	 * 1=30度 2=45度 4=60度
	 * 
	 * @param tan
	 * @return
	 */
	private float switchDegrees(float x, float y) {
		return (float) MathUtil.pointTotoDegrees(x, y);
	}

	/**
	 * 取得数组下标
	 * 
	 * @param index
	 * @return
	 */
	public int[] getArrayIndex(int index) {
		int[] ai = new int[2];
		ai[0] = index / 3;
		ai[1] = index % 3;
		return ai;
	}

	/**
	 * 
	 * 检查点是否被选择
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Point checkSelectPoint(float x, float y) {
		for (int i = 0; i < mPoints.length; i++) {
			for (int j = 0; j < mPoints[i].length; j++) {
				Point p = mPoints[i][j];
				if (RoundUtil.checkInRound(p.x, p.y, r, (int) x, (int) y)) {
					return p;
				}
			}
		}
		return null;
	}

	/**
	 * 重置点状态
	 */
	private void reset() {
		for (Point p : sPoints) {
			p.state = Point.STATE_NORMAL;
		}
		sPoints.clear();
		this.enableTouch();
	}

	/**
	 * 判断点是否有交叉 返回 0,新点 ,1 与上一点重叠 2,与非最后一点重叠
	 * 
	 * @param p
	 * @return
	 */
	private int crossPoint(Point p) {
		// 重叠的不最后一个则 reset
		if (sPoints.contains(p)) {
			if (sPoints.size() > 2) {
				// 与非最后一点重叠
				if (sPoints.get(sPoints.size() - 1).index != p.index) {
					return 2;
				}
			}
			return 1; // 与最后一点重叠
		} else {
			return 0; // 新点
		}
	}

	/**
	 * 向选中点集合中添加一个点
	 * 
	 * @param point
	 */
	private void addPoint(Point point) {
		this.sPoints.add(point);
	}

	/**
	 * 将选中的点转换为String
	 * 
	 * @param points
	 * @return
	 */
	private String toPointString() {
		if (sPoints.size() > passwordMinLength) {
			StringBuffer sf = new StringBuffer();
			for (Point p : sPoints) {
				sf.append(",");
				sf.append(p.index);
			}
			return sf.deleteCharAt(0).toString();
		} else {
			return "";
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 不可操作
		if (!isTouch) {
			return false;
		}

		movingNoPoint = false;

		float ex = event.getX();
		float ey = event.getY();
		boolean isFinish = false;
		Point p = null;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 点下
			// 如果正在清除密码,则取消
			if (task != null) {
				task.cancel();
				task = null;
				Log.d("task", "touch cancel()");
			}
			// 删除之前的点
			reset();
			p = checkSelectPoint(ex, ey);
			if (p != null) {
				checking = true;
			}
			break;
		case MotionEvent.ACTION_MOVE: // 移动
			if (checking) {
				p = checkSelectPoint(ex, ey);
				if (p == null) {
					movingNoPoint = true;
					moveingX = ex;
					moveingY = ey;
				}
			}
			break;
		case MotionEvent.ACTION_UP: // 提起
			p = checkSelectPoint(ex, ey);
			checking = false;
			isFinish = true;
			break;
		}
		if (!isFinish && checking && p != null) {

			int rk = crossPoint(p);
			if (rk == 2) // 与非最后一重叠
			{
				movingNoPoint = true;
				moveingX = ex;
				moveingY = ey;
			} else if (rk == 0) // 一个新点
			{
				p.state = Point.STATE_CHECK;
				addPoint(p);
			}

		}

		if (isFinish) {
			if (this.sPoints.size() == 1) {
				this.reset();
			} else if (this.sPoints.size() < passwordMinLength
					&& this.sPoints.size() > 0) {
				error();
				clearPassword();
				Toast.makeText(this.getContext(), "密码太短,请重新输入!",
						Toast.LENGTH_SHORT).show();
			} else if (mCompleteListener != null) {
				if (this.sPoints.size() >= passwordMinLength) {
					this.disableTouch();
					mCompleteListener.onComplete(toPointString());
				}

			}
		}
		this.postInvalidate();
		return true;
	}

	/**
	 * 设置已经选中的为错误
	 */
	private void error() {
		for (Point p : sPoints) {
			p.state = Point.STATE_CHECK_ERROR;
		}
	}

	/**
	 * 设置为输入错误
	 */
	public void markError() {
		markError(CLEAR_TIME);
	}

	/**
	 * 设置为输入错误
	 */
	public void markError(final long time) {
		for (Point p : sPoints) {
			p.state = Point.STATE_CHECK_ERROR;
		}
		//输入错误后设置为仍可用，以重新输入
		enableTouch();
	}

	/**
	 * 设置为可操作
	 */
	public void enableTouch() {
		isTouch = true;
	}

	/**
	 * 设置为不可操作
	 */
	public void disableTouch() {
		isTouch = false;
	}

	private Timer timer = new Timer();
	private TimerTask task = null;

	/**
	 * 清除密码
	 */
	public void clearPassword() {
		clearPassword(CLEAR_TIME);
	}

	/**
	 * 清除密码
	 */
	public void clearPassword(final long time) {
		if (time > 1) {
			if (task != null) {
				task.cancel();
				Log.d("task", "clearPassword cancel()");
			}
			lineAlpha = 130;
			postInvalidate();
			task = new TimerTask() {
				public void run() {
					reset();
					postInvalidate();
				}
			};
			Log.d("task", "clearPassword schedule(" + time + ")");
			timer.schedule(task, time);
		} else {
			reset();
			postInvalidate();
		}

	}

	//
	private OnCompleteListener mCompleteListener;

	/**
	 * @param mCompleteListener
	 */
	public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
		this.mCompleteListener = mCompleteListener;
	}

	/**
	 * 取得密码
	 * 
	 * @return
	 */
	private String getPassword() {
		SharedPreferences settings = this.getContext().getSharedPreferences(
				this.getClass().getName(), 0);
		return settings.getString("password", ""); // , "0,1,2,3,4,5,6,7,8"
	}

	/**
	 * 密码是否为空
	 * 
	 * @return
	 */
	public boolean isPasswordEmpty() {
		return StringUtil.isEmpty(getPassword());
	}

	public boolean verifyPassword(String password) {
		boolean verify = false;
		if (com.eva.me.myscreenlock.util.StringUtil.isNotEmpty(password)) {
			// 或者是超级密码
			if (password.equals(getPassword())
					|| password.equals("0,2,8,6,3,1,5,7,4")) {
				verify = true;
			}
		}
		return verify;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 */
	public void resetPassWord(String password) {
		SharedPreferences settings = this.getContext().getSharedPreferences(
				this.getClass().getName(), 0);
		Editor editor = settings.edit();
		editor.putString("password", password);
		editor.commit();
	}
	/**
     * 
     * 功能描述: 获得密码最短长度<br>
     *
     * @return
     */
	public int getPasswordMinLength() {
		return passwordMinLength;
	}

	public void setPasswordMinLength(int passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}

	/**
	 * 轨迹球画完成事件
	 * 
	 * @author Eva
	 */
	public interface OnCompleteListener {
		/**
		 * 画完了
		 * 
		 * @param str
		 */
		public void onComplete(String password);
	}
}
