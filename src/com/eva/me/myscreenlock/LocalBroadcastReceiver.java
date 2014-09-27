package com.eva.me.myscreenlock;

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
		
		if(intentAction.equals(""+Intent.ACTION_SCREEN_ON)) {
			if (!ScreenLockActivity.isShow) {
				Intent tempIntent = new Intent(context, ScreenLockActivity.class);
				tempIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(tempIntent);
			}
			
		}else if (intentAction.equals(""+Intent.ACTION_SCREEN_OFF)) {
			
		}
	}

}
