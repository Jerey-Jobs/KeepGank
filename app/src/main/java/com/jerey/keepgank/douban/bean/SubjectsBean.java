package com.jerey.keepgank.douban.bean;

import java.util.List;

/**
 * @author xiamin
 * @date 8/17/17.
 */

public class SubjectsBean {
    /**
     * rating : {"max":10,"average":7.5,"stars":"40","min":0}
     * genres : ["动作"]
     * title : 战狼2
     * casts : [{"alt":"https://movie.douban.com/celebrity/1000525/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/39105.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/39105.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/39105.jpg"},"name":"吴京",
     * "id":"1000525"},{"alt":"https://movie.douban.com/celebrity/1100321/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1415801312.29.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1415801312.29.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1415801312.29.jpg"},
     * "name":"弗兰克·格里罗","id":"1100321"},{"alt":"https://movie.douban.com/celebrity/1274840/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/1401440361.14.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/1401440361.14.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/1401440361.14.jpg"},
     * "name":"吴刚","id":"1274840"}]
     * collect_count : 331481
     * original_title : 战狼2
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1000525/",
     * "avatars":{"small":"http://img3.doubanio.com/img/celebrity/small/39105.jpg",
     * "large":"http://img3.doubanio.com/img/celebrity/large/39105.jpg",
     * "medium":"http://img3.doubanio.com/img/celebrity/medium/39105.jpg"},"name":"吴京",
     * "id":"1000525"}]
     * year : 2017
     * images : {"small":"http://img3.doubanio
     * .com/view/movie_poster_cover/ipst/public/p2485983612.webp",
     * "large":"http://img3.doubanio
     * .com/view/movie_poster_cover/lpst/public/p2485983612.webp",
     * "medium":"http://img3.doubanio.com/view/movie_poster_cover/spst/public/p2485983612.webp"}
     * alt : https://movie.douban.com/subject/26363254/
     * id : 26363254
     */

    private RatingBean rating;
    private String title;
    private int collect_count;
    private String original_title;
    private String subtype;
    private String year;
    private ImagesBean images;
    private String alt;
    private String id;
    private List<String> genres;
    private List<CastsBean> casts;
    private List<DirectorsBean> directors;

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<CastsBean> getCasts() {
        return casts;
    }

    public void setCasts(List<CastsBean> casts) {
        this.casts = casts;
    }

    public List<DirectorsBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorsBean> directors) {
        this.directors = directors;
    }

    public static class RatingBean {
        /**
         * max : 10
         * average : 7.5
         * stars : 40
         * min : 0
         */

        private int max;
        private double average;
        private String stars;
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }

    public static class ImagesBean {
        /**
         * small : http://img3.doubanio.com/view/movie_poster_cover/ipst/public/p2485983612.webp
         * large : http://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2485983612.webp
         * medium : http://img3.doubanio
         * .com/view/movie_poster_cover/spst/public/p2485983612.webp
         */

        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    public static class CastsBean {
        /**
         * alt : https://movie.douban.com/celebrity/1000525/
         * avatars : {"small":"http://img3.doubanio.com/img/celebrity/small/39105.jpg",
         * "large":"http://img3.doubanio.com/img/celebrity/large/39105.jpg",
         * "medium":"http://img3.doubanio.com/img/celebrity/medium/39105.jpg"}
         * name : 吴京
         * id : 1000525
         */

        private String alt;
        private AvatarsBean avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBean getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBean avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBean {
            /**
             * small : http://img3.doubanio.com/img/celebrity/small/39105.jpg
             * large : http://img3.doubanio.com/img/celebrity/large/39105.jpg
             * medium : http://img3.doubanio.com/img/celebrity/medium/39105.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }

    public static class DirectorsBean {
        /**
         * alt : https://movie.douban.com/celebrity/1000525/
         * avatars : {"small":"http://img3.doubanio.com/img/celebrity/small/39105.jpg",
         * "large":"http://img3.doubanio.com/img/celebrity/large/39105.jpg",
         * "medium":"http://img3.doubanio.com/img/celebrity/medium/39105.jpg"}
         * name : 吴京
         * id : 1000525
         */

        private String alt;
        private AvatarsBeanX avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public AvatarsBeanX getAvatars() {
            return avatars;
        }

        public void setAvatars(AvatarsBeanX avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class AvatarsBeanX {
            /**
             * small : http://img3.doubanio.com/img/celebrity/small/39105.jpg
             * large : http://img3.doubanio.com/img/celebrity/large/39105.jpg
             * medium : http://img3.doubanio.com/img/celebrity/medium/39105.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }
    }
}

