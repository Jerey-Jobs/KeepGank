package com.jerey.themelib.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;


public class BackgroundAttr extends SkinAttr {

    @Override
    public void apply(View view) {
        if (isColor()) {
            int color = SkinResourcesUtils.getColor(attrValueRefId);
            view.setBackgroundColor(color);
        } else if (isDrawable()) {
            Drawable bg = SkinResourcesUtils.getDrawable(attrValueRefId);
            view.setBackgroundDrawable(bg);
        }
    }
}
