package com.jerey.keepgank.douban.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.jerey.keepgank.R;
import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.mutitype.ItemViewBinder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xiamin
 * @date 8/17/17.
 */
public class SubjectsBinder extends ItemViewBinder<SubjectsBean, SubjectsBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup
            parent) {
        View view = inflater.inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final SubjectsBean item) {
        if (item == null) return;
        /** 图片，影片名 */
        String imageUrl = null;
        if (item.getImages() != null){
            imageUrl = item.getImages().getMedium();
            if (imageUrl == null){
                imageUrl = item.getImages().getLarge();
            }
        }
        Glide.with(holder.mItemImageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_grey)
                .into(holder.mItemImageView);
        holder.mItemName.setText(item.getTitle());
        if (item.getRating() != null){
            holder.mItemRating.setText("评分：" + item.getRating().getAverage());
        }
        StringBuilder stringBuilder = new StringBuilder("导演：");

        /** 导演 */
        if (item.getDirectors() == null) {
            return;
        }

        for (int i = 0; i < item.getDirectors().size(); i++) {
            if (i == 0) {
                stringBuilder.append(item.getDirectors().get(i).getName());
            } else {
                stringBuilder.append("/" + item.getDirectors().get(i).getName());
            }
        }
        holder.mItemDirector.setText(stringBuilder.toString());
        /** 演员 */
        if (item.getCasts() == null) {
            return;
        }

        StringBuilder actorBuilder = new StringBuilder("主演：");
        for (int i = 0; i < item.getCasts().size(); i++) {
            if (i == 0) {
                actorBuilder.append(item.getCasts().get(i).getName());
            } else {
                actorBuilder.append("/" + item.getCasts().get(i).getName());
            }
        }
        holder.mItemActor.setText(actorBuilder.toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build("/douban/MovieActivity")
                        .withTransition(R.anim.in_from_right, 0)
                        .withString("movieId", item.getId())
                        .navigation(holder.itemView.getContext());
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_imageView)
        ImageView mItemImageView;
        @BindView(R.id.item_name)
        TextView mItemName;
        @BindView(R.id.item_rating)
        TextView mItemRating;
        @BindView(R.id.item_director)
        TextView mItemDirector;
        @BindView(R.id.item_actor)
        TextView mItemActor;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
