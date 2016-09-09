package com.allan.adbtool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import android.util.Log;

public class CmdHelper {
    // /*
    // * cmmand[0] : shell 命令 如"ls" 或"ls -1"; cmmand[1] : 命令执行路径 如"/" ;
    // */
    // public static String processBuilderExecute(String[] cmmand)
    // throws IOException {
    // String result = "";
    // try {
    // ProcessBuilder builder = new ProcessBuilder(cmmand);
    // builder.redirectErrorStream(true);
    // Process process = builder.start();
    // // 得到命令执行后的结果
    // InputStream is = process.getInputStream();
    // byte[] buffer = new byte[2048];
    // while (is.read(buffer) != -1) {
    // result = result + new String(buffer);
    // }
    // is.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return result;
    // }

    public static void execSuShell(String cmd) {
        try {
            // 权限设置
            Process p = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            // 将命令写入
            dataOutputStream.writeBytes(cmd);
            // 提交命令
            dataOutputStream.flush();
            // 关闭流操作
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            Log.d("allan", "ssdsds");
            t.printStackTrace();
        }
    }

    /**
     * @param command
     *            command
     * @return null
     */
    public static String RuntimeExec(String command) {
        BufferedReader br2 = null;
        String line = null;
        String ret = "";
        InputStream is = null;
        InputStreamReader isReader = null;
        try {
            Process proc = Runtime.getRuntime().exec(command);
            is = proc.getInputStream();
            isReader = new InputStreamReader(is, "utf-8");
            br2 = new BufferedReader(isReader);
            while ((line = br2.readLine()) != null) {
                ret += "\n" + line;
            }
        } catch (IOException e) {
            Log.d("allan", "IOExcpe RUntimeExec");
            return ret;
        } finally {
            if (isReader != null) {
                try {
                    isReader.close();
                } catch (IOException e) {
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e) {
                }
            }
        }
        return ret;
    }

    public static void startAdbWifi() {
        RuntimeExec("setprop service.adb.tcp.port 5678");
        RuntimeExec("stop adbd");
        RuntimeExec("start adbd");
    }

    public static void stopAdbWifi() {
        RuntimeExec("setprop service.adb.tcp.port -1");
        RuntimeExec("stop adbd");
        RuntimeExec("start adbd");
    }
    
    public static String screenshot() {
        int id = 1;
        File file = null;
        String filepath = "";
        while (true) {
            filepath = "/sdcard/screenshot" + id + ".png";
            file = new File(filepath);
            if (file.exists()) {
                id++;
            } else {
                break;
            }
        }
        RuntimeExec("rm " + filepath);
        RuntimeExec("screencap -p " + filepath);
        
        if (file.exists()) {
            return filepath;
        }
        return "";
    }

    public static String getPkgId(String pkg) {
        // return RuntimeExec("ps | grep " + pkg); TODO 貌似不能传入管道
        String string = RuntimeExec("ps");
        String[] ss = string.split("\n");
        string = "";
        for (int i = 0; i < ss.length; i++) {
            if (ss[i] != null && ss[i].contains(pkg)) {
                string +=  ss[i] + "\n";
            }
        }
        return string;
    }
    
    public static void killAllPs(String pss) {
        String[] ss = pss.split("\n");
        String replaceMent = ",";
        for (int i = 0; i < ss.length; i++) {
            String newstr1 = ss[i].replaceAll(" ", replaceMent); // 先把所有空格替换成
                                                                 // 逗号。
            String newstr2 = newstr1.replaceAll("\t", replaceMent); // 再把所有的制表符替换成逗号。
            String newstr3 = newstr2.replaceAll(replaceMent + "+", replaceMent); // 把所有重复的逗号合并成一个逗号。
            String[] strings = newstr3.split(replaceMent);
            //进程号是第二个
            String s = strings[1];
            RuntimeExec("kill " + s);
        }
    }

    public static void awaitShowAllLog(boolean isHaveTime, String tag, char level) {
        if (tag == null || tag.trim().equals("")) {
            tag = "";
        }
        ArrayList<String> commandLine = new ArrayList<String>();
        //第一步杀掉所有logcat进程
        String pss = getPkgId("logcat");
        killAllPs(pss);
        // 第二步
        commandLine.add("rm -r /sdcard/logcat.txt");// 如果不删除之前的logcat.txt文件，每次执行logcat命令也不会更新该文件
        commandLine.add("logcat -c");// 使用 d 该参数可以让logcat获取日志完毕后终止进程
        String cmd = "";
        String levels = "*:" + level;
        String tagLevels = tag + ":" + level + " *:S";
        if (isHaveTime && "".equals(tag)) {
            cmd = "logcat " + levels + " -v time > /sdcard/logcat.txt &";
        } else if (!isHaveTime && "".equals(tag)) {
            cmd = "logcat " + levels + " > /sdcard/logcat.txt &";
        } else if (isHaveTime && !"".equals(tag)) {
            cmd = "logcat " + tagLevels + " -v time > /sdcard/logcat.txt &";
        } else if (!isHaveTime && !"".equals(tag)) {
            cmd = "logcat " + tagLevels + " > /sdcard/logcat.txt &";
        }
        RuntimeExec(cmd);
    }
}
