 package com.eva.me.myscreenlock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eva.me.myscreenlock.slide.ScreenLockActivity;

public class LocalBroadcastReceiver extends BroadcastReceiver{
	private static final String TAG = "LocalBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String intentAction = intent.getAction();
		Log.e(TAG, "The Intent Action is: "+intentAction);
		
		Log.e(TAG, "The Restart label isShow is: "+ScreenLockActivity.isShow);
		
		if(intentAction.equals(""+Intent.ACTION_SCREEN_ON)) {
//			if (!ScreenLockActivity.isShow) {
//				Intent tempIntent = new Intent(context, ScreenLockActivity.class);
//				tempIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(tempIntent);
//			}
			
		}else if (intentAction.equals(""+Intent.ACTION_SCREEN_OFF)) {
//			这几行出现了一个bug 会造成用户的交互效果不好，感觉到程序很卡，每次都是先显示一下桌面然后在显示自己的锁屏界面，这个很不好 效果很差
//			if (ScreenLockActivity.isShow) {
//				if (ScreenLockActivity.getInstance() != null) {
//					((Activity) ScreenLockActivity.getInstance()).finish();
////					ScreenLockActivity.isShow = false;
//				}
//			}
			//为了赶紧最后的效果，决定每次在屏幕灭掉的时候进行锁屏的启动，唯一的不好处就是第一次启动的时候还是有一点卡
			Intent tempIntent = new Intent(context, ScreenLockActivity.class);
			tempIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			tempIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			context.startActivity(tempIntent);
		
		}
	}

}
