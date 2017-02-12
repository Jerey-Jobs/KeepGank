package com.jerey.keepgank;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private int mCurrentUIIndex = 0;
    private static final int INDEX_HOME = 0;
    private static final int INDEX_COLLECTION = 1;

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


    private void updateUI(){
        switch (mCurrentUIIndex){
            case INDEX_HOME:
                getSupportFragmentManager().beginTransaction().replace(R.id.content)
                break;
            case INDEX_COLLECTION:
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Logger.i("home被点击");
                item.setChecked(true);
                break;
            case R.id.nav_collection:
                Logger.i("收藏被点击");
                item.setChecked(true);
                break;
            case R.id.nav_settings:
                Logger.i("设置被点击");
                break;
        }
        return false;
    }


    /**
     * 监听按钮，在Drawer打开状态下，若不监听，按下返回键则会退出该界面
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }else{
                finish();
                overridePendingTransition(0, R.anim.out_to_bottom);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
