package com.jerey.keepgank.douban

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.jerey.keepgank.R
import com.jerey.keepgank.fragment.BaseFragment

import kotlinx.android.synthetic.main.fragment_douban.*

/**
 * Created by xiamin on 8/16/17.
 */
class DoubanFragment: BaseFragment (),View.OnClickListener{


    override fun returnLayoutID(): Int {
        return R.layout.fragment_douban
    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        mDoubanRecyclerView.layoutManager = LinearLayoutManager(activity)
        mDoubanRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}