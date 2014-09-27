package com.eva.me.myscreenlock;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocalBroadcastReceiver extends BroadcastReceiver{
	private static final String TAG = "LocalBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String intentAction = intent.getAction();
		Log.e(TAG, "The Intent Action is: "+intentAction);
		
		Log.e(TAG, "The Restart label isShow is: "+ScreenLockActivity.isShow);
		
		if(intentAction.equals(""+Intent.ACTION_SCREEN_ON)) {
			if (!ScreenLockActivity.isShow) {
				Intent tempIntent = new Intent(context, ScreenLockActivity.class);
				tempIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(tempIntent);
			}
			
		}else if (intentAction.equals(""+Intent.ACTION_SCREEN_OFF)) {
			if (ScreenLockActivity.isShow) {
				if (ScreenLockActivity.getInstance() != null) {
					((Activity) ScreenLockActivity.getInstance()).finish();
//					ScreenLockActivity.isShow = false;
				}
			}
		}
	}

}
