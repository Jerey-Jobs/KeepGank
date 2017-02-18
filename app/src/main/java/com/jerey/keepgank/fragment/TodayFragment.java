package com.jerey.keepgank.fragment;

import android.animation.ValueAnimator;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jerey.keepgank.R;
import com.jerey.keepgank.adapter.DayFragmentAdapter;
import com.jerey.keepgank.bean.GankDay;
import com.jerey.keepgank.net.GankApi;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.Date;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.jerey.keepgank.R.id.toolbar;

/**
 * Created by Xiamin on 2017/2/15.
 */

public class TodayFragment extends BaseFragment {

    @Bind(toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.day_recycleview)
    RecyclerView mRecyclerView;
    @Bind(R.id.story_img)
    ImageView mImageView;

    private LinearLayoutManager mLinearLayoutManager;
    private DayFragmentAdapter mAdapter;
    int year;
    int month;
    int day;
    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_day;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        initUI();
        mAdapter = new DayFragmentAdapter(getActivity());
        Calendar c = null;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        year = calendar.get(java.util.Calendar.YEAR);
        month = calendar.get(java.util.Calendar.MONTH) + 1;
        day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        mToolbarLayout.setTitle(year + "年" + month + "月" + day + "日");
        GankApi.getInstance()
                .getWebService()
                .getGoodsByDay(year, month, day)
                .compose(this.<GankDay>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankDay>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(),"请求网络出错",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GankDay gankDay) {
                        Logger.d(gankDay.toString());
                        Logger.d(gankDay.results.福利.get(0).getUrl());
                        Glide.with(TodayFragment.this)
                                .load(gankDay.results.福利.get(0).getUrl())
                                .override(300,200)
                                .error(R.drawable.captain_android)
                                .placeholder(R.drawable.captain_android)
                                .into(mImageView);
                        mAdapter.setData(gankDay.results);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }

    private void initUI(){
        mToolbarLayout.setTitle("今日热点");
        //设置渐显动画，替换状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarColor = ((AppCompatActivity) getActivity()).getWindow().getStatusBarColor();
            mToolbarLayout.setContentScrimColor(statusBarColor);
            if (statusBarColor != ((AppCompatActivity) getActivity()).getWindow().getStatusBarColor()) {
                ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                        ((AppCompatActivity) getActivity()).getWindow().getStatusBarColor(), statusBarColor);
                statusBarColorAnim.addUpdateListener(new ValueAnimator
                        .AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor((int) animation.getAnimatedValue());
                        }
                    }
                });
                //设置转换颜色的动画时间
                statusBarColorAnim.setDuration(1000L);
                statusBarColorAnim.setInterpolator(
                        new AccelerateInterpolator());
                statusBarColorAnim.start();
            }
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLinearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }
}
