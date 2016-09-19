package com.allan.adbtool;

import com.allan.adbtool.wifiAdb.WifiIp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    Button mEnterButton, mStartAdbWifiBtn, mStopAdbWifiBtn, mPrevButton,
            mNextButton;
    TextView mShowTextView;
    EditText mEditText;

    private SharedPreferenceHelper mspHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mspHelper = new SharedPreferenceHelper(this);
        mEnterButton = (Button) findViewById(R.id.enterBtn);
        mStartAdbWifiBtn = (Button) findViewById(R.id.startAdbWifi);
        mStopAdbWifiBtn = (Button) findViewById(R.id.stopAdbWifi);
        mPrevButton = (Button) findViewById(R.id.prev);
        mNextButton = (Button) findViewById(R.id.next);
        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mEnterButton.setOnClickListener(this);
        mStartAdbWifiBtn.setOnClickListener(this);
        mStopAdbWifiBtn.setOnClickListener(this);

        mShowTextView = (TextView) findViewById(R.id.showTextView);
        mEditText = (EditText) findViewById(R.id.edit);
    }

    /*
     * 得到回显
     */
    public String getResult02(String cmd) {
        String result = "";
        result = CmdHelper.RuntimeExec(cmd);
        return result;
    }

    private void showInfo(String s) {
        mShowTextView.setText(s);
    }

    @Override
    public void onClick(View v) {
        Log.d("allan", "onclick in adb tool");
        switch (v.getId()) {
        case R.id.next: {
            mEditText.setText(mspHelper.getNext());
            break;
        }
        case R.id.prev: {
            mEditText.setText(mspHelper.getPrev());
            break;
        }
        case R.id.enterBtn:
            String cmds = (mEditText.getText().toString());
            if (cmds.equals("")) {
                showInfo("cmd为空");
            } else {
                String s = getResult02(mEditText.getText().toString());
                Log.i("allan", "s " + s);
                showInfo(s);
                mspHelper.writeInto(cmds);
            }
            break;
        case R.id.startAdbWifi:
            CmdHelper.startAdbWifi();
            String s1 = WifiIp.getWifiIp(getApplicationContext()) + ":5678";
            showInfo("保证电脑跟该机器一个网段，输入\n adb connect " + s1);
            break;
        case R.id.stopAdbWifi:
            CmdHelper.stopAdbWifi();
            break;
        default:
            break;
        }
    }
}