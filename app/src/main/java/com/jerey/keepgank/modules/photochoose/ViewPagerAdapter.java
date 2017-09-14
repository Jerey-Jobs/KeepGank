package com.jerey.keepgank.modules.photochoose;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jerey.keepgank.data.bean.Result;

import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Result> mDatas;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mDatas = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoChooseActivity.PhotoItemFragment.newInstance(mDatas.get(position).getUrl());
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    public void addData(List<Result> datas) {
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setData(List<Result> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public Result getItemData(int positon) {
        return mDatas.get(positon);
    }
}