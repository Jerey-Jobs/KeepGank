### KeepGank
一款Gank客户端,希望大家保持学习的动力

RxJava+Retrofit+DiskLruCache+Material Design

目前功能有:

- 主页为Gank分类<br>
- 每日Gank<br>
- 网页缓存(我的博客)<br>
- Disk数据缓存，避免下次打开重新加载
- 图片大图浏览效果
- 内置浏览器
- 瀑布流妹子


![](/pic/pic4.jpg)
![](/pic/pic8.jpg)
![](/pic/pic10.jpg)


目前用到的依赖有：

``` gradle
    //Google的json解析库
    compile 'com.google.code.gson:gson:2.2.4'
    //一个滑动返回库
    compile 'me.imid.swipebacklayout.lib:library:1.0.0'
    //注解绑定
    compile 'com.jakewharton:butterknife:7.0.1'
    //日志库
    compile 'com.orhanobut:logger:1.3'
    //图片加载库
    compile 'com.github.bumptech.glide:glide:3.5.2'

    compile 'io.reactivex:rxandroid:0.24.0'

    //严格控制由于发布了一个订阅后，由于没有及时取消，导致Activity/Fragment无法销毁导致的内存泄露的库
    //http://www.jianshu.com/p/a3ad9dd20655
    compile 'com.trello:rxlifecycle:0.3.0'
    compile 'com.trello:rxlifecycle-components:0.3.0'

    //rmlite、greenDao、SugarORM等等，这些orm框架基本都是基于sqlite的。
    //Realm，是用来替代sqlite的一种解决方案，它有一套自己的数据库存储引擎，比sqlite更轻量级，拥有更快的速度，最重要的是跨平台
    compile 'io.realm:realm-android:0.87.0'

    //添加retrofit
    compile 'com.squareup.retrofit2:retrofit:2.0.0-beta4'
    compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'

    //Time
    compile 'joda-time:joda-time:2.8.2'
 ```
