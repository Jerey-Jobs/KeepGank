package com.jerey.keepgank.modules.gank.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerey.keepgank.R;
import com.jerey.keepgank.modules.common.MyWebActivity;
import com.jerey.keepgank.data.bean.Result;
import com.jerey.keepgank.utils.IconUtils;
import com.jerey.loglib.LogTools;
import com.jerey.themelib.loader.SkinManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiamin on 2017/2/12.
 */

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.ViewHolder> {
    private List<Result> mDatas;
    private Activity mContext;

    public ListFragmentAdapter(Activity context) {
        this.mContext = context;
        mDatas = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = mDatas.get(position);
        holder.setResult(result);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        }
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
                    MyWebActivity.startWebActivity(mContext, result, iconView);
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

    public void addData(List<Result> datas) {
        int start = mDatas.size();
        this.mDatas.addAll(datas);
        LogTools.d("start" + start + "mDatas.size()" + mDatas.size());
        notifyItemRangeInserted(start + 1, mDatas.size());
    }

    public void setData(List<Result> datas) {
        this.mDatas = datas;
    }

}
