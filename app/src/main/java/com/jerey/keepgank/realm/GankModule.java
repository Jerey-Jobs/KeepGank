package com.jerey.keepgank.realm;


import io.realm.annotations.RealmModule;


@RealmModule(classes = { RealmType.class, RealmBookmark.class, RealmImage.class})
public class GankModule {
}
