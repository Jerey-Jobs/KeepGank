package com.jerey.animationadapter;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.List;

public abstract class AnimationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
  private int mDuration = 300;
  private Interpolator mInterpolator = new LinearInterpolator();
  private int mLastPosition = -1;

  private boolean isFirstOnly = true;

  public AnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
    mAdapter = adapter;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return mAdapter.onCreateViewHolder(parent, viewType);
  }

  @Override public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.registerAdapterDataObserver(observer);
    mAdapter.registerAdapterDataObserver(observer);
  }

  @Override public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.unregisterAdapterDataObserver(observer);
    mAdapter.unregisterAdapterDataObserver(observer);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
    mAdapter.onBindViewHolder(holder, position, payloads);
    int adapterPosition = holder.getAdapterPosition();
    if (!isFirstOnly || adapterPosition > mLastPosition) {
      for (Animator anim : getAnimators(holder.itemView)) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
      }
      mLastPosition = adapterPosition;
    } else {
      ViewHelper.clear(holder.itemView);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

  }

  @Override public void onViewRecycled(RecyclerView.ViewHolder holder) {
    mAdapter.onViewRecycled(holder);
    super.onViewRecycled(holder);
  }

  @Override public int getItemCount() {
    return mAdapter.getItemCount();
  }

  public void setDuration(int duration) {
    mDuration = duration;
  }

  public void setInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public void setStartPosition(int start) {
    mLastPosition = start;
  }

  protected abstract Animator[] getAnimators(View view);

  public void setFirstOnly(boolean firstOnly) {
    isFirstOnly = firstOnly;
  }

  @Override public int getItemViewType(int position) {
    return mAdapter.getItemViewType(position);
  }

  public RecyclerView.Adapter<RecyclerView.ViewHolder> getWrappedAdapter() {
    return mAdapter;
  }

  @Override public long getItemId(int position) {
    return mAdapter.getItemId(position);
  }
}
