package com.jerey.animationadapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 淡入Adapter
 */
public class AlphaInAnimationAdapter extends AnimationAdapter {

  private static final float DEFAULT_ALPHA_FROM = 0f;
  private final float mFrom;

  public AlphaInAnimationAdapter(RecyclerView.Adapter adapter) {
    this(adapter, DEFAULT_ALPHA_FROM);
  }

  public AlphaInAnimationAdapter(RecyclerView.Adapter adapter, float from) {
    super(adapter);
    mFrom = from;
  }

  @Override protected Animator[] getAnimators(View view) {
    return new Animator[] { ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f) };
  }
}
