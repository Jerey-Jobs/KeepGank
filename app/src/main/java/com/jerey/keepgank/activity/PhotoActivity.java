package com.jerey.keepgank.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jerey.keepgank.R;
import com.jerey.keepgank.View.PinchImageView;
import com.jerey.keepgank.base.AppSwipeBackActivity;
import com.jerey.keepgank.utils.ImageSave;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamin on 2017/3/2.
 */

public class PhotoActivity extends AppSwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "PhotoActivity";
    private static final String URL = "URL";

    @Bind(R.id.meizi_image)
    PinchImageView pinchImageView;
    @Bind(R.id.btn_back)
    ImageView mBtnBack;
    @Bind(R.id.btn_save)
    ImageView mBtnSave;

    private String mUrl;
    private Bitmap mBitmap;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mBtnBack.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(URL);
        Log.d(TAG, "url: " + mUrl);
        Glide.with(this)
                .load(mUrl)
                .asBitmap()
                .error(R.drawable.bg_cyan)
                .placeholder(R.drawable.bg_cyan)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        pinchImageView.setImageBitmap(resource);
                        mBitmap = resource;
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                Logger.d("点击back,结束Activity");
                finish();
                break;
            case R.id.btn_save:
                Logger.d("点击保存,保存图片");
                for(int i = 0; i < 20; i++) {
                    Toast.makeText(this, "保存图片", Toast.LENGTH_LONG).show();
                    ImageSave.with(getApplicationContext())
                            .save(mBitmap,mBitmap)
                            .setImageSaveListener(new ImageSave.ImageSaveListener() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
                break;
        }
    }
}
