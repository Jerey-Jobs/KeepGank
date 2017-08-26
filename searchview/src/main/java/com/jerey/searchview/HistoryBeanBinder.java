package com.jerey.searchview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jerey.mutitype.ItemViewBinder;

import butterknife.OnClick;

/**
 * @author xiamin
 * @date 8/24/17.
 */
public class HistoryBeanBinder extends ItemViewBinder<HistoryBean, HistoryBeanBinder.ViewHolder> {

    private OnClickListener mOnClickListener;

    public HistoryBeanBinder(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    protected HistoryBeanBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                                              @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final HistoryBean item) {
        if (item.getDrawableURL() != null) {
            Glide.with(holder.mIcon.getContext())
                    .load(item.getDrawableURL())
                    .placeholder(R.drawable.ic_history_black_24dp)
                    .crossFade()
                    .error(R.drawable.ic_history_black_24dp)
                    .into(holder.mIcon);
        } else if (item.getDrawable() != null) {
            Glide.with(holder.mIcon.getContext())
                    .load(item.getDrawable())
                    .placeholder(R.drawable.ic_history_black_24dp)
                    .crossFade()
                    .error(R.drawable.ic_history_black_24dp)
                    .into(holder.mIcon);
        } else if (item.getBitmap() != null) {
            Glide.with(holder.mIcon.getContext())
                    .load(item.getBitmap())
                    .placeholder(R.drawable.ic_history_black_24dp)
                    .crossFade()
                    .error(R.drawable.ic_history_black_24dp)
                    .into(holder.mIcon);
        } else {
            holder.mIcon.setImageResource(R.drawable.ic_history_black_24dp);
        }
        holder.mTvHistoryItem.setText(item.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(item);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        TextView mTvHistoryItem;

        ViewHolder(View view) {
            super(view);
            mIcon = view.findViewById(R.id.right_icon);
            mTvHistoryItem = view.findViewById(R.id.history_item);
        }
    }

    interface OnClickListener {
        void onClick(HistoryBean item);
    }
}
