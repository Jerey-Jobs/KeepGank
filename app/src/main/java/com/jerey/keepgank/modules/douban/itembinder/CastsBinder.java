package com.jerey.keepgank.modules.douban.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.jerey.keepgank.R;
import com.jerey.keepgank.modules.douban.bean.SubjectsBean;
import com.jerey.mutitype.ItemViewBinder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Xiamin
 * @date 2017/9/3
 */
public class CastsBinder extends ItemViewBinder<List<SubjectsBean.CastsBean>, CastsBinder
        .ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup
            parent) {
        View root = inflater.inflate(R.layout.item_douban_typeinfo, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull List<SubjectsBean
            .CastsBean> item) {
        holder.mTextView.setText("影人");
        holder.mRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.mRecyclerView.getContext(), LinearLayoutManager
                        .HORIZONTAL, false));
        holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        holder.mRecyclerView.setNestedScrollingEnabled(false);
        CastsAdapter adapter = new CastsAdapter(item);
        holder.mRecyclerView.setAdapter(adapter);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        RecyclerView mRecyclerView;
        ImageView mImageViewMore;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_title);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recyclerView);
            mImageViewMore = (ImageView) itemView.findViewById(R.id.iv_more);
            mImageViewMore.setVisibility(View.GONE);
        }
    }

    class CastsAdapter extends RecyclerView.Adapter<CastsAdapter.CastViewHolder> {
        List<SubjectsBean.CastsBean> dataList;

        public CastsAdapter(List<SubjectsBean.CastsBean> itemList) {
            this.dataList = itemList;
        }

        @Override
        public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_cast, parent, false);
            return new CastViewHolder(root);
        }

        @Override
        public void onBindViewHolder(final CastViewHolder holder, final int position) {
            String imgUrl = null;
            final SubjectsBean.CastsBean castsBean = dataList.get(position);
            if (castsBean.getAvatars() != null) {
                imgUrl = castsBean.getAvatars().getMedium();
                if (imgUrl == null) {
                    imgUrl = castsBean.getAvatars().getLarge();
                }
                if (imgUrl == null) {
                    imgUrl = castsBean.getAvatars().getSmall();
                }
            }
            Glide.with(holder.itemView.getContext())
                 .load(imgUrl)
                 .centerCrop()
                 .placeholder(R.drawable.bg_grey)
                 .into(holder.mItemImageView);
            holder.mItemName.setText(castsBean.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ARouter.getInstance()
                           .build("/activity/MyWebActivity")
                           .withString("title", castsBean.getName())
                           .withString("url", castsBean.getAlt())
                           .withTransition(R.anim.in_from_right, 0)
                           .navigation(holder.itemView.getContext());
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class CastViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_imageView)
            ImageView mItemImageView;
            @BindView(R.id.item_name)
            TextView mItemName;

            public CastViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
