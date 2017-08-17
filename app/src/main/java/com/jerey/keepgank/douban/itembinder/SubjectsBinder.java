package com.jerey.keepgank.douban.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerey.keepgank.douban.bean.SubjectsBean;
import com.jerey.keepgank.multitype.ItemViewBinder;

/**
 * @author xiamin
 * @date 8/17/17.
 */
public class SubjectsBinder extends ItemViewBinder<SubjectsBean,SubjectsBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return null;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SubjectsBean item) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
