package com.jerey.themelib.base;

import android.app.Application;

import com.jerey.themelib.SkinConfig;
import com.jerey.themelib.loader.SkinManager;
import com.jerey.themelib.utils.SkinFileUtils;

import java.io.File;
import java.io.IOException;

public class SkinBaseApplication extends Application {

    public void onCreate() {

        super.onCreate();
        initSkinLoader();
    }

    /**
     * Must call init first
     */
    private void initSkinLoader() {
        setUpSkinFile();
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().loadSkin();
    }

    private void setUpSkinFile() {
        try {
            String[] skinFiles = getAssets().list(SkinConfig.SKIN_DIR_NAME);
            for (String fileName : skinFiles) {
                File file = new File(SkinFileUtils.getSkinDir(this), fileName);
                if (!file.exists())
                    SkinFileUtils.copySkinAssetsToDir(this, fileName, SkinFileUtils.getSkinDir(this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
