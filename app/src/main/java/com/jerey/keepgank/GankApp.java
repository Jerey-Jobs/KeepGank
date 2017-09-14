package com.jerey.keepgank;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jerey.keepgank.utils.NetworkManager;
import com.jerey.lruCache.DiskLruCacheManager;
import com.jerey.themelib.SkinConfig;
import com.jerey.themelib.base.SkinBaseApplication;

import java.io.IOException;

/**
 * Created by Xiamin on 2017/2/12.
 */
public class GankApp extends SkinBaseApplication {

    private static String mCachePath;
    private static DiskLruCacheManager diskLruCacheManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mCachePath = getExternalCacheDir().getPath();
        try {
            diskLruCacheManager = new DiskLruCacheManager(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NetworkManager.init(this);
        SkinConfig.setCanChangeStatusColor(true);
        SkinConfig.setCanChangeFont(true);
        SkinConfig.setDebug(false);
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
    }

    public static String getmCachePath() {
        return mCachePath;
    }

    public static DiskLruCacheManager getDiskCacheManager() {
    }
}
