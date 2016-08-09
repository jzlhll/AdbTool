package com.allan.adbwifi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class LogcatActivity extends Activity implements OnClickListener {
    private TextView mShowTextView;
    private Button mEnterButton;

    private CheckBox mTimeCheckBox;
    private EditText editText;
    private RadioButton mRadioButtonV, mRadioButtonD, mRadioButtonI,
            mRadioButtonW, mRadioButtonE;

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferenceHelper mSharedPreferenceHelper = new SharedPreferenceHelper(
                this);
        mSharedPreferenceHelper.writeLevel(getLevel());
        mSharedPreferenceHelper.writeTag(editText.getText().toString());
        mSharedPreferenceHelper.writeTimeChecked(mTimeCheckBox.isChecked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferenceHelper mSharedPreferenceHelper = new SharedPreferenceHelper(
                this);
        mTimeCheckBox.setChecked(mSharedPreferenceHelper.getTimeChecked());
        editText.setText(mSharedPreferenceHelper.getTag());
        switch (mSharedPreferenceHelper.getLevel()) {
        case 'V':
            mRadioButtonV.setChecked(true);
            break;
        case 'D':
            mRadioButtonD.setChecked(true);
            break;
        case 'I':
            mRadioButtonI.setChecked(true);
            break;
        case 'W':
            mRadioButtonW.setChecked(true);
            break;
        case 'E':
            mRadioButtonE.setChecked(true);
            break;
        default:
            break;
        }
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.logcat_act);
        mShowTextView = (TextView) findViewById(R.id.showTextView);
        mEnterButton = (Button) findViewById(R.id.alllog);
        editText = (EditText) findViewById(R.id.tagEdit);
        mRadioButtonV = (RadioButton) findViewById(R.id.radio0);
        mRadioButtonD = (RadioButton) findViewById(R.id.radio1);
        mRadioButtonI = (RadioButton) findViewById(R.id.radio2);
        mRadioButtonW = (RadioButton) findViewById(R.id.radio3);
        mRadioButtonE = (RadioButton) findViewById(R.id.radio4);
        mTimeCheckBox = (CheckBox) findViewById(R.id.checkBox1);
        mEnterButton.setOnClickListener(this);
    }

    private char getLevel() {
        if (mRadioButtonV.isChecked())
            return 'V';
        if (mRadioButtonI.isChecked())
            return 'I';
        if (mRadioButtonD.isChecked())
            return 'D';
        if (mRadioButtonW.isChecked())
            return 'W';
        if (mRadioButtonE.isChecked())
            return 'E';
        return 'V';
    }

    private class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(30);
                CmdHelper.awaitShowAllLog(mTimeCheckBox.isChecked(), editText
                        .getText().toString(), getLevel());
            } catch (InterruptedException e) {
                Log.d("allan", "stoped!");
            } finally {
                Log.d("allan", "over!");
            }
        }
    }

    private Thread mThread;

    private boolean isStop = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.alllog:
            if (isStop) {
                mThread = new Thread(new MyThread());
                mThread.start();
                mEnterButton.setText("loging...");
            } else {
                String pss = CmdHelper.getPkgId("logcat");
                CmdHelper.killAllPs(pss);
                mEnterButton.setText("Logcat");
            }
            isStop = !isStop;
            break;

        default:
            break;
        }
    }

    // logcat -s allan
    // logcat logcat DVRRemoteService:D *:S 仅有这个DVRRemoteService:D级别的log
    // logcat *:W 所有W级别的log
    // -d 将日志显示在控制台后退出
    // -c 清理已存在的日志
    // -f <filename> 将日志输出到文件
    // logcat -f /sdcard/test.txt
    // 目前支持 TODO
    // 全部log; -s tag的机制；过滤级别的机制;
}
