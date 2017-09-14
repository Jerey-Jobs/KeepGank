package com.jerey.keepgank.modules.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jerey.keepgank.R;
import com.jerey.keepgank.modules.base.AppSwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author xiamin
 * @date 9/1/17.
 */
@Route(path = "/activity/AboutActivity")
public class AboutActivity extends AppSwipeBackActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.preference_fragment)
    LinearLayout mPreferenceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.preference_fragment, new AboutFragment())
                .commit();
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
