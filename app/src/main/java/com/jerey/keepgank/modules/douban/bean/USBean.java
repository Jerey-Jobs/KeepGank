package com.jerey.keepgank.modules.douban.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Xiamin
 * @date 2017/8/19
 */
public class USBean {

    /**
     * date : 8月11日 - 8月13日
     * subjects : [{"box":35040000,"new":true,"rank":1,"subject":{"rating":{"max":10,
     * "average":6.9,"stars":"35","min":0},"genres":["悬疑","惊悚","恐怖"],"title":"安娜贝尔2：诞生",
     * "casts":[{"alt":"https://movie.douban.com/celebrity/1348677/",
     * "avatars":{"small":"http://img7.doubanio
     * .com/img/celebrity/small/65ElerYkknAcel_avatar_uploaded1426423408.11.jpg",
     * "large":"http://img7.doubanio
     * .com/img/celebrity/large/65ElerYkknAcel_avatar_uploaded1426423408.11.jpg",
     * "medium":"http://img7.doubanio
     * .com/img/celebrity/medium/65ElerYkknAcel_avatar_uploaded1426423408.11.jpg"},
     * "name":"斯黛芬妮·西格曼","id":"1348677"},{"alt":"https://movie.douban.com/celebrity/1354273/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1453796323.41.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1453796323.41.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1453796323.41.jpg"},
     * "name":"特丽莎·贝特曼","id":"1354273"},{"alt":"https://movie.douban.com/celebrity/1359629/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1477291682.77.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1477291682.77.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1477291682.77.jpg"},
     * "name":"露露·威尔逊","id":"1359629"}],"collect_count":1059,"original_title":"Annabelle:
     * Creation","subtype":"movie","directors":[{"alt":"https://movie.douban
     * .com/celebrity/1354769/","avatars":{"small":"http://img3.doubanio
     * .com/img/celebrity/small/1455853108.97.jpg","large":"http://img3.doubanio
     * .com/img/celebrity/large/1455853108.97.jpg","medium":"http://img3.doubanio
     * .com/img/celebrity/medium/1455853108.97.jpg"},"name":"大卫·F·桑德伯格","id":"1354769"}],
     * "year":"2017","images":{"small":"http://img3.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2473041989.webp","large":"http://img3.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2473041989.webp","medium":"http://img3.doubanio
     * .com/view/movie_poster_cover/spst/public/p2473041989.webp"},"alt":"https://movie.douban
     * .com/subject/26644205/","id":"26644205"}},{"box":11405000,"new":false,"rank":2,
     * "subject":{"rating":{"max":10,"average":8.3,"stars":"45","min":0},"genres":["剧情","历史",
     * "战争"],"title":"敦刻尔克","casts":[{"alt":"https://movie.douban.com/celebrity/1355522/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1494157438.59.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1494157438.59.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1494157438.59.jpg"},
     * "name":"菲恩·怀特海德","id":"1355522"},{"alt":"https://movie.douban.com/celebrity/1376362/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1499056832.63.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1499056832.63.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1499056832.63.jpg"},
     * "name":"汤姆·格林-卡尼","id":"1376362"},{"alt":"https://movie.douban.com/celebrity/1344553/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1501901049.22.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1501901049.22.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1501901049.22.jpg"},
     * "name":"杰克·劳登","id":"1344553"}],"collect_count":14301,"original_title":"Dunkirk",
     * "subtype":"movie","directors":[{"alt":"https://movie.douban.com/celebrity/1054524/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/673.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/673.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/673.jpg"},"name":"克里斯托弗·诺兰",
     * "id":"1054524"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2494950714.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2494950714.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2494950714.webp"},"alt":"https://movie.douban
     * .com/subject/26607693/","id":"26607693"}},{"box":8934748,"new":true,"rank":3,
     * "subject":{"rating":{"max":10,"average":0,"stars":"00","min":0},"genres":["喜剧","动画","冒险"],
     * "title":"抢劫坚果店2","casts":[{"alt":"https://movie.douban.com/celebrity/1044709/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/7197.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/7197.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/7197.jpg"},"name":"威尔·阿奈特",
     * "id":"1044709"},{"alt":"https://movie.douban.com/celebrity/1008068/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/2685.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/2685.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/2685.jpg"},"name":"凯瑟琳·海格尔",
     * "id":"1008068"},{"alt":"https://movie.douban.com/celebrity/1025183/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/7974.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/7974.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/7974.jpg"},"name":"玛娅·鲁道夫",
     * "id":"1025183"}],"collect_count":29,"original_title":"The Nut Job 2: Nutty by Nature",
     * "subtype":"movie","directors":[{"alt":"https://movie.douban.com/celebrity/1337216/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1500206136.19.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1500206136.19.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1500206136.19.jpg"},
     * "name":"卡尔·布伦克尔","id":"1337216"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2493285332.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2493285332.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2493285332.webp"},"alt":"https://movie.douban
     * .com/subject/25817617/","id":"25817617"}},{"box":7875000,"new":false,"rank":4,
     * "subject":{"rating":{"max":10,"average":5.5,"stars":"30","min":0},"genres":["动作","奇幻",
     * "冒险"],"title":"黑暗塔","casts":[{"alt":"https://movie.douban.com/celebrity/1036354/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/4568.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/4568.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/4568.jpg"},"name":"凯瑟琳·温妮克",
     * "id":"1036354"},{"alt":"https://movie.douban.com/celebrity/1049501/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1410696282.74.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1410696282.74.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1410696282.74.jpg"},
     * "name":"伊德里斯·艾尔巴","id":"1049501"},{"alt":"https://movie.douban.com/celebrity/1040511/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1392653727.04.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1392653727.04.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1392653727.04.jpg"},
     * "name":"马修·麦康纳","id":"1040511"}],"collect_count":336,"original_title":"The Dark Tower",
     * "subtype":"movie","directors":[{"alt":"https://movie.douban.com/celebrity/1171954/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1456968596.89.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1456968596.89.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1456968596.89.jpg"},
     * "name":"尼科莱·阿尔赛","id":"1171954"}],"year":"2017","images":{"small":"http://img3.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2480303169.webp","large":"http://img3.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2480303169.webp","medium":"http://img3.doubanio
     * .com/view/movie_poster_cover/spst/public/p2480303169.webp"},"alt":"https://movie.douban
     * .com/subject/4943938/","id":"4943938"}},{"box":6605000,"new":false,"rank":5,
     * "subject":{"rating":{"max":10,"average":5.3,"stars":"30","min":0},"genres":["喜剧","动画",
     * "冒险"],"title":"表情奇幻冒险","casts":[{"alt":"https://movie.douban.com/celebrity/1248294/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1413043156.4.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1413043156.4.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1413043156.4.jpg"},
     * "name":"T·J·米勒","id":"1248294"},{"alt":"https://movie.douban.com/celebrity/1017966/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1449532609.88.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1449532609.88.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1449532609.88.jpg"},
     * "name":"詹姆斯·柯登","id":"1017966"},{"alt":"https://movie.douban.com/celebrity/1041008/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/445.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/445.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/445.jpg"},"name":"安娜·法瑞丝",
     * "id":"1041008"}],"collect_count":178,"original_title":"Emoji Movie: Express Yourself",
     * "subtype":"movie","directors":[{"alt":"https://movie.douban.com/celebrity/1001046/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/19875.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/19875.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/19875.jpg"},"name":"安东尼·莱昂迪斯",
     * "id":"1001046"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2459287552.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2459287552.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2459287552.webp"},"alt":"https://movie.douban
     * .com/subject/26577354/","id":"26577354"}},{"box":6520500,"new":false,"rank":6,
     * "subject":{"rating":{"max":10,"average":7.4,"stars":"40","min":0},"genres":["喜剧"],
     * "title":"嗨翻姐妹行","casts":[{"alt":"https://movie.douban.com/celebrity/1036471/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/16185.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/16185.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/16185.jpg"},"name":"雷吉娜·赫尔",
     * "id":"1036471"},{"alt":"https://movie.douban.com/celebrity/1031870/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/5303.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/5303.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/5303.jpg"},"name":"奎恩·拉提法",
     * "id":"1031870"},{"alt":"https://movie.douban.com/celebrity/1054154/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/7218.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/7218.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/7218.jpg"},"name":"贾达·萍克·史密斯",
     * "id":"1054154"}],"collect_count":86,"original_title":"Girls Trip","subtype":"movie",
     * "directors":[{"alt":"https://movie.douban.com/celebrity/1014323/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1371127231.59.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1371127231.59.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1371127231.59.jpg"},
     * "name":"马尔科姆·D·李","id":"1014323"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2426954322.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2426954322.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2426954322.webp"},"alt":"https://movie.douban
     * .com/subject/26964234/","id":"26964234"}},{"box":6100000,"new":false,"rank":7,
     * "subject":{"rating":{"max":10,"average":7.8,"stars":"40","min":0},"genres":["动作","科幻",
     * "冒险"],"title":"蜘蛛侠：英雄归来","casts":[{"alt":"https://movie.douban.com/celebrity/1325351/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1467942867.09.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1467942867.09.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1467942867.09.jpg"},
     * "name":"汤姆·霍兰德","id":"1325351"},{"alt":"https://movie.douban.com/celebrity/1016681/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/56339.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/56339.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/56339.jpg"},"name":"小罗伯特·唐尼",
     * "id":"1016681"},{"alt":"https://movie.douban.com/celebrity/1047974/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/231.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/231.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/231.jpg"},"name":"玛丽莎·托梅",
     * "id":"1047974"}],"collect_count":8371,"original_title":"Spider-Man: Homecoming",
     * "subtype":"movie","directors":[{"alt":"https://movie.douban.com/celebrity/1350194/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1435142487.62.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1435142487.62.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1435142487.62.jpg"},"name":"乔·沃茨",
     * "id":"1350194"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2494135894.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2494135894.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2494135894.webp"},"alt":"https://movie.douban
     * .com/subject/24753477/","id":"24753477"}},{"box":5225000,"new":false,"rank":8,
     * "subject":{"rating":{"max":10,"average":5.7,"stars":"30","min":0},"genres":["动作","惊悚"],
     * "title":"绑架","casts":[{"alt":"https://movie.douban.com/celebrity/1054415/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/51174.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/51174.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/51174.jpg"},"name":"哈莉·贝瑞",
     * "id":"1054415"},{"alt":"https://movie.douban.com/celebrity/1360781/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1472006667.67.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1472006667.67.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1472006667.67.jpg"},
     * "name":"塞奇·科雷亚","id":"1360781"},{"alt":"https://movie.douban.com/celebrity/1124076/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1502098135.51.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1502098135.51.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1502098135.51.jpg"},
     * "name":"克里斯·麦金","id":"1124076"}],"collect_count":221,"original_title":"Kidnap",
     * "subtype":"movie","directors":[{"alt":"https://movie.douban.com/celebrity/1345618/",
     * "avatars":{"small":"http://img7.doubanio
     * .com/img/celebrity/small/ZzMt3ZfYpaYcel_avatar_uploaded1418815628.53.jpg",
     * "large":"http://img7.doubanio
     * .com/img/celebrity/large/ZzMt3ZfYpaYcel_avatar_uploaded1418815628.53.jpg",
     * "medium":"http://img7.doubanio
     * .com/img/celebrity/medium/ZzMt3ZfYpaYcel_avatar_uploaded1418815628.53.jpg"},"name":"
     * 路易斯·普瑞托","id":"1345618"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2460612380.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2460612380.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2460612380.webp"},"alt":"https://movie.douban
     * .com/subject/3808583/","id":"3808583"}},{"box":4875000,"new":true,"rank":9,
     * "subject":{"rating":{"max":10,"average":0,"stars":"00","min":0},"genres":["剧情","传记"],
     * "title":"玻璃城堡","casts":[{"alt":"https://movie.douban.com/celebrity/1027194/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1392029372.12.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1392029372.12.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1392029372.12.jpg"},
     * "name":"布丽·拉尔森","id":"1027194"},{"alt":"https://movie.douban.com/celebrity/1053560/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/501.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/501.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/501.jpg"},"name":"伍迪·哈里森",
     * "id":"1053560"},{"alt":"https://movie.douban.com/celebrity/1016675/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/24419.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/24419.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/24419.jpg"},"name":"娜奥米·沃茨",
     * "id":"1016675"}],"collect_count":34,"original_title":"The Glass Castle","subtype":"movie",
     * "directors":[{"alt":"https://movie.douban.com/celebrity/1310608/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1390555643.59.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1390555643.59.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1390555643.59.jpg"},
     * "name":"德斯汀·克里顿","id":"1310608"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2494581252.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2494581252.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2494581252.webp"},"alt":"https://movie.douban
     * .com/subject/10743963/","id":"10743963"}},{"box":4572350,"new":false,"rank":10,
     * "subject":{"rating":{"max":10,"average":7.4,"stars":"40","min":0},"genres":["动作","悬疑",
     * "惊悚"],"title":"极寒之城","casts":[{"alt":"https://movie.douban.com/celebrity/1018991/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/44470.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/44470.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/44470.jpg"},"name":"查理兹·塞隆",
     * "id":"1018991"},{"alt":"https://movie.douban.com/celebrity/1006958/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/93.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/93.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/93.jpg"},"name":"詹姆斯·麦卡沃伊",
     * "id":"1006958"},{"alt":"https://movie.douban.com/celebrity/1291072/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/21349.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/21349.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/21349.jpg"},"name":"埃迪·马森",
     * "id":"1291072"}],"collect_count":1636,"original_title":"Atomic Blonde","subtype":"movie",
     * "directors":[{"alt":"https://movie.douban.com/celebrity/1289765/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1416221591.26.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1416221591.26.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1416221591.26.jpg"},
     * "name":"大卫·里奇","id":"1289765"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2462070640.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2462070640.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2462070640.webp"},"alt":"https://movie.douban
     * .com/subject/26386034/","id":"26386034"}},{"box":3700000,"new":false,"rank":11,
     * "subject":{"rating":{"max":10,"average":7.2,"stars":"40","min":0},"genres":["剧情","动作",
     * "科幻"],"title":"猩球崛起3：终极之战","casts":[{"alt":"https://movie.douban.com/celebrity/1002708/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/1375081883.31.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/1375081883.31.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/1375081883.31.jpg"},
     * "name":"安迪·瑟金斯","id":"1002708"},{"alt":"https://movie.douban.com/celebrity/1053560/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/501.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/501.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/501.jpg"},"name":"伍迪·哈里森",
     * "id":"1053560"},{"alt":"https://movie.douban.com/celebrity/1035639/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/7723.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/7723.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/7723.jpg"},"name":"史蒂夫·茨恩",
     * "id":"1035639"}],"collect_count":4030,"original_title":"War for the Planet of the Apes",
     * "subtype":"movie","directors":[{"alt":"https://movie.douban.com/celebrity/1045032/",
     * "avatars":{"small":"http://img7.doubanio.com/img/celebrity/small/18161.jpg",
     * "large":"http://img7.doubanio.com/img/celebrity/large/18161.jpg",
     * "medium":"http://img7.doubanio.com/img/celebrity/medium/18161.jpg"},"name":"马特·里夫斯",
     * "id":"1045032"}],"year":"2017","images":{"small":"http://img7.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2494093630.webp","large":"http://img7.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2494093630.webp","medium":"http://img7.doubanio
     * .com/view/movie_poster_cover/spst/public/p2494093630.webp"},"alt":"https://movie.douban
     * .com/subject/25808075/","id":"25808075"}}]
     * title : 豆瓣电影北美票房榜
     */

