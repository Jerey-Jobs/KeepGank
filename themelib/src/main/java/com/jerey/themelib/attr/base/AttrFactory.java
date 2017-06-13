package com.jerey.themelib.attr.base;

import com.jerey.themelib.attr.BackgroundAttr;
import com.jerey.themelib.attr.ContentScrimColorAttr;
import com.jerey.themelib.attr.ImageViewSrcAttr;
import com.jerey.themelib.attr.NavigationIconAttr;
import com.jerey.themelib.attr.TabLayoutIndicatorAttr;
import com.jerey.themelib.attr.TextColorAttr;

import java.util.HashMap;


public class AttrFactory {

    private static HashMap<String, SkinAttr> sSupportAttr = new HashMap<>();

    static {
        sSupportAttr.put("background", new BackgroundAttr());
        sSupportAttr.put("textColor", new TextColorAttr());
        sSupportAttr.put("src", new ImageViewSrcAttr());
        sSupportAttr.put("tabLayoutIndicator", new TabLayoutIndicatorAttr());
        sSupportAttr.put("navigationIcon", new NavigationIconAttr());
        sSupportAttr.put("ContentScrimColor", new ContentScrimColorAttr());
    }


    public static SkinAttr get(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        SkinAttr mSkinAttr = sSupportAttr.get(attrName).clone();
        if (mSkinAttr == null) return null;
        mSkinAttr.attrName = attrName;
        mSkinAttr.attrValueRefId = attrValueRefId;
        mSkinAttr.attrValueRefName = attrValueRefName;
        mSkinAttr.attrValueTypeName = typeName;
        return mSkinAttr;
    }

    /**
     * check current attribute if can be support
     * @param attrName attribute name
     * @return true : supported <br>
     * false: not supported
     */
    public static boolean isSupportedAttr(String attrName) {
        return sSupportAttr.containsKey(attrName);
    }

    /**
     * add support's attribute
     * @param attrName attribute name
     * @param skinAttr skin attribute
     */
    public static void addSupportAttr(String attrName, SkinAttr skinAttr) {
        sSupportAttr.put(attrName, skinAttr);
    }
}
