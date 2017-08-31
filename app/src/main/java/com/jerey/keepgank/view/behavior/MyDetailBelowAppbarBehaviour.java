package com.jerey.keepgank.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author xiamin
 * @date 8/30/17.
 */
public class MyDetailBelowAppbarBehaviour extends CoordinatorLayout.Behavior<View> {

    public MyDetailBelowAppbarBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
