package com.jerey.keepgank.data;

import com.jerey.keepgank.bean.Bookmark;
import com.jerey.keepgank.bean.Type;

import java.util.List;

import rx.Observable;

/**
 * Created by Xiamin on 2017/2/12.
 */

public interface IData {

    Observable<List<Type>> getVisibilityTypeList();

    Observable<List<Type>> getAllTypeList();

    Observable<Boolean> updateTypeList(List<Type> list);

    void addTypeList();

    Observable<Bookmark> addBookmark(Bookmark bookmark);

    Observable<Bookmark> removeBookmark(String id);

    Observable<Bookmark> findBookmarkById(String id);

    Observable<List<Bookmark>> getBookmarkList();

    Observable<List<Bookmark>> getBookmarkList(String type);


}
