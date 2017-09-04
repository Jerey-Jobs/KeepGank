package com.jerey.keepgank.douban;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.douban.bean.MovieInfoBean;
import com.jerey.keepgank.douban.itembinder.CastsBinder;
import com.jerey.keepgank.net.DoubanApi;
import com.jerey.loglib.LogTools;
import com.jerey.mutitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Xiamin
 * @date 2017/8/20
 */
@Route(path = "/douban/MovieActivity")
public class MovieActivity extends AppSwipeBackActivity {

    @Autowired
    String movieId;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.movie_imageView)
    ImageView mMovieImageView;
    @BindView(R.id.movie_layout)
    LinearLayout mMovieLayout;
    @BindView(R.id.movie_recyclerview)
    RecyclerView mMovieRecyclerview;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.tv_movie_title)
    TextView mTvMovieTitle;
    @BindView(R.id.tv_movie_detail1)
    TextView mTvMovieDetail1;
    @BindView(R.id.tv_movie_detail2)
    TextView mTvMovieDetail2;
    @BindView(R.id.tv_movie_detail3)
    TextView mTvMovieDetail3;
    @BindView(R.id.tv_rating)
    TextView mTvRating;
    @BindView(R.id.tv_ratings_count)
    TextView mTvRatingsCount;
    @BindView(R.id.tv_summary)
    TextView mTvSummary;
    @BindView(R.id.list_container)
    LinearLayout mContainer;

    MultiTypeAdapter mMultiTypeAdapter;
    List<Object> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        dynamicAddView(mToolbarLayout, "ContentScrimColor", R.color.app_main_color);
        //dynamicAddView(mToolbar, "background", R.color.app_main_color);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /** 使用ARouter解析 */
        ARouter.getInstance().inject(this);
        /** 使用Android自带 */
        //movieId = getIntent().getStringExtra("movieId");
        LogTools.d("movieId = " + movieId);
        mList = new ArrayList<>();

        loadData(movieId);


        // TODO 测试代码
        //        mMultiTypeAdapter = new MultiTypeAdapter(mList);
        //        mMultiTypeAdapter.register(SubjectsBean.class, new SubjectsBinder());
        //        mMovieRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        //        mMovieRecyclerview.setAdapter(mMultiTypeAdapter);
        //        mMovieRecyclerview.setNestedScrollingEnabled(false);
        //        for (int i = 0; i < 15; i++) {
        //            mList.add(new SubjectsBean());
        //        }
        //        mMultiTypeAdapter.notifyDataSetChanged();
    }

    private void loadData(String id) {
        DoubanApi.getInstance()
                 .getDoubanInterface()
                 .getMonvieInfo(id)
                 .filter(new Func1<MovieInfoBean, Boolean>() {
                     @Override
                     public Boolean call(MovieInfoBean movieInfoBean) {
                         return !TextUtils.isEmpty(movieInfoBean.getTitle());
                     }
                 })
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<MovieInfoBean>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {
                         e.printStackTrace();
                     }

                     @Override
                     public void onNext(MovieInfoBean movieInfoBean) {
                         if (movieInfoBean == null) {
                             return;
                         }
                         updateUI(movieInfoBean);
                     }
                 });
    }

    private void updateUI(MovieInfoBean movieInfoBean) {
        String imageUrl = null;
        if (movieInfoBean.getImages() != null) {
            imageUrl = movieInfoBean.getImages().getLarge();
            if (imageUrl == null) {
                imageUrl = movieInfoBean.getImages().getMedium();
            }
            if (imageUrl == null) {
                imageUrl = movieInfoBean.getImages().getSmall();
            }
        }
        Glide.with(MovieActivity.this)
             .load(imageUrl)
             .placeholder(R.drawable.bg_grey)
             .error(R.drawable.bg_red)
             .into(mMovieImageView);
        mTvMovieTitle.setText(movieInfoBean.getTitle());
        mTitleText.setText(movieInfoBean.getTitle());
        /** 设置年代 */
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(movieInfoBean.getYear())) {
            builder.append(movieInfoBean.getYear());
        }
        if (movieInfoBean.getCountries() != null) {
            for (String s : movieInfoBean.getCountries()) {
                builder.append("/");
                builder.append(s);
            }
        }
        if (!TextUtils.isEmpty(builder.toString())) {
            mTvMovieDetail1.setText(builder.toString());
        }
        /** 设置类型 */
        if (movieInfoBean.getGenres() != null) {
            StringBuilder stringBuilder = new StringBuilder("类型: ");
            for (String s : movieInfoBean.getGenres()) {
                stringBuilder.append(s);
            }
            mTvMovieDetail2.setText(stringBuilder.toString());
        }

        if (!movieInfoBean.getTitle().equals(movieInfoBean.getOriginal_title())) {
            mTvMovieDetail3.setText("原名: " + movieInfoBean.getOriginal_title());
        }

        mTvSummary.setText(movieInfoBean.getSummary());
        if (movieInfoBean.getRating() != null) {
            mTvRating.setText("" + movieInfoBean.getRating().getAverage());
            mTvRatingsCount.setText("评分人数:" + movieInfoBean.getRatings_count());
        }

        if (movieInfoBean.getCasts() != null) {
            new CastsBinder().addView(mContainer, movieInfoBean.getCasts());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                scrollToFinishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
