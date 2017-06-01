package com.jerey.keepgank;

import android.app.Application;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatDelegate;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.jerey.keepgank.Constant.AppConstant;
import com.jerey.loglib.LogTools;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class GankApp extends Application implements ThemeUtils.switchColor{
    @Override
    public void onCreate() {
        super.onCreate();
        //判断主题是否是白天
        if (getSharedPreferences(AppConstant.SP, MODE_PRIVATE).getBoolean(AppConstant.Theme, true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        ThemeUtils.setSwitchColor(this);
    }

    @Override
    public int replaceColorById(Context context, @ColorRes int colorId) {
        LogTools.i("replaceColorById colorres :" + colorId);
        return context.getResources().getColor(R.color.blue);
    }

    @Override
    public int replaceColor(Context context, @ColorInt int color) {
        LogTools.i("replaceColor color :" + color);
        return  context.getResources().getColor(R.color.blue);
    }
}
