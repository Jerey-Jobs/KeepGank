package com.jerey.keepgank.douban;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jerey.keepgank.R;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.douban.itembinder.SubjectsBinder;
import com.jerey.keepgank.douban.itembinder.TypeInfoBeanBinder;
import com.jerey.keepgank.fragment.BaseFragment;
import com.jerey.keepgank.multitype.MultiTypeAdapter;
import com.jerey.keepgank.net.DoubanApi;
import com.jerey.loglib.LogTools;

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
        adapter = new MultiTypeAdapter();
        items = new ArrayList<>();
        adapter.register(TypeInfoBean.class,new TypeInfoBeanBinder());
        adapter.register(SubjectsBean.class,new SubjectsBinder());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        DoubanApi.getInstance()
                .getDoubanInterface()
                .getTop250(0, 10)
                .compose(this.<TypeInfoBean>bindToLifecycle())
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
                        items.add(typeInfoBean);
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
