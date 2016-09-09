package com.allan.adbtool;

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
            mNextButton, mScreenshotBtn;
    TextView mShowTextView;
    EditText mEditText;

    private SharedPreferenceHelper mspHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        mspHelper = new SharedPreferenceHelper(this);
        mEnterButton = (Button) findViewById(R.id.enterBtn);
        mStartAdbWifiBtn = (Button) findViewById(R.id.startAdbWifi);
        mStopAdbWifiBtn = (Button) findViewById(R.id.stopAdbWifi);
        mPrevButton = (Button) findViewById(R.id.prev);
        mNextButton = (Button) findViewById(R.id.next);
        mScreenshotBtn = (Button) findViewById(R.id.screnshot);

        mScreenshotBtn.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mEnterButton.setOnClickListener(this);
        mStartAdbWifiBtn.setOnClickListener(this);
        mStopAdbWifiBtn.setOnClickListener(this);

        mShowTextView = (TextView) findViewById(R.id.showTextView);
        mEditText = (EditText) findViewById(R.id.edit);

        Intent it = new Intent();
        it.setComponent(new ComponentName("com.aliyun.filemanager",
                "com.aliyun.filemanager.service.filemanagerservice"));
        it.putExtra("action", "start");
        startService(it);

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
        case R.id.screnshot:
            mHandler.removeCallbacks(mScreenshotRunnable);
            mHandler.post(mScreenshotRunnable);
            break;
        default:
            break;
        }
    }

    private Handler mHandler = new Handler();
    private int sleepTime = 10;
    Runnable mScreenshotRunnable = new Runnable() {

        @Override
        public void run() {
            String reg = "^\\d+$";
            String s = mEditText.getText().toString();
            if (s != null && s.matches(reg) && !s.equals("0")) {
                int sl = Integer.parseInt(s);
                sleepTime = Integer.parseInt(s) * 1000;
                Toast.makeText(MainActivity.this, "将会在" + s + "秒后截图..",
                        Toast.LENGTH_SHORT).show();
                sl= (sl - 2) >= 0 ? sl - 2 : 0;
                mEditText.setText("" + sl);
                mHandler.postDelayed(mScreenshotRunnable, 2000);
            } else {
                String screenshot = CmdHelper.screenshot();
                Toast.makeText(
                        MainActivity.this,
                        "截图 " + screenshot + (!screenshot.equals("") ? "成功" : "失败")
                                + "\n你可以在Edit中输入数字，将会等待x秒后再截图",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(
                        MainActivity.this,
                        "截图 " + screenshot + (!screenshot.equals("") ? "成功" : "失败")
                                + "\n你可以在Edit中输入数字，将会等待x秒后再截图",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(
                        MainActivity.this,
                        "截图 " + screenshot + (!screenshot.equals("") ? "成功" : "失败")
                                + "\n你可以在Edit中输入数字，将会等待x秒后再截图",
                        Toast.LENGTH_LONG).show();
            }
        }
    };
}