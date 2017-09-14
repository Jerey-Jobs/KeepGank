package com.jerey.animationlib;

/**
 * @Explain：执行动画回调，在MyFramelayout中实现；
 */

public interface SherlockAnimationCallBack {
    /**
     * 执行自定义动画方法；
     */
    void excuteanimation(float moveRadio);

    /**
     * 恢复初始状态；
     */
    void resetViewanimation();
}
