package com.jerey.themelib.attr;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;

/**
 * Created by xiamin on 6/8/17.
 */

public class NavigationIconAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) view;
            if (isDrawable()) {
                Log.e("NavigationIconAttr", "RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)");
                Log.e("NavigationIconAttr", toString());
                toolbar.setNavigationIcon(SkinResourcesUtils.getDrawable(attrValueRefId));
            }
        }
    }
}
