package com.jerey.keepgank.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.jerey.keepgank.R;

import butterknife.Bind;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class WebFragment extends BaseFragment {
    private static final String TAG = "WebFragment";
    public static final String DATA_ID = "data_id";
    public static final String DATA_TITLE = "data_title";
    public static final String DATA_URL = "data_url";
    public static final String DATA_TYPE = "data_type";
    public static final String DATA_WHO = "data_who";

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.web_view)
    android.webkit.WebView mWebView;

    private String mId;
    private String mTitle;
    private String mURl;
    private String mType;
    private String mWho;
    public static WebFragment newInstance(String id, String title, String url, String type, String who) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_ID, id);
        bundle.putSerializable(DATA_TITLE, title);
        bundle.putSerializable(DATA_URL, url);
        bundle.putSerializable(DATA_TYPE, type);
        bundle.putSerializable(DATA_WHO, who);
        WebFragment Fragment = new WebFragment();
        Fragment.setArguments(bundle);
        return Fragment;
    }


    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_web;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            Log.d(TAG,"afterCreate have savedInstanceState ");
            mId = getArguments().getString(DATA_ID);
            mTitle = getArguments().getString(DATA_TITLE);
            mType = getArguments().getString(DATA_TYPE);
            mURl = getArguments().getString(DATA_URL);
            mWho = getArguments().getString(DATA_WHO);
        }

        mWebView.getSettings().setJavaScriptEnabled(true);

    }
}
