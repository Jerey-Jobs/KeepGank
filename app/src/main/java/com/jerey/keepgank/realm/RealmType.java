package com.jerey.keepgank.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Hui on 2016/2/6.
 */
public class RealmType extends RealmObject{
    @PrimaryKey
    private String title;

    private int sort;
    private boolean visibility;

    public RealmType() {
        title = null;
        sort = -1;
        visibility = false;
    }
    public RealmType(String title, int sort, boolean visibility) {
        this.title = title;
        this.sort = sort;
        this.visibility = visibility;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int mSort) {
        this.sort = mSort;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
