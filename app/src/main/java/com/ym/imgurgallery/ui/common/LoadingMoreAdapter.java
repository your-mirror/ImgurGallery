package com.ym.imgurgallery.ui.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ym.imgurgallery.R;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadingMoreAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_PROGRESS = 10;

    private EndlessRecyclerViewScrollListener scrollListener;
    private OnLoadMoreListener loadMoreListener;

    private RecyclerView.LayoutManager layoutManager;

    protected List<T> dataSet = new ArrayList<>();
    protected boolean isMoreLoading = true;
    protected int currentPage = 1;


    public interface OnLoadMoreListener{
        void onLoadMore(int page);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        layoutManager = recyclerView.getLayoutManager();
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!isMoreLoading && loadMoreListener != null)
                    loadMoreListener.onLoadMore(++currentPage);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        /* show loading progress in full width of the line, special for GridLayoutManager */
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager mLayoutManager = ((GridLayoutManager) layoutManager);
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == VIEW_TYPE_ITEM)
                        return 1;
                    return mLayoutManager.getSpanCount();
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(scrollListener);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindItemView(VH holder, int position);


    public void setLoadMoreListener(OnLoadMoreListener listener) {
        loadMoreListener = listener;
    }

    public void removeLoadMoreListener() {
        loadMoreListener = null;
    }

    /**
     * Call this method when you want to show loading bar
     * Obviously it can be called from your custom listener {@link OnLoadMoreListener}
     */
    public void showProgressBar() {
        addItem(null);
        isMoreLoading = true;
    }

    /**
     * Call this method when you want to hide loading bar
     */
    public void hideProgressBar() {
        removeItem(null);
        isMoreLoading = false;
    }

    public void addItems(@NonNull List<T> newDataSetItems) {
        dataSet.addAll(newDataSetItems);
        notifyItemRangeChanged(0, dataSet.size());
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public void removeItems() {
        dataSet.clear();
        notifyDataSetChanged();
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_PROGRESS;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return onCreateItemViewHolder(parent, viewType);
        } else if (viewType == VIEW_TYPE_PROGRESS) {
            return onCreateFooterViewHolder(parent, viewType);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            onBindItemView(((VH) holder), position);
        } else {
            onBindFooterView(holder, position);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progress_bar);
        }
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_progress_bar, parent, false);
        return new ProgressViewHolder(v);
    }

    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
        /* show loading progress in full width of the line, special for StaggeredGridLayoutManager */
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }
        ((ProgressViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
    }
}
