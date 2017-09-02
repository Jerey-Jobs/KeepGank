package com.jerey.keepgank;
/**
 * ┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  ┌┐    ┌┐    ┌┐
 * └───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘  └┘    └┘    └┘
 * ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
 * │~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
 * ├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
 * │ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
 * ├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
 * │ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
 * ├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
 * │ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
 * ├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
 * │ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
 * └─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
 */

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cn.jerey.permissiontools.Callback.PermissionCallbacks;
import com.cn.jerey.permissiontools.PermissionTools;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jerey.keepgank.data.Constants;
import com.jerey.keepgank.fragment.HomeFragment;
import com.jerey.keepgank.fragment.MeiziFragment;
import com.jerey.keepgank.fragment.TodayFragment;
import com.jerey.keepgank.fragment.WebView;
import com.jerey.keepgank.utils.BlurImageUtils;
import com.jerey.keepgank.utils.SPUtils;
import com.jerey.loglib.LogTools;
import com.jerey.themelib.base.SkinBaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends SkinBaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {


    private int mCurrentUIIndex = 0;
    private static final int INDEX_HOME = 0;
    private static final int INDEX_COLLECTION = 1;
    private static final int INDEX_Blog = 2;
    private static final int INDEX_TODAY = 3;
    private static final int REQUEST_CODE = 111;

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.content)
    FrameLayout mContent;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    CircleImageView mUserimage;
    View mHeadViewContainer;
    PermissionTools permissionTools;
    Handler mHander;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
        // TODO 此透明状态栏会引起主题切换时systemUI的bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mHander = new Handler();
        mHeadViewContainer = mNavigationView.getHeaderView(0);
        mHeadViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                       .build("/activity/PhotoChooseActivity")
                       .withTransition(R.anim.in_from_right, 0)
                       .navigation(MainActivity.this);
            }
        });
        mUserimage = (CircleImageView) mHeadViewContainer.findViewById(R.id.userimage);
        mNavigationView.setNavigationItemSelectedListener(this);

        loadHead(SPUtils.get(this, Constants.HEAD_URL, ""));

        permissionTools = new PermissionTools.Builder(this)
                .setOnPermissionCallbacks(new PermissionCallbacks() {
                    @Override
                    public void onPermissionsGranted(int requestCode, List<String> perms) {
                        //    Toast.makeText(MainActivity.this, "权限申请通过", Toast.LENGTH_SHORT)
                        // .show();

                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, List<String> perms) {
                        Toast.makeText(MainActivity.this, "权限申请被拒绝", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRequestCode(REQUEST_CODE)
                .build();
        permissionTools.requestPermissions(Manifest.permission.INTERNET
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_NETWORK_STATE);
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            String uesername = uri.getQueryParameter("username");
            LogTools.d(url);
            Toast.makeText(this, "userName = " + uesername, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
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
                if (mMeiziFragment == null) {
                    mMeiziFragment = new MeiziFragment();
                }
                switchFragment(mMeiziFragment);
                break;
            case INDEX_Blog:
                if (mBlogFragment == null) {
                    mBlogFragment = new WebView();
                }
                switchFragment(mBlogFragment);

                break;
            case INDEX_TODAY:
                if (mTodayFragment == null) {
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
                LogTools.d("home被点击");
                item.setChecked(true);
                mCurrentUIIndex = INDEX_HOME;
                updateUI();
                break;
            case R.id.nav_today:
                LogTools.d("今日被点击");
                item.setChecked(true);
                mCurrentUIIndex = INDEX_TODAY;
                updateUI();
                break;
            case R.id.nav_collection:
                LogTools.d("收藏被点击");
                item.setChecked(true);
                mCurrentUIIndex = INDEX_COLLECTION;
                updateUI();
                break;
            case R.id.nav_movie:
                ARouter.getInstance().build("/douban/DoubanActivity")
                       .withTransition(R.anim.in_from_right, 0)
                       .navigation(this);
                break;
            case R.id.nav_settings:
                LogTools.d("主题被点击");
                ARouter.getInstance()
                       .build("/activity/ThemeChooseActivity")
                       .withTransition(R.anim.in_from_right, 0)
                       .navigation(this);
                break;
            case R.id.my_blog:
                LogTools.d("about被点击");
                ARouter.getInstance()
                       .build("/activity/AboutActivity")
                       .withTransition(R.anim.in_from_right, 0)
                       .navigation(this);
                break;
        }
        /**
         * 延时收回Drawer,使得后台收回,解决打开Theme界面时,低端手机上卡顿问题
         */
        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        }, 1500);
        return false;
    }

    private long exitTime = 0;

    /**
     * 监听按钮，在Drawer打开状态下，若不监听，按下返回键则会做两次确认退出处理
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
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    } else {
                        finish();
                    }
                    overridePendingTransition(0, R.anim.out_to_bottom);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
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

    public void openDrawer() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void loadHead(final String url) {
        Glide.with(this)
             .load(TextUtils.isEmpty(url) ? R.drawable.jay : url)
             .asBitmap()
             .centerCrop()
             .into(new SimpleTarget<Bitmap>() {
                 @Override
                 public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                         glideAnimation) {
                     mUserimage.setImageBitmap(resource);
                     Bitmap overlay = BlurImageUtils.blur(mUserimage, 3, 3);
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                         mHeadViewContainer.setBackground(new BitmapDrawable(getResources(),
                                                                             overlay));
                     }
                 }
             });
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
            LogTools.d("onOptionsItemSelected android.R.id.home");
            openDrawer();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionTools.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * RxBus, 主线程, 接收Photo_URL的TAG的String类方法
     * @param url
     */
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {@Tag("Photo_URL")}
    )
    public void onHeadPicSelected(String url) {
        LogTools.i("threadid: " + Thread.currentThread().getId());
        LogTools.w(url);
        SPUtils.put(this, Constants.HEAD_URL, url);
        loadHead(url);
    }
}
