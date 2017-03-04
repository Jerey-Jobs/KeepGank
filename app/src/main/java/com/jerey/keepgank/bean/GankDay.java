package com.jerey.keepgank.bean;

import java.io.Serializable;
import java.util.List;

public class GankDay extends BaseEntity implements Serializable{

    public List<String> category;
    public GankDayResults results;

    @Override
    public String toString() {
        return "GankDay{" +
                "category=" + category +
                ", results=" + results +
                '}';
    }
}