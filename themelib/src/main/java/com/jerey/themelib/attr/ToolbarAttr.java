package com.jerey.themelib.attr;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;

/**
 * Created by xiamin on 6/8/17.
 */

public class ToolbarAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                Log.e("xiamin", "RES_TYPE_NAME_COLOR.equals(attrValueTypeName)");
                toolbar.setBackgroundColor(attrValueRefId);
            }
            if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
                Log.e("xiamin", "RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)");
                toolbar.setNavigationIcon(SkinResourcesUtils.getDrawable(attrValueRefId));
            }
        }
    }
}
