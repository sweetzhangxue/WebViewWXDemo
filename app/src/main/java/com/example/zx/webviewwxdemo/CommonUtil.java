package com.example.zx.webviewwxdemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    // 获取当前版本号name
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * double转String避免科学计数
     * @param number
     * @return
     */
    public static String double2Str(double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 获取最大值
     * @param values
     * @return
     */
    public static int max(int... values) {
        if (values == null || values.length == 0) return 0;

        int max = values[0];
        for (int i : values)
            if (i > max) max = i;
        return max;
    }

    /** 获取分割字符串
     *
     * @param sourceString
     * @param splitChar
     * @return
     */
    public static String[] getSplitArray(String sourceString,char splitChar) {
        if (TextUtils.isEmpty(sourceString)) {
            return null;
        }

        int count = 0;
        for (int i = 0; i < sourceString.length(); i++) {
            if (sourceString.charAt(i) == splitChar) {
                count++;
            }
        }

        String[] result = new String[count + 1];

        int charIndex = 0;
        for (int i = 0; i < result.length; i++) {
            int start = charIndex;
            int end = sourceString.indexOf(splitChar, charIndex);

            if (end == -1) {
                end = sourceString.length();
            }

            result[i] = sourceString.substring(start, end);
            charIndex = end + 1;

            if (charIndex >= sourceString.length()) {
                charIndex = sourceString.length() - 1;
            }
        }
        return result;
    }

    /**
     * String转double
     * @param str
     * @return
     */
    public static double string2double(String str) {
        if (TextUtils.isEmpty(str))
            return 0;

        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

}
