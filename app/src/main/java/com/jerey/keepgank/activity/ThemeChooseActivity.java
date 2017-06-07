package com.jerey.keepgank.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.themelib.SkinLoaderListener;
import com.jerey.themelib.loader.SkinManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiamin on 6/7/17.
 */

public class ThemeChooseActivity extends AppSwipeBackActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.theme_default)
    RadioButton themeDefault;
    @Bind(R.id.theme_night)
    RadioButton themeNight;
    @Bind(R.id.theme_ocean)
    RadioButton themeOcean;
    @Bind(R.id.font_default)
    RadioButton fontDefault;
    @Bind(R.id.font_wryh)
    RadioButton fontWryh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_choose);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.theme_default)
    public void onThemeDefaultClicked() {
        SkinManager.getInstance().restoreDefaultTheme();
    }

    @OnClick(R.id.theme_night)
    public void onThemeNightClicked() {
        SkinManager.getInstance().loadSkin("theme-night.skin",
                new SkinLoaderListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(getApplicationContext(), "正在切换中", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String errMsg) {
                        Toast.makeText(getApplicationContext(), "切换失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                }
        );

    }

    @OnClick(R.id.theme_ocean)
    public void onThemeOceanClicked() {
    }

    @OnClick(R.id.font_default)
    public void onFontDefaultClicked() {
//        SkinManager.getInstance().restoreDefaultTheme();
    }

    @OnClick(R.id.font_wryh)
    public void onFontWryhClicked() {
        SkinManager.getInstance().loadFont("WRYHZT.ttf");
    }
}
