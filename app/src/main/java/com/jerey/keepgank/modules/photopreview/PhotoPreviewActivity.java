package com.jerey.keepgank.modules.photopreview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jerey.keepgank.R;
import com.jerey.keepgank.widget.imgpreview.SmoothImageView;
import com.jerey.loglib.LogTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Xiamin
 * @date 2017/9/17
 * <pre>
 *      ARouter.getInstance()
 *          .build("/activity/PhotoPreviewActivity")
 *          .withInt("index", position)
 *          .withParcelableArrayList("photo_beans", mPhotoBeanArrayList)
 *          .navigation();
 * </pre>
 */
@Route(path = "/activity/PhotoPreviewActivity")
public class PhotoPreviewActivity extends FragmentActivity {
    public static final String KEY_PHOTO_BEANS = "photo_beans";
    public static final String KEY_INDEX = "index";

    @BindView(R.id.fragment_pager)
    ViewPager mFragmentPager;
    @BindView(R.id.indicator_tv)
    TextView mIndicatorTv;

    private ArrayList<PhotoBean> photoBeans;
    private int index;
    private ViewPagerAdapter adapter;
    private List<PhotoPreviewFragment> mPhotoPreviewFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.bind(this);
        photoBeans = getIntent().getParcelableArrayListExtra(KEY_PHOTO_BEANS);
        index = getIntent().getIntExtra(KEY_INDEX, 0);
        LogTools.d("urls" + photoBeans + " index:" + index);
        initView();
    }

    private void initView() {
        if (photoBeans != null) {
            for (PhotoBean s : photoBeans) {
                mPhotoPreviewFragments.add(PhotoPreviewFragment.newInstance(s.url, s.rect));
            }
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            mFragmentPager.setOffscreenPageLimit(4);
            mFragmentPager.setAdapter(adapter);
            mFragmentPager.setCurrentItem(index);
            mFragmentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mIndicatorTv.setVisibility(View.VISIBLE);
                    mIndicatorTv.setText((position + 1) + "/" + photoBeans.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            /**
             * 设置视图监听器，
             */
            mFragmentPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                    .OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mFragmentPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    PhotoPreviewFragment fragment = (PhotoPreviewFragment) adapter.getItem(index);
                    fragment.transformIn();
                }
            });
        }
    }

    public void transformOut() {
        int currentItem = mFragmentPager.getCurrentItem();
        LogTools.d("currentItem: " + currentItem);
        if (currentItem < photoBeans.size()) {
            PhotoPreviewFragment fragment = (PhotoPreviewFragment) adapter.getItem(currentItem);
            if (mIndicatorTv != null) {
                mIndicatorTv.setVisibility(View.GONE);
            }
            fragment.changeBg(Color.TRANSPARENT);
            fragment.transformOut(new SmoothImageView.onTransformListener() {
                @Override
                public void onTransformCompleted(SmoothImageView.Status status) {
                    exit();
                }
            });
        } else {
            exit();
        }
    }

    /**
     * 关闭页面
     */
    public void exit() {
        finish();
        overridePendingTransition(0, 0);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mPhotoPreviewFragments.get(position);
        }

        @Override
        public int getCount() {
            return mPhotoPreviewFragments.size();
        }
    }
}
