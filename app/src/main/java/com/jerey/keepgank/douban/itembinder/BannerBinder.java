package com.jerey.keepgank.douban.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerey.keepgank.douban.bean.BannerBean;
import com.jerey.keepgank.multitype.ItemViewBinder;
import com.youth.banner.Banner;

/**
 * @author xiamin
 * @date 8/18/17.
 */
public class BannerBinder extends ItemViewBinder<BannerBean, BannerBinder.BannerHolder> {

    @NonNull
    @Override
    protected BannerBinder.BannerHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                                           @NonNull ViewGroup parent) {
        return new BannerHolder(new Banner(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull BannerHolder holder, @NonNull BannerBean item) {

    }


    class BannerHolder extends RecyclerView.ViewHolder {
        Banner mBanner;
        public BannerHolder(View itemView) {
            super(itemView);
            mBanner = (Banner) itemView;
        }
    }
}
