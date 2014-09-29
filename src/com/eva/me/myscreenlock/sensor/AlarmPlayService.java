package com.eva.me.myscreenlock.sensor;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class AlarmPlayService extends Service{
	private static final String TAG  = "AlarmPlayService";
	private MediaPlayer mediaPlayer = null;
	private int resID = com.eva.me.myscreenlock.R.raw.apple;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "OnCreate()");
		
		if(mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(this, resID);
			Log.e(TAG, "OnCreate() -> mediaPalyer");
			mediaPlayer.setLooping(true);
		}
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e(TAG, "OnStartCommand()");
		int op = intent.getIntExtra("op", -1);
		Log.e(TAG, "OnStartCommand --> op: "+op);
		switch (op) {
		case 1:
			start();
			break;

		case 2:
			pause();
			break;

		case 3:
			stop();
			break;

		case 4:
//			exit();
			Log.e(TAG, "OnStartCommand --> "+"stopSelf");
			stopSelf();//???????????????????
			Log.e(TAG, "OnStartCommand --> "+"EXIT");
			break;

		default:
			Log.e(TAG, "OnStartCommand --> "+"ERROR");
			break;
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void stop() {
		if(mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare();
				Log.e(TAG, "stop() -> prepare");
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void pause() {
		if(mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			Log.e(TAG, "pause() -> mediaPlayer.pause()");
		}
	}

	private void start() {
		if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
			mediaPlayer.start();
			Log.e(TAG, "start() -> mediaPlayer.start()");
		}
		
	}
	
	@Override
	public void onDestroy() {
		Log.e(TAG, "OnDestroy()");
		
		super.onDestroy();
		if(mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
}
