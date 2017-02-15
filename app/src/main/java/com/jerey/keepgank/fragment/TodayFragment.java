package com.jerey.keepgank.fragment;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;

import com.jerey.keepgank.R;

import butterknife.Bind;

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

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_day;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
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
    }
}
