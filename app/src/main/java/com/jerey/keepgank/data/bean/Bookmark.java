package com.jerey.keepgank.data.bean;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class Bookmark {

    @PrimaryKey
    private String objectId;

    private long collectionAt;
    private String desc;
    private String type;
    private String url;
    private String who;

    public Bookmark(){

    }
    public Bookmark(String objectId, long collectionAt, String desc, String type, String url,String who) {
        this.objectId = objectId;
        this.collectionAt = collectionAt;
        this.desc = desc;
        this.type = type;
        this.url = url;
        this.who = who;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public long getCollectionAt() {
        return collectionAt;
    }

    public void setCollectionAt(long collectionAt) {
        this.collectionAt = collectionAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
