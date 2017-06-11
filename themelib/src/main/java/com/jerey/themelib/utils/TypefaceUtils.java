package com.jerey.themelib.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.jerey.themelib.SkinConfig;

import java.io.File;


/**
 * 加载字体工具类
 */
public class TypefaceUtils {

    public static Typeface CURRENT_TYPEFACE;

    public static Typeface createTypeface(Context context, String fontName) {
        Typeface tf;
        if (!TextUtils.isEmpty(fontName)) {
            tf = Typeface.createFromAsset(context.getAssets(), getFontPath(fontName));
            SkinPreferencesUtils.putString(context, SkinConfig.PREF_FONT_PATH, getFontPath(fontName));
        } else {
            tf = Typeface.DEFAULT;
            SkinPreferencesUtils.putString(context, SkinConfig.PREF_FONT_PATH, "");
        }
        TypefaceUtils.CURRENT_TYPEFACE = tf;
        return tf;
    }

    /**
     * 从sp中读取字体入路径加载, 若没有,则为默认
     * @param context
     * @return
     */
    public static Typeface getTypeface(Context context) {
        String fontPath = SkinPreferencesUtils.getString(context, SkinConfig.PREF_FONT_PATH);
        Typeface tf;
        if (!TextUtils.isEmpty(fontPath)) {
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            tf = Typeface.DEFAULT;
            SkinPreferencesUtils.putString(context, SkinConfig.PREF_FONT_PATH, "");
        }
        return tf;
    }

    private static String getFontPath(String fontName) {
        return SkinConfig.FONT_DIR_NAME + File.separator + fontName;
    }
}
