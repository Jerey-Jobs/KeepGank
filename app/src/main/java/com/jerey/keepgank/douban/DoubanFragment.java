package com.jerey.keepgank.douban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jerey.keepgank.R;
import com.jerey.keepgank.douban.bean.BannerBean;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.douban.bean.USBean;
import com.jerey.keepgank.douban.itembinder.BannerBinder;
import com.jerey.keepgank.douban.itembinder.SubjectsBinder;
import com.jerey.keepgank.douban.itembinder.TypeInfoBeanBinder;
import com.jerey.keepgank.fragment.BaseFragment;
import com.jerey.keepgank.multitype.MultiTypeAdapter;
import com.jerey.keepgank.net.DoubanApi;
import com.jerey.loglib.LogTools;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author xiamin
 * @date 8/17/17.
 */
public class DoubanFragment extends BaseFragment {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.m_recyclerView)
    RecyclerView mRecyclerView;

    private MultiTypeAdapter adapter;
    private List<Object> items;

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_douban;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        dynamicAddView(mToolbar, "background", R.color.app_main_color);
        mToolbar.setTitle("豆瓣电影推荐");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        adapter = new MultiTypeAdapter();
        items = new ArrayList<>();
        adapter.register(TypeInfoBean.class, new TypeInfoBeanBinder());
        adapter.register(SubjectsBean.class, new SubjectsBinder());
        adapter.register(BannerBean.class, new BannerBinder());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        for (int i = 0; i < 4; i++) {
            items.add(new TypeInfoBean());
        }
        adapter.setItems(items);
        mRecyclerView.setAdapter(adapter);
        getHead();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TypeInfoBean>() {
                    @Override
                    public void onCompleted() {
                        getInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(TypeInfoBean typeInfoBean) {
                        LogTools.w(typeInfoBean.toString());
                        typeInfoBean.setType(DoubanApi.MOVIE_TYPE.TYPE_in_theaters);
                        items.set(0, new BannerBean(typeInfoBean.getSubjects()));
                        LogTools.w("itemsize:" + items.size());
                        for (SubjectsBean s : typeInfoBean.getSubjects()) {
                            items.add(4, s);
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
                .compose(this.<USBean>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<USBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(USBean usBean) {
                        TypeInfoBean typeInfoBean = new TypeInfoBean();
                        typeInfoBean.setTitle(usBean.getTitle());
                        typeInfoBean.setType(DoubanApi.MOVIE_TYPE.TYPE_UsMovie);
                        List<SubjectsBean> list = new ArrayList<SubjectsBean>();
                        for (USBean.SBean b : usBean.getSubjects()) {
                            list.add(b.getSubject());
                        }
                        typeInfoBean.setSubjects(list);
                        LogTools.w(typeInfoBean.toString());
                        items.set(3, typeInfoBean);
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
