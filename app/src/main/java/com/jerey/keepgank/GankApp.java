package com.jerey.keepgank;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class GankApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("xiamin");
    }
}
