package com.jerey.keepgank.activity.PhotoChoose;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.bean.Data;
import com.jerey.keepgank.net.GankApi;
import com.jerey.loglib.LogTools;
import com.jerey.lruCache.DiskLruCacheManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiamin on 6/16/17.
 */

public class PhotoChooseActivity extends AppSwipeBackActivity {

    private static final String TAG = "PhotoChooseActivity";
    @Bind(R.id.m_viewpager)
    ViewPager mViewpager;

    ViewPagerAdapter mAdapter;

    //当前页数
    private int currentPager = 1;
    //是否刷新状态
    private boolean isLoadingNewData = false;
    //是否载入更多状态
    private boolean isLoadingMore = false;
    //是否已经载入去全部
    private boolean isALlLoad = false;
    private DiskLruCacheManager mDiskLruCacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_choose);
        ButterKnife.bind(this);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewpager.setPageTransformer(false, new CustomViewPagerTransformer(this));
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(5);
        mViewpager.setCurrentItem(1);
        mViewpager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);
        try {
            Log.i(TAG, "DiskLruCacheManager 创建");
            mDiskLruCacheManager = new DiskLruCacheManager(this);
            Data data = mDiskLruCacheManager.getAsSerializable(TAG);
            Log.i(TAG, "DiskLruCacheManager 读取");
            if (data != null) {
                Log.i(TAG, "获取到缓存数据");
                mAdapter.setData(data.getResults());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadData(1);
    }


    private void loadData(int pager) {
        GankApi.getInstance()
                .getWebService()
                .getBenefitsGoods(GankApi.LOAD_LIMIT, pager)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataObservable);
    }

    private Observer<Data> dataObservable = new Observer<Data>() {

        @Override
        public void onCompleted() {
            LogTools.i("数据onCompleted 停止刷新");
            isLoadingNewData = false;
            isLoadingMore = false;
        }

        @Override
        public void onError(Throwable e) {
            LogTools.i("onError 停止刷新");
            isLoadingNewData = false;
            isLoadingMore = false;
        }

        @Override
        public void onNext(Data data) {
            LogTools.i("onNext " + data.toString());
            if (data != null && data.getResults() != null) {
                /**
                 * 没有更多数据
                 */
                if (data.getResults().size() < GankApi.LOAD_LIMIT) {
                    isALlLoad = true;
                }

                mAdapter.addData(data.getResults());
                //   mDiskLruCacheManager.put(TAG, data);
            }
        }
    };

    public static class PhotoItemFragment extends Fragment {
        public static final String URL = "url";

        public static PhotoItemFragment newInstance(String url) {
            PhotoItemFragment itemFragment = new PhotoItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString(URL, url);
            itemFragment.setArguments(bundle);
            return itemFragment;
        }

        private PolygonView mPolygonView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_viewpager_item, null);
            mPolygonView = (PolygonView) view.findViewById(R.id.item_image);
            // 做一个属性动画
            ObjectAnimator animator = ObjectAnimator.ofFloat(mPolygonView, "rotation", 0f, 10f);
            animator.setDuration(10);
            animator.start();
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            String url = getArguments().getString(URL);
            Glide.with(this).
                    load(url)
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mPolygonView.setImageBitmap(resource);
                        }
                    });

//            int resId = getArguments().getInt("resId");
 //           mPolygonView.setImageResource(R.drawable.jay);// 设置图片
        }
    }

}
