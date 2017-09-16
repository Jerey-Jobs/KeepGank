package com.jerey.animationlib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;


/**
 * @Explain：自定义ScrollView，获取滚动监听，根据不同情况进行动画；
 */

public class SherlockScrollView extends ScrollView {
    private final static String TAG = "MyScrollView";
    private SherlockLinearLayout mLinearLayout;

    public SherlockScrollView(Context context) {
        this(context, null);
    }

    public SherlockScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    //获取内部LinearLayout；
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLinearLayout = (SherlockLinearLayout) getChildAt(0);
    }

    //将第一张图片设置成全屏；
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLinearLayout.getChildAt(0).getLayoutParams().height = getHeight();
        mLinearLayout.getChildAt(0).getLayoutParams().width = getWidth();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        parseViewGroup(mLinearLayout, l, t, oldl, oldt, true, 0);
    }

    /**
     * @param linearLayout
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     * @param isRootLinearLayout 是否是顶层布局
     * @param getTop             距离顶部高度
     */
    private void parseViewGroup(ViewGroup linearLayout,
                                int l, int t, int oldl, int oldt,
                                boolean isRootLinearLayout, int getTop) {
        int scrollViewHeight = getHeight();
        Log.w(TAG, "linearLayout.getChildCount()" + linearLayout.getChildCount());
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            //如果子控件不是MyFrameLayout则循环下一个子控件；
            View child = linearLayout.getChildAt(i);

            // 若不是动画控件,则进入判断是否是ViewGroup,是的话递归其子view.不是的话则判断下一个
            if (!(child instanceof SherlockAnimationCallBack)) {
                if (child instanceof ViewGroup) {
                    Log.d(TAG, "parseViewGroup: 该View不是FrameLayout,是ViewGroup: " + child
                            .getClass().getName());
                    parseViewGroup((ViewGroup) child, l, t, oldl, oldt, false,
                                   child.getTop() + getTop);
                }
                continue;
            }
            //以下为执行动画逻辑；
            SherlockAnimationCallBack myCallBack = (SherlockAnimationCallBack) child;
            //获取子View高度；
            int childHeight = child.getHeight();
            //子控件到父控件的距离；
            int childTop = child.getTop();
            if (!isRootLinearLayout) {
                childTop += getTop;
            }
            //滚动过程中，子View距离父控件顶部距离；
            int childAbsluteTop = childTop - t;
            //进入了屏幕
            if (childAbsluteTop <= scrollViewHeight) {
                //当前子控件显示出来的高度；
                int childShowHeight = scrollViewHeight - childAbsluteTop - 100;
                float moveRadio = childShowHeight / (float) childHeight;//这里一定要转化成float类型；
                //执行动画；
                myCallBack.excuteanimation(getMiddleValue(moveRadio, 0, 1));
            } else {
                //没在屏幕内,恢复数据；
                myCallBack.resetViewanimation();
            }
        }
    }


    /**
     * 求中间大小的值；
     * @param radio
     * @param minValue
     * @param maxValue
     * @return
     */
    private float getMiddleValue(float radio, float minValue, float maxValue) {
        return Math.max(Math.min(maxValue, radio), minValue);
    }
}
