package com.jerey.keepgank.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jerey.keepgank.R;
import com.jerey.keepgank.utils.ApplicationUtils;
import com.jerey.loglib.LogTools;

import butterknife.Bind;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class WebView extends BaseFragment {
    private static final String BLOG = "http://jerey.cn";

    @Bind(R.id.web_view)
    android.webkit.WebView mWebView;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected int returnLayoutID() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initWebView();
        mWebView.loadUrl(BLOG);
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
        /**
         * 根据网络状态,设置浏览器的缓存策略
         */
        if (ApplicationUtils.isNetworkAvailable(getContext())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
            LogTools.w("无网络,加载缓存网页");
            showSnackbar("无网络,加载缓存网页");
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(true);
    }
}
