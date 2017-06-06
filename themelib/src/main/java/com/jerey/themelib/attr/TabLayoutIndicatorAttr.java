package com.jerey.themelib.attr;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;

public class TabLayoutIndicatorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TabLayout) {
            TabLayout tl = (TabLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinResourcesUtils.getColor(attrValueRefId);
                tl.setSelectedTabIndicatorColor(color);
            }
        }
    }
}