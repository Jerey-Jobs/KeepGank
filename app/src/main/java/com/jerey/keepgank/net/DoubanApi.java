package com.jerey.keepgank.net;


import com.jerey.keepgank.GankApp;
import com.jerey.keepgank.douban.bean.MovieInfoBean;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.douban.bean.USBean;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
        File httpCacheDirectory = new File(GankApp.getmCachePath(), "networkcache");
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)                         //retry
                .connectTimeout(15, TimeUnit.SECONDS)                   //timeout 15S
                // .cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024)) // add cache dir
                .addInterceptor(new HttpDiskLruCacheInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        doubanInterface = retrofit.create(DoubanInterface.class);
    }

    public class MOVIE_TYPE {
        public static final String TYPE_TOP250 = "top250";
        public static final String TYPE_CommingSoon = "coming_soon";
        public static final String TYPE_UsMovie = "us_box";
        public static final String TYPE_in_theaters = "in_theaters";
    }

    public interface DoubanInterface {
        @GET("/v2/movie/{type}")
        Observable<TypeInfoBean> getTypeData(@Path("type") String type,
                                             @Query("start") int start,
                                             @Query("count") int count);

        //http://api.douban.com/v2/movie/top250?start=10&count=10
        @GET("/v2/movie/top250")
        Observable<TypeInfoBean> getTop250(@Query("start") int start,
                                           @Query("count") int count);

        /**
         * http://api.douban.com/v2/movie/coming_soon?start=10&count=10
         * 即将上映
         */
        @GET("/v2/movie/coming_soon")
        Observable<TypeInfoBean> getCommingSoon(@Query("start") int start,
                                                @Query("count") int count);

        /**
         * http://api.douban.com/v2/movie/us_box
         * 欧美热映
         */
        @GET("/v2/movie/us_box")
        Observable<USBean> getUSMovie();

        /**
         * http://api.douban.com/v2/movie/in_theaters?start=10&count=10
         * 正在上映的电影
         */
        @GET("/v2/movie/in_theaters")
        Observable<TypeInfoBean> getInTheaters(@Query("start") int start,
                                               @Query("count") int count);

        /**
         * http://api.douban.com/v2/movie/subject/26363254
         * 某个电影信息
         */
        @GET("/v2/movie/subject/{id}")
        Observable<MovieInfoBean> getMonvieInfo(@Path("id") String id);

        /**
         * https://api.douban.com/v2/movie/search?q=content
         * 搜索
         */
        @GET("/v2/movie/search")
        Observable<TypeInfoBean> search(@Query("q") String content);

    }

    public DoubanInterface getDoubanInterface() {
        return doubanInterface;
    }
}
