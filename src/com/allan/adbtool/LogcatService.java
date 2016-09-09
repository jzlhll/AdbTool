package com.allan.adbtool;

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

    private class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(30);
                CmdHelper.awaitShowAllLog(isTimeChecked, tagString, level);
            } catch (InterruptedException e) {
                Log.d("allan", "stoped!");
            } finally {
                Log.d("allan", "over!");
            }
        }
    }

    private Thread mThread;

    private boolean isStop = true;

    private boolean isTimeChecked;
    private String tagString;
    private char level;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "start".equals(intent.getStringExtra("action"))) {
            if (!isStop) {
                return super.onStartCommand(intent, flags, startId);
            }
            isTimeChecked = intent.getBooleanExtra("isTime", false);
            tagString = intent.getStringExtra("tag");
            level = intent.getCharExtra("level", 'V');
            mThread = new Thread(new MyThread());
            mThread.start();
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
