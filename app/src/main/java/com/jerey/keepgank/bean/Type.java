package com.jerey.keepgank.bean;

/**
 * 供HomeFragment使用，记录每个分类的信息
 * Created by Xiamin on 2017/2/12.
 */

public class Type {
    private String mTitle;
    private int mSort;
    private boolean mVisibility;
    public Type() {
        mTitle = null;
        mSort = -1;
        mVisibility = false;
    }
    public Type(String title, int sort, boolean visibility) {
        mTitle = title;
        mSort = sort;
        mVisibility = visibility;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getSort() {
        return mSort;
    }

    public void setSort(int mSort) {
        this.mSort = mSort;
    }

    public boolean isVisibility() {
        return mVisibility;
    }

    public void setVisibility(boolean mVisibility) {
        this.mVisibility = mVisibility;
    }
}
