package com.allan.adbtool.logcat;

import java.util.ArrayList;
import java.util.List;

import com.allan.adbtool.R;
import com.allan.adbtool.SharedPreferenceHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class LogcatActivity extends Activity implements OnClickListener {
	private class ChangedClass implements OnCheckedChangeListener{
		int id = 0;
		public ChangedClass(int id) {
			this.id = id;
		}
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				for (RadioButton rButton : mRadioButtons) {
					rButton.setChecked(false);
				}
				mRadioButtons[id].setChecked(true);
			}
		}
	}
	
	private Button mLogCatBtn;

	private TextView mLogShowTextView;
	private CheckBox mIsPrintTimeLogCheckbox;
	private EditText editText;
	private RadioButton mRadioButtons[] = null; // 0, 1, 2, 3, 4 分别是V,D,I,W,E
	SharedPreferenceHelper mSharedPreferenceHelper = null;
	
	private RadioButton mState0, mState1;

	//viewpager{
	private ViewPager mViewPager;
	private MyViewPageAdapter mPageAdapter = null;
	private List<RelativeLayout> mListViews;
	//viewpager}

	//page2 {
	private EditText mSearch01Et, mSearch02Et, mRefreshTimeEt;
	private Button mRefreshBtn;
	//page2}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferenceHelper mSharedPreferenceHelper = new SharedPreferenceHelper(this);
		Log.d("allan", "getLeve" + getLevel());
		mSharedPreferenceHelper.writeLevel(getLevel());
		mSharedPreferenceHelper.writeTag(editText.getText().toString());
		mSharedPreferenceHelper.writeTimeChecked(mIsPrintTimeLogCheckbox.isChecked());
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 初始化 radios{
		if (mRadioButtons == null) {
			mRadioButtons = new RadioButton[5];
			mRadioButtons[0] = (RadioButton) mListViews.get(0).findViewById(R.id.radio0);
			mRadioButtons[1] = (RadioButton) mListViews.get(0).findViewById(R.id.radio1);
			mRadioButtons[2] = (RadioButton) mListViews.get(0).findViewById(R.id.radio2);
			mRadioButtons[3] = (RadioButton) mListViews.get(0).findViewById(R.id.radio3);
			mRadioButtons[4] = (RadioButton) mListViews.get(0).findViewById(R.id.radio4);
			int id = 0;
			for (RadioButton rb : mRadioButtons) {
				rb.setOnCheckedChangeListener(new ChangedClass(id));
				id++;
			}
		}
		// }
		for (RadioButton rb : mRadioButtons) {
			rb.setChecked(false);
		}
		int lv = mSharedPreferenceHelper.getLevel();
		mRadioButtons[lv].setChecked(true);
		mIsPrintTimeLogCheckbox.setChecked(mSharedPreferenceHelper.getTimeChecked());
		editText.setText(mSharedPreferenceHelper.getTag());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.logcat_act);
		
		mState0 = (RadioButton) findViewById(R.id.logger);
		mState1 = (RadioButton) findViewById(R.id.logshow);
		mListViews = new ArrayList<RelativeLayout>();  
		LayoutInflater mInflater = getLayoutInflater();  
        mListViews.add((RelativeLayout)mInflater.inflate(R.layout.logcat_act1, null));  
        mListViews.add((RelativeLayout)mInflater.inflate(R.layout.logcat_act2, null));  

		mViewPager = (ViewPager) findViewById(R.id.awesomepager);
		mPageAdapter = new MyViewPageAdapter(mListViews);  
        mViewPager = (ViewPager) findViewById(R.id.awesomepager);  
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				mState0.setChecked(position == 0);
				mState1.setChecked(position == 1);
			}
			
			@Override
			public void onPageScrolled(int position,float positionOffset, int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		mLogCatBtn = (Button) mListViews.get(0).findViewById(R.id.logcatBtn);
		editText = (EditText) mListViews.get(0).findViewById(R.id.tagEdit);
		mLogShowTextView = (TextView) mListViews.get(0).findViewById(R.id.showact1);
		mIsPrintTimeLogCheckbox = (CheckBox) mListViews.get(0).findViewById(R.id.timecheckbox);
		mLogCatBtn.setOnClickListener(this);
		mSharedPreferenceHelper = new SharedPreferenceHelper(this);
		//page2
		mSearch01Et = (EditText) mListViews.get(1).findViewById(R.id.searchFilter01);
		mSearch02Et = (EditText) mListViews.get(1).findViewById(R.id.searchFilter02);
		mRefreshTimeEt = (EditText) mListViews.get(1).findViewById(R.id.timeToRf);
		mRefreshBtn = (Button) mListViews.get(1).findViewById(R.id.refleshShow);
		mRefreshBtn.setOnClickListener(this);
	}

	private int getLevel() {
		for (int j = 0; j < mRadioButtons.length; j++) {
			if (mRadioButtons[j].isChecked()) {
				return j;
			}
		}
		return 0;
	}

	private boolean isStop = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logcatBtn:
			if (isStop) {
				Intent intent = new Intent(this, LogcatService.class);
				intent.putExtra("action", "start");
				intent.putExtra("isTime", mIsPrintTimeLogCheckbox.isChecked());
				intent.putExtra("tag", editText.getText().toString());
				intent.putExtra("level", getLevel());
				startService(intent);
				mLogCatBtn.setText("Logging...");
				mLogShowTextView.setVisibility(View.VISIBLE);
			} else {
				Intent intent = new Intent(this, LogcatService.class);
				intent.putExtra("action", "stop");
				startService(intent);
				mLogCatBtn.setText("Start");
				mLogShowTextView.setVisibility(View.GONE);
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
