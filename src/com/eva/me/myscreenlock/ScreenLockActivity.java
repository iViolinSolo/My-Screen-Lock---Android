package com.eva.me.myscreenlock;

import com.eva.me.myscreenlock.SliderRelativeLayout;
import com.eva.me.myscreenlock.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ScreenLockActivity extends Activity {
	public static final String TAG = "ScreenLockActivity";
//	private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;//屏蔽home键
	private Button btn_test;
	public static boolean isShow = false;//防止重复开启这个activity
	
	//--Start--slide unlock
	private SliderRelativeLayout sliderLayout = null;

	private ImageView imgView_getup_arrow; // 动画图片
	private AnimationDrawable animArrowDrawable = null;

    private static Context mContext = null ;
	
    public static int MSG_LOCK_SUCESS = 1;
  //--loop--slide unlock
    
    public static  Context getInstance() {
		return mContext;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = ScreenLockActivity.this;
//		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED); 
		setContentView(R.layout.activity_screen_lock);
		
		this.isShow = true;
		init();
		Log.e(TAG, "ScreenLockActivity Start--> OnCreate");
		Intent intentTmp = new Intent("com.eva.me.localService");
		startService(intentTmp);
		
		sliderLayout.setMainHandler(mHandler);
	}

	private void init() {
		btn_test = (Button) findViewById(R.id.btn_nulock);
		btn_test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(ScreenLockActivity.this, "The Screen is unlocked!", Toast.LENGTH_SHORT).show();
				ScreenLockActivity.this.finish();
				
			}
		});
		
		//--Start--slide unlock
		sliderLayout = (SliderRelativeLayout)findViewById(R.id.slider_layout);
    	//获得动画，并开始转动
    	imgView_getup_arrow = (ImageView)findViewById(R.id.getup_arrow);
    	animArrowDrawable = (AnimationDrawable) imgView_getup_arrow.getBackground() ;
    	//--Start--slide unlock
	}
	
	@Override
	protected void onDestroy() {
		this.isShow = false;
		super.onDestroy();
	}

	//--Start--slide unlock
	@Override
	protected void onResume() {
		super.onResume();
		//设置动画
		mHandler.postDelayed(AnimationDrawableTask, 300);  //开始绘制动画
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		animArrowDrawable.stop();
	}
	
	//通过延时控制当前绘制bitmap的位置坐标
	private Runnable AnimationDrawableTask = new Runnable(){
		
		public void run(){
			animArrowDrawable.start();
			mHandler.postDelayed(AnimationDrawableTask, 300);
		}
	};
	
	private Handler mHandler =new Handler (){
		
		public void handleMessage(Message msg){
			
			Log.i(TAG, "handleMessage :  #### " );
			
			if(MSG_LOCK_SUCESS == msg.what)
				finish(); // 锁屏成功时，结束我们的Activity界面
		}
	};
	//--Start--slide unlock
	
//	@Override
//	public void onAttachedToWindow() {
//		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//		super.onAttachedToWindow();
//	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			Log.e(TAG, "KEYCODE_HOME");
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_BACK) {//屏蔽back按键
			Log.e(TAG, "KEYCODE_BACK");
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_MENU) {//屏蔽menu按键
			Log.e(TAG, "KEYCODE_MENU");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_lock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
