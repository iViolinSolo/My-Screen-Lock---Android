package com.eva.me.myscreenlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eva.me.myscreenlock.LocusPassWordView.OnCompleteListener;
import com.eva.me.myscreenlock.util.StatusUtil;

public class LoginActivity extends Activity {
	private static final String TAG = "LoginActivity";
	public static final String AUTHENTICATION_SUCCESS= "AuthenticationSuccess";
	private LocusPassWordView lpwv;
	private Toast toast;
	private int operation = -1;

	private void showToast(CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}

		toast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		init();
	}

	private void init() {
		Intent intFrOther = getIntent();
		operation = intFrOther.getIntExtra("operation", -1);
		
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				
				switch (operation) {
				case LocusMainActivity.OP_CLOSE_LOCK:
					Log.e(TAG, "CASE: LocusMainActivity.OP_CLOSE_LOCK");
					
					// 如果密码正确,则进入主页面。
					if (lpwv.verifyPassword(mPassword)) {
						showToast("密码验证成功！");
						lpwv.resetPassWord(LoginActivity.AUTHENTICATION_SUCCESS);
						StatusUtil.setStatus(false, LoginActivity.this);
//						Intent iTmp = new Intent(LoginActivity.this, LocusMainActivity.class);
//						startActivity(iTmp);
						finish();
					} else {
						showToast("密码输入错误,请重新输入");
						lpwv.markError();
					}
					
					break;

				case -1:
					Log.e(TAG, "UNKONWN ERROR: -1");
					break;
					
				default:
					break;
				}
				
//				// 如果密码正确,则进入主页面。
//				if (lpwv.verifyPassword(mPassword)) {
//					showToast("登陆成功！");
//					Intent intent = new Intent(LoginActivity.this,
//							LocusMainActivity.class);
//					// 打开新的Activity
//					startActivity(intent);
//					finish();
//				} else {
//					showToast("密码输入错误,请重新输入");
//					lpwv.markError();
//				}
				
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 如果密码为空,则进入设置密码的界面
		View noSetPassword = (View) this.findViewById(R.id.tvNoSetPassword);
		TextView toastTv = (TextView) findViewById(R.id.login_toast);
		if (lpwv.isPasswordEmpty()) {
			lpwv.setVisibility(View.GONE);
			noSetPassword.setVisibility(View.VISIBLE);
			toastTv.setText("请先绘制手势密码");
			noSetPassword.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LoginActivity.this,
							SetPasswordActivity.class);
					// 打开新的Activity
					startActivity(intent);
					finish();
				}

			});
		} else {
			toastTv.setText("请输入手势密码");
			lpwv.setVisibility(View.VISIBLE);
			noSetPassword.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
