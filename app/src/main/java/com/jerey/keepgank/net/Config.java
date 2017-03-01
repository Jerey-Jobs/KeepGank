package com.jerey.keepgank.net;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Hui on 2016/2/13.
 */
public class Config {

    @StringDef({TYPE_ANDROID, TYPE_IOS,TYPE_FRONT_END,TYPE_RECOMMEND,TYPE_VIDEO,TYPE_GIRL,TYPE_RESOURCES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {}

    public static final String TYPE_ANDROID = "Android";
    public static final String TYPE_IOS = "iOS";
    public static final String TYPE_FRONT_END = "前端";
    public static final String TYPE_RECOMMEND  = "瞎推荐";
    public static final String TYPE_VIDEO = "休息视频";
    public static final String TYPE_GIRL = "福利";
    public static final String TYPE_RESOURCES = "拓展资源";

    public static final String[] TYPES = {TYPE_ANDROID,TYPE_IOS,TYPE_FRONT_END,TYPE_RECOMMEND,TYPE_VIDEO,TYPE_RESOURCES};
}
