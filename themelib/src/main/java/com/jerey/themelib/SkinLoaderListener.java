package com.jerey.themelib;

/**
 * 皮肤加载监听器
 */
public interface SkinLoaderListener {
    void onStart();

    void onSuccess();

    void onFailed(String errMsg);

    /**
     * called when from network load skin
     *
     * @param progress download progress
     */
    void onProgress(int progress);
}
