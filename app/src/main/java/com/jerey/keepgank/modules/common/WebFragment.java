package com.jerey.keepgank.modules.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jerey.keepgank.R;
import com.jerey.keepgank.modules.base.BaseFragment;
import com.jerey.keepgank.modules.base.AppSwipeBackActivity;
import com.jerey.keepgank.utils.ApplicationUtils;
import com.jerey.loglib.LogTools;

import butterknife.BindView;

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

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_view)
    android.webkit.WebView mWebView;

    Toolbar mToolBar;
    private String mId;
    private String mTitle;
    private String mURl;
    private String mType;
    private String mWho;

    public static WebFragment newInstance(String id, String title, String url, String type,
                                          String who) {
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
        dynamicAddView(mToolBar, "background", R.color.app_main_color);
        mId = getArguments().getString(DATA_ID);
        mTitle = getArguments().getString(DATA_TITLE);
        mType = getArguments().getString(DATA_TYPE);
        mURl = getArguments().getString(DATA_URL);
        LogTools.d("title:" + mTitle + " url:" + mURl);
        if (!TextUtils.isEmpty(mTitle)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
        }
        initWebView();
        mWebView.loadUrl(mURl);
    }

    /* 根据元素判断是否，含有"View all of README.md"
    var myParagragh = document.getElementsByClassName("bubble-actions");
	console.log("myParagragh length:", myParagragh.length);
	for (var i = myParagragh.length - 1; i >= 0; i--) {
        var a = myParagragh[i].getElementsByClassName("bubble-action");
        if (a.length > 0) {
            console.log("action:", a[0]);
            console.log("action:", a[0].firstChild.data);
            if (a[0].firstChild.data.indexOf("View all of README.md") > 0){
                console.log("含有");
                window.location.href=a[0].href;
            }
        }
    }
    */
    String jsCode = "javascript:\tvar myParagragh = document.getElementsByClassName" +
            "(\"bubble-actions\"); \n" +
            "\tconsole.log(\"myParagragh length:\", myParagragh.length);\n" +
            "\tfor (var i = myParagragh.length - 1; i >= 0; i--) {\n" +
            "\t\tvar a = myParagragh[i].getElementsByClassName(\"bubble-action\");\n" +
            "\t\tif (a.length > 0) {\n" +
            "\t\t\tconsole.log(\"action:\", a[0]);\n" +
            "\t\t\tconsole.log(\"action:\", a[0].firstChild.data);\n" +
            "\t\t\tif (a[0].firstChild.data.indexOf(\"View all of README.md\") > 0){\n" +
            "\t\t\t\t    console.log(\"含有\");\n" +
            "\t\t\t\t    window.location.href=a[0].href;\n" +
            "\t\t\t }\n" +
            "\t\t\n" +
            "\t\t}\n" +
            "\t}";

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl(jsCode);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_web, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy_the_url:
                ClipboardManager c = (ClipboardManager) getActivity().getSystemService
                        (getActivity().CLIPBOARD_SERVICE);
                c.setText(mURl);
                break;
            case R.id.action_open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURl));
                getActivity().startActivity(intent);
                break;
            case R.id.action_share:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_TEXT, mURl);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(Intent.createChooser(intent1, mTitle));
                break;
            case android.R.id.home:
                LogTools.d("onOptionsItemSelected android.R.id.home");
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    ((AppSwipeBackActivity) getActivity()).scrollToFinishActivity();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.w("HTML", html);

        }
    }
}
