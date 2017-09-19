package com.jerey.keepgank.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerey.keepgank.R;
import com.jerey.keepgank.data.bean.Result;
import com.jerey.loglib.LogTools;

import java.util.List;

/**
 * @author xiamin
 * @date 9/19/17.
 */
public class GankHeadItemDecoration extends RecyclerView.ItemDecoration {


    private List<Object> mResultList;
    private Paint mPaint;

    private int mGroupHeight = 50;
    private int mDevideHeight = 5;

    public GankHeadItemDecoration(Context context, List<Object> results) {
        mResultList = results;
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (isFirstInGroup(pos)) {
            outRect.top = mGroupHeight;
        } else {
            outRect.top = mDevideHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int itemCount = state.getItemCount();
        /** childCount 为屏幕显示的childcount数量 ,比如7或者8*/
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        String preGroupName;
        String currentGroupName = null;
        /** 获取第一个View的位置 */
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view); //计算出显示出的view的每个的数据位

            preGroupName = currentGroupName;
            currentGroupName = getGroupName(position);
            if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName)) {
                continue;
            }

            int viewBottom = view.getBottom();
            int top = Math.max(mGroupHeight, view.getTop());//top 决定当前顶部第一个悬浮Group的位置
            if (position + 1 < itemCount) {
                //获取下个GroupName
                String nextGroupName = getGroupName(position + 1);
                //下一组的第一个View接近头部
                if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                    top = viewBottom;
                }
            }

            //LogTools.d("getChildCount:" + childCount + "position:" + position + "");
            View drawingView = LayoutInflater.from(parent.getContext())
                                             .inflate(R.layout.item_gank_header, parent, false);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(right, mGroupHeight);
            drawingView.setLayoutParams(layoutParams);
            drawingView.setDrawingCacheEnabled(true);
            drawingView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            drawingView.layout(0, 0, right, mGroupHeight);
            TextView tv = drawingView.findViewById(R.id.tv_header);
            LogTools.i("group name: " + getGroupName(position));
            tv.setText(getGroupName(position));
            drawingView.buildDrawingCache();
            Bitmap bitmap = drawingView.getDrawingCache();
            c.drawBitmap(bitmap, left, top - mGroupHeight, null);
        }

    }

    public void setDevideHeight(int devideHeight) {
        mDevideHeight = devideHeight;
    }

    public void setGroupHeight(int groupHeight) {
        mGroupHeight = groupHeight;
    }

    /**
     * 判断是不是组中的第一个位置
     * 根据前一个组名，判断当前是否为新的组
     */
    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupId = getGroupName(pos - 1);
            String groupId = getGroupName(pos);
            return !TextUtils.equals(prevGroupId, groupId);
        }
    }

    public String getGroupName(int position) {
        if (position >= mResultList.size()) {
            return null;
        }
        return ((Result) mResultList.get(position)).getPublishedAt();
    }
}
