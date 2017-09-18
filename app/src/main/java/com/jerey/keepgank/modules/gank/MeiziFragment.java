package com.jerey.keepgank.modules.gank;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jerey.animationadapter.AnimationAdapter;
import com.jerey.animationadapter.SlideInBottomAnimationAdapter;
import com.jerey.keepgank.R;
import com.jerey.keepgank.api.GankApi;
import com.jerey.keepgank.data.bean.Data;
import com.jerey.keepgank.data.bean.Result;
import com.jerey.keepgank.modules.base.BaseFragment;
import com.jerey.keepgank.modules.gank.adapter.MeiziAdapter;
import com.jerey.keepgank.modules.photopreview.PhotoBean;
import com.jerey.keepgank.widget.SwipeToRefreshLayout;
import com.jerey.loglib.LogTools;
import com.jerey.lruCache.DiskLruCacheManager;
import com.trello.rxlifecycle.FragmentEvent;

import java.io.IOException;
import java.util.ArrayList;
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

public class MeiziFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, MeiziAdapter
        .OnItemClickListener {

    private static final String TAG = MeiziFragment.class.getSimpleName();

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
    List<Result> mResultList;
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
        mResultList = new ArrayList<>();
        mAdapter = new MeiziAdapter(getActivity(), mResultList);
        mAdapter.setOnItemClickListener(this);
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
                          mResultList.addAll(results);
                          mAdapter.notifyDataSetChanged();
                      }
                  });
        onRefresh();

    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
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

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //但RecyclerView滑动到倒数第三个之请求加载更多
            int totalItemCount = mAdapter.getItemCount();
            // dy>0 表示向下滑动
            if (getLastVisibleItemPosition() >= totalItemCount - 4 && dy > 0 && !isLoading() && !isALlLoad) {
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
                               addData(results);
                           } else if (isLoadingNewData) {
                               isALlLoad = false;
                               mResultList.clear();
                               mResultList.addAll(results);
                               mAdapter.notifyDataSetChanged();
                           }

                       }
                   }
               });
    }

    private int getFirstVisibleItemPositions() {
        int[] first = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null);
        return first[0];
    }

    private int getLastVisibleItemPosition() {
        int[] lastVisibleItem = mStaggeredGridLayoutManager.findLastVisibleItemPositions(null);
        return lastVisibleItem[lastVisibleItem.length - 1];
    }

    private void addData(List<Result> data) {
        int start = mResultList.size() - 1;
        mResultList.addAll(data);
        mAdapter.notifyItemRangeInserted(start + 1, mResultList.size());
    }

    /**
     * 1. Assemble the photo data
     * 2. Acquisition photo's Rect, default rect is the clicked view's Rect
     * 3. navigation
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        ArrayList<PhotoBean> mPhotoBeanArrayList = new ArrayList<PhotoBean>();
        Rect bounds = new Rect();
        view.getGlobalVisibleRect(bounds);
        LogTools.w("mPhotoBeanArrayList:" + mPhotoBeanArrayList.size()
                + " position:" + position + " bounds:" + bounds.toString());
        for (Result result : mResultList) {
            PhotoBean photoBean = new PhotoBean(result.getUrl(), bounds);
            mPhotoBeanArrayList.add(photoBean);
        }
        int start = getFirstVisibleItemPositions();
        int last = getLastVisibleItemPosition();

        for (int i = start; i < last; i++) {
            View itemView = mStaggeredGridLayoutManager.findViewByPosition(i);
            Rect bound = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.item_img);
                thumbView.getGlobalVisibleRect(bound);
            }
            mPhotoBeanArrayList.get(i).setRect(bound);
        }

        ARouter.getInstance()
               .build("/activity/PhotoPreviewActivity")
               .withInt("index", position)
               .withParcelableArrayList("photo_beans", mPhotoBeanArrayList)
               .navigation();
    }
}
