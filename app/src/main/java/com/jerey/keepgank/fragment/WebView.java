package com.jerey.keepgank.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jerey.keepgank.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class WebView extends BaseFragment {
    private static final String BLOG = "http://jerey.cn/portfolio/";

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
        ButterKnife.bind(this, mContainView);
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
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(true);
    }
}
