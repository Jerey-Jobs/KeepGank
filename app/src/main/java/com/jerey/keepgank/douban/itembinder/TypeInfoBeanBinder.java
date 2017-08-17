package com.jerey.keepgank.douban.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerey.keepgank.R;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.multitype.ItemViewBinder;
import com.jerey.loglib.LogTools;

/**
 * @author xiamin
 * @date 8/17/17.
 */
public class TypeInfoBeanBinder extends ItemViewBinder<TypeInfoBean, TypeInfoBeanBinder
        .ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup
            parent) {
        View root = inflater.inflate(R.layout.item_douban_typeinfo, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TypeInfoBean item) {
        LogTools.i("Title：" + item.getTitle());
        holder.mTextView.setText(item.getTitle());
        holder.mRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.mRecyclerView.getContext()));

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_title);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recyclerView);
        }
    }
}
