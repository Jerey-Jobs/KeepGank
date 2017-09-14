package com.jerey.keepgank.modules.about;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        dynamicAddView(mToolbarLayout, "ContentScrimColor", R.color.app_main_color);
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
            case R.id.action_picture_choose:
                ARouter.getInstance()
                       .build("/activity/PhotoChooseActivity")
                       .withTransition(R.anim.in_from_right, 0)
                       .navigation(this);
                break;
            case R.id.action_first_enter:
                ARouter.getInstance()
                       .build("/activity/WelcomeActivity")
                       .withTransition(R.anim.in_from_right, 0)
                       .navigation(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_enter_firstin, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
