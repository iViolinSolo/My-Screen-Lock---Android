package com.eva.me.myscreenlock.slide;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.eva.me.myscreenlock.R;
import com.eva.me.myscreenlock.psdlock.LoginActivity;
import com.eva.me.myscreenlock.util.FolderUtil;
import com.eva.me.myscreenlock.util.StatusUtil;
import com.eva.me.myscreenlock.viewpager.ScreenLockViewPagerAdpter;

public class ScreenLockActivity extends Activity {
	public static final String TAG = "ScreenLockActivity";
	public static final int OP_EXIT_SCREEN = 2;
//	private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;//屏蔽home键
//	private Button btn_test;
	public static boolean isShow = false;//防止重复开启这个activity
	
	//--Start--slide unlock
	private SliderRelativeLayout sliderLayout = null;

	private ImageView imgView_getup_arrow; // 动画图片
	private AnimationDrawable animArrowDrawable = null;

    private static Context mContext = null ;
	
    public static int MSG_LOCK_SUCESS = 1;
	public static int MSG_AUTHENTICATION_SUCCESS = 2;
	
	//-----
	private ViewPager viewPager;  
    private Bitmap[] bmps = null;  
    private final int len = 13;
    private Drawable[] drawables = new Drawable[len];  
	//-----
    
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
		
		ScreenLockActivity.isShow = true;
		init();
		Log.e(TAG, "ScreenLockActivity Start--> OnCreate");
		Intent intentTmp = new Intent("com.eva.me.localService");
		startService(intentTmp);
		
		sliderLayout.setMainHandler(mHandler);
	}

	private void init() {
		
		//--Start--slide unlock
		sliderLayout = (SliderRelativeLayout)findViewById(R.id.slider_layout);
    	//获得动画，并开始转动
    	imgView_getup_arrow = (ImageView)findViewById(R.id.getup_arrow);
    	animArrowDrawable = (AnimationDrawable) imgView_getup_arrow.getBackground() ;
    	//--Start--slide unlock
    	
    	initViewPager();
	}
	
	private void initViewPager() {
		FolderUtil.initFolder();
		initControl();
		autoSwitch();
	}

	private void initControl() { 
        viewPager = (ViewPager) findViewById(R.id.viewpager);  
        File f = new File(FolderUtil.getSaveFolder());  
        File[] files = f.listFiles();// 得到所有子目录  
        bmps = new Bitmap[files.length];  
          
        //如果文件夹为空，则从资源文件加载图片  
        if (files.length == 0) {  
            drawables[0] = getResources().getDrawable(R.drawable.bg_01);  
            drawables[1] = getResources().getDrawable(R.drawable.bg_02);  
            drawables[2] = getResources().getDrawable(R.drawable.bg_03);  
            drawables[3] = getResources().getDrawable(R.drawable.bg_031);  
            drawables[4] = getResources().getDrawable(R.drawable.bg_04);    
            drawables[5] = getResources().getDrawable(R.drawable.bg_041);  
            drawables[6] = getResources().getDrawable(R.drawable.bg_05); 
            drawables[7] = getResources().getDrawable(R.drawable.bg_051);
            drawables[8] = getResources().getDrawable(R.drawable.bg_06);  
            drawables[9] = getResources().getDrawable(R.drawable.bg_061);  
            drawables[10] = getResources().getDrawable(R.drawable.bg_062);  
            drawables[11] = getResources().getDrawable(R.drawable.bg_07);  
            drawables[12] = getResources().getDrawable(R.drawable.bg_08);   
            ScreenLockViewPagerAdpter adapter = new ScreenLockViewPagerAdpter(  
                    ScreenLockActivity.this, drawables, len); 
            viewPager.setAdapter(adapter);  
        } else {  
            //文件夹不为空则循环遍历加载sd卡指定目录中图片  
            for (int i = 0; i < files.length; i++) {  
                String path = files[i].getAbsolutePath();
                bmps[i] = BitmapFactory.decodeFile(path);  
                Log.d("PDA", "====H===" + path);  
  
            }  
            ScreenLockViewPagerAdpter adapter = new ScreenLockViewPagerAdpter(  
            		ScreenLockActivity.this, bmps, files.length);  
            viewPager.setAdapter(adapter);  
  
        }  
  
    }  
	
	/** 图片定时自动切换 */  
    private void autoSwitch() {  
        int interval = 3000;  
        Timer timer = new Timer();  
        TimerTask task = new TimerTask() {  
  
            @Override  
            public void run() {  
                Message message = new Message();  
                handler.sendMessage(message);  
            }  
        };  
        timer.schedule(task, interval, interval);  
    } 

	@Override
	protected void onDestroy() {
		ScreenLockActivity.isShow = false;
		
		//将bitmap回收，尽量避免OOM  
        if (bmps != null) {  
            for (int i = 0; i < bmps.length; i++) {  
                bmps[i].recycle();  
            }  
        } else {  
            return;  
        } 
		
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
	
	Handler handler = new Handler() {  
		  
        @Override  
        public void handleMessage(Message msg) {  
            int currentPage = viewPager.getCurrentItem();  
            int tempNum = bmps.length == 0 ? drawables.length : bmps.length;  
            int nextPage = (currentPage + 1) % tempNum;  
            viewPager.setCurrentItem(nextPage);  
            super.handleMessage(msg);  
        }  
    };
	
	private Handler mHandler =new Handler (){
		
		public void handleMessage(Message msg){
			
			Log.i(TAG, "handleMessage :  #### " );
			
			if(MSG_LOCK_SUCESS == msg.what) {
				
				boolean isOn = false;
				isOn = StatusUtil.getStatus(ScreenLockActivity.this);
				if (isOn) {
					LoginActivity.mainHandler = mHandler;
					Intent intToLogin = new Intent();
					intToLogin.putExtra("operation", ScreenLockActivity.OP_EXIT_SCREEN);
					intToLogin.setClass(ScreenLockActivity.this, LoginActivity.class);
					startActivity(intToLogin);
				} else {
					Toast.makeText(mContext, "解锁成功", Toast.LENGTH_SHORT).show();
					finish(); // 锁屏成功时，结束我们的Activity界面
				}

			}else if (MSG_AUTHENTICATION_SUCCESS == msg.what) {
				Toast.makeText(mContext, "解锁成功", Toast.LENGTH_SHORT).show();
				finish(); // 锁屏成功时，结束我们的Activity界面
			}
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
