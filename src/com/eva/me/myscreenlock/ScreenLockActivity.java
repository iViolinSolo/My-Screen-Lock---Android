package com.eva.me.myscreenlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class ScreenLockActivity extends Activity {
	public static final String TAG = "ScreenLockActivity";
//	private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;//屏蔽home键
	private Button btn_test;
	public static boolean isShow = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED); 
		setContentView(R.layout.activity_screen_lock);
		
		this.isShow = true;
		init();

		Intent intentTmp = new Intent("com.eva.me.localService");
		startService(intentTmp);
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

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.isShow = false;
		super.onDestroy();
	}

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
