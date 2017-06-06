package com.jerey.themelib.utils;

import android.util.Log;

import com.jerey.themelib.SkinConfig;


/**
 * Created by _SOLID
 * Date:2016/12/14
 * Time:10:24
 */
public class SkinL {

    private static final boolean DEBUG;

    static {
        DEBUG = SkinConfig.isDebug();
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }
}
