package com.jerey.themelib;

import android.view.View;
import android.widget.TextView;

import com.jerey.themelib.attr.base.DynamicAttr;

import java.util.List;


/**
 * 动态添加View接口
 */
public interface IDynamicNewView {
    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);

    void dynamicAddView(View view, String attrName, int attrValueResId);

    /**
     * add the textview for font switch
     *
     * @param textView textview
     */
    void dynamicAddFontView(TextView textView);
}
