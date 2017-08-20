package com.jerey.keepgank.douban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.douban.bean.BannerBean;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.douban.itembinder.BannerBinder;
import com.jerey.keepgank.douban.itembinder.SubjectsBinder;
import com.jerey.keepgank.multitype.MultiTypeAdapter;
import com.jerey.loglib.LogTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@Route(path = "/douban/MovieListActivity")
public class MovieListActivity extends AppSwipeBackActivity {
    public static final String TAG = "MovieListActivity";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    TypeInfoBean mTypeInfoBean;
    List<SubjectsBean> mSubjectsBeanList;
    private MultiTypeAdapter adapter;

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
        } else {
            mSubjectsBeanList = new ArrayList<>();
        }
        adapter.setItems(mSubjectsBeanList);
        adapter.register(SubjectsBean.class, new SubjectsBinder());
        adapter.register(BannerBean.class, new BannerBinder());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    public static void startActivity(Context context, TypeInfoBean typeInfoBean) {
        Intent intent = new Intent(context, MovieListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, typeInfoBean);
        intent.putExtra(TAG, bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, 0);
    }
}
