package com.jerey.themelib.attr;

import android.support.v7.widget.CardView;
import android.view.View;

import com.jerey.themelib.attr.base.SkinAttr;
import com.jerey.themelib.utils.SkinResourcesUtils;

/**
 * @author xiamin
 * @date 9/18/17.
 */
public class CardViewBackgroundAttr extends SkinAttr {

    @Override
    public void apply(View view) {
        if (view instanceof CardView) {
            CardView cardView = (CardView) view;
            cardView.setCardBackgroundColor(SkinResourcesUtils.getColor(attrValueRefId));
        }
    }
}
