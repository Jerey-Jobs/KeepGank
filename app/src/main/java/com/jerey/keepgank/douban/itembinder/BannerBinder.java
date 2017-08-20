package com.jerey.keepgank.douban.itembinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jerey.keepgank.R;
import com.jerey.keepgank.douban.bean.BannerBean;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.multitype.ItemViewBinder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author xiamin
 * @date 8/18/17.
 */
public class BannerBinder extends ItemViewBinder<BannerBean, BannerBinder.BannerHolder> {

    @NonNull
    @Override
    protected BannerHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                              @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_banner, parent, false);
        return new BannerHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull BannerHolder holder, @NonNull BannerBean item) {
        List<SubjectsBean> subjects = item.getSubjects();
        List<String> imageUrls = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (SubjectsBean s : subjects) {
            imageUrls.add(s.getImages().getLarge());
            titles.add(s.getTitle());
        }

        holder.mItemBanner.setImages(imageUrls)
                .setBannerTitles(titles)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .start();
    }


    static class BannerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_banner)
        Banner mItemBanner;

        BannerHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .crossFade()
                    .into(imageView);
        }
    }

}
