package com.jerey.animationlib;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @Explain：自定义FrameLayout包裹子View,执行动画；
 */

public class SherlockFrame extends FrameLayout implements SherlockAnimationCallBack {
    //从哪个方向开始动画；
    private static final int TRANSLATION_LEFT = 0x01;
    private static final int TRANSLATION_TOP = 0x02;
    private static final int TRANSLATION_RIGHT = 0x04;
    private static final int TRANSLATION_BOTTOM = 0x08;

    //是否支持透明度；
    private boolean mAlphaSupport;
    //颜色变化的起始值；
    private int mBgColorStart;
    private int mBgColorEnd;

    //是否支持X Y轴缩放；
    private boolean mScaleXSupport;
    private boolean mScaleYSupport;
    //移动值；
    private int mTranslationValue;
    //当前View宽高；
    private int mHeight, mWidth;
    /**
     * 颜色估值器；
     */
    private static ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    public SherlockFrame(@NonNull Context context) {
        super(context);
    }

    public SherlockFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SherlockFrame(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClipChildren(false);
    }

    public boolean ismAlphaSupport() {
        return mAlphaSupport;
    }

    public void setmAlphaSupport(boolean mAlphaSupport) {
        this.mAlphaSupport = mAlphaSupport;
    }

    public int getmBgColorStart() {
        return mBgColorStart;
    }

    public void setmBgColorStart(int mBgColorStart) {
        this.mBgColorStart = mBgColorStart;
    }

    public int getmBgColorEnd() {
        return mBgColorEnd;
    }

    public void setmBgColorEnd(int mBgColorEnd) {
        this.mBgColorEnd = mBgColorEnd;
    }

    public boolean ismScaleXSupport() {
        return mScaleXSupport;
    }

    public void setmScaleXSupport(boolean mScaleXSupport) {
        this.mScaleXSupport = mScaleXSupport;
    }

    public boolean ismScaleYSupport() {
        return mScaleYSupport;
    }

    public void setmScaleYSupport(boolean mScaleYSupport) {
        this.mScaleYSupport = mScaleYSupport;
    }

    public int getmTranslationValue() {
        return mTranslationValue;
    }

    public void setmTranslationValue(int mTranslationValue) {
        this.mTranslationValue = mTranslationValue;
    }

    //TODO 获取当前View宽高；
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void excuteanimation(float moveRadio) {
        //设置动画；
        if (mAlphaSupport) {
            setAlpha(moveRadio);
        }
        if (mScaleXSupport) {
            setScaleX(moveRadio);
        }
        if (mScaleYSupport) {
            setScaleY(moveRadio);
        }
        //从左边移动到原位置；
        if (isContainDirection(TRANSLATION_LEFT)) {
            setTranslationX(-mWidth * (1 - moveRadio));
        }

        if (isContainDirection(TRANSLATION_TOP)) {
            setTranslationY(-mHeight * (1 - moveRadio));
        }
        if (isContainDirection(TRANSLATION_RIGHT)) {
            setTranslationX(mWidth * (1 - moveRadio));
        }
        if (isContainDirection(TRANSLATION_BOTTOM)) {
            setTranslationY(mHeight * (1 - moveRadio));
        }
        if (mBgColorStart != -1 && mBgColorEnd != -1) {
            setBackgroundColor((int) mArgbEvaluator.evaluate(moveRadio, mBgColorStart, mBgColorEnd));
        }
    }

    //
    @Override
    public void resetViewanimation() {
        if (mAlphaSupport) {
            setAlpha(0);

        }
        if (mScaleXSupport) {
            setScaleX(0);
        }
        if (mScaleYSupport) {
            setScaleY(0);
        }//从左边移动到原位置；
        if (isContainDirection(TRANSLATION_LEFT)) {
            setTranslationX(-mWidth);
        }
        if (isContainDirection(TRANSLATION_TOP)) {
            setTranslationY(-mHeight);
        }
        if (isContainDirection(TRANSLATION_RIGHT)) {
            setTranslationX(mWidth);
        }
        if (isContainDirection(TRANSLATION_BOTTOM)) {
            setTranslationY(mHeight);
        }
    }

    private boolean isContainDirection(int direction) {
        if (mTranslationValue == -1)
            return false;
        return (mTranslationValue & direction) == direction;
    }
}
