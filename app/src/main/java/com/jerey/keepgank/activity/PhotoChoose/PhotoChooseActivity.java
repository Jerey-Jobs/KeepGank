package com.jerey.keepgank.activity.PhotoChoose;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hwangjr.rxbus.RxBus;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.bean.Data;
import com.jerey.keepgank.bean.Result;
import com.jerey.keepgank.net.GankApi;
import com.jerey.keepgank.utils.BlurImageUtils;
import com.jerey.loglib.LogTools;
import com.jerey.lruCache.DiskLruCacheManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiamin on 6/16/17.
 */

public class PhotoChooseActivity extends AppSwipeBackActivity {

    private static final String TAG = "PhotoChooseActivity";
    @Bind(R.id.my_viewpager)
    ViewPager mViewpager;

    ViewPagerAdapter mAdapter;
    @Bind(R.id.activity_bg)
    ImageView mActivityBg;
    @Bind(R.id.m_choose_btn)
    Button mMChooseBtn;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    //当前选择pic
    int mPicCount;
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
        RxBus.get().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_photo_choose);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("头像选择");
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewpager.setPageTransformer(false, new CustomViewPagerTransformer(this));
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(2);
        mViewpager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);
        mViewpager.addOnPageChangeListener(mOnPageChangeListener);
        //  mIndicatorView.setUpWithViewPager(mViewpager);
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
        loadData(currentPager);
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

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            LogTools.i("onPageSelected position: " + position);
            Result result = mAdapter.getItemData(position);
            loadBgImage(result.getUrl());
            if (position > mAdapter.getCount() - 4 && !isLoadingMore) {
                loadData(++currentPager);
                isLoadingMore = true;
            }
            mPicCount = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
                if (currentPager == 1) {
                    mViewpager.setCurrentItem(1);
                }
                //   mDiskLruCacheManager.put(TAG, data);
                //  Result result = mAdapter.getItemData(0);
                //    loadBgImage(result.getUrl());
            }
        }
    };

    private void loadBgImage(String url) {
        Glide.with(PhotoChooseActivity.this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(mActivityBg, "alpha", 1f, 0.5f);
                        animator.setDuration(300);
                        animator.start();
                        Bitmap overlay = BlurImageUtils.blur(resource, 12, 12);
                        mActivityBg.setImageBitmap(overlay);
                        animator = ObjectAnimator.ofFloat(mActivityBg, "alpha", 0.5f, 1f);
                        animator.setDuration(300);
                        animator.start();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        LogTools.d("onDestroy");
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @OnClick(R.id.m_choose_btn)
    public void onViewClicked() {
        if (mAdapter.getCount() < mPicCount) {
            return;
        }
        String url = mAdapter.getItemData(mPicCount).getUrl();
        /**
         * 选中之后，发送头像URL，结束界面
         */
        RxBus.get().post("Photo_URL", url);
        scrollToFinishActivity();
    }

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
            mPolygonView.setImageResource(R.drawable.meizi_grey_on);
            Glide.with(this).
                    load(url)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.jay)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mPolygonView.setImageBitmap(resource);
                            //   mPolygonView.setImageResource(R.drawable.jay);
                        }
                    });
            // mPolygonView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.captain_android));
        }

        @Override
        public void onDestroyView() {
            LogTools.d("onDestroyView");
            mPolygonView.recycle();
            mPolygonView = null;
            super.onDestroyView();
        }

        @Override
        public void onDestroy() {
            LogTools.d("onDestroy");
            super.onDestroy();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                scrollToFinishActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
