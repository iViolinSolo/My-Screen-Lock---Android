package com.eva.me.myscreenlock.util;

import java.io.File;

import android.os.Environment;
import android.util.Log;


public class FolderUtil {
	private static final String TAG = "FolderUtil";
	private static String path = ""; 
	private static String sdpath = "";
	private static String nosdpath = "";
	private final static String folderPos = "/MyScreenLockBG"; 
	
	private static boolean hasSDCard  = false;
	
	public static void initFolder() {
		init();
		Log.e(TAG, "sdpath: "+sdpath+" Nosdpath: "+nosdpath);
		String dir = getSaveFolder();
		File des = new File(dir);
		if (!des.exists()) {
			des.mkdirs();
		}
	}
	
	private static void init() {
		hasSDCard();
		initPath();
	}
	
	private static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		  if (status.equals(Environment.MEDIA_MOUNTED)) {
			  FolderUtil.hasSDCard = true;
			  return true;
		  } else {
			  FolderUtil.hasSDCard = false;
			  return false;
		  }
	}
	
	private static void initPath() {
		if (hasSDCard) {
			FolderUtil.sdpath = initSDPath();
			FolderUtil.nosdpath = initNoSDPath();
		} else {
			FolderUtil.nosdpath = initNoSDPath();
		}
	}	
	
	private static String initSDPath() {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	private static String initNoSDPath() {
			return Environment.getRootDirectory().getAbsolutePath();	
	}

	
	public static String  getSaveFolder() {
		if (hasSDCard) {
			path = sdpath + folderPos;
		}else {
			path = nosdpath + folderPos;
		}
		return path;
	}
}
