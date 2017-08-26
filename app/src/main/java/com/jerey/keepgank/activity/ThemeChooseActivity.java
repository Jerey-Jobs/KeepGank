package com.jerey.keepgank.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.jerey.keepgank.R;
import com.jerey.keepgank.view.GlideRoundTransform;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.utils.SPUtils;
import com.jerey.loglib.LogTools;
import com.jerey.themelib.SkinLoaderListener;
import com.jerey.themelib.loader.SkinManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiamin on 6/7/17.
 */
@Route(path = "/activity/ThemeChooseActivity")
public class ThemeChooseActivity extends AppSwipeBackActivity {
    public static final String FONT_STRING = "current-font";
    public static final String FONT_DEFAULT = "font_default";

    @BindView(R.id.theme_default)
    RadioButton mRadioThemeDefault;
    @BindView(R.id.theme_night)
    RadioButton mRadioThemeNight;
    @BindView(R.id.theme_ocean)
    RadioButton mRadioThemeOcean;
    @BindView(R.id.font_default)
    RadioButton mRadioFontDefault;
    @BindView(R.id.font_wryh)
    RadioButton mRadioFontWryh;
    @BindView(R.id.theme_default_img)
    ImageView mThemeDefaultImg;
    @BindView(R.id.theme_night_img)
    ImageView mThemeNightImg;
    @BindView(R.id.theme_ocean_img)
    ImageView mThemeOceanImg;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_choose);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Glide.with(this)
                .load(R.drawable.theme_defalut)
                .transform(new GlideRoundTransform(this, 5))
                .into(mThemeDefaultImg);
        Glide.with(this)
                .load(R.drawable.theme_dark)
                .transform(new GlideRoundTransform(this, 5))
                .into(mThemeNightImg);
        Glide.with(this)
                .load(R.drawable.theme_ocean)
                .transform(new GlideRoundTransform(this, 5))
                .into(mThemeOceanImg);
        updateUI();
        if (SPUtils.get(this, FONT_DEFAULT, true)) {
            mRadioFontDefault.setChecked(true);
            mRadioFontWryh.setChecked(false);
        } else {
            mRadioFontDefault.setChecked(false);
            mRadioFontWryh.setChecked(true);
        }
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
        SPUtils.put(this, FONT_DEFAULT, true);
        mRadioFontWryh.setChecked(false);
        mRadioFontDefault.setChecked(true);
    }

    @OnClick(R.id.font_wryh)
    public void onFontWryhClicked() {
        SkinManager.getInstance().loadFont("WRYHZT.ttf");
        SPUtils.put(this, FONT_DEFAULT, false);
        mRadioFontWryh.setChecked(true);
        mRadioFontDefault.setChecked(false);
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
