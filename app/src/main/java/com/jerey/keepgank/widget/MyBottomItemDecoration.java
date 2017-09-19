package com.jerey.keepgank.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jerey.keepgank.R;

/**
 * @author Xiamin
 * @date 2017/9/6
 */
public class MyBottomItemDecoration extends RecyclerView.ItemDecoration {
    private int dividerHeight;


    public MyBottomItemDecoration(Context context) {
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.space_normal);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            outRect.top = dividerHeight;
        }
    }

    public MyBottomItemDecoration setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        return this;
    }
}
