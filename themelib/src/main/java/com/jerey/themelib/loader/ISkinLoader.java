package com.jerey.themelib.loader;


import com.jerey.themelib.ISkinUpdate;

/**
 * <p></p>
 * 用来添加、删除需要皮肤更新的界面以及通知界面皮肤更新
 */
public interface ISkinLoader {
    void attach(ISkinUpdate observer);

    void detach(ISkinUpdate observer);

    void notifySkinUpdate();
}
