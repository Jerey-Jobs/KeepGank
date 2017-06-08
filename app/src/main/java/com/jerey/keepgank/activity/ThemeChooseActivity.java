package com.jerey.keepgank.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.loglib.LogTools;
import com.jerey.themelib.SkinLoaderListener;
import com.jerey.themelib.loader.SkinManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiamin on 6/7/17.
 */

public class ThemeChooseActivity extends AppSwipeBackActivity {

    @Bind(R.id.theme_default)
    RadioButton mRadioThemeDefault;
    @Bind(R.id.theme_night)
    RadioButton mRadioThemeNight;
    @Bind(R.id.theme_ocean)
    RadioButton mRadioThemeOcean;
    @Bind(R.id.font_default)
    RadioButton mRadioFontDefault;
    @Bind(R.id.font_wryh)
    RadioButton mRadioFontWryh;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_choose);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        updateUI();

    }

    @OnClick(R.id.theme_default)
    public void onThemeDefaultClicked() {
        updateUIByName(THEME_DEFAULT);
        SkinManager.getInstance().restoreDefaultTheme();
    }

    @OnClick(R.id.theme_night)
    public void onThemeNightClicked() {
        updateUIByName(THEME_NIGHT);
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
                        updateUI();
                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                }
        );

    }

    @OnClick(R.id.theme_ocean)
    public void onThemeOceanClicked() {
        updateUIByName(THEME_OCEAN);
        SkinManager.getInstance().loadSkin("theme-ocean.skin",
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
                        updateUI();
                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                }
        );
    }

    @OnClick(R.id.font_default)
    public void onFontDefaultClicked() {
        SkinManager.getInstance().loadFont("");
    }

    @OnClick(R.id.font_wryh)
    public void onFontWryhClicked() {
        SkinManager.getInstance().loadFont("WRYHZT.ttf");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LogTools.i("android.R.id.home");
                scrollToFinishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        String theme = SkinManager.getInstance().getCurSkinPackageName();
        LogTools.d("skin: " + theme);
        if (theme == null) {
            theme = THEME_DEFAULT;
        }
        updateUIByName(theme);
    }

    private static final String THEME_NIGHT = "com.jerey.theme_night";
    private static final String THEME_OCEAN = "com.jerey.theme_ocean";
    private static final String THEME_DEFAULT = "com.jerey.keepgank";

    private void updateUIByName(String string) {
        mRadioThemeOcean.setChecked(false);
        mRadioThemeDefault.setChecked(false);
        mRadioThemeNight.setChecked(false);
        switch (string) {
            case THEME_NIGHT:
                mRadioThemeNight.setChecked(true);
                break;
            case THEME_OCEAN:
                mRadioThemeOcean.setChecked(true);
                break;
            case THEME_DEFAULT:
                mRadioThemeDefault.setChecked(true);
                break;
        }
    }

    @OnClick({R.id.theme_default_layout, R.id.theme_night_layout, R.id.theme_ocean_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.theme_default_layout:
                onThemeDefaultClicked();
                break;
            case R.id.theme_night_layout:
                onThemeNightClicked();
                break;
            case R.id.theme_ocean_layout:
                onThemeOceanClicked();
                break;
        }


    }
}
