package com.jerey.animationlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @Explain：自定义LinearLayout，主要功能是判断每一个子View是否包含自定义动画属性，
 * @1.如果包括解析自定义属性，赋值给自定义LinearLayout.LayoutParams，创建MyFrameLayout赋值包裹子View；
 * @2.不包含自定义属性，不做处理。
 */
public class SherlockLinearLayout extends LinearLayout {
    public SherlockLinearLayout(Context context) {
        super(context);
    }

    public SherlockLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SherlockLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClipChildren(false);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //返回自己定义的Params.
        return new LinearLayoutParams(getContext(), attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        //看源码可知addView的第三个参数（params）就是我们在generateLayoutParams（）方法中设置的自定义Params;
        LinearLayoutParams myLayoutParams = (LinearLayoutParams) params;
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

    //自定义LayoutParams,存储从子控件获取的自定义属性。
    public class LinearLayoutParams extends LayoutParams {
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

        public LinearLayoutParams(Context c, AttributeSet attrs) {
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
