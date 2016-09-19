package com.allan.adbtool.logcat;

import com.allan.adbtool.CmdHelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LogcatService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(30);
                char l = 'V';
                switch (level) {
				case 1:
					l = 'D';
					break;
				case 2:
					l = 'I';
					break;
				case 3:
					l = 'W';
					break;
				case 4:
					l = 'E';
					break;
				default:
					break;
				}
                CmdHelper.awaitShowAllLog(isTimeChecked, tagString, l);
            } catch (InterruptedException e) {
                Log.d("allan", "stoped!");
            } finally {
                Log.d("allan", "over!");
            }
        }
    }

    private boolean isStop = true;

    private boolean isTimeChecked;
    private String tagString;
    private int level;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "start".equals(intent.getStringExtra("action"))) {
            if (!isStop) {
                return super.onStartCommand(intent, flags, startId);
            }
            isTimeChecked = intent.getBooleanExtra("isTime", false);
            tagString = intent.getStringExtra("tag");
            level = intent.getIntExtra("level", 0);
            new MyRunnable().run(); //不是子线程
            isStop = true;
            return super.onStartCommand(intent, flags, startId);
        } else if (intent != null
                && "stop".equals(intent.getStringExtra("action"))) {
            if (isStop) {
                return super.onStartCommand(intent, flags, startId);
            }
            String pss = CmdHelper.getPkgId("logcat");
            CmdHelper.killAllPs(pss);
            isStop = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
