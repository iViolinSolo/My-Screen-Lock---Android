package com.eva.me.myscreenlock.slide;

import com.eva.me.myscreenlock.R;
import com.eva.me.myscreenlock.R.drawable;
import com.eva.me.myscreenlock.R.id;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SliderRelativeLayout extends RelativeLayout {

	private static String TAG = "SliderRelativeLayout";

	private TextView tv_slider_icon = null; // 初始控件，用来判断是否为拖动？

	private Bitmap dragBitmap = null; //拖拽图片
	private Context mContext = null; // 初始化图片拖拽时的Bitmap对象

	
	private Handler mainHandler = null; //与主Activity通信的Handler对象
	
	public SliderRelativeLayout(Context context) {
		super(context);
		mContext = context;
		initDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		mContext = context;
		initDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initDragBitmap();
	}
	
	// 初始化图片拖拽时的Bitmap对象
	private void initDragBitmap() {
		if (dragBitmap == null)
			dragBitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.getup_slider_ico_pressed);
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		// 该控件主要判断是否处于滑动点击区域。滑动时 处于INVISIBLE(不可见)状态，滑动时处于VISIBLE(可见)状态
		tv_slider_icon = (TextView) findViewById(R.id.slider_icon);
	}
	
	private final int startPos = 2000;
	private int mLastMoveX = startPos;  //当前bitmap应该绘制的地方 ， 初始值为足够大，可以认为看不见	
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		Log.i(TAG, "onTouchEvent" + " X is " + x + " Y is " + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "OnTouchEvent: ACTION_DOWN");
			mLastMoveX = (int) event.getX();
			//处理Action_Down事件：  判断是否点击了滑动区域
			return handleActionDownEvenet(event);
		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "OnTouchEvent: ACTION_MOVE");
			mLastMoveX = x; //保存了X轴方向
            invalidate(); //重新绘制			    
			return true;
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "OnTouchEvent: ACTION_UP");
			//处理Action_Up事件：  判断是否解锁成功，成功则结束我们的Activity ；否则 ，缓慢回退该图片。
			handleActionUpEvent(event);
			return true;
		}
		return super.onTouchEvent(event);
	}

	// 绘制拖动时的图片
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
		//Log.(TAG, "onDraw ######" );
		// 图片更随手势移动
		invalidateDragImg(canvas);
	}

	// 图片更随手势移动
	private void invalidateDragImg(Canvas canvas) {
		//Log.e(TAG, "handleActionUpEvenet : invalidateDragImg" );
		//以合适的坐标值绘制该图片
		int drawXCor = mLastMoveX - dragBitmap.getWidth();
		int drawYCor = tv_slider_icon.getTop();
		Log.i(TAG, "invalidateDragImg" + " drawXCor "+ drawXCor + " and drawYCor" + drawYCor);
	    canvas.drawBitmap(dragBitmap,  drawXCor < 0 ? 5 : drawXCor , drawYCor , null);
	}

	// 手势落下是，是否点中了图片，即是否需要开始移动
	private boolean handleActionDownEvenet(MotionEvent event) {
		Rect rect = new Rect();
		tv_slider_icon.getHitRect(rect);
		boolean isHit = rect.contains((int) event.getX(), (int) event.getY());
		
		if(isHit)  //开始拖拽 ，隐藏该图片
			tv_slider_icon.setVisibility(View.INVISIBLE);
		
		//Log.e(TAG, "handleActionDownEvenet : isHit" + isHit);
		
		return isHit;
	}

	//回退动画时间间隔值 
	private static int BACK_DURATION = 20 ;   // 20ms
    //水平方向前进速率
	private static float VE_HORIZONTAL = 0.7f ;  //0.1dip/ms
	
    //判断松开手指时，是否达到末尾即可以开锁了 , 是，则开锁，否则，通过一定的算法使其回退。
	private void handleActionUpEvent(MotionEvent event){		
		int x = (int) event.getX() ;	
		Log.e(TAG, "handleActionUpEvent : x -->" + x + "   getRight() " + getRight() );
		//距离在15dip以内代表解锁成功。
		boolean isSucess= Math.abs(x - getRight()) <= (22+15) ;
		
		if(isSucess){
//		   Toast.makeText(mContext, "解锁成功", 1000).show();
		   resetViewState();	
		   virbate(); //震动一下
		   //结束我们的主Activity界面
		   mainHandler.obtainMessage(ScreenLockActivity.MSG_LOCK_SUCESS).sendToTarget();
		}
		else {//没有成功解锁，以一定的算法使其回退
		    //每隔20ms , 速率为0.6dip/ms ,  使当前的图片往后回退一段距离，直到到达最左端	
			mLastMoveX = x ;  //记录手势松开时，当前的坐标位置。
			int distance = x - tv_slider_icon.getRight() ;
			//只有移动了足够距离才回退
			Log.e(TAG, "handleActionUpEvent : mLastMoveX -->" + mLastMoveX + " distance -->" + distance );
			if(distance >= 0)
			    mHandler.postDelayed(BackDragImgTask, BACK_DURATION);
			else{  //复原初始场景
				resetViewState();
			}
		}
	}
	//重置初始的状态，显示tv_slider_icon图像，使bitmap不可见
	private void resetViewState(){
		mLastMoveX = startPos ;
		tv_slider_icon.setVisibility(View.VISIBLE);
		invalidate();        //重绘最后一次
	}
	
	//通过延时控制当前绘制bitmap的位置坐标
	private Runnable BackDragImgTask = new Runnable(){
		
		public void run(){
			//一下次Bitmap应该到达的坐标值
			mLastMoveX = mLastMoveX - (int)(BACK_DURATION * VE_HORIZONTAL);
			
			Log.e(TAG, "BackDragImgTask ############# mLastMoveX " + mLastMoveX);
			
			invalidate();//重绘		
			//是否需要下一次动画 ？ 到达了初始位置，不在需要绘制
			boolean shouldEnd = Math.abs(mLastMoveX - tv_slider_icon.getRight()) <= 8 ;			
			if(!shouldEnd)
			    mHandler.postDelayed(BackDragImgTask, BACK_DURATION);
			else { //复原初始场景
				resetViewState();	
			}				
		}
	};
	
	private Handler mHandler =new Handler (){
		
		public void handleMessage(Message msg){
			
			Log.i(TAG, "handleMessage :  #### " );
			
		}
	};
	//震动一下下咯
	private void virbate(){
		Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}
	public void setMainHandler(Handler handler){
		mainHandler = handler;//activity所在的Handler对象
	}
}
