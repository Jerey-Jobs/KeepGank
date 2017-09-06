package com.jerey.keepgank.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerey.keepgank.R;
import com.jerey.keepgank.bean.TitleBean;
import com.jerey.mutitype.ItemViewBinder;

/**
 * @author Xiamin
 * @date 2017/9/6
 */
public class TitleBeanBinder extends ItemViewBinder<TitleBean, TitleBeanBinder.TitleViewHolder> {

    @NonNull
    @Override
    protected TitleViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull
            ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_gank_title, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TitleViewHolder holder, @NonNull TitleBean item) {
        holder.textView.setText(item.getTitile());
    }


    public class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public TitleViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }
}
