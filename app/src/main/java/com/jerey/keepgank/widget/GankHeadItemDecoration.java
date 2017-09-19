package com.jerey.keepgank.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jerey.loglib.LogTools;

import java.util.List;

/**
 * @author xiamin
 * @date 9/19/17.
 */
public class GankHeadItemDecoration extends RecyclerView.ItemDecoration {


    List<Object> mResultList;
    Paint mPaint;

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
        if (pos == 0) {
            outRect.top = 50;
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

        /** 获取第一个View的位置 */

        View view = parent.getChildAt(0);
        if (childCount > 1) {
            view = parent.getChildAt(1);
        }
        int top = view.getTop();
        int position = parent.getChildAdapterPosition(view); //计算出显示出的view的每个的数据位
        LogTools.d("getChildCount:" + childCount + "position:" + position + "");
        //        View drawingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_header,
        // parent, false);
        //        drawingView.setDrawingCacheEnabled(true);
        //        drawingView.buildDrawingCache();
        //        Bitmap bitmap = drawingView.getDrawingCache();
        //        c.drawBitmap(bitmap, left, top, null);

        c.drawText("eee", left, top, mPaint);
    }
}
