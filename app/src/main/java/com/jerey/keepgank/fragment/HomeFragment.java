package com.jerey.keepgank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jerey.keepgank.R;
import com.jerey.keepgank.adapter.HomeFragmentPagerAdapter;
import com.jerey.keepgank.bean.Data;
import com.jerey.keepgank.bean.Result;
import com.jerey.keepgank.binder.GankResultBinder;
import com.jerey.keepgank.net.Config;
import com.jerey.keepgank.net.GankApi;
import com.jerey.loglib.LogTools;
import com.jerey.searchview.HistoryBean;
import com.jerey.searchview.SearchView;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class HomeFragment extends BaseFragment implements SearchView.OnSearchActionListener {
    private static final String DB_NAME = "gank_list";
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.searchView)
    SearchView mSearchView;

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

        mSearchView.setType(DB_NAME)
                   .registerData(Result.class, new GankResultBinder())
                   .setOnSearchActionListener(this)
                   .setHistoryItemClickListener(new SearchView.OnHistoryClickListener() {
                       @Override
                       public void onClick(HistoryBean data) {
                           onSearchAction(data.getContent());
                       }
                   });
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            LogTools.d("search selected");
            mSearchView.autoOpenOrClose();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchAction(String searchText) {
        LogTools.d("onSearchAction : " + searchText);
        GankApi.getInstance()
               .getWebService()
               .getSearchResult(searchText)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<Data>() {
                   @Override
                   public void onCompleted() {

                   }

                   @Override
                   public void onError(Throwable e) {
                       e.printStackTrace();
                   }

                   @Override
                   public void onNext(Data data) {
                       mSearchView.setListObjects(data.getResults());
                   }
               });
    }
}
