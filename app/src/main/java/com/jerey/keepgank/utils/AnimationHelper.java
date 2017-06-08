package com.jerey.keepgank.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jerey.themelib.loader.SkinManager;

/**
 * Created by Xiamin on 2017/4/18.
 */

public class AnimationHelper {

    public static final int MINI_RADIUS = 0;
    public static final int DEFAULT_DURIATION = 500;

    /**
     * 屏蔽Android提示错误, 5.0以下不做动画处理
     *
     * @param view
     * @param startRadius
     * @param durationMills
     */
    @SuppressLint("NewApi")
    public static void show(View view, float startRadius, long durationMills) {
        // Android L 以下不做处理,直接显示
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            view.setVisibility(View.VISIBLE);
            return;
        }

        int xCenter = (view.getLeft() + view.getRight()) / 2;
        int yCenter = (view.getTop() + view.getBottom()) / 2;
        int w = view.getWidth();
        int h = view.getHeight();
        //计算最大半径, 边界效应+1
        int endRadius = (int) (Math.sqrt(w * w + h * h) + 1);
        Animator animation = ViewAnimationUtils.createCircularReveal(view,
                xCenter, yCenter, startRadius, endRadius);
        view.setVisibility(View.VISIBLE);
        animation.setDuration(durationMills);
        animation.start();
    }

    @SuppressLint("NewApi")
    public static void hide(final View view, float endRadius, long durationMills, final int visible) {
        // Android L 以下不做处理,直接显示
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            view.setVisibility(View.INVISIBLE);
            return;
        }

        int xCenter = (view.getLeft() + view.getRight()) / 2;
        int yCenter = (view.getTop() + view.getBottom()) / 2;
        int w = view.getWidth();
        int h = view.getHeight();
        //计算最大半径, 边界效应+1
        int startRadius = (int) (Math.sqrt(w * w + h * h) + 1);
        Animator animation = ViewAnimationUtils.createCircularReveal(view,
                xCenter, yCenter, startRadius, endRadius);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(visible);
            }
        });
        animation.setDuration(durationMills);
        animation.start();
    }

    public static void show(View myView) {
        show(myView, MINI_RADIUS, DEFAULT_DURIATION);
    }

    /**
     * 默认View隐藏状态为 INVISIBLE
     *
     * @param myView
     */
    public static void hide(View myView) {
        hide(myView, MINI_RADIUS, DEFAULT_DURIATION, View.INVISIBLE);
    }

    /*
     * @param myView 要隐藏的view
     * @param endVisible  动画执行结束是view的状态, 是View.INVISIBLE 还是GONE
     */
    public static void hide(View myView, int endVisible) {
        hide(myView, MINI_RADIUS, DEFAULT_DURIATION, endVisible);
    }

    @SuppressLint("NewApi")
    public static void startActivityForResult(
            final Activity thisActivity, final Intent intent, final Integer requestCode,
            final Bundle bundle, final View view,
            int colorOrImageRes, final long durationMills) {
        // SDK 低于LOLLIPOP不做处理,直接跳转
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == null) {
                thisActivity.startActivity(intent);
            } else if (bundle == null) {
                thisActivity.startActivityForResult(intent, requestCode);
            } else {
                thisActivity.startActivityForResult(intent, requestCode, bundle);
            }
            return;
        }
        int[] location = new int[2];
        view.getLocationInWindow(location);
        final int xCenter = location[0] + view.getWidth() / 2;
        final int yCenter = location[1] + view.getHeight() / 2;
        final ImageView imageView = new ImageView(thisActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(SkinManager.getInstance().getColor(colorOrImageRes));

        final ViewGroup decorView = (ViewGroup) thisActivity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(imageView, w, h);

        // 计算中心点至view边界的最大距离
        int maxW = Math.max(xCenter, w - xCenter);
        int maxH = Math.max(yCenter, h - yCenter);
        final int finalRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;

        Animator anim = ViewAnimationUtils.createCircularReveal(imageView, xCenter, yCenter, 0, finalRadius);
        int maxRadius = (int) Math.sqrt(w * w + h * h) + 1;
        long finalDuration = durationMills;
        /**
         * 计算时间
         */
        if (finalDuration == DEFAULT_DURIATION) {
            // 算出实际边距与最大边距的比率
            double rate = 1d * finalRadius / maxRadius;
            // 水波扩散的距离与扩散时间成正比
            finalDuration = (long) (DEFAULT_DURIATION * rate);
        }
        anim.setDuration(finalDuration);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (requestCode == null) {
                    thisActivity.startActivity(intent);
                } else if (bundle == null) {
                    thisActivity.startActivityForResult(intent, requestCode);
                } else {
                    thisActivity.startActivityForResult(intent, requestCode, bundle);
                }

                // 默认渐隐过渡动画.
                thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                // 默认显示返回至当前Activity的动画.
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(imageView, xCenter, yCenter, finalRadius, 0);
                        anim.setDuration(durationMills);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                try {
                                    decorView.removeView(imageView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        anim.start();
                    }
                }, 1000);
            }
        });
        anim.start();
    }

    public static void startActivityForResult(
            Activity thisActivity, Intent intent, Integer requestCode, View triggerView, int colorOrImageRes) {
        startActivityForResult(thisActivity, intent, requestCode, null, triggerView, colorOrImageRes, DEFAULT_DURIATION);
    }

    public static void startActivity(
            Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes, long durationMills) {
        startActivityForResult(thisActivity, intent, null, null, triggerView, colorOrImageRes, durationMills);
    }

    public static void startActivity(
            Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes) {
        startActivity(thisActivity, intent, triggerView, colorOrImageRes, DEFAULT_DURIATION);
    }


    public static void startActivity(Activity thisActivity, Class<?> targetClass, View triggerView, int colorOrImageRes) {
        startActivity(thisActivity, new Intent(thisActivity, targetClass), triggerView, colorOrImageRes, DEFAULT_DURIATION);
    }

}