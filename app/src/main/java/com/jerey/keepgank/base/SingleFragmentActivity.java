package com.jerey.keepgank.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jerey.keepgank.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Xiamin on 2017/2/11.
 */
public abstract class SingleFragmentActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Fragment fragment = getFragment();
        fragment.setArguments(getArguments());
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();

    }

    protected abstract Fragment getFragment();

    protected abstract Bundle getArguments() ;
}
