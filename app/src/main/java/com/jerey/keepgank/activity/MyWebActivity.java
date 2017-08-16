package com.jerey.keepgank.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jerey.keepgank.R;
import com.jerey.keepgank.base.SingleFragmentActivity;
import com.jerey.keepgank.bean.Result;
import com.jerey.keepgank.fragment.WebFragment;
import com.jerey.keepgank.utils.AnimationHelper;
import com.jerey.loglib.LogTools;
import com.jerey.themelib.loader.SkinManager;

/**
 * Created by Xiamin on 2017/2/25.
 */
@Route(path = "/activity/MyWebActivity")
public class MyWebActivity extends SingleFragmentActivity {
    private static final String TAG = "MyWebActivity";
    private WebFragment mWebFragment;

    @Override
    protected Fragment getFragment() {
        if (mWebFragment == null) {
            mWebFragment = new WebFragment();
        }
        return mWebFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected Bundle getArguments() {
        Intent intent = getIntent();
        String id = intent.getStringExtra(WebFragment.DATA_ID);
        String title = intent.getStringExtra(WebFragment.DATA_TITLE);
        String type = intent.getStringExtra(WebFragment.DATA_TYPE);
        String url = intent.getStringExtra(WebFragment.DATA_URL);
        String who = intent.getStringExtra(WebFragment.DATA_WHO);
        Log.d(TAG, "id: " + id + " title: " + title + " url: " + url);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WebFragment.DATA_ID, id);
        bundle.putSerializable(WebFragment.DATA_TITLE, title);
        bundle.putSerializable(WebFragment.DATA_URL, url);
        bundle.putSerializable(WebFragment.DATA_TYPE, type);
        bundle.putSerializable(WebFragment.DATA_WHO, who);
        return bundle;
    }

    public static void startWebActivity(Context context, Result result) {
        Intent intent = new Intent(context, MyWebActivity.class);
        intent.putExtra(WebFragment.DATA_ID, result.getObjectId());
        intent.putExtra(WebFragment.DATA_TITLE, result.getDesc());
        intent.putExtra(WebFragment.DATA_TYPE, result.getType());
        intent.putExtra(WebFragment.DATA_URL, result.getUrl());
        intent.putExtra(WebFragment.DATA_WHO, result.getWho());
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, 0);
    }

    public static void startWebActivity(Context context, Result result, View view) {
        Intent intent = new Intent(context, MyWebActivity.class);
        intent.putExtra(WebFragment.DATA_ID, result.getObjectId());
        intent.putExtra(WebFragment.DATA_TITLE, result.getDesc());
        intent.putExtra(WebFragment.DATA_TYPE, result.getType());
        intent.putExtra(WebFragment.DATA_URL, result.getUrl());
        intent.putExtra(WebFragment.DATA_WHO, result.getWho());
//        context.startActivity(intent);
        LogTools.d(SkinManager.getInstance().getColor(R.color.colorPrimary));
        AnimationHelper.startActivity((Activity) context
                , intent
                , view
                , R.color.colorPrimary);
    }
}
