package com.allan.adbwifi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
