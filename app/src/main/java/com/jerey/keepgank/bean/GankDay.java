package com.jerey.keepgank.bean;

import java.util.List;

public class GankDay extends BaseEntity {

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