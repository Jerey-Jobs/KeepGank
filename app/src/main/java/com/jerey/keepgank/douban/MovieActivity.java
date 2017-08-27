package com.jerey.keepgank.douban;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.douban.itembinder.SubjectsBinder;
import com.jerey.loglib.LogTools;
import com.jerey.mutitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Xiamin
 * @date 2017/8/20
 */
@Route(path = "/douban/MovieActivity")
public class MovieActivity extends AppSwipeBackActivity {

    @Autowired
    String movieId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.movie_imageView)
    ImageView mMovieImageView;
    @BindView(R.id.movie_layout)
    LinearLayout mMovieLayout;
    @BindView(R.id.movie_recyclerview)
    RecyclerView mMovieRecyclerview;

    MultiTypeAdapter mMultiTypeAdapter;
    List<Object> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        /** 使用ARouter解析 */
        ARouter.getInstance().inject(this);
        /** 使用Android自带 */
        //movieId = getIntent().getStringExtra("movieId");
        LogTools.d("movieId = " + movieId);
        mList = new ArrayList<>();
        mMultiTypeAdapter = new MultiTypeAdapter(mList);
        mMultiTypeAdapter.register(SubjectsBean.class,new SubjectsBinder());
        mMovieRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mMovieRecyclerview.setAdapter(mMultiTypeAdapter);
        mMovieRecyclerview.setNestedScrollingEnabled(false);
        for (int i = 0; i < 15; i++) {
            mList.add(new SubjectsBean());
        }
        mMultiTypeAdapter.notifyDataSetChanged();
    }
}
