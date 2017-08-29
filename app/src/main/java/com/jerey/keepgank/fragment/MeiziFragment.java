package com.jerey.keepgank.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jerey.animationadapter.AnimationAdapter;
import com.jerey.animationadapter.SlideInBottomAnimationAdapter;
import com.jerey.keepgank.R;
import com.jerey.keepgank.adapter.MeiziAdapter;
import com.jerey.keepgank.bean.Data;
import com.jerey.keepgank.bean.Result;
import com.jerey.keepgank.net.GankApi;
import com.jerey.keepgank.view.SwipeToRefreshLayout;
import com.jerey.loglib.LogTools;
import com.jerey.lruCache.DiskLruCacheManager;
import com.trello.rxlifecycle.FragmentEvent;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Xiamin on 2017/3/1.
 */

public class MeiziFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MeiziFragment";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_ly)
    SwipeToRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    //当前页数
    private int currentPager = 1;
    //是否刷新状态
    private boolean isLoadingNewData = false;
    //是否载入更多状态
    private boolean isLoadingMore = false;
    //是否已经载入去全部
    private boolean isALlLoad = false;
    MeiziAdapter mAdapter;
    private DiskLruCacheManager mDiskLruCacheManager;

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        mToolBar.setTitle("妹子");
        mToolBar.setTitleTextColor(Color.WHITE);
        dynamicAddView(mToolBar, "background", R.color.app_main_color);
        initRecyclerView(mRecyclerView);
        initSwipeRefreshLayout(mSwipeRefreshLayout);
        mAdapter = new MeiziAdapter(getActivity());
        AnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        animationAdapter.setDuration(800);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(mOnScrollListener);


        Observable.create(new Observable.OnSubscribe<Data>() {

            @Override
            public void call(Subscriber<? super Data> subscriber) {
                Log.i(TAG, "DiskLruCacheManager 创建");
                try {
                    mDiskLruCacheManager = new DiskLruCacheManager(getActivity());
                    Data data = mDiskLruCacheManager.getAsSerializable(TAG);
                    Log.i(TAG, "DiskLruCacheManager 读取");
                    if (data != null) {
                        subscriber.onNext(data);
                    } else {
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                    return;
                }
            }
        })
                  .map(new Func1<Data, List<Result>>() {

                      @Override
                      public List<Result> call(Data data) {
                          return data.getResults();
                      }
                  })
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<List<Result>>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {
                          e.printStackTrace();
                      }

                      @Override
                      public void onNext(List<Result> results) {
                          Log.i(TAG, "获取到缓存数据");
                          mAdapter.setData(results);
                          mAdapter.notifyDataSetChanged();
                      }
                  });
        onRefresh();

    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                                                                     StaggeredGridLayoutManager
                                                                             .VERTICAL);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);
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

    @Override
    public void onRefresh() {
        LogTools.i("开始刷新");
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = false;
        isLoadingNewData = true;
        loadData(1);
    }

    int[] lastPositions;
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //但RecyclerView滑动到倒数第三个之请求加载更多
            if (lastPositions == null) {
                lastPositions = new int[mStaggeredGridLayoutManager.getSpanCount()];
            }
            int[] lastVisibleItem = mStaggeredGridLayoutManager.findLastVisibleItemPositions(
                    lastPositions);
            int totalItemCount = mAdapter.getItemCount();
            // dy>0 表示向下滑动
            if (lastVisibleItem[0] >= totalItemCount - 4 && dy > 0 && !isLoading() && !isALlLoad) {
                requestMoreData();
            }
        }
    };

    private boolean isLoading() {
        return isLoadingMore || isLoadingNewData;
    }

    private void requestMoreData() {
        LogTools.i("加载更多");
        mSwipeRefreshLayout.setRefreshing(true);
        isLoadingMore = true;
        isLoadingNewData = false;
        loadData(++currentPager);
    }

    private void loadData(int pager) {
        GankApi.getInstance()
               .getWebService()
               .getBenefitsGoods(GankApi.LOAD_LIMIT, pager)
               .compose(this.<Data>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
               .cache()
               .subscribeOn(Schedulers.io())
               .map(new Func1<Data, List<Result>>() {
                   @Override
                   public List<Result> call(Data data) {
                       if (data != null) {
                           Log.i(TAG, "DiskLruCacheManager 写入");
                           mDiskLruCacheManager.put(TAG, data);
                           mAdapter.notifyDataSetChanged();
                           return data.getResults();
                       }
                       return null;
                   }
               })
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<List<Result>>() {
                   @Override
                   public void onCompleted() {
                       LogTools.i("数据onCompleted 停止刷新");
                       mSwipeRefreshLayout.setRefreshing(false);
                       isLoadingNewData = false;
                       isLoadingMore = false;
                   }

                   @Override
                   public void onError(Throwable e) {
                       LogTools.i("onError 停止刷新");
                       mSwipeRefreshLayout.setRefreshing(false);
                       isLoadingNewData = false;
                       isLoadingMore = false;
                       showSnackbar("OnError");
                   }

                   @Override
                   public void onNext(List<Result> results) {
                       LogTools.i("onNext " + results);
                       if (results != null) {
                           /**
                            * 没有更多数据
                            */
                           if (results.size() < GankApi.LOAD_LIMIT) {
                               isALlLoad = true;
                               showSnackbar(R.string.no_more);
                           }

                           if (isLoadingMore) {
                               mAdapter.addData(results);
                           } else if (isLoadingNewData) {
                               isALlLoad = false;
                               mAdapter.setData(results);
                           }

                       }
                   }
               });
    }

}
