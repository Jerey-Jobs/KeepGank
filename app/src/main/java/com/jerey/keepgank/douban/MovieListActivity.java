package com.jerey.keepgank.douban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jerey.animationadapter.AnimationAdapter;
import com.jerey.animationadapter.SlideInBottomAnimationAdapter;
import com.jerey.footerrecyclerview.FooterRecyclerView;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.douban.bean.BannerBean;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.douban.itembinder.BannerBinder;
import com.jerey.keepgank.douban.itembinder.SubjectsBinder;
import com.jerey.keepgank.net.DoubanApi;
import com.jerey.loglib.LogTools;
import com.jerey.mutitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Route(path = "/douban/MovieListActivity")
public class MovieListActivity extends AppSwipeBackActivity implements FooterRecyclerView
                                                                               .onLoadMoreListener {
    public static final String TAG = "MovieListActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.m_recyclerView)
    FooterRecyclerView mRecyclerView;
    TypeInfoBean mTypeInfoBean;
    List<SubjectsBean> mSubjectsBeanList;
    private MultiTypeAdapter adapter;

    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_douban);
        ButterKnife.bind(this);
        dynamicAddView(mToolbar, "background", R.color.app_main_color);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        adapter = new MultiTypeAdapter();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = getIntent().getBundleExtra(TAG);
            mTypeInfoBean = (TypeInfoBean) bundle.getSerializable(TAG);
        }
        if (mTypeInfoBean != null) {
            mSubjectsBeanList = mTypeInfoBean.getSubjects();
            getSupportActionBar().setTitle(mTypeInfoBean.getTitle().toString());
            LogTools.d("title：" + mTypeInfoBean.getTitle().toString());
            mType = mTypeInfoBean.getType();
        } else {
            mSubjectsBeanList = new ArrayList<>();
        }
        adapter.setItems(mSubjectsBeanList);
        adapter.register(SubjectsBean.class, new SubjectsBinder());
        adapter.register(BannerBean.class, new BannerBinder());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(800);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnLoadMoreListener(this);
    }

    public static void startActivity(Context context, TypeInfoBean typeInfoBean) {
        Intent intent = new Intent(context, MovieListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, typeInfoBean);
        intent.putExtra(TAG, bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, 0);
    }

    private void loadData(int from, int count) {

        if (TextUtils.isEmpty(mType)) {
            toast("没有类型");
            return;
        }
        DoubanApi.getInstance()
                 .getDoubanInterface()
                 .getTypeData(mType, from, count)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<TypeInfoBean>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {
                         e.printStackTrace();
                     }

                     @Override
                     public void onNext(TypeInfoBean typeInfoBean) {
                         LogTools.d("数据来了： " + typeInfoBean.getTitle());
                         int begin = mSubjectsBeanList.size() + 1;
                         mSubjectsBeanList.addAll(typeInfoBean.getSubjects());
                         adapter.notifyItemRangeInserted(begin, mSubjectsBeanList.size());
                     }
                 });
    }

    @Override
    public void onLoadMore(int lastPosition) {
        LogTools.d("lastPosition " + lastPosition);
        loadData(lastPosition, 20);
    }
}
