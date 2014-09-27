package com.eva.me.myscreenlock;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LocalService extends Service {
	
	private static final String TAG = "LocalService";

	private Intent startIntent = null;
	private LocalBroadcastReceiver receiver = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		IntentFilter filter = new IntentFilter(); 
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		
		receiver = new LocalBroadcastReceiver();
		
		registerReceiver(receiver, filter);
		
		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("KeyguardLock");
		keyguardLock.disableKeyguard();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startIntent = intent;
		
		Log.e(TAG, "OnStartCommand -> action: "+intent.getAction()+"\n startId: "+startId);
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "OnDestroy");
		
		unregisterReceiver(receiver);
		
		if(startIntent != null) {
			Log.e(TAG, "OnDestroy -> startIntent is not null");
			startService(startIntent);
		}
		super.onDestroy();
	}
}
