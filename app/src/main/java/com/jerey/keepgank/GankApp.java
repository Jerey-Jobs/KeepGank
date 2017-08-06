package com.jerey.keepgank;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jerey.themelib.SkinConfig;
import com.jerey.themelib.base.SkinBaseApplication;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class GankApp extends SkinBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinConfig.setCanChangeStatusColor(true);
        SkinConfig.setCanChangeFont(true);
        SkinConfig.setDebug(false);
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
    }
}
