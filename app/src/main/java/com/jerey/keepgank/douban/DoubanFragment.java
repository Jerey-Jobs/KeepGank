package com.jerey.keepgank.douban;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerey.animationadapter.AnimationAdapter;
import com.jerey.animationadapter.SlideInBottomAnimationAdapter;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.douban.bean.BannerBean;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.douban.bean.USBean;
import com.jerey.keepgank.douban.itembinder.BannerBinder;
import com.jerey.keepgank.douban.itembinder.SubjectsBinder;
import com.jerey.keepgank.douban.itembinder.TypeInfoBeanBinder;
import com.jerey.keepgank.fragment.BaseFragment;
import com.jerey.keepgank.net.DoubanApi;
import com.jerey.loglib.LogTools;
import com.jerey.mutitype.MultiTypeAdapter;
import com.jerey.searchview.HistoryBean;
import com.jerey.searchview.SearchView;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author xiamin
 * @date 8/17/17.
 */
public class DoubanFragment extends BaseFragment implements SearchView.OnSearchActionListener {
    private static final String DB_TYPE = "douban_search";

    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_text)
    TextView mTextView;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.header_layout)
    LinearLayout mHeadLayout;

    LinearLayoutManager mLinearLayoutManager;
    private MultiTypeAdapter adapter;
    private List<Object> items;

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_douban;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        dynamicAddView(mToolbarLayout, "ContentScrimColor", R.color.app_main_color);
        mTextView.setText("豆瓣电影推荐");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbarLayout.setTitleEnabled(false);
        /** searchView处理*/
        mSearchView.setType(DB_TYPE)
                   .registerData(SubjectsBean.class, new SubjectsBinder())
                   .setOnSearchActionListener(this)
                   .setHistoryItemClickListener(new SearchView.OnHistoryClickListener() {
                       @Override
                       public void onClick(HistoryBean data) {
                           onSearchAction(data.getContent());
                       }
                   });
        /** adapter初始化*/
        adapter = new MultiTypeAdapter();
        items = new ArrayList<>();
        adapter.register(TypeInfoBean.class, new TypeInfoBeanBinder())
               .register(SubjectsBean.class, new SubjectsBinder())
               .register(BannerBean.class, new BannerBinder());

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        /** 伪造数据，实现预加载*/
        List<SubjectsBean> tmpList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tmpList.add(new SubjectsBean());
        }
        TypeInfoBean typeInfoBean = new TypeInfoBean();
        typeInfoBean.setSubjects(tmpList);
        for (int i = 0; i < 3; i++) {
            items.add(typeInfoBean);
        }
        adapter.setItems(items);
        AnimationAdapter animationAdapter = new SlideInBottomAnimationAdapter(adapter);
        animationAdapter.setDuration(800);
        mRecyclerView.setAdapter(animationAdapter);
        /**
         * 增加滚动监听,避免下拉时AppBar还要多拉动一次,没有了连贯效果
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int visiblePosition = mLinearLayoutManager
                            .findFirstCompletelyVisibleItemPosition();
                    if (visiblePosition == 0) {
                        mAppBarLayout.setExpanded(true, true);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        getHead();
        getInfo();
    }

    /**
     * 先加载头部列表
     */
    private void getHead() {
        DoubanApi.getInstance()
                 .getDoubanInterface()
                 .getTypeData(DoubanApi.MOVIE_TYPE.TYPE_in_theaters, 0, 6)
                 .compose(this.<TypeInfoBean>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                 .subscribeOn(Schedulers.io())
                 .filter(new Func1<TypeInfoBean, Boolean>() {
                     @Override
                     public Boolean call(TypeInfoBean typeInfoBean) {
                         return typeInfoBean != null;
                     }
                 })
                 .map(new Func1<TypeInfoBean, List<SubjectsBean>>() {
                     @Override
                     public List<SubjectsBean> call(TypeInfoBean typeInfoBean) {
                         return typeInfoBean.getSubjects();
                     }
                 })
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<List<SubjectsBean>>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {
                         e.printStackTrace();
                     }

                     @Override
                     public void onNext(List<SubjectsBean> subjects) {
                         /**
                          * Bannerbinder直接转View 添加到头部视差布局中
                          */
                         new BannerBinder().addView(mHeadLayout, new BannerBean(subjects));
                         LogTools.w("itemsize:" + items.size());
                         for (SubjectsBean s : subjects) {
                             items.add(3, s);
                         }
                         adapter.setItems(items);
                         adapter.notifyDataSetChanged();
                     }
                 });
    }

    /**
     * 加载类型列表
     */
    private void getInfo() {
        DoubanApi.getInstance()
                 .getDoubanInterface()
                 .getTop250(0, 10)
                 .compose(this.<TypeInfoBean>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                         LogTools.w(typeInfoBean.toString());
                         typeInfoBean.setType(DoubanApi.MOVIE_TYPE.TYPE_TOP250);
                         items.set(1, typeInfoBean);
                         adapter.setItems(items);
                         adapter.notifyDataSetChanged();
                     }
                 });

        DoubanApi.getInstance()
                 .getDoubanInterface()
                 .getCommingSoon(0, 10)
                 .compose(this.<TypeInfoBean>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<TypeInfoBean>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onNext(TypeInfoBean typeInfoBean) {
                         LogTools.w(typeInfoBean.toString());
                         typeInfoBean.setType(DoubanApi.MOVIE_TYPE.TYPE_CommingSoon);
                         items.set(2, typeInfoBean);
                         adapter.setItems(items);
                         adapter.notifyDataSetChanged();
                     }
                 });

        DoubanApi.getInstance()
                 .getDoubanInterface()
                 .getUSMovie()
                 .map(new Func1<USBean, TypeInfoBean>() {
                     @Override
                     public TypeInfoBean call(USBean usBean) {
                         TypeInfoBean typeInfoBean = new TypeInfoBean();
                         typeInfoBean.setTitle(usBean.getTitle());
                         typeInfoBean.setType(DoubanApi.MOVIE_TYPE.TYPE_UsMovie);
                         List<SubjectsBean> list = new ArrayList<SubjectsBean>();
                         for (USBean.SBean b : usBean.getSubjects()) {
                             list.add(b.getSubject());
                         }
                         typeInfoBean.setSubjects(list);
                         return typeInfoBean;
                     }
                 })
                 .compose(this.<TypeInfoBean>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<TypeInfoBean>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onNext(TypeInfoBean typeInfoBean) {
                         LogTools.w(typeInfoBean.toString());
                         items.set(0, typeInfoBean);
                         adapter.setItems(items);
                         adapter.notifyDataSetChanged();
                     }
                 });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //自动打开关闭SearchView
                mSearchView.autoOpenOrClose();
                break;
            case android.R.id.home:
                ((AppSwipeBackActivity) getActivity()).scrollToFinishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchAction(String searchText) {
        DoubanApi.getInstance()
                 .getDoubanInterface()
                 .search(searchText.trim())
                 .map(new Func1<TypeInfoBean, List<SubjectsBean>>() {
                     @Override
                     public List<SubjectsBean> call(TypeInfoBean typeInfoBean) {
                         return typeInfoBean.getSubjects();
                     }
                 })
                 .compose(this.<List<SubjectsBean>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Observer<List<SubjectsBean>>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onNext(List<SubjectsBean> subjectsBeen) {
                         mSearchView.setListObjects(subjectsBeen);
                     }
                 });

    }
}
