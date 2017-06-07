package com.jerey.themelib.attr;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;

/**
 * Created by Xiamin on 2017/6/7.
 */

public class CoordinatorLayoutAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof CoordinatorLayout) {
            CoordinatorLayout c = (CoordinatorLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinResourcesUtils.getColor(attrValueRefId);
                c.setBackgroundColor(color);
            } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
                Drawable drawable = SkinResourcesUtils.getDrawable(attrValueRefId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    c.setBackground(drawable);
                }
            }
        }
    }
}