    private String date;
    private String title;
    private List<SBean> subjects;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SBean> subjects) {
        this.subjects = subjects;
    }

    public static class SBean {
        /**
         * box : 35040000
         * new : true
         * rank : 1
         * subject : {"rating":{"max":10,"average":6.9,"stars":"35","min":0},"genres":["悬疑","惊悚",
         * "恐怖"],"title":"安娜贝尔2：诞生","casts":[{"alt":"https://movie.douban
         * .com/celebrity/1348677/","avatars":{"small":"http://img7.doubanio
         * .com/img/celebrity/small/65ElerYkknAcel_avatar_uploaded1426423408.11.jpg",
         * "large":"http://img7.doubanio
         * .com/img/celebrity/large/65ElerYkknAcel_avatar_uploaded1426423408.11.jpg",
         * "medium":"http://img7.doubanio
         * .com/img/celebrity/medium/65ElerYkknAcel_avatar_uploaded1426423408.11.jpg"},
         * "name":"斯黛芬妮·西格曼","id":"1348677"},{"alt":"https://movie.douban
         * .com/celebrity/1354273/","avatars":{"small":"http://img7.doubanio
         * .com/img/celebrity/small/1453796323.41.jpg","large":"http://img7.doubanio
         * .com/img/celebrity/large/1453796323.41.jpg","medium":"http://img7.doubanio
         * .com/img/celebrity/medium/1453796323.41.jpg"},"name":"特丽莎·贝特曼","id":"1354273"},
         * {"alt":"https://movie.douban.com/celebrity/1359629/",
         * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1477291682.77.jpg",
         * "large":"http://img3.doubanio.com/img/celebrity/large/1477291682.77.jpg",
         * "medium":"http://img3.doubanio.com/img/celebrity/medium/1477291682.77.jpg"},
         * "name":"露露·威尔逊","id":"1359629"}],"collect_count":1059,"original_title":"Annabelle:
         * Creation","subtype":"movie","directors":[{"alt":"https://movie.douban
         * .com/celebrity/1354769/","avatars":{"small":"http://img3.doubanio
         * .com/img/celebrity/small/1455853108.97.jpg","large":"http://img3.doubanio
         * .com/img/celebrity/large/1455853108.97.jpg","medium":"http://img3.doubanio
         * .com/img/celebrity/medium/1455853108.97.jpg"},"name":"大卫·F·桑德伯格","id":"1354769"}],
         * "year":"2017","images":{"small":"http://img3.doubanio
         * .com/view/movie_poster_cover/ipst/public/p2473041989.webp",
         * "large":"http://img3.doubanio
         * .com/view/movie_poster_cover/lpst/public/p2473041989.webp",
         * "medium":"http://img3.doubanio
         * .com/view/movie_poster_cover/spst/public/p2473041989.webp"},"alt":"https://movie
         * .douban.com/subject/26644205/","id":"26644205"}
         */

        private int box;
        @SerializedName("new")
        private boolean newX;
        private int rank;
        private SubjectsBean subject;

        public int getBox() {
            return box;
        }

        public void setBox(int box) {
            this.box = box;
        }

        public boolean isNewX() {
            return newX;
        }

        public void setNewX(boolean newX) {
            this.newX = newX;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public SubjectsBean getSubject() {
            return subject;
        }

        public void setSubject(SubjectsBean subject) {
            this.subject = subject;
        }

    }
}
