## KeepGank

一款Gank客户端,希望大家保持学习的动力

---

## 主体

RxJava + Retrofit + Glide + DiskLruCache + Material-Design

## 缘由

本着学习MD军火库的目的, 外加练习一下使用第三方框架, 毕竟在公司很少使用第三方的框架. 于是写了个Gank.io客户端. 本打算写一个笑话大全类的客户端, 想着代码家大神比那些第三方网站靠谱的多. 其实都一样. 不如写Gank.io了


## 仓库 (希望大家多多Star)
[https://github.com/Jerey-Jobs/KeepGank](https://github.com/Jerey-Jobs/KeepGank)

## APK下载

[App下载链接](http://www.coolapk.com/apk/com.jerey.keepgank)


版本记录
---
#### 1.0
1. 主页为Gank分类
2. 每日Gank
3. 我的博客)
4. Disk数据缓存，避免下次打开时无网络, 加载老数据
5. 图片大图浏览效果
6. 图片保存
7. 瀑布流妹子浏览
8. 内容分享
9. 夜间模式

#### 1.1
1. 增加揭面水波动画

#### 1.2
1. 增加多主题框架，并内置了两个主题包
2. 增加字体选择，应用内支持微软雅黑
3. 启动模式不再白屏
4. 主题切换不再有过渡动画

#### 1.3
1. 增加头像选择功能， 滑动浏览
2. 使用多类型扩展Adapter增加豆瓣电影界面

### 预览
---
<a><img src="pic/pic0.jpg" width="32%"/></a>
<a><img src="pic/pic1.jpg" width="32%"/></a>
<a><img src="pic/pic3.jpg" width="32%"/></a>
<a><img src="pic/pic5.jpg" width="32%"/></a>
<a><img src="pic/pic11.jpg" width="32%"/></a>
<a><img src="pic/pic4.jpg" width="32%"/></a>
<a><img src="pic/pic6.jpg" width="32%"/></a>
<a><img src="pic/pic7.jpg" width="32%"/></a>
<a><img src="pic/pic8.jpg" width="32%"/></a>
<a><img src="pic/pic9.jpg" width="32%"/></a>
<a><img src="pic/pic2.jpg" width="32%"/></a>
<a><img src="pic/pic10.jpg" width="32%"/></a>


### 头像选择
<a><img src="pic/choose_head.gif" width="32%"/></a>

### 总之, 希望大家多多star, 一起进步

ps: 后续我可能还是会加上笑话大全功能


----------
本文作者：Anderson/Jerey_Jobs

博客地址   ： [夏敏的博客/Anderson大码渣/Jerey_Jobs][1] <br>
简书地址   :  [Anderson大码渣][2] <br>
github地址 :  [Jerey_Jobs][3]



[1]: http://jerey.cn/
[2]: http://www.jianshu.com/users/016a5ba708a0/latest_articles
[3]: https://github.com/Jerey-Jobs


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
