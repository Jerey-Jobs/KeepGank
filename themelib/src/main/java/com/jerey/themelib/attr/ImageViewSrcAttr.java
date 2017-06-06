package com.jerey.themelib.attr;

import android.view.View;
import android.widget.ImageView;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;


public class ImageViewSrcAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof ImageView) {
            ImageView iv = (ImageView) view;
            if (isDrawable()) {
                iv.setImageDrawable(SkinResourcesUtils.getDrawable(attrValueRefId));
            } else if (isColor()) {
                iv.setBackgroundColor(SkinResourcesUtils.getColor(attrValueRefId));
            }
        }
    }
}
