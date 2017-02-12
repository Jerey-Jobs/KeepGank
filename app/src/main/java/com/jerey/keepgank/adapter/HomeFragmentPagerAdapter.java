package com.jerey.keepgank.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = null;
    private  List<String> mFragmentTitles = null;
    private FragmentManager mFragmentManager;

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragmentTitles = new ArrayList<>();
        mFragmentManager = fm;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
