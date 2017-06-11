package com.jerey.themelib.utils;

import android.util.Log;

import com.jerey.themelib.SkinConfig;


/**
 * Skin log库
 */
public class SkinL {
    public static void i(String tag, String msg) {
        if (SkinConfig.isDebug()) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (SkinConfig.isDebug()) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (SkinConfig.isDebug()) {
            Log.e(tag, msg);
        }
    }
}
