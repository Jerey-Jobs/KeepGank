package com.jerey.themelib.loader;

import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

import com.jerey.themelib.utils.TypefaceUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 字体改变工厂
 */
public class TextViewRepository {
    private static final String TAG = "TextViewRepository";
    /**
     * 虽使用了弱引用来解决TextView得不到释放的问题,但是没有效果,内存中的TextView依旧在不断增多.
     * 即使不加入列表里面,TextView数量依旧是在使用过程中中变多
     */
    private static ArrayList<WeakReference<TextView>> mTextViewsWeakList = new ArrayList<>();

    public static void add(TextView textView) {
        mTextViewsWeakList.add(new WeakReference<TextView>(textView));
        textView.setTypeface(TypefaceUtils.CURRENT_TYPEFACE);
        Log.d(TAG, "TextViewRepository mTextViews count: " + mTextViewsWeakList.size());
    }

    public static void clear() {
        mTextViewsWeakList.clear();
    }

    public static void remove(TextView textView) {
        mTextViewsWeakList.remove(new WeakReference<TextView>(textView));
    }

    public static void applyFont(Typeface tf) {
        for (WeakReference<TextView> textViewWeak : mTextViewsWeakList) {
            TextView textView = textViewWeak.get();
            if (textView != null) {
                textView.setTypeface(tf);
            }
        }
    }
}
