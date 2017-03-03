package com.jerey.keepgank;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jerey.keepgank.fragment.HomeFragment;
import com.jerey.keepgank.fragment.MeiziFragment;
import com.jerey.keepgank.fragment.TodayFragment;
import com.jerey.keepgank.fragment.WebView;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int mCurrentUIIndex = 0;
    private static final int INDEX_HOME = 0;
    private static final int INDEX_COLLECTION = 1;
    private static final int INDEX_Blog = 2;
    private static final int INDEX_TODAY = 3;

    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        ImageView drawHeaderImageView = (ImageView) mNavigationView.getHeaderView(0);
        Glide.with(this).load(R.drawable.captain_android).into(drawHeaderImageView);

        updateUI();
    }


    Fragment mHomeFragment;
    Fragment mBlogFragment;
    Fragment mTodayFragment;
    Fragment mMeiziFragment;
    Fragment mCurrentFragment;

    private void updateUI() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        switch (mCurrentUIIndex) {
            case INDEX_HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }
                switchFragment(mHomeFragment);

                break;
            case INDEX_COLLECTION:
                if(mMeiziFragment == null){
                    mMeiziFragment = new MeiziFragment();
                }
                switchFragment(mMeiziFragment);
                break;
            case INDEX_Blog:
                if (mBlogFragment == null){
                    mBlogFragment = new WebView();
                }
                switchFragment(mBlogFragment);

                break;
            case INDEX_TODAY:
                if (mTodayFragment == null){
                    mTodayFragment = new TodayFragment();
                }
                switchFragment(mTodayFragment);
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Logger.i("home被点击");
                item.setChecked(true);
                mCurrentUIIndex = INDEX_HOME;
                updateUI();
                break;
            case R.id.nav_today:
                Logger.i("今日被点击");
                item.setChecked(true);
                mCurrentUIIndex = INDEX_TODAY;
                updateUI();
                break;
            case R.id.nav_collection:
                Logger.i("收藏被点击");
                item.setChecked(true);
                mCurrentUIIndex = INDEX_COLLECTION;
                updateUI();
                break;
            case R.id.my_blog:
                Logger.i("博客被点击");
                item.setChecked(true);
                mCurrentUIIndex = INDEX_Blog;
                updateUI();
                break;
            case R.id.nav_settings:
                Logger.i("设置被点击");
                break;
        }
        return false;
    }


    /**
     * 监听按钮，在Drawer打开状态下，若不监听，按下返回键则会退出该界面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                finish();
                overridePendingTransition(0, R.anim.out_to_bottom);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(mCurrentFragment != null){
            fragmentTransaction.hide(mCurrentFragment);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.content, fragment);
        }
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }

    public void openDrawer(){
        if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * 无需在子Fragment中设置该点击事件响应,只要
     * ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
     * 就OK
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Logger.d("onOptionsItemSelected android.R.id.home");
            openDrawer();
        }
        return super.onOptionsItemSelected(item);
    }
}
