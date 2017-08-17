package com.jerey.keepgank.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class HttpInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //打印请求链接
            String TAG_REQUEST = "request";
            Log.e(TAG_REQUEST, "request" + request.url().toString());
            Response response = chain.proceed(request);
            //打印返回的message
            //Log.e(TAG_REQUEST, "response" + response.toString());
            return response;
        }
    }
