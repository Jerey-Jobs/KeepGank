package com.jerey.keepgank.douban.itembinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.jerey.keepgank.R;
import com.jerey.keepgank.douban.bean.BannerBean;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.mutitype.ItemViewBinder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
    protected void onBindViewHolder(@NonNull final BannerHolder holder, @NonNull final BannerBean
            item) {
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
        holder.mItemBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ARouter.getInstance()
                        .build("/douban/MovieActivity")
                        .withTransition(R.anim.in_from_right, 0)
                        .withString("movieId", item.getSubjects().get(position).getId())
                        .navigation(holder.mItemBanner.getContext());
            }
        });
    }


    static class BannerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_banner)
        Banner mItemBanner;

        BannerHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .crossFade()
                    .into(imageView);
        }
    }

}
