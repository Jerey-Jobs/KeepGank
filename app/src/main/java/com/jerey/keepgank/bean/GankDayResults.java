package com.jerey.keepgank.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunfusheng on 2017/1/17.
 */
public class GankDayResults implements Serializable{

    public List<Result> 福利;
    public List<Result> Android;
    public List<Result> iOS;
    public List<Result> App;
    public List<Result> 瞎推荐;
    public List<Result> 休息视频;

    @Override
    public String toString() {
        return "GankDayResults{" +
                "福利=" + 福利 +
                ", Android=" + Android +
                ", iOS=" + iOS +
                ", App=" + App +
                ", 瞎推荐=" + 瞎推荐 +
                ", 休息视频=" + 休息视频 +
                '}';
    }
}
