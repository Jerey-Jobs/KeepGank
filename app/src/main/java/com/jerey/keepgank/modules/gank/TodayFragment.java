package com.jerey.keepgank.modules.gank;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jerey.footerrecyclerview.FooterRecyclerView;
import com.jerey.keepgank.R;
import com.jerey.keepgank.api.GankApi;
import com.jerey.keepgank.data.bean.GankDay;
import com.jerey.keepgank.data.bean.GankDayResults;
import com.jerey.keepgank.data.bean.Result;
import com.jerey.keepgank.data.bean.TitleBean;
import com.jerey.keepgank.modules.base.BaseFragment;
import com.jerey.keepgank.modules.gank.binder.GankResultBinder;
import com.jerey.keepgank.modules.gank.binder.TitleBeanBinder;
import com.jerey.keepgank.widget.MyBottomItemDecoration;
import com.jerey.keepgank.widget.SlideInOutRightItemAnimator;
import com.jerey.loglib.LogTools;
import com.jerey.lruCache.DiskLruCacheManager;
import com.jerey.mutitype.MultiTypeAdapter;
import com.trello.rxlifecycle.FragmentEvent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.jerey.keepgank.R.id.toolbar;

/**
 * Created by Xiamin on 2017/2/15.
 */

public class TodayFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "TodayFragment";

    @BindView(toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.day_recycleview)
    FooterRecyclerView mRecyclerView;
    @BindView(R.id.story_img)
    ImageView mImageView;
    @BindView(R.id.float_action_button)
    FloatingActionButton mButton;

    private LinearLayoutManager mLinearLayoutManager;
    private MultiTypeAdapter mAdapter;
    int year;
    int month;
    int day;
    private DiskLruCacheManager mDiskLruCacheManager;
    private List<Object> mDataList;

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_day;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        initUI();
        dynamicAddView(mToolbarLayout, "ContentScrimColor", R.color.app_main_color);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        year = calendar.get(java.util.Calendar.YEAR);
        month = calendar.get(java.util.Calendar.MONTH) + 1;
        day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        //加载前一天的先
        load(year, month, day - 1);
        //再加载今天的
        load(year, month, day);
        try {
            mDiskLruCacheManager = new DiskLruCacheManager(getActivity(), "xiamin");
            Log.i(TAG, "DiskLruCacheManager 创建");
            GankDay gankDay = mDiskLruCacheManager.getAsSerializable(TAG);
            Log.i(TAG, "DiskLruCacheManager 读取数据 " + gankDay);
            if (gankDay != null) {
                loadUIByGankday(gankDay);
            }
        } catch (IOException e) {
            Log.e(TAG, "DiskLruCacheManager 创建失败");
            e.printStackTrace();
        }
    }

    private void initUI() {
        mToolbarLayout.setTitle("今日热点");
        mImageView.setImageResource(R.drawable.jay);
        //设置渐显动画，替换状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarColor = ((AppCompatActivity) getActivity()).getWindow()
                                                                    .getStatusBarColor();
            mToolbarLayout.setContentScrimColor(statusBarColor);
            if (statusBarColor != ((AppCompatActivity) getActivity()).getWindow()
                                                                     .getStatusBarColor()) {
                ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                        ((AppCompatActivity) getActivity()).getWindow().getStatusBarColor(),
                        statusBarColor);
                statusBarColorAnim.addUpdateListener(new ValueAnimator
                        .AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatActivity) getActivity()).getWindow().setStatusBarColor(
                                    (int) animation.getAnimatedValue());
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
        mDataList = new ArrayList<>();
        mAdapter = new MultiTypeAdapter(mDataList)
                .register(TitleBean.class, new TitleBeanBinder())
                .register(Result.class, new GankResultBinder());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyBottomItemDecoration(getContext()));
        mRecyclerView.setEndText("没有更多数据了...");
        mLinearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        TodayFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                                                                                );
                datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    private void load(final int year, final int month, final int day) {
        GankApi.getInstance()
               .getWebService()
               .getGoodsByDay(year, month, day)
               .filter(new Func1<GankDay, Boolean>() {
                   @Override
                   public Boolean call(GankDay gankDay) {
                       return gankDay != null
                               && gankDay.results != null
                               && gankDay.results.Android != null;
                   }
               })
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
                       e.printStackTrace();
                       showSnackbar(R.string.error);
                       showSnackbar("该日可能没有更新哦");
                   }

                   @Override
                   public void onNext(GankDay gankDay) {
                       LogTools.d(gankDay.toString());
                       if (gankDay != null && gankDay.results != null && gankDay.results.Android
                               != null) {
                           mToolbarLayout.setTitle(year + "年" + month + "月" + day + "日");
                           mDiskLruCacheManager.put(TAG, gankDay);
                           loadUIByGankday(gankDay);
                       }
                   }
               });
    }

    private void loadUIByGankday(final GankDay gankDay) {
        LogTools.d(gankDay.results.福利.get(0).getUrl());
        Glide.with(TodayFragment.this)
             .load(gankDay.results.福利.get(0).getUrl())
             .centerCrop()
             .crossFade()
             .error(R.drawable.jay)
             .into(mImageView);
        LogTools.d("loadUIByGankday");
        Observable.create(new Observable.OnSubscribe<GankDayResults>() {
            @Override
            public void call(Subscriber<? super GankDayResults> subscriber) {
                mDataList.clear();
                subscriber.onNext(gankDay.results);
            }
        })
                  .subscribeOn(Schedulers.io())
                  .map(new Func1<GankDayResults, List<Object>>() {

                      @Override
                      public List<Object> call(GankDayResults gankDayResults) {
                          if (gankDayResults.Android != null) {
                              mDataList.add(new TitleBean("Android"));
                              mDataList.addAll(gankDayResults.Android);
                          }
                          if (gankDayResults.iOS != null) {
                              mDataList.add(new TitleBean("IOS"));
                              mDataList.addAll(gankDayResults.iOS);
                          }
                          if (gankDayResults.App != null) {
                              mDataList.add(new TitleBean("APP"));
                              mDataList.addAll(gankDayResults.App);
                          }
                          if (gankDayResults.休息视频 != null) {
                              mDataList.add(new TitleBean("休息视频"));
                              mDataList.addAll(gankDayResults.休息视频);
                          }
                          if (gankDayResults.瞎推荐 != null) {
                              mDataList.add(new TitleBean("瞎推荐"));
                              mDataList.addAll(gankDayResults.瞎推荐);
                          }
                          return mDataList;
                      }
                  })
                  .map(new Func1<List<Object>, List<Object>>() {
                      @Override
                      public List<Object> call(List<Object> objects) {
                          for (Object obj : objects) {
                              if (obj instanceof Result) {
                                  Result result = (Result) obj;
                                  String string = result.getPublishedAt();
                                  if (!TextUtils.isEmpty(string)) {
                                      String tmp = string.substring(0, 10);
                                      result.setPublishedAt(tmp);
                                  }
                              }
                          }
                          return objects;
                      }
                  })
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<List<Object>>() {
                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onError(Throwable e) {
                          e.printStackTrace();
                      }

                      @Override
                      public void onNext(List<Object> objects) {
                          LogTools.d("onNext");

                          mRecyclerView.setItemAnimator(new SlideInOutRightItemAnimator
                                  (mRecyclerView));
                          mAdapter.notifyDataSetChanged();
                      }
                  });


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        LogTools.d("DatePickerDialog" + year + "-" + monthOfYear + "-" + dayOfMonth);
        load(year, monthOfYear + 1, dayOfMonth);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
