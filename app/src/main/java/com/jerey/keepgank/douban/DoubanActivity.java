package com.jerey.keepgank.douban;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jerey.keepgank.base.SingleFragmentActivity;

/**
 * @author Xiamin
 * @date 2017/8/17
 */
@Route(path = "/douban/DoubanActivity")
public class DoubanActivity extends SingleFragmentActivity{
    @Override
    protected Fragment getFragment() {
        return new DoubanFragment();
    }

    @Override
    protected Bundle getArguments() {
        return null;
    }
}
