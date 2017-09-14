package com.jerey.keepgank.modules.douban.bean;

import java.util.List;

/**
 * @author xiamin
 * @date 8/18/17.
 */
public class BannerBean {

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    List<SubjectsBean> subjects;

    public BannerBean(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

}
