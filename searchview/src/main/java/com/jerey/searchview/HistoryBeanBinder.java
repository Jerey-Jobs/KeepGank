package com.jerey.searchview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerey.mutitype.ItemViewBinder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xiamin
 * @date 8/24/17.
 */
public class HistoryBeanBinder extends ItemViewBinder<HistoryBean,HistoryBeanBinder.ViewHolder> {

    @NonNull
    @Override
    protected HistoryBeanBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                                         @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull HistoryBean item) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mRightIcon;
        TextView mTvHistoryItem;

        ViewHolder(View view) {
            super(view);
            mRightIcon = view.findViewById(R.id.right_icon);
            mTvHistoryItem = view.findViewById(R.id.history_item);
        }
    }
}
