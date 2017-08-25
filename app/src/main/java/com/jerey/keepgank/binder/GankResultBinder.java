package com.jerey.keepgank.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerey.keepgank.R;
import com.jerey.keepgank.activity.MyWebActivity;
import com.jerey.keepgank.bean.Result;
import com.jerey.keepgank.utils.IconUtils;
import com.jerey.mutitype.ItemViewBinder;
import com.jerey.searchview.KeyboardUtils;
import com.jerey.themelib.loader.SkinManager;

/**
 * @author Xiamin
 * @date 2017/8/25
 */
public class GankResultBinder extends ItemViewBinder<Result, GankResultBinder.ViewHolder> {
    @NonNull
    @Override
    protected GankResultBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                                             @NonNull ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Result item) {
        holder.setResult(item);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconView;
        final TextView titleView;
        final TextView whoView;
        final View rootView;
        private Result result;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            titleView = (TextView) view.findViewById(R.id.title);
            whoView = (TextView) view.findViewById(R.id.who);
            iconView = (ImageView) view.findViewById(R.id.icon);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtils.hideSoftInput(v, v.getContext());
                    MyWebActivity.startWebActivity(rootView.getContext(), result, iconView);
                }
            });
        }

        public void setResult(Result result) {
            this.result = result;
            titleView.setText(result.getDesc());
            whoView.setText(result.getWho());
            iconView.setImageDrawable(
                    SkinManager.getInstance().getDrawable(
                            IconUtils.getIconRes(result.getUrl(), result.getType())));

        }
    }
}
