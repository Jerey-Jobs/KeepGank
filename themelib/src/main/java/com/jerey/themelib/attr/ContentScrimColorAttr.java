package com.jerey.themelib.attr;

import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.View;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;

/**
 * Created by xiamin on 6/8/17.
 */

public class ContentScrimColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof CollapsingToolbarLayout) {
            CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) view;
            Log.e("ContentScrimColorAttr", toString());
            if (isColor()) {
                toolbar.setContentScrimColor(SkinResourcesUtils.getColor(attrValueRefId));
            }
        }
    }
}
