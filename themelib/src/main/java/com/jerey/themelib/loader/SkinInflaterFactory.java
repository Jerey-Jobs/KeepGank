package com.jerey.themelib.loader;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jerey.themelib.SkinConfig;
import com.jerey.themelib.SkinItem;
import com.jerey.themelib.attr.base.AttrFactory;
import com.jerey.themelib.attr.base.DynamicAttr;
import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinL;
import com.jerey.themelib.utils.SkinListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.R.attr.textColor;

/**
 * 自定义的InflaterFactory，用来代替默认的LayoutInflaterFactory
 */
public class SkinInflaterFactory implements LayoutInflaterFactory {

    private static final String TAG = "SkinInflaterFactory";
    /**
     * 存储那些有皮肤更改需求的View及其对应的属性的集合
     */
    private Map<View, SkinItem> mSkinItemMap = new HashMap<>();
    private AppCompatActivity mAppCompatActivity;

    /**
     * 入口方法，在Activcity创建view的时候会走该方法获取view
     * @param parent
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);
        AppCompatDelegate delegate = mAppCompatActivity.getDelegate();
        View view = delegate.createView(parent, name, context, attrs);

        // 此处供改变字体使用,建立对出现的TextView的引用
        if (view instanceof TextView && SkinConfig.isCanChangeFont()) {
            TextViewRepository.add((TextView) view);
        }
        // 此处判断是否可以切换view, 可以则解析皮肤属性
        if (isSkinEnable || SkinConfig.isGlobalSkinApply()) {
            if (view == null) {
                view = ViewProducer.createViewFromTag(context, name, attrs);
            }
            if (view == null) {
                return null;
            }
            parseSkinAttr(context, attrs, view);
        }
        return view;
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        mAppCompatActivity = appCompatActivity;
    }

    /**
     * collect skin view
     */
    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
        List<SkinAttr> viewAttrs = new ArrayList<>();//存储View可更换皮肤属性的集合
        SkinL.i(TAG, "viewName:" + view.getClass().getSimpleName());
        for (int i = 0; i < attrs.getAttributeCount(); i++) {//遍历当前View的属性
            String attrName = attrs.getAttributeName(i);//属性名
            String attrValue = attrs.getAttributeValue(i);//属性值
            SkinL.i(TAG, "********AttributeName:" + attrName + "| attrValue: " + attrValue);
            //region  style
            if ("style".equals(attrName)) {//style theme
                String styleName = attrValue.substring(attrValue.indexOf("/") + 1);
                int styleID = context.getResources().getIdentifier(styleName, "style", context.getPackageName());
                int[] skinAttrs = new int[]{textColor, android.R.attr.background};
                TypedArray a = context.getTheme().obtainStyledAttributes(styleID, skinAttrs);
                int textColorId = a.getResourceId(0, -1);
                int backgroundId = a.getResourceId(1, -1);
                if (textColorId != -1) {
                    String entryName = context.getResources().getResourceEntryName(textColorId);//入口名，例如text_color_selector
                    String typeName = context.getResources().getResourceTypeName(textColorId);
                    SkinAttr skinAttr = AttrFactory.get("textColor", textColorId, entryName, typeName);
                    SkinL.w(TAG, "    textColor in style is supported:" + "\n" +
                            "    resource id:" + textColorId + "\n" +
                            "    attrName:" + attrName + "\n" +
                            "    attrValue:" + attrValue + "\n" +
                            "    entryName:" + entryName + "\n" +
                            "    typeName:" + typeName);
                    if (skinAttr != null) {
                        viewAttrs.add(skinAttr);
                    }
                }
                if (backgroundId != -1) {
                    String entryName = context.getResources().getResourceEntryName(backgroundId);//入口名，例如text_color_selector
                    String typeName = context.getResources().getResourceTypeName(backgroundId);
                    SkinAttr skinAttr = AttrFactory.get("background", backgroundId, entryName, typeName);
                    SkinL.w(TAG, "    background in style is supported:" + "\n" +
                            "    resource id:" + backgroundId + "\n" +
                            "    attrName:" + attrName + "\n" +
                            "    attrValue:" + attrValue + "\n" +
                            "    entryName:" + entryName + "\n" +
                            "    typeName:" + typeName);
                    if (skinAttr != null) {
                        viewAttrs.add(skinAttr);
                    }

                }
                a.recycle();
                continue;
            }
            //endregion
            if (AttrFactory.isSupportedAttr(attrName) && attrValue.startsWith("@")) {//也就是引用类型，形如@color/red
                try {
                    int id = Integer.parseInt(attrValue.substring(1));//资源的id
                    if (id == 0) continue;
                    String entryName = context.getResources().getResourceEntryName(id);//入口名，例如text_color_selector
                    String typeName = context.getResources().getResourceTypeName(id);//类型名，例如color、drawable
                    SkinAttr mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
                    SkinL.w(TAG, "    " + attrName + " is supported:" + "\n" +
                            "    resource id:" + id + "\n" +
                            "    attrName:" + attrName + "\n" +
                            "    attrValue:" + attrValue + "\n" +
                            "    entryName:" + entryName + "\n" +
                            "    typeName:" + typeName
                    );
                    if (mSkinAttr != null) {
                        viewAttrs.add(mSkinAttr);
                    }
                } catch (NumberFormatException e) {
                    SkinL.e(TAG, e.toString());
                }
            }
        }
        // 若皮肤属性不为空，则进入
        if (!SkinListUtils.isEmpty(viewAttrs)) {
            SkinItem skinItem = new SkinItem();
            skinItem.view = view;
            skinItem.attrs = viewAttrs;
            mSkinItemMap.put(skinItem.view, skinItem);
            if (SkinManager.getInstance().isExternalSkin()) {//如果当前皮肤来自于外部
                skinItem.apply();
            }
        }
    }

    /**
     * 在Activity收到主题更新的回调时调用
     * 遍历集合，应用皮肤
     */
    public void applySkin() {
        if (mSkinItemMap.isEmpty()) {
            return;
        }
        for (View view : mSkinItemMap.keySet()) {
            if (view == null) {
                continue;
            }
            mSkinItemMap.get(view).apply();
        }
    }

    /**
     * 清除有皮肤更改需求的View及其对应的属性的集合
     */
    public void clean() {
        if (mSkinItemMap.isEmpty()) {
            return;
        }
        for (View view : mSkinItemMap.keySet()) {
            if (view == null) {
                continue;
            }
            mSkinItemMap.get(view).clean();
        }
    }

    private void addSkinView(SkinItem item) {
        if (mSkinItemMap.get(item.view) != null) {
            mSkinItemMap.get(item.view).attrs.addAll(item.attrs);
        } else {
            mSkinItemMap.put(item.view, item);
        }
    }

    public void removeSkinView(View view) {
        mSkinItemMap.remove(view);
        if (SkinConfig.isCanChangeFont() && view instanceof TextView) {
            TextViewRepository.remove((TextView) view);
        }
    }

    /**
     * 动态添加那些有皮肤更改需求的View，及其对应的属性
     * 将其添加到mSkinItemMap中
     * @param context        context
     * @param view           added view
     * @param attrName       attribute name
     * @param attrValueResId resource id
     */
    public void dynamicAddSkinEnableView(Context context, View view, String attrName, int attrValueResId) {
        String entryName = context.getResources().getResourceEntryName(attrValueResId);
        String typeName = context.getResources().getResourceTypeName(attrValueResId);
        SkinAttr mSkinAttr = AttrFactory.get(attrName, attrValueResId, entryName, typeName);
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;
        List<SkinAttr> viewAttrs = new ArrayList<>();
        viewAttrs.add(mSkinAttr);
        skinItem.attrs = viewAttrs;
        skinItem.apply();
        addSkinView(skinItem);
    }

    /**
     * 动态添加那些有皮肤更改需求的View，及其对应的属性集合
     * @param context
     * @param view
     * @param attrs
     */
    public void dynamicAddSkinEnableView(Context context, View view, List<DynamicAttr> attrs) {
        List<SkinAttr> viewAttrs = new ArrayList<SkinAttr>();
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;

        for (DynamicAttr dAttr : attrs) {
            int id = dAttr.refResId;
            String entryName = context.getResources().getResourceEntryName(id);
            String typeName = context.getResources().getResourceTypeName(id);
            SkinAttr mSkinAttr = AttrFactory.get(dAttr.attrName, id, entryName, typeName);
            viewAttrs.add(mSkinAttr);
        }

        skinItem.attrs = viewAttrs;
        skinItem.apply();
        addSkinView(skinItem);
    }

    /**
     * 动态添加可以修改字体格式的TextView
     * @param textView
     */
    public void dynamicAddFontEnableView(TextView textView) {
        TextViewRepository.add(textView);
    }

}
