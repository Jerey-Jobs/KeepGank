package com.jerey.keepgank.net;

import com.jerey.keepgank.douban.bean.Top250;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * @author xiamin
 * @date 8/17/17.
 */
public class DoubanApi {
    public static final String BASE_URL = "http://api.douban.com";
    public static final int LOAD_LIMIT = 20;

    public static DoubanApi INSTACE;
    private static DoubanInterface doubanInterface;

    public static DoubanApi getInstance() {
        if (INSTACE == null) {
            synchronized (DoubanApi.class) {
                if (INSTACE == null) {
                    INSTACE = new DoubanApi();
                }
            }
        }
        return INSTACE;
    }

    public DoubanApi() {

        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .connectTimeout(15, TimeUnit.SECONDS)//超时时间15S
                .addInterceptor(new HttpInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        doubanInterface = retrofit.create(DoubanInterface.class);
    }

    public interface DoubanInterface {
        //http://api.douban.com/v2/movie/top250?start=10&count=10
        @GET("/v2/movie/top250?start={startcount}&count={num}")
        Observable<Top250> getTop250(@Path("startcount") int start,
                                     @Path("num") int count);

        /**
         * http://api.douban.com/v2/movie/coming_soon?start=10&count=10
         * 即将上映
         */

        /**
         * http://api.douban.com/v2/movie/us_box
         * 欧美热映
         */

        /**
         * http://api.douban.com/v2/movie/in_theaters?start=10&count=10
         * 正在上映的电影
         */

        /**
         * http://api.douban.com/v2/movie/subject/26363254
         * 某个电影信息
         */
    }

    public DoubanInterface getDoubanInterface() {
        return doubanInterface;
    }

}
