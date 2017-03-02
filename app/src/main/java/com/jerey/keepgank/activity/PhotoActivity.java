package com.jerey.keepgank.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jerey.keepgank.R;
import com.jerey.keepgank.base.AppSwipeBackActivity;

/**
 * Created by Xiamin on 2017/3/2.
 */

public class PhotoActivity extends AppSwipeBackActivity{

    public static void startActivity(Context context){
        Intent intent = new Intent(context, PhotoActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
    }
}
