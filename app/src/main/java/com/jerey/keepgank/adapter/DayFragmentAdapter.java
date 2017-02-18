package com.jerey.keepgank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerey.keepgank.R;
import com.jerey.keepgank.bean.GankDayResults;
import com.jerey.keepgank.bean.Result;
import com.jerey.keepgank.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiamin on 2017/2/17.
 */

public class DayFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "DayFragmentAdapter";
    private static final int TYPE_DISC = 1;
    private static final int TYPE_TITLE = 2;

    private List<ViewItem> mList;
    private Context mContext;
    private class ViewItem{
        String title;
        String url;
        int type;

        public ViewItem(String title, String url, int type) {
            this.title = title;
            this.url = url;
            this.type = type;
        }

        @Override
        public String toString() {
            return type + " " + title + " " + url;
        }
    }

    public DayFragmentAdapter(Context context){
        mContext = context;
        mList = new ArrayList<ViewItem>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_DISC){
            view = inflater.inflate(R.layout.item_gank,parent, false);
            return new DescViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_gank_title,parent, false);
            return new TitleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.setResult(mList.get(position));
        } else if(holder instanceof DescViewHolder) {
                DescViewHolder descViewHolder = (DescViewHolder) holder;
                descViewHolder.setResult(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private ViewItem result;
        public TitleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
        }

        public void setResult(ViewItem item){
            result = item;
            textView.setText(item.title);
        }
    }

    public class DescViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        private ViewItem result;
        public DescViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(GankApplication.getOpenUrl()== GeneralPrefs.OPEN_URL_WEB_VIEW){
//                        WebActivity.startWebActivity(mContext, result);
//                    }else{
                    SystemUtils.openUrlByBrowser(mContext, result.url);
//                    }
                }
            });
        }

        public void setResult(ViewItem item){
            result = item;
            textView.setText(item.title);
        }
    }

    public void setData(GankDayResults results){
        if (results.Android != null){
            mList.add(new ViewItem("Android",null,TYPE_TITLE));
            for (Result r: results.Android){
                mList.add(new ViewItem(r.getDesc(),r.getUrl(),TYPE_DISC));
            }
        }
        if(results.iOS != null){
            mList.add(new ViewItem("iOS",null,TYPE_TITLE));
            for (Result r: results.iOS){
                mList.add(new ViewItem(r.getDesc(),r.getUrl(),TYPE_DISC));
            }
        }
        if(results.App != null){
            mList.add(new ViewItem("App",null,TYPE_TITLE));
            for (Result r: results.App){
                mList.add(new ViewItem(r.getDesc(),r.getUrl(),TYPE_DISC));
            }
        }
        if(results.瞎推荐 != null){
            mList.add(new ViewItem("瞎推荐",null,TYPE_TITLE));
            for (Result r: results.瞎推荐){
                mList.add(new ViewItem(r.getDesc(),r.getUrl(),TYPE_DISC));
            }
        }
        if(results.休息视频 != null){
            mList.add(new ViewItem("休息视频",null,TYPE_TITLE));
            for (Result r: results.休息视频){
                mList.add(new ViewItem(r.getDesc(),r.getUrl(),TYPE_DISC));
            }
        }

        for (ViewItem v : mList){
            Log.d(TAG, v.toString());
        }

        notifyDataSetChanged();

    }
}
