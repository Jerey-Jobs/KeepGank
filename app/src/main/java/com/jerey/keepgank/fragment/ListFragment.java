package com.jerey.keepgank.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jerey.animationadapter.AnimationAdapter;
import com.jerey.animationadapter.SlideInBottomAnimationAdapter;
import com.jerey.footerrecyclerview.FooterRecyclerView;
import com.jerey.keepgank.R;
import com.jerey.keepgank.View.SlideInOutRightItemAnimator;
import com.jerey.keepgank.View.SwipeToRefreshLayout;
import com.jerey.keepgank.adapter.ListFragmentAdapter;
import com.jerey.keepgank.bean.Data;
import com.jerey.keepgank.net.Config;
import com.jerey.keepgank.net.GankApi;
import com.jerey.loglib.LogTools;
import com.jerey.lruCache.DiskLruCacheManager;
import com.trello.rxlifecycle.FragmentEvent;

import java.io.IOException;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class ListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
                                                          FooterRecyclerView.onLoadMoreListener {
    private static final String TAG = "ListFragment";
    public static final String KEY_TYPE = "type";

    @Bind(R.id.recycler_view)
    FooterRecyclerView mRecyclerView;
    @Bind(R.id.swipe_ly)
    SwipeToRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private ListFragmentAdapter mAdapter;

    //当前页数
    private int currentPager = 1;
    //是否刷新状态
    private boolean isLoadingNewData = false;
    //是否载入更多状态
    private boolean isLoadingMore = false;
    //是否已经载入去全部
    private boolean isALlLoad = false;
    //类型
    private String mType = "Android";

    private DiskLruCacheManager mDiskLruCacheManager;

    private Observer<Data> dataObserver = new Observer<Data>() {
        @Override
        public void onNext(final Data goodsResult) {
            if (null != goodsResult && null != goodsResult.getResults()) {
                //如果取得的Results小于 预先设定的数量（GankApi.LOAD_LIMIT）就表示已经是最后一页
                if (goodsResult.getResults().size() < GankApi.LOAD_LIMIT) {
                    isALlLoad = true;
                    showSnackbar(R.string.no_more);
                }
                if (isLoadingMore) {
                    mAdapter.addData(goodsResult.getResults());
                } else if (isLoadingNewData) {
                    isALlLoad = false;
                    currentPager = 1;
                    mAdapter.setData(goodsResult.getResults());
                    Log.i(TAG, "DiskLruCacheManager 写入");
                    mDiskLruCacheManager.put(mType, goodsResult);
                    mAdapter.notifyDataSetChanged();
                }

            }
        }

        @Override
        public void onCompleted() {
            LogTools.i("数据onCompleted 停止刷新");
            mSwipeRefreshLayout.setRefreshing(false);
            isLoadingNewData = false;
            isLoadingMore = false;
        }

        @Override
        public void onError(final Throwable error) {
            LogTools.i("onError 停止刷新");
            mSwipeRefreshLayout.setRefreshing(false);
            isLoadingNewData = false;
            isLoadingMore = false;
            showSnackbar("OnError");
        }
    };

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mType = getArguments().getString(KEY_TYPE, Config.TYPE_ANDROID);
        initRecyclerView(mRecyclerView);
        initSwipeRefreshLayout(mSwipeRefreshLayout);
        mAdapter = new ListFragmentAdapter(getActivity());
        AnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        animationAdapter.setDuration(600);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new SlideInOutRightItemAnimator(mRecyclerView));
        mRecyclerView.setOnLoadMoreListener(this);

        Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                try {
                    mDiskLruCacheManager = new DiskLruCacheManager(getActivity());
                    Data mData = mDiskLruCacheManager.getAsSerializable(mType);
                    Log.i(TAG, "DiskLruCacheManager 读取");
                    subscriber.onNext(mData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<Data>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {

                      }

                      @Override
                      public void onNext(Data data) {
                          if (data != null) {
                              Log.d(TAG, "获取缓存数据成功");
                              mAdapter.setData(data.getResults());
                              mAdapter.notifyDataSetChanged();
                          }
                      }
                  });
        //        try {
        //            Log.i(TAG, "DiskLruCacheManager 创建");
        //            mDiskLruCacheManager = new DiskLruCacheManager(getActivity());
        //            Data mData = mDiskLruCacheManager.getAsSerializable(mType);
        //            Log.i(TAG, "DiskLruCacheManager 读取");
        //            if (mData != null) {
        //                Log.d(TAG, "获取缓存数据成功");
        //                mAdapter.setData(mData.getResults());
        //                mAdapter.notifyDataSetChanged();
        //            }
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        requestRefresh();
    }

    @Override
    public void onRefresh() {
        LogTools.i("开始刷新");
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = false;
        isLoadingNewData = true;
        loadData(1);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLinearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        //日期说明
        // recyclerView.addItemDecoration(new SectionsDecoration(true));
    }

    private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        Resources resources = getResources();
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.blue_dark),
                resources.getColor(R.color.red_dark),
                resources.getColor(R.color.yellow_dark),
                resources.getColor(R.color.green_dark)
                                               );
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public static ListFragment getListFragment(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean isLoading() {
        return isLoadingMore || isLoadingNewData;
    }

    private void loadData(int pager) {
        GankApi.getInstance()
               .getCommonGoods(mType, GankApi.LOAD_LIMIT, pager)
               .compose(this.<Data>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
               .cache()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(dataObserver);
    }

    private void requestRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = false;
        isLoadingNewData = true;
        loadData(1);
    }

    private void requestMoreData() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = true;
        isLoadingNewData = false;
        currentPager++;
        loadData(currentPager);
    }

    @Override
    public void onLoadMore(int lastPosition) {
        requestMoreData();
    }
}
