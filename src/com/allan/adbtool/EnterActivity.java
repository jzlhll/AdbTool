package com.allan.adbtool;

import java.io.File;

import com.allan.adbtool.logcat.LogcatActivity;
import com.allan.adbtool.wifiAdb.AdbWifiActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class EnterActivity extends Activity implements OnClickListener {
	private EditText mScreenshotEditText;

	private Handler mHandler = new Handler();
	private int sleepTime = 10;

	Runnable mScreenshotRunnable = new Runnable() {

		@Override
		public void run() {
			if (sleepTime > 0) {
				Toast.makeText(EnterActivity.this, "将会在" + (sleepTime / 1000) + "秒后截图..", Toast.LENGTH_SHORT).show();
				sleepTime -= 2000;
				mHandler.postDelayed(mScreenshotRunnable, 2000);
			} else {
				screenshot();
				mHandler.postDelayed(mScreenshotEndRunnable, 1000);
			}
		}
	};

	private static final String SCREENSHOT_NAME = "/sdcard/screenshot.png";

	private void screenshot() {
		File file = new File(SCREENSHOT_NAME);
		if (file.exists()) {
			file.delete();
		}
		Log.d("allan", "screenshot..");
		CmdHelper.RuntimeExec("screencap -p " + SCREENSHOT_NAME);
	}

	Runnable mScreenshotEndRunnable = new Runnable() {

		@Override
		public void run() {
			File file = new File(SCREENSHOT_NAME);
			String ret = "失败";
			if (file.exists()) {
				if (file.length() > 1024 * 1024) {
					ret = "成功";
				} else {
					ret = "失败";
					file.delete();
				}
			}
			Toast.makeText(EnterActivity.this, "截图 " + SCREENSHOT_NAME + ret + "!", Toast.LENGTH_LONG).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter);
		mScreenshotEditText = (EditText) findViewById(R.id.screnshot_edit);
		SharedPreferenceHelper spHelper = new SharedPreferenceHelper(getApplicationContext());
		sleepTime = spHelper.getScreenshotSleepTime() * 1000;
		mScreenshotEditText.setText("" + sleepTime / 1000);
		findViewById(R.id.adbwifi).setOnClickListener(this);
		findViewById(R.id.logcat).setOnClickListener(this);
		findViewById(R.id.cmd).setOnClickListener(this);
		findViewById(R.id.screnshot).setOnClickListener(this);
		findViewById(R.id.MTKLOG).setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.adbwifi:
			startActivity(new Intent(EnterActivity.this.getApplicationContext(), AdbWifiActivity.class));
			break;
		case R.id.logcat:
			startActivity(new Intent(EnterActivity.this.getApplicationContext(), LogcatActivity.class));
			break;
		case R.id.cmd:
			// startActivity(new
			// Intent(EnterActivity.this.getApplicationContext(),
			// CmdActivity.class));
			break;
		case R.id.screnshot:
			String s = mScreenshotEditText.getText().toString();
			SharedPreferenceHelper spHelper = new SharedPreferenceHelper(getApplicationContext());
			spHelper.writeScreenshotSleepTime(Integer.parseInt(s));
			sleepTime = Integer.parseInt(s) * 1000;
			mHandler.removeCallbacks(mScreenshotRunnable);
			mHandler.post(mScreenshotRunnable);
			break;
		case R.id.MTKLOG:
			Intent it = new Intent();
			it.setComponent(new ComponentName("com.mediatek.engineermode", "com.mediatek.engineermode.EngineerMode"));
			try {
				startActivity(it);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(getApplicationContext(), "No MTK Logger!", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
