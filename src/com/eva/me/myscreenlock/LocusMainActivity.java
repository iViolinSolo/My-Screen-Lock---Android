package com.eva.me.myscreenlock;

import com.eva.me.myscreenlock.R;
import com.eva.me.myscreenlock.SetPasswordActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LocusMainActivity extends Activity {
	private static final String TAG = "LocusMainActivity";
	private TextView btnSwitch;
	private String password = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		init();
	}

	private void init() {
		password = this.getPassword();
		Log.e(TAG, "PASSWORD is : "+password);
		
		View v = (View) this.findViewById(R.id.tvReset);
		v.setOnClickListener(new OnClickListener() {
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
		btnSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnSwitch.setText("关闭锁屏");
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
