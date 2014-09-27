package com.eva.me.myscreenlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eva.me.myscreenlock.LocusPassWordView.OnCompleteListener;

public class LoginActivity extends Activity {
	private LocusPassWordView lpwv;
	private Toast toast;

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
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				// 如果密码正确,则进入主页面。
				if (lpwv.verifyPassword(mPassword)) {
					showToast("登陆成功！");
					Intent intent = new Intent(LoginActivity.this,
							LocusMainActivity.class);
					// 打开新的Activity
					startActivity(intent);
					finish();
				} else {
					showToast("密码输入错误,请重新输入");
					lpwv.markError();
				}
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
