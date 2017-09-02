package com.jerey.keepgank.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jerey.keepgank.R;

/**
 * Created by Xiamin on 2017/2/11.
 */
public abstract class SingleFragmentActivity extends AppSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        ARouter.getInstance().inject(this);
        Fragment fragment = getFragment();
        fragment.setArguments(getArguments());
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();

    }

    protected abstract Fragment getFragment();

    protected abstract Bundle getArguments() ;
}
