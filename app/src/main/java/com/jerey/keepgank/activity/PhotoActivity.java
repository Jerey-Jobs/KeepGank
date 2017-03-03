package com.jerey.keepgank.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jerey.keepgank.R;
import com.jerey.keepgank.View.PinchImageView;
import com.jerey.keepgank.base.AppSwipeBackActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamin on 2017/3/2.
 */

public class PhotoActivity extends AppSwipeBackActivity{
    private static final String TAG = "PhotoActivity";
    private static final String URL = "URL";

    @Bind(R.id.meizi_image)
    PinchImageView pinchImageView;

    private String mUrl;

    public static void startActivity(Context context, String url){
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(URL,url);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra(URL);
        Log.d(TAG, "url: " + mUrl);
        Glide.with(this)
                .load(mUrl)
                .error(R.drawable.bg_cyan)
                .placeholder(R.drawable.bg_cyan)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pinchImageView);
    }
}
