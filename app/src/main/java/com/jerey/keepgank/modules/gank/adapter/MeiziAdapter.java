package com.jerey.keepgank.modules.gank.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jerey.keepgank.R;
import com.jerey.keepgank.data.bean.Result;
import com.jerey.keepgank.modules.photopreview.PhotoBean;
import com.jerey.loglib.LogTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Xiamin on 2017/3/1.
 */

public class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.ViewHolder> {
    private List<Result> mDatas;
    private Context mContext;
    private List<Integer> heightList;//装产出的随机数

    public MeiziAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
        //记录为每个控件产生的随机高度,避免滑回到顶部出现空白
        heightList = new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.meizi_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.height = heightList.get(position);
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        holder.imageView.setLayoutParams(layoutParams);
        final Result data = mDatas.get(position);
        if (data.getUrl() != null) {
            Glide.with(mContext)
                 .load(data.getUrl())
                 .error(R.drawable.jay)
                 .centerCrop()
                 .placeholder(R.drawable.bg_cyan)
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                 .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.jay);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PhotoBean> mPhotoBeanArrayList = new ArrayList<PhotoBean>();
                Rect bounds = new Rect();
                holder.imageView.getGlobalVisibleRect(bounds);
                LogTools.w("mPhotoBeanArrayList:" + mPhotoBeanArrayList.size()
                                   + " position:" + position + " bounds:" + bounds.toString());
                for (Result result : mDatas) {
                    PhotoBean photoBean = new PhotoBean(result.getUrl(), bounds);
                    mPhotoBeanArrayList.add(photoBean);
                }

                ARouter.getInstance()
                       .build("/activity/PhotoPreviewActivity")
                       .withInt("index", position)
                       .withParcelableArrayList("photo_beans", mPhotoBeanArrayList)
                       .navigation();
            }
        });
        holder.textView.setText(data.getDesc());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            textView = (TextView) itemView.findViewById(R.id.item_title);
        }
    }

    public void addData(List<Result> datas) {
        int start = mDatas.size() - 1;
        this.mDatas.addAll(datas);
        for (int i = 0; i < mDatas.size(); i++) {
            int height = new Random().nextInt(200) + 250;//[100,300)的随机数
            heightList.add(height);
        }
        notifyItemRangeInserted(start + 1, mDatas.size());
    }

    public void setData(List<Result> datas) {
        this.mDatas = datas;
        for (int i = 0; i < mDatas.size(); i++) {
            int height = new Random().nextInt(200) + 250;//[100,300)的随机数
            heightList.add(height);
        }

    }
}
