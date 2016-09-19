package com.allan.adbtool.wifiAdb;

import com.allan.adbtool.CmdHelper;
import com.allan.adbtool.R;
import com.allan.adbtool.R.id;
import com.allan.adbtool.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AdbWifiActivity extends Activity implements OnClickListener{
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adbwifi);
		mTextView = (TextView) findViewById(R.id.showTextView);
		findViewById(R.id.startAdbWifi).setOnClickListener(this);
		findViewById(R.id.stopAdbWifi).setOnClickListener(this);
	}

	private void showInfo(String s) {
		mTextView.setText(s);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startAdbWifi:
			CmdHelper.startAdbWifi();
			String s1 = WifiIp.getWifiIp(getApplicationContext()) + ":5678";
			showInfo("保证电脑跟该机器一个网段，输入\n adb connect " + s1);
			break;
		case R.id.stopAdbWifi:
			CmdHelper.stopAdbWifi();
			break;
		}
	}
}
