package com.jerey.keepgank.modules.gank;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.jerey.animationadapter.AnimationAdapter;
import com.jerey.animationadapter.SlideInBottomAnimationAdapter;
import com.jerey.footerrecyclerview.FooterRecyclerView;
import com.jerey.keepgank.R;
import com.jerey.keepgank.api.Config;
import com.jerey.keepgank.api.GankApi;
import com.jerey.keepgank.data.bean.Data;
import com.jerey.keepgank.data.bean.Result;
import com.jerey.keepgank.modules.base.BaseFragment;
import com.jerey.keepgank.modules.gank.binder.GankResultBinder;
import com.jerey.keepgank.widget.GankHeadItemDecoration;
import com.jerey.keepgank.widget.MyBottomItemDecoration;
import com.jerey.keepgank.widget.SlideInOutRightItemAnimator;
import com.jerey.keepgank.widget.SwipeToRefreshLayout;
import com.jerey.loglib.LogTools;
import com.jerey.lruCache.DiskLruCacheManager;
import com.jerey.mutitype.MultiTypeAdapter;
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
 * Created by Xiamin on 2017/2/12.
 */

public class ListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
                                                          FooterRecyclerView.onLoadMoreListener {
    private static final String TAG = "ListFragment";
    public static final String KEY_TYPE = "type";

    @BindView(R.id.recycler_view)
    FooterRecyclerView mRecyclerView;
    @BindView(R.id.swipe_ly)
    SwipeToRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private MultiTypeAdapter mAdapter;
    private List<Object> mDataList;

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

    private Observer<List<Result>> dataObserver = new Observer<List<Result>>() {
        @Override
        public void onNext(final List<Result> results) {
            if (null != results) {
                //如果取得的Results小于 预先设定的数量（GankApi.LOAD_LIMIT）就表示已经是最后一页
                if (results.size() < GankApi.LOAD_LIMIT) {
                    isALlLoad = true;
                    showSnackbar(R.string.no_more);
                }
                if (isLoadingMore) {
                    addData(results);
                } else if (isLoadingNewData) {
                    isALlLoad = false;
                    currentPager = 1;
                    setData(results);
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
        mDataList = new ArrayList<>();
        mAdapter = new MultiTypeAdapter(mDataList)
                .register(Result.class, new GankResultBinder());
        AnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(mAdapter);
        animationAdapter.setDuration(600);
        mRecyclerView.setAdapter(animationAdapter);

        mRecyclerView.setItemAnimator(new SlideInOutRightItemAnimator(mRecyclerView));
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.addItemDecoration(new MyBottomItemDecoration(getContext()));
        mRecyclerView.addItemDecoration(new GankHeadItemDecoration(getContext(), mDataList));

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
        })
                  .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                  .filter(new Func1<Data, Boolean>() {
                      @Override
                      public Boolean call(Data data) {
                          return (data != null && data.getResults() != null);
                      }
                  })
                  .map(new Func1<Data, List<Result>>() {
                      @Override
                      public List<Result> call(Data data) {
                          return data.getResults();
                      }
                  })
                  .subscribe(new Observer<List<Result>>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {

                      }

                      @Override
                      public void onNext(List<Result> results) {
                          Log.d(TAG, "获取缓存数据成功");
                          setData(results);
                          mAdapter.notifyDataSetChanged();
                      }
                  });

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
               .filter(new Func1<Data, Boolean>() {
                   @Override
                   public Boolean call(Data data) {
                       return data.getResults() != null;
                   }
               })
               .map(new Func1<Data, List<Result>>() {
                   @Override
                   public List<Result> call(Data data) {
                       Log.i(TAG, "DiskLruCacheManager 写入");
                       mDiskLruCacheManager.put(mType, data);
                       for (Result result : data.getResults()) {
                           String string = result.getPublishedAt();
                           if (!TextUtils.isEmpty(string)) {
                               String tmp = string.substring(0, 10);
                               result.setPublishedAt(tmp);
                           }
                       }
                       return data.getResults();
                   }
               })
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

    private void addData(List<Result> datas) {
        int start = mDataList.size();
        mDataList.addAll(datas);
        LogTools.d("start" + start + "mDatas.size()" + mDataList.size());
        mAdapter.notifyItemRangeInserted(start + 1, mDataList.size());
    }

    private void setData(List<Result> results) {
        mDataList.clear();
        mDataList.addAll(results);
        mAdapter.notifyDataSetChanged();
    }

}
