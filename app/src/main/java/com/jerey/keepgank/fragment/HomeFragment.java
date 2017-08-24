package com.jerey.keepgank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.jerey.keepgank.R;
import com.jerey.keepgank.adapter.HomeFragmentPagerAdapter;
import com.jerey.keepgank.net.Config;
import com.jerey.loglib.LogTools;

import butterknife.BindView;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    private HomeFragmentPagerAdapter mFragmentAdapter;

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        LogTools.i("afterCreate");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        dynamicAddView(mToolbar, "background", R.color.app_main_color);
        /**
         * 注: 在该Fragment设置mToolbar的onOptionsItemSelected是无效的
         */
        mFragmentAdapter = new HomeFragmentPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mFragmentAdapter);

        //根据类型添加fragment
        for (String type : Config.TYPES) {
            mFragmentAdapter.addFragment(ListFragment.getListFragment(type), type);
        }
        mFragmentAdapter.notifyDataSetChanged();
        //所有子fragment均不销毁
        mViewPager.setOffscreenPageLimit(mFragmentAdapter.getCount());
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogTools.i("onAttach");
    }
}
