package com.allan.adbtool;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceHelper {
    private SharedPreferences spPreferences;
    private Editor editor;

    private int top = 10; // top顶层 大于总数0~9
    private static final String NAME = "cmd"; // 加上一个数字就是存储的位置
    private ArrayList<String> cmdList = new ArrayList<>();

    /**
     * 最多保存10份
     * 
     * @param context
     */
    public SharedPreferenceHelper(Context context) {
        spPreferences = context.getSharedPreferences("mine",
                Context.MODE_PRIVATE);
        editor = spPreferences.edit();
        top = 10;
        for (int i = 0; i < 10; i++) {
            cmdList.add(spPreferences.getString(NAME + i, ""));
        }
    }

    public void writeInto(String cmd) {
        top = 10;
        for (int i = 0; i < 9; i++) {
            editor.putString(NAME + i, cmdList.get(i + 1)).commit();
            cmdList.set(i, cmdList.get(i + 1));
        }
        editor.putString(NAME + "9", cmd).commit();
        cmdList.set(9, cmd);
    }
    
    public void writeLevel(char lev) {
        editor.putString("logLevel", ""+lev).commit();
    }
    
    public char getLevel() {
        return spPreferences.getString("logLevel", "V").charAt(0);
    }
    
    public void writeTag(String tag) {
        editor.putString("logTag", tag).commit();
    }
    
    public String getTag() {
        return spPreferences.getString("logTag", "");
    }
    
    public boolean getTimeChecked() {
        return spPreferences.getBoolean("logTime", false);
    }
    
    public void writeTimeChecked(boolean isChecked) {
        editor.putBoolean("logTime", isChecked).commit();
    }

    public String getPrev() {
        if (top > 0) {
            top--;
        }
        return cmdList.get(top);
    }

    public String getNext() {
        if (top < 9) {
            top++;
        } else {
            top = 9;
        }
        return cmdList.get(top);
    }
}
