package com.jerey.keepgank.modules.photopreview;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Xiamin
 * @date 2017/9/17
 */
public class PhotoBean implements Parcelable {
    String url;
    Rect rect;

    public PhotoBean(String url) {
        this.url = url;
    }

    public PhotoBean(String url, Rect rect) {
        this.url = url;
        this.rect = rect;
    }

    protected PhotoBean(Parcel in) {
        url = in.readString();
        rect = in.readParcelable(Rect.class.getClassLoader());
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeParcelable(rect, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel in) {
            return new PhotoBean(in);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };
}
