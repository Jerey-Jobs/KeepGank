package com.jerey.themelib.attr;

import android.view.View;
import android.widget.TextView;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;


public class TextColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                tv.setTextColor(SkinResourcesUtils.getColorStateList(attrValueRefId));
            }
        }
    }
}
