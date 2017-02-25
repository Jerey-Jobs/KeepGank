package com.jerey.keepgank.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

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

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.web_view)
    android.webkit.WebView mWebView;

    Toolbar mToolBar;
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
        Log.d(TAG, "afterCreate");
        mToolBar = (Toolbar) mContainView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        mId = getArguments().getString(DATA_ID);
        mTitle = getArguments().getString(DATA_TITLE);
        mType = getArguments().getString(DATA_TYPE);
        mURl = getArguments().getString(DATA_URL);
        initWebView();
        mWebView.loadUrl(mURl);
    }

    private void initWebView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                        mProgressBar.setProgress(0);
                    }
                } else {
                    // 加载中
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.setProgress(newProgress);
                    }
                }
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(true);
    }
}
