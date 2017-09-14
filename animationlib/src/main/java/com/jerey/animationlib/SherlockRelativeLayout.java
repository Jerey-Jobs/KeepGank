package com.jerey.animationlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Xiamin on 2017/6/28.
 */

public class SherlockRelativeLayout extends RelativeLayout {
    public SherlockRelativeLayout(Context context) {
        super(context);
    }

    public SherlockRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SherlockRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RelativeLayoutParams(getContext(), attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        RelativeLayoutParams myLayoutParams = (RelativeLayoutParams) params;
        if (myLayoutParams.isHaveMyProperty()) {
            SherlockFrame myFrameLayout = new SherlockFrame(getContext());
            myFrameLayout.addView(child);
            myFrameLayout.setmAlphaSupport(myLayoutParams.mAlphaSupport);
            myFrameLayout.setmScaleXSupport(myLayoutParams.mScaleXSupport);
            myFrameLayout.setmScaleYSupport(myLayoutParams.mScaleYSupport);
            myFrameLayout.setmBgColorStart(myLayoutParams.mBgColorStart);
            myFrameLayout.setmBgColorEnd(myLayoutParams.mBgColorEnd);
            myFrameLayout.setmTranslationValue(myLayoutParams.mTranslationValue);
            super.addView(myFrameLayout, index, params);
        } else {
            super.addView(child, index, params);
        }
    }


    /**
     * 不能将此LayoutParams抽象出来, 其继承的是自己内部类的Params
     */
    public class RelativeLayoutParams extends LayoutParams {
        //是否支持透明度；
        public boolean mAlphaSupport;
        //是否支持X Y轴缩放；
        public boolean mScaleXSupport;
        public boolean mScaleYSupport;

        //颜色变化的起始值；
        public int mBgColorStart;
        public int mBgColorEnd;
        //移动值；
        public int mTranslationValue;

        public RelativeLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.MyFrameLayout);
            mAlphaSupport = typedArray.getBoolean(R.styleable.MyFrameLayout_animation_alpha, false);
            mBgColorStart = typedArray.getColor(R.styleable.MyFrameLayout_bgColorStart, -1);
            mBgColorEnd = typedArray.getColor(R.styleable.MyFrameLayout_bgColorEnd, -1);
            mScaleXSupport = typedArray.getBoolean(R.styleable.MyFrameLayout_animation_scaleX, false);
            mScaleYSupport = typedArray.getBoolean(R.styleable.MyFrameLayout_animation_scaleY, false);
            mTranslationValue = typedArray.getInt(R.styleable.MyFrameLayout_animation_translation, -1);
            typedArray.recycle();
        }

        /**
         * 判断当前params是否包含自定义属性；
         *
         * @return
         */
        public boolean isHaveMyProperty() {
            if (mAlphaSupport || mScaleXSupport || mScaleYSupport || (mBgColorStart != -1 && mBgColorEnd != -1) || mTranslationValue != -1) {
                return true;
            }
            return false;
        }
    }
}
