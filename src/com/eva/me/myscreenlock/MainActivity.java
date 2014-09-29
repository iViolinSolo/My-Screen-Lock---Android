package com.eva.me.myscreenlock;

import java.net.ContentHandler;

import com.eva.me.myscreenlock.sensor.SensorMainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static String TAG = "MainActivity";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}

	private void init() {
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.eva.me.localService");
				startService(intent);
				Toast.makeText(MainActivity.this, "锁屏功能已经开启，关闭屏幕尝试一下吧~", Toast.LENGTH_SHORT).show();
			}
		});
		
		Button btnStart = (Button) findViewById(R.id.button2);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LocusMainActivity.class);
				startActivity(intent);
			}
		});
		
		Button btnSensor = (Button) findViewById(R.id.button3);
		btnSensor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SensorMainActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button btnAbout = (Button) findViewById(R.id.button4);
		btnAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View content = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_about_us_content, null);
				AlertDialog aboutUsAlertDialog = new AlertDialog.Builder(MainActivity.this)
					.setTitle("关于我们")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					})
					.setView(content)
					.create();
				aboutUsAlertDialog.show();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
