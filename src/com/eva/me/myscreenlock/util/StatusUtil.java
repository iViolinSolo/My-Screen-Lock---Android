package com.eva.me.myscreenlock.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.eva.me.myscreenlock.LocusMainActivity;
import com.eva.me.myscreenlock.LocusPassWordView;

public class StatusUtil {
	private static final String TAG= "StatusUtil";
	/**
	 * 取得当前状态，是否开启了九宫格锁屏
	 * 
	 * @return
	 */
	public static boolean getStatus(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				LocusMainActivity.name, 0);
		Log.e(TAG, "NAME : "+LocusMainActivity.name);
		return settings.getBoolean("status", false); // , "0,1,2,3,4,5,6,7,8"
	}
	
	/**
	 * 设置当前状态，是否开启了九宫格锁屏
	 * 
	 * @param status
	 * @return status
	 */
	public static void setStatus(boolean status, Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				LocusMainActivity.name, 0);
		Editor editor = settings.edit();
		editor.putBoolean("status", status);
		editor.commit();
	}
	
}
