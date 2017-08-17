package com.jerey.keepgank.douban;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerey.keepgank.R;
import com.jerey.keepgank.fragment.BaseFragment;
import com.jerey.keepgank.multitype.MultiTypeAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
