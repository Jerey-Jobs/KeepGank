package com.jerey.footerrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * <p>这是一个自动带脚标的RecyclerView,采用装饰模式对传进来的Adapter进行了脚标扩展
 * 脚标ItemType ID为-999,勿重复使用</p>
 * @author xiamin
 * @date 8/21/17.
 */
public class FooterRecyclerView extends RecyclerView {
    public static final String TAG = FooterRecyclerView.class.getSimpleName();

    private onLoadMoreListener mOnLoadMoreListener;
    private boolean isLoadingMore = false;
    private int mLimitLastPosition = 4;

    private View mLoadingView;


    public FooterRecyclerView(Context context) {
        this(context, null);
    }

    public FooterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnLoadMoreListener(onLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /**
                 * 已经在回调loading状态了，不处理
                 */
                if (isLoadingMore) {
                    return;
                }

                LayoutManager layoutManager = recyclerView.getLayoutManager();
                int total = layoutManager.getItemCount();
                int lastPosition = getLastPosition(recyclerView);
                Log.i(TAG, "onScrolled: dy:" + dy + " positon :" + lastPosition);
                if (!isLoadingMore && total >= lastPosition && (
                        total - lastPosition) <= mLimitLastPosition) {
                    isLoadingMore = true;
                    mOnLoadMoreListener.onLoadMore(total);
                }
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof LoadMoreAdapterWrapper) {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
            super.setAdapter(adapter);
        } else {
            LoadMoreAdapterWrapper loadMoreAdapterWrapper = new LoadMoreAdapterWrapper(adapter);
            loadMoreAdapterWrapper.registerAdapterDataObserver(mAdapterDataObserver);
            super.setAdapter(loadMoreAdapterWrapper);
        }
    }

    private int getLastPosition(RecyclerView recyclerView) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        int lastPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager) layoutManager)
                    .findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    (StaggeredGridLayoutManager) layoutManager;
            int last[] = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastVisibleItemPositions(last);
            lastPosition = last[last.length - 1];
        }
        return lastPosition;
    }

    public int getLimitLastPosition() {
        return mLimitLastPosition;
    }

    public void setLimitLastPosition(int limitLastPosition) {
        mLimitLastPosition = limitLastPosition;
    }

    private int mViewState;
    private static final int LOADING_VIEW = 0;
    private static final int LOADING_TEXT = 1;
    private static final int LOADING_GONE = -1;
    private String mEndText;
    private int mLoadingColor;

    public void setEndText(String text) {
        mEndText = text;
        mViewState = LOADING_TEXT;
        int last = getLastPosition(this);
        if (getAdapter() != null) {
            getAdapter().notifyItemChanged(last, 1);
        }
    }

    public void restartEndLoading() {
        mViewState = LOADING_VIEW;
        int last = getLastPosition(this);
        if (getAdapter() != null) {
            getAdapter().notifyItemChanged(last, 1);
        }
    }

    public void removeFooter() {
        mViewState = LOADING_GONE;
    }


    public interface onLoadMoreListener {
        void onLoadMore(int lastPosition);
    }

    /**
     * 当有数据变动时结束加载更多View
     */
    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Log.w(TAG, "onChanged: ");
            reset();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            reset();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            reset();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            reset();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            reset();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            reset();
        }

        private void reset() {
            Log.i(TAG, "reset");
            isLoadingMore = false;
        }
    };

    public View getLoadingView() {
        return mLoadingView;
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
    }


    class LoadMoreAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int LOAD_MORE_VIEW_TYPE = -999;

        public Adapter mAdapterWrapper;

        public LoadMoreAdapterWrapper(Adapter adapterWrapper) {
            mAdapterWrapper = adapterWrapper;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == LOAD_MORE_VIEW_TYPE) {
                if (mLoadingView == null) {
                    mLoadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                            .footer_layout, parent, false);
                }
                return new ProgressViewHolder(mLoadingView);
            }
            return mAdapterWrapper.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if (getItemViewType(position) == LOAD_MORE_VIEW_TYPE) {
                Log.i(TAG, "onBindViewHolder: LOAD_MORE_VIEW_TYPE");
                ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
                if (mViewState == LOADING_VIEW) {
                    progressViewHolder.loadingView.setVisibility(VISIBLE);
                    progressViewHolder.text.setVisibility(GONE);
                } else if (mViewState == LOADING_TEXT) {
                    progressViewHolder.loadingView.setVisibility(GONE);
                    progressViewHolder.text.setVisibility(VISIBLE);
                    progressViewHolder.text.setText(mEndText);
                }
            } else {
                mAdapterWrapper.onBindViewHolder(holder, position, payloads);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        }

        @Override
        public int getItemViewType(int position) {
            if (mViewState != LOADING_GONE && position == getItemCount() - 1) {
                return LOAD_MORE_VIEW_TYPE;
            }
            return mAdapterWrapper.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            if (mViewState == LOADING_GONE) {
                return mAdapterWrapper.getItemCount();
            }
            return mAdapterWrapper.getItemCount() + 1;
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
            mAdapterWrapper.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
            mAdapterWrapper.unregisterAdapterDataObserver(observer);
        }

        class ProgressViewHolder extends ViewHolder {
            View loadingView;
            TextView text;

            public ProgressViewHolder(View itemView) {
                super(itemView);
                loadingView = itemView.findViewById(R.id.footer_loadingview);
                text = (TextView) itemView.findViewById(R.id.footer_text);
            }
        }
    }
}
