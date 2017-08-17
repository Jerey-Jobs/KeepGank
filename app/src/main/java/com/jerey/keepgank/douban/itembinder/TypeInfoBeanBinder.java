package com.jerey.keepgank.douban.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerey.keepgank.R;
import com.jerey.keepgank.douban.bean.TypeInfoBean;
import com.jerey.keepgank.multitype.ItemViewBinder;

/**
 * @author xiamin
 * @date 8/17/17.
 */
public class TypeInfoBeanBinder extends ItemViewBinder<TypeInfoBean,TypeInfoBeanBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_douban_typeinfo, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TypeInfoBean item) {
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
