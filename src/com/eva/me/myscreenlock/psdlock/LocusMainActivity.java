package com.eva.me.myscreenlock.psdlock;

import com.eva.me.myscreenlock.R;
import com.eva.me.myscreenlock.R.id;
import com.eva.me.myscreenlock.R.layout;
import com.eva.me.myscreenlock.R.menu;
import com.eva.me.myscreenlock.util.StatusUtil;
import com.eva.me.myscreenlock.util.StringUtil;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocusMainActivity extends Activity {
	private static final String TAG = "LocusMainActivity";
	public static final int OP_CLOSE_LOCK = 1;
	public static String  name =  "com.eva.me.myscreenlock.LocusMainActivity";
	private TextView btnSwitch, btnReset;
	private String password = "";
	public static boolean isOn = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		init();
	}

	private void init() {
		password = this.getPassword();
		Log.e(TAG, "PASSWORD is : "+password);
		
		LocusMainActivity.name = this.getClass().getName();
		
		LocusMainActivity.isOn = StatusUtil.getStatus(this);
		Log.e(TAG, "isOn status: "+isOn);
		
		btnReset = (TextView) this.findViewById(R.id.tvReset);
		btnReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocusMainActivity.this,
						SetPasswordActivity.class);
				// 打开新的Activity
				startActivity(intent);
				finish();
			}
		});
		
		btnSwitch = (TextView) findViewById(R.id.tvSwitch);
		//重新绘制button
		if (LocusMainActivity.isOn) {
			//开启了密码锁屏
			btnSwitch.setText("关闭密码\n锁屏功能");
			btnReset.setVisibility(View.VISIBLE);
		}else if (!LocusMainActivity.isOn ||  password.equals(LoginActivity.AUTHENTICATION_SUCCESS)) {
			//如果处于本身就是关闭状态，或者是验证成功之后，就可以直接绘制图片为关闭状态，同时初始化密码
			//关闭了密码锁屏
			btnSwitch.setText("打开密码\n锁屏功能");
			btnReset.setVisibility(View.GONE);
			//重新赋值为空值
			password = "";
			resetPassWord(password);
		}
		btnSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//单机之后改变状态
				if (LocusMainActivity.isOn) {
					//首先检查是否存在密码，存在，先输入密码，验证通过后才能关闭密码锁屏
					//跳转至Login 进行验证，同时将进行的操作的参数穿进去
					if(StringUtil.isNotEmpty(getPassword())) {
						Intent intToLogin = new Intent();
						intToLogin.putExtra("operation", LocusMainActivity.OP_CLOSE_LOCK);
						intToLogin.setClass(LocusMainActivity.this, LoginActivity.class);
						startActivity(intToLogin);
						Log.e(TAG, "hhhhhhhhere");
						return;//把当前的activity进程进行关闭，最后成功之后跳转回来，在处理相应请求，如果输入密码成功了，就可以把密码设成一个独特的值，前面判断，就可以关闭了
					}
					
					//不存在密码，直接绘制成空
					//现在关闭密码锁屏,绘制上面图片
					StatusUtil.setStatus(false, LocusMainActivity.this);
					LocusMainActivity.isOn = false;
					btnSwitch.setText("打开密码\n锁屏功能");
					Toast.makeText(LocusMainActivity.this, "密码锁屏功能关闭，需要时请重新打开", Toast.LENGTH_SHORT).show();
					btnReset.setVisibility(View.GONE);
					//绘制完成，就要进行密码的设为空
					resetPassWord("");
					
				}else if (!LocusMainActivity.isOn) {
					//现在打开密码锁屏,打开锁屏，就肯定是空密码，既然是空密码，就会是一定重新绘制，根据重新绘制的逻辑，当前界面要finish掉才能够不会出现两个activity
					StatusUtil.setStatus(true, LocusMainActivity.this);
					LocusMainActivity.isOn = true;
					btnSwitch.setText("关闭密码\n锁屏功能");
					Toast.makeText(LocusMainActivity.this, "密码锁屏功能已经开启", Toast.LENGTH_SHORT).show();
					btnReset.setVisibility(View.VISIBLE);
					//现在存在一个逻辑是如果我关闭的时候 我会把密码赋值为空，那么这个时候既然密码为空，
					//那么最后可以直接跳转到login那个界面，进行初始化，那么这样在进行一个的锁屏过程中就不会出现空值的情况，就好多了
					Intent intToLogin = new Intent(LocusMainActivity.this, LoginActivity.class);
					startActivity(intToLogin);
					finish();
				}else {
					Log.e(TAG, "UNKOWN ERROR! ");
				}
				
			}
		});
		
		
		
	}
	
	
	/**
	 * 取得密码
	 * 
	 * @return
	 */
	private String getPassword() {
		SharedPreferences settings = this.getSharedPreferences(
				LocusPassWordView.name, 0);
		Log.e(TAG, "NAME : "+LocusPassWordView.name);
		return settings.getString("password", ""); // , "0,1,2,3,4,5,6,7,8"
	}
	
	/**
	 * 设置密码
	 * 
	 * @param password
	 */
	public void resetPassWord(String password) {
		SharedPreferences settings = this.getSharedPreferences(
				LocusPassWordView.name, 0);
		Editor editor = settings.edit();
		editor.putString("password", password);
		editor.commit();
	}

	@Override
	protected void onPause() {
		Log.e(TAG, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Log.e(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		Log.e(TAG, "onResume");
		LocusMainActivity.isOn = StatusUtil.getStatus(LocusMainActivity.this);
				
		//重新绘制
		if (LocusMainActivity.isOn) {
			//开启了密码锁屏
			btnSwitch.setText("关闭密码\n锁屏功能");
			btnReset.setVisibility(View.VISIBLE);
		}else if (!LocusMainActivity.isOn ||  password.equals(LoginActivity.AUTHENTICATION_SUCCESS)) {
			//如果处于本身就是关闭状态，或者是验证成功之后，就可以直接绘制图片为关闭状态，同时初始化密码
			//关闭了密码锁屏
			btnSwitch.setText("打开密码\n锁屏功能");
			btnReset.setVisibility(View.GONE);
			//重新赋值为空值
			password = "";
			resetPassWord(password);
		}
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locus_main, menu);
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
