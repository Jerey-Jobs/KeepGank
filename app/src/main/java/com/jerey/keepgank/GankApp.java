package com.jerey.keepgank;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.jerey.keepgank.Constant.AppConstant;
import com.jerey.themelib.base.SkinBaseApplication;
import com.jerey.themelib.SkinConfig;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class GankApp extends SkinBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinConfig.setCanChangeStatusColor(true);
        SkinConfig.setCanChangeFont(true);
        SkinConfig.setDebug(true);
//        //判断主题是否是白天
//        if (getSharedPreferences(AppConstant.SP, MODE_PRIVATE).getBoolean(AppConstant.Theme, true)) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }
    }
}
